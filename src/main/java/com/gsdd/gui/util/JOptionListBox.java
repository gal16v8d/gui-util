package com.gsdd.gui.util;

import com.gsdd.constants.GralConstants;
import java.util.List;
import javax.swing.JOptionPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JOptionListBox {

    private List<String> input;
    private String title;
    private String value;

    public JOptionListBox(List<String> input, String title, String value) {
        this.input = input;
        this.title = title;
        this.value = value;
    }

    public String getSelectedValue() {
        String output = null;
        try {
            if (input != null && !input.isEmpty()) {
                String[] choices = input.toArray(new String[input.size()]);
                if (choices.length > 0) {
                    String criteria = value + GralConstants.COLON;
                    output =
                            (String)
                                    JOptionPane.showInputDialog(
                                            null,
                                            criteria,
                                            title,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null, // Use
                                            // default
                                            // icon
                                            choices, // Array of choices
                                            choices[0]); // Initial choice
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return output;
    }
}
