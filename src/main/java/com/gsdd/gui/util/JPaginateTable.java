package com.gsdd.gui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JPaginateTable extends JTable {

  private static final long serialVersionUID = -2905053270333371347L;

  private static final int BUTTON_FACTOR = 2;
  private static final int BUTTON_SPACE = 10;
  private static final int BUTTON_HEIGHT = 20;
  private static final int BUTTON_WIDTH = 50;
  private static final int ACTION_MAP_KEY = 0;
  private static final int ITEMS_PER_PAGE_DEF = 10;
  private static final String PAG_FORMAT = "/ %d";
  private static final String ARROW_FIRST = "|<";
  private static final String ARROW_PREV = "<";
  private static final String ARROW_NEXT = ">";
  private static final String ARROW_LAST = ">|";

  private final JScrollPane tableScroll;
  private final JButton tableFirst;
  private final JButton tablePrev;
  private final JButton tableNext;
  private final JButton tableLast;
  private TableRowSorter<TableModel> paginateSorter;
  private final JTextField field;
  private final JTextField fieldTop;
  private int currentPageIndex = 1;
  private int maxPageIndex = 1;
  private int itemsPerPage = ITEMS_PER_PAGE_DEF;

  public JPaginateTable() {
    tableScroll = new JScrollPane();
    tableFirst = new JButton();
    tablePrev = new JButton();
    tableNext = new JButton();
    tableLast = new JButton();
    field = new JTextField(2);
    fieldTop = new JTextField();
    fieldTop.setEditable(false);
    setProperties();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
    Component component = super.prepareRenderer(renderer, row, column);
    int rendererWidth = component.getPreferredSize().width;
    TableColumn tableColumn = getColumnModel().getColumn(column);
    tableColumn.setPreferredWidth(
        Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
    return component;
  }

  private final void setProperties() {
    this.setAutoCreateRowSorter(true);
    this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    this.setGridColor(new java.awt.Color(0, 0, 0));
    this.setBackground(Color.LIGHT_GRAY);
    this.setSelectionBackground(SystemColor.textHighlight);
    this.setSelectionForeground(SystemColor.inactiveCaptionBorder);
    tableScroll.setViewportView(this);
    setListeners();
    tableFirst.setText(ARROW_FIRST);
    tablePrev.setText(ARROW_PREV);
    tableNext.setText(ARROW_NEXT);
    tableLast.setText(ARROW_LAST);
  }

  @SuppressWarnings({"serial", "rawtypes", "unchecked"})
  public void setTableModel(String[] columnNames, Class[] types) {
    final Class[] tipos = types;
    this.setModel(new DefaultTableModel(new Object[][] {}, columnNames) {
      @Override
      public Class getColumnClass(int columnIndex) {
        return tipos[columnIndex];
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    });
  }

  @SuppressWarnings("serial")
  public void setListeners() {
    AbstractAction alValue = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          int v = Integer.parseInt(field.getText());
          if (v > 0 && v <= maxPageIndex) {
            currentPageIndex = v;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }
        initFilterAndButton(paginateSorter, itemsPerPage);
      }
    };
    ActionListener alFirst = (ActionEvent e) -> {
      currentPageIndex = 1;
      initFilterAndButton(paginateSorter, itemsPerPage);
    };
    ActionListener alPrev = (ActionEvent e) -> {
      currentPageIndex -= 1;
      initFilterAndButton(paginateSorter, itemsPerPage);
    };
    ActionListener alNext = (ActionEvent e) -> {
      currentPageIndex += 1;
      initFilterAndButton(paginateSorter, itemsPerPage);
    };
    ActionListener alLast = (ActionEvent e) -> {
      currentPageIndex = maxPageIndex;
      initFilterAndButton(paginateSorter, itemsPerPage);
    };
    tableNext.addActionListener(alNext);
    tablePrev.addActionListener(alPrev);
    tableFirst.addActionListener(alFirst);
    tableLast.addActionListener(alLast);
    KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
    field.getInputMap(JComponent.WHEN_FOCUSED).put(enter, ACTION_MAP_KEY);
    field.getActionMap().put(ACTION_MAP_KEY, alValue);
  }

  public void initFilterAndButton(TableRowSorter<TableModel> paginateSorter, Integer itemsPerPage) {
    final int rowMaxSort = itemsPerPage;
    int rowCount = this.getModel().getRowCount();
    int v = rowCount % itemsPerPage == 0 ? 0 : 1;
    maxPageIndex = rowCount / itemsPerPage + v;
    fieldTop.setText(String.format(PAG_FORMAT, maxPageIndex));
    paginateSorter.setRowFilter(new RowFilter<TableModel, Integer>() {
      @Override
      public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
        int ti = currentPageIndex - 1;
        int ei = entry.getIdentifier();
        return ti * rowMaxSort <= ei && ei < ti * rowMaxSort + rowMaxSort;
      }
    });
    tableFirst.setEnabled(currentPageIndex > 1);
    tablePrev.setEnabled(currentPageIndex > 1);
    tableNext.setEnabled(currentPageIndex < maxPageIndex);
    tableLast.setEnabled(currentPageIndex < maxPageIndex);
    field.setText(Integer.toString(currentPageIndex));
  }

  public void setComponentBounds(int x, int y, int width, int height) {
    tableScroll.setBounds(x, y, width, height);
    tableFirst.setBounds(((width / (BUTTON_FACTOR * 3))), (y + height + BUTTON_SPACE), BUTTON_WIDTH,
        BUTTON_HEIGHT);
    tablePrev.setBounds(((width / (BUTTON_FACTOR * 3)) + (BUTTON_SPACE + BUTTON_WIDTH)),
        (y + height + BUTTON_SPACE), BUTTON_WIDTH, BUTTON_HEIGHT);
    field.setBounds(((width / (BUTTON_FACTOR * 3)) + ((BUTTON_SPACE + BUTTON_WIDTH) * 2)),
        (y + height + BUTTON_SPACE), BUTTON_WIDTH, BUTTON_HEIGHT);
    fieldTop.setBounds(((width / (BUTTON_FACTOR * 3)) + ((BUTTON_SPACE + BUTTON_WIDTH) * 3)),
        (y + height + BUTTON_SPACE), BUTTON_WIDTH, BUTTON_HEIGHT);
    tableNext.setBounds(((width / (BUTTON_FACTOR * 3)) + ((BUTTON_SPACE + BUTTON_WIDTH) * 4)),
        (y + height + BUTTON_SPACE), BUTTON_WIDTH, BUTTON_HEIGHT);
    tableLast.setBounds(((width / (BUTTON_FACTOR * 3)) + ((BUTTON_SPACE + BUTTON_WIDTH) * 5)),
        (y + height + BUTTON_SPACE), BUTTON_WIDTH, BUTTON_HEIGHT);
  }

  /**
   * @return el valor de la paginacion de la tabla.
   */
  public Integer getPaginationSize() {
    return tableScroll.getViewport().getViewRect().height;
  }

  /**
   * @return the tableScroll
   */
  public JScrollPane getTableScroll() {
    return tableScroll;
  }

  /**
   * @return the tablePrev
   */
  public JButton getTablePrev() {
    return tablePrev;
  }

  /**
   * @return the tableNext
   */
  public JButton getTableNext() {
    return tableNext;
  }

  /**
   * @return the sorter
   */
  public TableRowSorter<TableModel> getPaginateSorter() {
    return paginateSorter;
  }

  /**
   * @param sorter the sorter to set
   */
  public void setPaginateSorter(TableRowSorter<TableModel> sorter) {
    this.paginateSorter = sorter;
  }

  /**
   * @return the tableFirst
   */
  public JButton getTableFirst() {
    return tableFirst;
  }

  /**
   * @return the tableLast
   */
  public JButton getTableLast() {
    return tableLast;
  }

  /**
   * @return the field
   */
  public JTextField getField() {
    return field;
  }

  /**
   * @return the label
   */
  public JTextField getLabel() {
    return fieldTop;
  }

  /**
   * @return the itemsPerPage
   */
  public int getItemsPerPage() {
    return itemsPerPage;
  }

  /**
   * @param itemsPerPage the itemsPerPage to set
   */
  public void setItemsPerPage(int itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }

}
