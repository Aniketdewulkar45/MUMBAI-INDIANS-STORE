package mi.store.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends JFrame {
    private final List<Product> productList = new ArrayList<>();
    private final JPanel productGridPanel;
    private final JScrollPane scrollPane;
    private final JTextField searchField;
    private List<Product> cartList = new ArrayList<>();
    private final JLabel totalAmountLabel;
    private final JPanel cartItemsPanel;

    public HomePage() {
        setTitle("Mumbai Indians Merchandise Store - Home");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Gradient background
        GradientPanel backgroundPanel = new GradientPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Top Panel (Header)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 0, 128));

        JLabel logoLabel = new JLabel("Mumbai Indians Merchandise Store", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 28));
        logoLabel.setForeground(Color.WHITE);
        topPanel.add(logoLabel, BorderLayout.CENTER);

        // Search Bar
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterProducts(searchField.getText().trim());
            }
        });

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        // Fetch products
        fetchProductsFromDatabase();

        // Product Grid
        productGridPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        productGridPanel.setOpaque(false);
        displayProducts(productList);

        // Scrollable product view
        scrollPane = new JScrollPane(productGridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // Shopping Cart Sidebar
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setOpaque(false);

        JLabel cartLabel = new JLabel("Shopping Cart", SwingConstants.CENTER);
        cartLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Panel to hold cart items dynamically
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        JScrollPane cartScrollPane = new JScrollPane(cartItemsPanel);

        cartPanel.add(cartLabel, BorderLayout.NORTH);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);

        // Proceed and Clear Cart Buttons
        JButton proceedButton = new JButton("Proceed to Billing");
        proceedButton.addActionListener(e -> {
            if (!cartList.isEmpty()) {
                new BillingPage(cartList);
                dispose();
            } else {
                JOptionPane.showMessageDialog(HomePage.this, "Your cart is empty!");
            }
        });

        JButton clearCartButton = new JButton("Clear Cart");
        clearCartButton.addActionListener(e -> {
            cartList.clear();
            refreshCartUI();
            updateTotalAmount();
        });

        totalAmountLabel = new JLabel("Total: ₹0.00");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(proceedButton);
        buttonPanel.add(clearCartButton);
        cartPanel.add(buttonPanel, BorderLayout.SOUTH);
        cartPanel.add(totalAmountLabel, BorderLayout.NORTH);

        backgroundPanel.add(cartPanel, BorderLayout.EAST);
        setVisible(true);
    }

    private void fetchProductsFromDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mistoremanagement", "root", "root");
             Statement stmt = connection.createStatement()) {

            ResultSet resultSet = stmt.executeQuery("SELECT product_name, price, image_url FROM products");

            while (resultSet.next()) {
                productList.add(new Product(
                        resultSet.getString("product_name"),
                        resultSet.getString("price"),
                        resultSet.getString("image_url")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching products from database.");
        }
    }

    private void displayProducts(List<Product> products) {
        productGridPanel.removeAll();
        for (Product product : products) {
            productGridPanel.add(createProductPanel(product));
        }
        productGridPanel.revalidate();
        productGridPanel.repaint();
    }

    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getProductName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        displayProducts(filteredList);
    }

    private JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        productPanel.setPreferredSize(new Dimension(400, 450));

        JLabel productImageLabel = new JLabel(new ImageIcon(product.getImageUrl()));
        productPanel.add(productImageLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        JLabel nameLabel = new JLabel(product.getProductName());
        JLabel priceLabel = new JLabel("₹" + product.getPrice());

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> {
            cartList.add(product);
            refreshCartUI();
            updateTotalAmount();
        });

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(addToCartButton);
        productPanel.add(infoPanel, BorderLayout.SOUTH);

        return productPanel;
    }

    private void refreshCartUI() {
        cartItemsPanel.removeAll();
        for (Product product : cartList) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            JLabel itemLabel = new JLabel(product.getProductName() + " - ₹" + product.getPrice());
            JButton removeButton = new JButton("Remove");

            removeButton.addActionListener(e -> {
                cartList.remove(product);
                refreshCartUI();
                updateTotalAmount();
            });

            itemPanel.add(itemLabel, BorderLayout.WEST);
            itemPanel.add(removeButton, BorderLayout.EAST);
            cartItemsPanel.add(itemPanel);
        }
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private void updateTotalAmount() {
        double totalAmount = 0.0;
        for (Product product : cartList) {
            totalAmount += Double.parseDouble(product.getPrice().replaceAll("[^0-9.]", ""));
        }
        totalAmountLabel.setText("Total: ₹" + totalAmount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomePage::new);
    }
}
