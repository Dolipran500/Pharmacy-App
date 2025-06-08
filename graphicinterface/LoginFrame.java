package graphicinterface;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import DAO.SingletonConnection;
import modele.UserSession;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Pharmacy Management System - Login");
        setSize(500, 350); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel 
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(0, 150, 136); // Teal
                Color color2 = new Color(38, 166, 154); // Light teal
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // panel avec background semi transparent
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add logo
        JLabel logoLabel = new JLabel("Pharmacie: User Login", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(logoLabel, gbc);

        // Reset insets
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 1;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(userLabel, gbc);

        usernameField = new JTextField(20);
        styleTextField(usernameField);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passLabel, gbc);

        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        loginButton = new JButton("LOGIN");
        styleButton(loginButton);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        formPanel.add(loginButton, gbc);

        mainPanel.add(formPanel);
        setContentPane(mainPanel);

        loginButton.addActionListener(e -> checkLogin());
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 150)
        ), null));
        field.setOpaque(false);
        field.setBackground(new Color(255, 255, 255, 100));
        field.setForeground(Color.WHITE);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 105, 92)); // Dark teal
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 121, 107));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 105, 92));
            }
        });
    }

    private void checkLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Connection cn = SingletonConnection.getInstance();
            String sql = "SELECT * FROM users WHERE nomUser = ? AND passwordUser = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserSession.getInstance().setCurrentUserId(rs.getInt("idUser"));
                String role = rs.getString("roleUser");
                JOptionPane.showMessageDialog(this, "Login successful! Role: " + role);
                this.dispose();

                if (role.equalsIgnoreCase("admin")) {
                    new AdminFrame().setVisible(true);
                } else if (role.equalsIgnoreCase("pharmacist")) {
                    new PharmacistFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Unknown role: " + role);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Wrong username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}