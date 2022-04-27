package com.gsdd.gui.util;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class JValidateTextField extends JTextField {

    private static final long serialVersionUID = 735926332748734356L;
    private Pattern pattern;
    private Integer maxSize;
    private Border wrongBorder = BorderFactory.createLineBorder(Color.RED);
    private Border defaultBorder;
    private boolean isValid;

    public JValidateTextField() {
        super();
        this.defaultBorder = this.getBorder();
        this.maxSize = this.getColumns();
        this.addKeyListener(
                new KeyListener() {

                    @Override
                    public void keyTyped(KeyEvent e) {}

                    @Override
                    public void keyReleased(KeyEvent e) {
                        validateText(e);
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {}
                });
    }

    /**
     * Constructor.
     *
     * @param regEx Expresión regular para evaluar
     */
    public JValidateTextField(String regEx) {
        super();
        this.defaultBorder = this.getBorder();
        this.maxSize = this.getColumns();
        if (regEx != null) {
            this.pattern = Pattern.compile(regEx);
            this.addKeyListener(
                    new KeyListener() {

                        @Override
                        public void keyTyped(KeyEvent e) {}

                        @Override
                        public void keyReleased(KeyEvent e) {
                            isValid = validateText(e);
                        }

                        @Override
                        public void keyPressed(KeyEvent e) {}
                    });
        }
    }

    public void setTextProperties(String regEx, Integer size, String toolTip) {
        this.setRegEx(regEx);
        this.setMaxSize(size);
        this.setToolTipText(toolTip);
        this.pattern = Pattern.compile(regEx);
    }

    private boolean validateText(KeyEvent e) {
        boolean r = true;
        if (pattern != null) {
            Matcher matcher = pattern.matcher(this.getText().trim());
            if (!matcher.matches()) {
                wrongAction(e);
                r = false;
            }
        }

        if (r) {
            if (this.getText().trim().length() > this.maxSize) {
                wrongAction(e);
                r = false;
            } else {
                this.setBorder(defaultBorder);
                r = true;
            }
        }
        return r;
    }

    private void wrongAction(KeyEvent e) {
        this.setBorder(wrongBorder);
        this.getToolkit().beep();
        e.consume();
    }

    public void setRegEx(String regEx) {
        this.pattern = Pattern.compile(regEx);
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public void setInitBorder() {
        this.setBorder(defaultBorder);
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }
}
