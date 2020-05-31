package co.com.gsdd.gui.util;

import javax.swing.JOptionPane;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JOptionUtil {

    public static void showAppMessage(String title, Object msg, int messageType) {
        JOptionPane.showMessageDialog(null, msg, title, messageType);
    }

    public static void showInfoMessage(String title, String msg) {
        showAppMessage(title, msg, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorMessage(String title, String msg) {
        showAppMessage(title, msg, JOptionPane.ERROR_MESSAGE);
    }

    public static String showInputMessage(String msg) {
        return JOptionPane.showInputDialog(null, msg);
    }
}
