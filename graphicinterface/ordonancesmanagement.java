package graphicinterface;
import javax.swing.*;
import modele.LigneMed;
import modele.Ordonnance;
import DAO.SingletonConnection;
import DAO.lignemed;
import DAO.ordonnance;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ordonancesmanagement extends JDialog {
    private ordonnance ordonnanceDAO;
    private lignemed ligneMedDAO;
    private Image backgroundImage;
    private static final Color PRIMARY_COLOR = new Color(103, 58, 183); // Deep purple
    private static final Color SECONDARY_COLOR = new Color(250, 250, 250);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);

    public ordonancesmanagement(JFrame owner) {
        super(owner, "Gestion des Ordonnances", true);
        setSize(500, 350);
        setLocationRelativeTo(owner);

        try {
            backgroundImage = ImageIO.read(new File("E:/ASUS/Downloads/prescription_bg.jpg")); // Your image path
        } catch (IOException e) {
            backgroundImage = null;
        }

        ordonnanceDAO = new ordonnance();
        ligneMedDAO = new lignemed();

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 120)); // Dark overlay
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g.setColor(SECONDARY_COLOR);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("GESTION DES ORDONNANCES");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        JButton addButton = createStyledButton("Ajouter Ordonnance", new Color(76, 175, 80));
        JButton deleteButton = createStyledButton("Supprimer Ordonnance", new Color(244, 67, 54));

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Action listeners
        addButton.addActionListener(e -> addordonnance());
        deleteButton.addActionListener(e -> deleteordonnance());
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darker(color, 0.8f), 2),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        button.setFocusPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(brighter(color, 1.15f));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        return button;
    }

    private static Color brighter(Color color, float factor) {
        return new Color(
            Math.min((int)(color.getRed() * factor), 255),
            Math.min((int)(color.getGreen() * factor), 255),
            Math.min((int)(color.getBlue() * factor), 255)
        );
    }

    private static Color darker(Color color, float factor) {
        return new Color(
            Math.max((int)(color.getRed() * factor), 0),
            Math.max((int)(color.getGreen() * factor), 0),
            Math.max((int)(color.getBlue() * factor), 0)
        );
    }

    private void addordonnance() {
        try {
            int idcli = Integer.parseInt(JOptionPane.showInputDialog(this, "ID Client:", "Nouvelle Ordonnance", JOptionPane.QUESTION_MESSAGE));
            String inst = JOptionPane.showInputDialog(this, "Instructions:", "Nouvelle Ordonnance", JOptionPane.QUESTION_MESSAGE);

            Ordonnance prescription = new Ordonnance(0, idcli, inst);
            ordonnanceDAO.ajouter(prescription);

            int idpresc = ordonnanceDAO.getLastInsertedPrescriptionId();
            while (true) {
                int idmed = Integer.parseInt(JOptionPane.showInputDialog(this, "ID Médicament:", "Ajouter Médicament", JOptionPane.QUESTION_MESSAGE));
                int quantmed = Integer.parseInt(JOptionPane.showInputDialog(this, "Quantité:", "Ajouter Médicament", JOptionPane.QUESTION_MESSAGE));

                LigneMed ligne = new LigneMed(idpresc, idmed, quantmed);
                ligneMedDAO.ajouter(ligne);

                int option = JOptionPane.showConfirmDialog(this, 
                    "Ajouter un autre médicament?", "Continuer?", 
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (option != JOptionPane.YES_OPTION) break;
            }

            JOptionPane.showMessageDialog(this, "Ordonnance ajoutée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Saisie invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteordonnance() {
        try {
            int idpresc = Integer.parseInt(JOptionPane.showInputDialog(this, 
                "ID Ordonnance à supprimer:", "Suppression Ordonnance", 
                JOptionPane.QUESTION_MESSAGE));
            
            Ordonnance prescription = new Ordonnance(idpresc, 0, "");
            ordonnanceDAO.supprimer(prescription);

            JOptionPane.showMessageDialog(this, "Ordonnance supprimée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format ID invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}