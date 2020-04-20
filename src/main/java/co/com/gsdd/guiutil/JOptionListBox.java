package co.com.gsdd.guiutil;

import java.util.List;

import javax.swing.JOptionPane;

import co.com.gsdd.constants.GralConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JOptionListBox {

    private List<String> input;
    private String output;
    private String title;
    private String value;

    public JOptionListBox(List<String> input, String title, String value) {
        this.input = input;
        this.title = title;
        this.value = value;
    }

    public String getSelectedValue() {
        String p = null;
        try {
            if (input != null && !input.isEmpty()) {
                String[] choices = input.toArray(new String[input.size()]);
                if (choices.length > 0) {
                    String criteria = value + GralConstants.COLON;
                    this.output = (String) JOptionPane.showInputDialog(null, criteria, title,
                            JOptionPane.QUESTION_MESSAGE, null, // Use
                                                                // default
                                                                // icon
                            choices, // Array of choices
                            choices[0]); // Initial choice
                    return output;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return p;
    }

}
