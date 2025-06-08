import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import graphicinterface.LoginFrame;

public class main {
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur: " + e.getMessage());
        }
    }
}