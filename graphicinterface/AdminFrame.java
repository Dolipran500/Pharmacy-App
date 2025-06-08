package graphicinterface;
import javax.swing.*;
import DAO.client;
import DAO.medicament;
import modele.Client;
import modele.Medicaments;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class AdminFrame extends JFrame {
    private Image clientsBackground;
    private Image medicamentsBackground;
    private Image stockBackground;
    private static final Color PRIMARY_COLOR = new Color(0, 100, 119); // Darker teal
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public AdminFrame() {
        try {
            clientsBackground = ImageIO.read(new File("E:/ASUS/Downloads/patient.jpg")); // Clients tab
            medicamentsBackground = ImageIO.read(new File("E:/ASUS/Downloads/meds2.jpg")); // Medicaments tab
            stockBackground = ImageIO.read(new File("E:/ASUS/Downloads/meds.jpg")); // Stock tab
        } catch (IOException e) {
            clientsBackground = medicamentsBackground = stockBackground = null;
        }

        setTitle("Admin Dashboard");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(SECONDARY_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel("ADMINISTRATOR DASHBOARD");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(SECONDARY_COLOR);
        tabs.setForeground(PRIMARY_COLOR);

        // Clients Tab
        JPanel clientsPanel = createTabPanel(clientsBackground);
        JButton manageClientsButton = createStyledButton("Consulter Clients", new Color(70, 130, 180));
        clientsPanel.add(manageClientsButton);
        tabs.addTab("Clients", clientsPanel);

        // Medicaments Tab
        JPanel medicamentsPanel = createTabPanel(medicamentsBackground);
        JButton manageMedicamentsButton = createStyledButton("Consulter Medicaments", new Color(56, 142, 60));
        medicamentsPanel.add(manageMedicamentsButton);
        tabs.addTab("Medicaments", medicamentsPanel);

        // Stock Tab
        JPanel stockPanel = createTabPanel(stockBackground);
        JButton manageStockButton = createStyledButton("Consulter Stock", new Color(198, 40, 40));
        stockPanel.add(manageStockButton);
        tabs.addTab("Stock", stockPanel);

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

        manageClientsButton.addActionListener(e -> manageClients());
        manageMedicamentsButton.addActionListener(e -> manageMedicaments());
        manageStockButton.addActionListener(e -> manageStock());
    }

    private JPanel createTabPanel(Image background) {
        return new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 120)); // Dark overlay for readability
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g.setColor(SECONDARY_COLOR);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
    }

    private JButton createStyledButton(String text, Color color) {
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
    private void manageClients() {
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
    
    private void manageMedicaments() {
        medicament medicamentDAO = new medicament();
        List<Medicaments> meds = medicamentDAO.getAll();
        StringBuilder sb = new StringBuilder("Medicaments:\n");
        for (Medicaments m : meds) {
            sb.append("Name: ").append(m.nommed())
              .append(" - Stock: ").append(m.stock())
              .append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
        new medicamentsmanagement(this).setVisible(true);
    }

    private void manageStock() {
        medicament medicamentDAO = new medicament();
        List<Medicaments> meds = medicamentDAO.getAll();

        if (meds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "pas de medicaments trouvés.");
            return;
        }

        String[] columnNames = {"ID", "Name", "Category", "Price", "Stock"};
        Object[][] data = new Object[meds.size()][5];
        for (int i = 0; i < meds.size(); i++) {
            Medicaments m = meds.get(i);
            data[i][0] = m.idmed();
            data[i][1] = m.nommed();
            data[i][2] = m.categorie();
            data[i][3] = m.prix();
            data[i][4] = m.stock();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        int option = JOptionPane.showConfirmDialog(
            this, scrollPane, "Select a medicament to update", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int idmed = (int) table.getValueAt(selectedRow, 0);
                String name = (String) table.getValueAt(selectedRow, 1);
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "voulez vous mettre a jour le stock de  \"" + name + "\"?",
                    "Confirmer Stock mise a jour",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        int newStock = Integer.parseInt(JOptionPane.showInputDialog(
                            this, "Entrer nouvelle stock :"));
                        Medicaments oldmed = medicamentDAO.getById(idmed);
                        Medicaments updatedMed = new Medicaments(oldmed.idmed(),oldmed.categorie(), oldmed.nommed(), oldmed.prix(),newStock);
                        medicamentDAO.modifierStock(updatedMed);
                        JOptionPane.showMessageDialog(this, "Stock mis a jour !");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "nombre Invalide.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "pas de medicaments Selectés.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new AdminFrame().setVisible(true));
    }
}