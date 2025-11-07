package mi.store.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class BillingPage extends JFrame {
    private final List<Product> cartList;
    private JLabel totalAmountLabel;

    public BillingPage(List<Product> cartList) {
        this.cartList = cartList;

        setTitle("Billing Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen adaptive
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background Panel
        GradientPanel backgroundPanel = new GradientPanel();
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(backgroundPanel);

        // MAIN CONTAINER (3 equal parts)
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        mainPanel.setOpaque(false);

        // ---------------- LEFT PANEL ----------------
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel leftTitle = new JLabel("<html><u><b>Billing Details</b></u></html>", SwingConstants.CENTER);
        leftTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        leftTitle.setForeground(Color.WHITE);
        // reduce vertical gap so fields start just below the heading
        leftTitle.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        leftPanel.add(leftTitle, BorderLayout.NORTH);

        JPanel customerPanel = new JPanel(new GridBagLayout());
        customerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JTextField nameField = createPlaceholderField("Enter Name");
        JTextField contactField = createPlaceholderField("Enter Contact");
        contactField.addKeyListener(digitOnlyListener());
        JTextField addressField = createPlaceholderField("Enter Address");
        JTextField stateField = createPlaceholderField("Enter State/City");
        JTextField landmarkField = createPlaceholderField("Enter Landmark");
        JTextField pincodeField = createPlaceholderField("Enter Pincode");
        pincodeField.addKeyListener(digitOnlyListener());
        JTextField deliveryField = createPlaceholderField("Delivery Instructions");

        addField(customerPanel, "Name:", nameField, gbc);
        addField(customerPanel, "Contact:", contactField, gbc);
        addField(customerPanel, "Address:", addressField, gbc);
        addField(customerPanel, "State/City:", stateField, gbc);
        addField(customerPanel, "Landmark:", landmarkField, gbc);
        addField(customerPanel, "Pincode:", pincodeField, gbc);
        addField(customerPanel, "Delivery Note:", deliveryField, gbc);

        leftPanel.add(customerPanel, BorderLayout.CENTER);

        // Create buttons (will be placed in the right Billing Summary panel)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JButton generateBillButton = createStyledButton("Generate Bill");
        JButton feedbackButton = createStyledButton("Feedback");
        // Make buttons expand to full available width and add spacing between them
        generateBillButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateBillButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        feedbackButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        buttonPanel.add(generateBillButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(feedbackButton);

        generateBillButton.addActionListener(e -> {
            String billingDetails = String.format(
                    "Customer Name: %s\nContact: %s\nAddress: %s\nState/City: %s\nLandmark: %s\nPincode: %s\nDelivery Note: %s\n\nItems:\n%s\n\nTotal: Rs. %.2f\nPayment Method: %s",
                    nameField.getText(), contactField.getText(), addressField.getText(),
                    stateField.getText(), landmarkField.getText(), pincodeField.getText(),
                    deliveryField.getText(), getItemsString(), calculateTotalAmount(),
                    "Cash on Delivery"
            );
            JOptionPane.showMessageDialog(this, billingDetails, "Bill Generated", JOptionPane.INFORMATION_MESSAGE);
        });

        feedbackButton.addActionListener(e -> {
            new FeedbackForm();
            dispose();
        });

        // ---------------- CENTER PANEL ----------------
        JTable table = createBillingTable();
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(255, 153, 51));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "Cart Summary",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 20),
                Color.WHITE
        ));
        scrollPane.setOpaque(false);
        mainPanel.add(leftPanel);
        mainPanel.add(scrollPane);

        // ---------------- RIGHT PANEL ----------------
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel rightTitle = new JLabel("<html><u><b>Billing Summary</b></u></html>", SwingConstants.CENTER);
        // Increased heading size to match left panel
        rightTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        rightTitle.setForeground(Color.WHITE);
        rightTitle.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        rightTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        totalAmountLabel = new JLabel("Total: Rs. " + String.format("%.2f", calculateTotalAmount()));
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalAmountLabel.setForeground(Color.WHITE);
        totalAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JComboBox<String> paymentComboBox = new JComboBox<>(new String[]{
                "Cash on Delivery", "Debit/Credit Card", "Google Pay", "Netbanking"
        });
        paymentComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        paymentComboBox.setMaximumSize(new Dimension(250, 40));

        JLabel paymentLabel = new JLabel("Payment Method:");
        paymentLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        paymentLabel.setForeground(Color.WHITE);
        paymentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(rightTitle);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(totalAmountLabel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(paymentLabel);
        rightPanel.add(paymentComboBox);
        // push content up, leave some space above the buttons, then add buttons full width
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(buttonPanel);
        rightPanel.add(Box.createVerticalStrut(12));

        mainPanel.add(rightPanel);

        // Add all 3 sections
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JTable createBillingTable() {
        String[] columns = {"Item", "Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Product product : cartList) {
            model.addRow(new Object[]{product.getProductName(), product.getPrice()});
        }
        return new JTable(model);
    }

    private void addField(JPanel panel, String labelText, JTextField field, GridBagConstraints gbc) {
         gbc.gridx = 0;
         JLabel label = new JLabel(labelText);
        // make labels slightly larger for better readability
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
         label.setForeground(Color.WHITE);
         panel.add(label, gbc);

         gbc.gridx = 1;
         panel.add(field, gbc);
         gbc.gridy++;
     }

     private JTextField createPlaceholderField(String placeholder) {
         JTextField tf = new JTextField(18);
        // increase field font size to match larger labels
         tf.setFont(new Font("Segoe UI", Font.PLAIN, 18));
         tf.setForeground(Color.GRAY);
         tf.setText(placeholder);
         tf.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));

         tf.addFocusListener(new FocusAdapter() {
             public void focusGained(FocusEvent e) {
                 if (tf.getText().equals(placeholder)) {
                     tf.setText("");
                     tf.setForeground(Color.BLACK);
                 }
             }

             public void focusLost(FocusEvent e) {
                 if (tf.getText().isEmpty()) {
                     tf.setText(placeholder);
                     tf.setForeground(Color.GRAY);
                 }
             }
         });
         return tf;
     }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setBackground(new Color(255, 102, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(255, 153, 0));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(255, 102, 0));
            }
        });
        return btn;
    }

    private KeyAdapter digitOnlyListener() {
        return new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        };
    }

    private String getItemsString() {
        StringBuilder sb = new StringBuilder();
        for (Product p : cartList) {
            sb.append(p.getProductName()).append(" - ").append(p.getPrice()).append("\n");
        }
        return sb.toString();
    }

    private double calculateTotalAmount() {
        double total = 0;
        for (Product p : cartList) {
            try {
                total += Double.parseDouble(p.getPrice().replaceAll("[^0-9.]", ""));
            } catch (Exception ignored) {}
        }
        return total;
    }

    private class GradientPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(0, 51, 102),
                    getWidth(), getHeight(), new Color(0, 153, 255)
            );
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BillingPage(new ArrayList<>()));
    }
}
