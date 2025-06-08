package graphicinterface;
import javax.swing.*;
import DAO.client;
import modele.Client;
import java.awt.*;
import java.awt.event.*;

public class ClientManagementDialog extends JDialog {
    private JTextField nomField, prenomField, creditField;
    private JButton addButton, updateButton, deleteButton;
    private int clientId;

    public ClientManagementDialog(JFrame parent) {
        super(parent, "Gestion des Clients", true);
        setSize(450, 300);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180)); // Nice blue
        titlePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JLabel titleLabel = new JLabel("GESTION CLIENT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        inputPanel.setBackground(Color.WHITE);

        // Simple form fields
        inputPanel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        inputPanel.add(nomField);

        inputPanel.add(new JLabel("Prénom:"));
        prenomField = new JTextField();
        inputPanel.add(prenomField);

        inputPanel.add(new JLabel("Crédit:"));
        creditField = new JTextField();
        inputPanel.add(creditField);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(220, 220, 220)); // Light gray
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Basic styled buttons
        addButton = createSimpleButton("Ajouter", new Color(60, 160, 60)); // Green
        updateButton = createSimpleButton("Modifier", new Color(70, 130, 180)); // Blue
        deleteButton = createSimpleButton("Supprimer", new Color(180, 60, 60)); // Red

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        addButton.setEnabled(true);
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);


        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);

        addButton.addActionListener(e -> addClient());
        updateButton.addActionListener(e -> updateClient());
        deleteButton.addActionListener(e -> deleteClient());
    }

    private JButton createSimpleButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    private void addClient() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        double credit = Double.parseDouble(creditField.getText());

        Client newClient = new Client(clientId, nom, prenom, credit);
        client clientDAO = new client();
        clientDAO.ajouter(newClient);

        JOptionPane.showMessageDialog(this, "Client ajouté avec succès!");
        dispose();
    }

    private void updateClient() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        double credit = Double.parseDouble(creditField.getText());

        Client updatedClient = new Client(clientId, nom, prenom, credit);
        client clientDAO = new client();
        clientDAO.modifier(updatedClient);

        JOptionPane.showMessageDialog(this, "Client mis à jour avec succès!");
        dispose();
    }

    private void deleteClient() {
        try {
            int idcli = Integer.parseInt(JOptionPane.showInputDialog(this, "ID Client à supprimer:"));
            Client client = new Client(idcli, "", "", 0.0);
            client clientDAO = new client();
            clientDAO.supprimer(client);

            JOptionPane.showMessageDialog(this, "Client supprimé avec succès!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format ID invalide!");
        }
    }

    private void modifyClient() {
        try {
            int idcli = Integer.parseInt(JOptionPane.showInputDialog(this, "ID Client à modifier:"));
            String nom = JOptionPane.showInputDialog(this, "Nouveau nom:");
            String prenom = JOptionPane.showInputDialog(this, "Nouveau prénom:");
            double credit = Double.parseDouble(JOptionPane.showInputDialog(this, "Nouveau crédit:"));

            Client client = new Client(idcli, nom, prenom, credit);
            client Client = new client();
            Client.modifier(client);

            JOptionPane.showMessageDialog(this, "Client modifié avec succès!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Saisie invalide!");
        }
    }
}