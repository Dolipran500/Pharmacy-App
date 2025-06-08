package graphicinterface;
import javax.swing.*;
import java.util.*;
import java.util.List;
import DAO.*;
import modele.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

public class PharmacistFrame extends JFrame {
    private Image clientsBackground;
    private Image medicamentsBackground;
    private Image prescriptionsBackground;
    private static final Color PRIMARY_COLOR = new Color(0, 120, 139);
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private static final Color ACCENT_COLOR = new Color(255, 111, 97);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public PharmacistFrame() {
    	try {
    	    clientsBackground = ImageIO.read(new File("E:/ASUS/Downloads/patient.jpg"));
    	    medicamentsBackground = ImageIO.read(new File("E:/ASUS/Downloads/meds2.jpg"));
    	    prescriptionsBackground = ImageIO.read(new File("E:/ASUS/Downloads/BirthControlPharmacist-Hero.webp"));
    	} catch (IOException e) {
    	    JOptionPane.showMessageDialog(this, "Error loading images: " + e.getMessage());
    	    e.printStackTrace();
    	    clientsBackground = medicamentsBackground = prescriptionsBackground = null;
    	}

        setTitle("Pharmacist Dashboard");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(SECONDARY_COLOR);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel("PHARMACIST DASHBOARD");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(SECONDARY_COLOR);
        tabs.setForeground(PRIMARY_COLOR);

        // Clients Tab
        JPanel clientsPanel = createTabPanel(clientsBackground);
        JButton viewClientsButton = createViewButton("View Clients & Credits", new Color(100, 181, 246));
        clientsPanel.add(viewClientsButton);
        tabs.addTab("Clients", clientsPanel);

        // Medicaments Tab
        JPanel medicamentsPanel = createTabPanel(medicamentsBackground);
        JButton viewMedicamentsButton = createViewButton("View Medicaments & Stocks", new Color(77, 182, 172));
        medicamentsPanel.add(viewMedicamentsButton);
        tabs.addTab("Medicaments", medicamentsPanel);

        // Prescriptions Tab
        JPanel prescriptionsPanel = createTabPanel(prescriptionsBackground);
        JButton managePrescriptionsButton = createViewButton("Manage Prescriptions", new Color(255, 138, 101));
        prescriptionsPanel.add(managePrescriptionsButton);
        tabs.addTab("Prescriptions", prescriptionsPanel);

        mainPanel.add(tabs, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        statusPanel.setBackground(Color.WHITE);
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        statusPanel.add(statusLabel);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        viewClientsButton.addActionListener(e -> viewClients());
        viewMedicamentsButton.addActionListener(e -> viewMedicaments());
        managePrescriptionsButton.addActionListener(e -> manageOrdonnances());
    }

    private JPanel createTabPanel(Image background) {
        return new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 100));
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g.setColor(SECONDARY_COLOR);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
    }

    private JButton createViewButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darker(color, 0.8f), 2),
            BorderFactory.createEmptyBorder(12, 30, 12, 30)
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

    private void viewClients() {
        List<Client> clients = getClientsFromDatabase();
        StringBuilder sb = new StringBuilder();
        for (Client client : clients) {
            sb.append("ID: ").append(client.idcli())
              .append(", Name: ").append(client.nom())
              .append(", Prenom: ").append(client.prenom())
              .append(", Credit: ").append(client.credit())
              .append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Clients", JOptionPane.INFORMATION_MESSAGE);
        new ClientManagementDialog(this).setVisible(true);
    }

    private List<Client> getClientsFromDatabase() {
        client clientDAO = new client();
        return clientDAO.getAll();
    }
    
    private void viewMedicaments() {
        medicament medicamentDAO = new medicament();
        List<Medicaments> meds = medicamentDAO.getAll();
        StringBuilder sb = new StringBuilder("Medicaments:\n");
        for (Medicaments m : meds) {
            sb.append("id: ").append(m.idmed()).append("Name: ").append(m.nommed())
              .append(" - Stock: ").append(m.stock())
              .append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
        new medicamentsmanagement(this).setVisible(true);
    }

    private void manageOrdonnances() {
        Map<Ordonnance, List<LigneMed>> prescriptionMap = getOrdonnancesFromDatabase();

        if (prescriptionMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No prescriptions found.");
        } else {
            String[] columnNames = {"Prescription ID", "Client ID", "Instructions", "Medicament ID", "Quantity"};
            int totalRows = 0;
            for (List<LigneMed> ligneList : prescriptionMap.values()) {
                totalRows += ligneList.size();
            }

            String[][] data = new String[totalRows][5];
            int row = 0;
            for (Map.Entry<Ordonnance, List<LigneMed>> entry : prescriptionMap.entrySet()) {
                Ordonnance p = entry.getKey();
                for (LigneMed lm : entry.getValue()) {
                    data[row][0] = String.valueOf(p.idpresc());
                    data[row][1] = String.valueOf(p.idcli());
                    data[row][2] = p.inst();
                    data[row][3] = String.valueOf(lm.medicamentId());
                    data[row][4] = String.valueOf(lm.stock());
                    row++;
                }
            }

            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);

            JButton modButton = new JButton("Modifier Prescription");
            styleButton(modButton, ACCENT_COLOR);
            modButton.addActionListener(e -> {
                new ordonancesmanagement((JFrame) this).setVisible(true);
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(false);
            buttonPanel.add(modButton);

            JFrame frame = new JFrame("Prescriptions");
            frame.setSize(700, 400);
            frame.setLocationRelativeTo(this);
            frame.setLayout(new BorderLayout());
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        }
    }

    private Map<Ordonnance, List<LigneMed>> getOrdonnancesFromDatabase() {
        Map<Ordonnance, List<LigneMed>> prescriptionMap = new HashMap<>();
        try {
            Connection cn = SingletonConnection.getInstance();
            String query = "SELECT * FROM prescription";
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idPrescription = rs.getInt("idpresc");
                int clientId = rs.getInt("client_id");
                String instructions = rs.getString("instructions");
                Ordonnance prescription = new Ordonnance(idPrescription, clientId, instructions);
                List<LigneMed> ligneMeds = new ArrayList<>();
                String ligneQuery = "SELECT lm.idmed, lm.quantmed FROM ligne_med lm WHERE lm.idpresc = ?";
                PreparedStatement psLigne = cn.prepareStatement(ligneQuery);
                psLigne.setInt(1, idPrescription);
                ResultSet rsLigne = psLigne.executeQuery();
                while (rsLigne.next()) {
                    int idmed = rsLigne.getInt("idmed");
                    int quantmed = rsLigne.getInt("quantmed");
                    LigneMed ligneMed = new LigneMed(idPrescription, idmed, quantmed);
                    ligneMeds.add(ligneMed);
                }
                prescriptionMap.put(prescription, ligneMeds);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptionMap;
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(darker(color, 0.8f), 2),
            BorderFactory.createEmptyBorder(12, 30, 12, 30)
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
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new PharmacistFrame().setVisible(true));
    }
}