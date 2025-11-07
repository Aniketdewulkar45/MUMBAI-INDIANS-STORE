package mi.store.management.system;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;

public class SignUpSystemUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JCheckBox showPasswordCheckBox;

    public SignUpSystemUI() {
        setTitle("Sign-Up - Mumbai Indians Merchandise Store");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Gradient Background Panel ===
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(70, 130, 180); // Medium Blue
                Color color2 = new Color(0, 0, 90);     // Dark Blue
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(new BorderLayout());
        add(gradientPanel);

        // === Heading ===
        JLabel headerLabel = new JLabel("SIGN UP AND BE A PART OF OURS!!", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 42));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));
        gradientPanel.add(headerLabel, BorderLayout.NORTH);

        // === Center Box ===
        JPanel boxPanel = new JPanel(new GridLayout(1, 2));
        boxPanel.setPreferredSize(new Dimension(1100, 700));
        boxPanel.setBackground(Color.WHITE);
        boxPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
        boxPanel.setOpaque(true);
        gradientPanel.add(boxPanel, BorderLayout.CENTER);

        // === Left Panel (Form) ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        // Username
        JLabel userLabel = makeLabel("Username:");
        formPanel.add(userLabel, gbc);
        gbc.gridy++;
        usernameField = makeTextField();
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridy++;
        JLabel passLabel = makeLabel("Password:");
        formPanel.add(passLabel, gbc);
        gbc.gridy++;
        passwordField = makePasswordField();
        formPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridy++;
        JLabel confirmLabel = makeLabel("Confirm Password:");
        formPanel.add(confirmLabel, gbc);
        gbc.gridy++;
        confirmPasswordField = makePasswordField();
        formPanel.add(confirmPasswordField, gbc);

        // Show Password
        gbc.gridy++;
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(new Font("Arial", Font.BOLD, 18));
        showPasswordCheckBox.setBackground(Color.WHITE);
        showPasswordCheckBox.setForeground(new Color(0, 0, 128));
        showPasswordCheckBox.addActionListener(e -> {
            boolean show = showPasswordCheckBox.isSelected();
            passwordField.setEchoChar(show ? (char) 0 : '•');
            confirmPasswordField.setEchoChar(show ? (char) 0 : '•');
        });
        formPanel.add(showPasswordCheckBox, gbc);

        // Sign Up Button
        gbc.gridy++;
        JButton signUpButton = makeButton("SIGN UP");
        signUpButton.addActionListener(e -> signUp());
        formPanel.add(signUpButton, gbc);

        // Already a User Button
        gbc.gridy++;
        JButton loginButton = makeButton("ALREADY A USER?");
        loginButton.addActionListener(e -> {
            new Login();
            dispose();
        });
        formPanel.add(loginButton, gbc);

        boxPanel.add(formPanel);

        // === Right Panel (Image) ===
        JLabel imageLabel;
        URL imageUrl = getClass().getResource("/icons/login2.png");
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image scaled = icon.getImage().getScaledInstance(700, 700, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaled));
        } else {
            imageLabel = new JLabel("Image Not Found", SwingConstants.CENTER);
            imageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        }

        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.add(imageLabel);
        boxPanel.add(imagePanel);

        setVisible(true);
    }

    // === Helper UI Methods ===
    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 26));
        label.setForeground(new Color(0, 0, 128));
        return label;
    }

    private JTextField makeTextField() {
        JTextField tf = new JTextField(20);
        tf.setFont(new Font("Arial", Font.PLAIN, 24));
        tf.setPreferredSize(new Dimension(300, 50));
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.BLACK);
        tf.setCaretColor(Color.BLACK);
        tf.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 128), 2, true));
        return tf;
    }

    private JPasswordField makePasswordField() {
        JPasswordField pf = new JPasswordField(20);
        pf.setFont(new Font("Arial", Font.PLAIN, 24));
        pf.setPreferredSize(new Dimension(300, 50));
        pf.setBackground(Color.WHITE);
        pf.setForeground(Color.BLACK);
        pf.setCaretColor(Color.BLACK);
        pf.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 128), 2, true));
        pf.setEchoChar('•');
        return pf;
    }

    private JButton makeButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 24));
        btn.setBackground(new Color(0, 0, 128));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(300, 60));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    // === Sign-Up Logic ===
    private void signUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (insertUserToDatabase(username, password)) {
                JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new Login();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error creating account!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // === Database Insertion ===
    private boolean insertUserToDatabase(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/mistoremanagement";
        String dbUsername = "root";
        String dbPassword = "root"; // Update if needed
        String query = "INSERT INTO login (username, password) VALUES (?, ?)";

        try (Connection con = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpSystemUI::new);
    }
}
