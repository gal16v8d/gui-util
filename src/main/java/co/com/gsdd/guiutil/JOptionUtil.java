package co.com.gsdd.guiutil;

import javax.swing.JOptionPane;

import co.com.gsdd.constantes.ConstanteGUI;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JOptionUtil {

    public static void showAppMessage(String titulo, Object msj, int messageType) {
        JOptionPane.showMessageDialog(null, msj, titulo, messageType);
    }

    public static void mostrarMensajeInfo(String msj) {
        showAppMessage(ConstanteGUI.SUCCESS, msj, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void mostrarMensajeError(String msj) {
        showAppMessage(ConstanteGUI.ERROR, msj, JOptionPane.ERROR_MESSAGE);
    }
    
    public static String showInputMessage(String msj) {
        return JOptionPane.showInputDialog(null, msj);
    }
}
