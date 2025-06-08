package graphicinterface;
import javax.swing.*;
import DAO.medicament;
import modele.Medicaments;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class medicamentsmanagement extends JDialog {
    private medicament medicamentDAO;
    private Image backgroundImage;
    private static final Color PRIMARY_COLOR = new Color(76, 175, 80); // Green
    private static final Color SECONDARY_COLOR = new Color(242, 242, 242);
    private static final Color ACCENT_COLOR = new Color(233, 30, 99);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);

    public medicamentsmanagement(Frame owner) {
        super(owner, "Gestion des Médicaments", true);
        setSize(500, 400);
        setLocationRelativeTo(owner);
    
        try {
            backgroundImage = ImageIO.read(new File("E:/ASUS/Downloads/meds_bg.jpg")); // Your image path
        } catch (IOException e) {
            backgroundImage = null;
        }

        medicamentDAO = new medicament();

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
        JLabel titleLabel = new JLabel("GESTION DES MÉDICAMENTS");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.BLACK);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton addButton = createStyledButton("Ajouter Médicament", PRIMARY_COLOR);
        JButton deleteButton = createStyledButton("Supprimer Médicament", new Color(244, 67, 54));
        JButton modifyButton = createStyledButton("Modifier Médicament", new Color(255, 152, 0));
        JButton saveButton = createStyledButton("Enregistrer", new Color(33, 150, 243));
        JButton exitButton = createStyledButton("Fermer", new Color(158, 158, 158));

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Action listeners
        addButton.addActionListener(e -> addMedicament());
        deleteButton.addActionListener(e -> deleteMedicament());
        modifyButton.addActionListener(e -> modifyMedicament());
        saveButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Succès d'enregistrement!", 
                "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        });

        exitButton.addActionListener(e -> {
            int confirmed = JOptionPane.showConfirmDialog(this, 
                "Fermer la fenêtre ?", "Confirmation", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirmed == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
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

    private void addMedicament() {
        String categorie = JOptionPane.showInputDialog(this, "Entrer catégorie medicament:");
        String nommed = JOptionPane.showInputDialog(this, "Entrer nom medicament :");
        double prix = Double.parseDouble(JOptionPane.showInputDialog(this, "Entrer prix medicament :"));
        int stock = Integer.parseInt(JOptionPane.showInputDialog(this, "Entrer stock medicament :"));

        Medicaments medicament = new Medicaments(0, categorie, nommed, prix, stock);
        medicamentDAO.ajouter(medicament);

        JOptionPane.showMessageDialog(this, "Medicament ajouté avec succés!");
    }

    private void deleteMedicament() {
        try {
            int idmed = Integer.parseInt(JOptionPane.showInputDialog(this, "donner id medicaments pour suppression:"));
            Medicaments medicament = new Medicaments(idmed, "", "", 0.0, 0);
            medicamentDAO.supprimer(medicament);

            JOptionPane.showMessageDialog(this, "Suppression avec succés!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Invalide!");
        }
    }

    private void modifyMedicament() {
        try {
            int idmed = Integer.parseInt(JOptionPane.showInputDialog(this, "Donner Id medicaments pour modifier:"));
            double prix = Double.parseDouble(JOptionPane.showInputDialog(this, "Donner nouvelle prix:"));
            int stock = Integer.parseInt(JOptionPane.showInputDialog(this, "donner nouvelle stock:"));

            Medicaments medicament = new Medicaments(idmed, "", "", prix, stock);
            medicamentDAO.modifier(medicament);

            JOptionPane.showMessageDialog(this, "Medicament modifié avec succés!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input Invalide!");
        }
    }
}