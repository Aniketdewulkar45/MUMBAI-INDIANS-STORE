package mi.store.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.net.URL;

public class Login extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/mistoremanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;

    public Login() {
        setTitle("Login - Mumbai Indians Merchandise Store");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Gradient Background ===
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

        // === Header ===
        JLabel headerLabel = new JLabel("WELCOME BACK TO OUR STORE!", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 42));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));
        gradientPanel.add(headerLabel, BorderLayout.NORTH);

        // === Center Box (White Panel) ===
        JPanel boxPanel = new JPanel(new GridLayout(1, 2));
        boxPanel.setPreferredSize(new Dimension(1100, 700));
        boxPanel.setBackground(Color.WHITE);
        boxPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5, true));
        gradientPanel.add(boxPanel, BorderLayout.CENTER);

        // === Left Form Panel ===
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

        // Show Password
        gbc.gridy++;
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(new Font("Arial", Font.BOLD, 18));
        showPasswordCheckBox.setBackground(Color.WHITE);
        showPasswordCheckBox.setForeground(new Color(0, 0, 128));
        showPasswordCheckBox.addActionListener(e -> {
            boolean show = showPasswordCheckBox.isSelected();
            passwordField.setEchoChar(show ? (char) 0 : '•');
        });
        formPanel.add(showPasswordCheckBox, gbc);

        // Login Button
        gbc.gridy++;
        JButton loginButton = makeButton("LOGIN");
        loginButton.addActionListener(e -> loginUser());
        formPanel.add(loginButton, gbc);

        // Sign Up Button
        gbc.gridy++;
        JButton signUpButton = makeButton("SIGN UP");
        signUpButton.addActionListener(e -> {
            new SignUpSystemUI();
            dispose();
        });
        formPanel.add(signUpButton, gbc);

        // Forgot Password Button
        gbc.gridy++;
        JButton forgotButton = makeButton("RESET PASSWORD");
        forgotButton.addActionListener(e -> forgotPassword());
        formPanel.add(forgotButton, gbc);

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

    // === Login Logic ===
    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (isValidUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new HomePage();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === Database Validation ===
    private boolean isValidUser(String username, String password) {
        String query = "SELECT * FROM login WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // === Forgot Password ===
    private void forgotPassword() {
        String username = JOptionPane.showInputDialog(this, "Enter your username:");
        if (username != null && !username.trim().isEmpty()) {
            resetPassword(username);
        } else {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetPassword(String username) {
        String query = "SELECT * FROM login WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String newPassword = JOptionPane.showInputDialog(this, "Enter your new password:");
                if (newPassword != null && !newPassword.trim().isEmpty()) {
                    String updateQuery = "UPDATE login SET password = ? WHERE username = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, newPassword);
                        updateStmt.setString(2, username);
                        updateStmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Password reset successfully!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Username not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}
