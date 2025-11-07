package mi.store.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

public class FeedbackForm extends JFrame {
    public FeedbackForm() {
        setTitle("Customer Feedback Form");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Apply gradient background
        GradientPanel feedbackPanel = new GradientPanel();
        feedbackPanel.setLayout(new BorderLayout());
        setContentPane(feedbackPanel);

        // Title label
        JLabel titleLabel = new JLabel("Customer Feedback", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        feedbackPanel.add(titleLabel, BorderLayout.NORTH);

       // Form Panel
JPanel formPanel = new JPanel(new GridLayout(8, 1, 10, 10));
formPanel.setOpaque(false);
formPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.WHITE, 2),
        "Please provide your feedback",
        0, 0,
        new Font("Arial", Font.BOLD, 20),
        Color.WHITE
));


        // Name Field
        JTextField nameField = new JTextField("Enter your name...");
        setTextFieldPlaceholder(nameField);

        // Email Field
        JTextField emailField = new JTextField("Enter your email...");
        setTextFieldPlaceholder(emailField);

        // Feedback Type Dropdown
        String[] feedbackTypes = {"Product", "Service", "Website", "Other"};
        JComboBox<String> feedbackTypeComboBox = new JComboBox<>(feedbackTypes);
        feedbackTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

        // Feedback Text Area
        JTextArea feedbackTextArea = new JTextArea(5, 20);
        feedbackTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        feedbackTextArea.setForeground(Color.GRAY);
        feedbackTextArea.setText("Enter your feedback here...");
        feedbackTextArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (feedbackTextArea.getText().equals("Enter your feedback here...")) {
                    feedbackTextArea.setText("");
                    feedbackTextArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (feedbackTextArea.getText().isEmpty()) {
                    feedbackTextArea.setText("Enter your feedback here...");
                    feedbackTextArea.setForeground(Color.GRAY);
                }
            }
        });

        // Rating Panel
        JPanel ratingPanel = new JPanel();
        ratingPanel.setOpaque(false);
        ratingPanel.setLayout(new FlowLayout());
        JLabel ratingLabel = new JLabel("Rate us (1-5):");
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        ratingLabel.setForeground(Color.WHITE);
        ratingPanel.add(ratingLabel);
        JComboBox<Integer> ratingComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        ratingComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        ratingPanel.add(ratingComboBox);

        // Submit Button
        JButton submitButton = new JButton("Submit Feedback");
        submitButton.setFont(new Font("Arial", Font.BOLD, 20));
        submitButton.setBackground(new Color(50, 150, 255));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createRaisedBevelBorder());

        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String feedback = feedbackTextArea.getText().trim();
            String feedbackType = (String) feedbackTypeComboBox.getSelectedItem();
            int rating = (int) ratingComboBox.getSelectedItem();

            if (!isValidInput(name, email, feedback)) return;

            JOptionPane.showMessageDialog(null, "Thank you for your feedback!\n" +
                    "Name: " + name + "\n" +
                    "Email: " + email + "\n" +
                    "Feedback Type: " + feedbackType + "\n" +
                    "Rating: " + rating + "\n" +
                    "Feedback: " + feedback);

            dispose();
        });

        // Add components to the form panel
        formPanel.add(nameField);
        formPanel.add(emailField);
        formPanel.add(feedbackTypeComboBox);
        formPanel.add(new JScrollPane(feedbackTextArea));
        formPanel.add(ratingPanel);
        formPanel.add(submitButton);

        // Add form panel to feedback window
        feedbackPanel.add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Function to validate user input
    private boolean isValidInput(String name, String email, String feedback) {
        if (name.isEmpty() || name.equals("Enter your name...")) {
            JOptionPane.showMessageDialog(null, "Please enter your name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "Please enter a valid email address.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (feedback.isEmpty() || feedback.equals("Enter your feedback here...")) {
            JOptionPane.showMessageDialog(null, "Please enter your feedback.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Function to check email format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Function to set placeholder behavior for text fields
    private void setTextFieldPlaceholder(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(textField.getToolTipText())) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(textField.getToolTipText());
                    textField.setForeground(Color.GRAY);
                }
            }
        });
        textField.setToolTipText(textField.getText());
    }

    // Custom Gradient Panel for feedback form
    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(0, 0, 139), // Dark Blue
                    getWidth(), getHeight(), new Color(70, 130, 180) // Steel Blue
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FeedbackForm::new);
    }
}
