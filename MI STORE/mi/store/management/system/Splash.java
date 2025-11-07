package mi.store.management.system;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.awt.event.*;

public class Splash extends JFrame {
    private int titleXPosition = -600;  // Initial position of the title off-screen on the left
    private final int titleSpeed = 5;   // Speed of the sliding effect

    Splash() {
        // Set up the window properties
        setTitle("Mumbai Indians Merchandise Store");
        setLayout(new BorderLayout());
        setSize(1950, 900);
        setLocation(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Title panel (same as before)
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color startColor = new Color(0, 0, 255); // Bright Blue
                Color endColor = new Color(0, 0, 139);   // Dark Blue
                GradientPaint gradient = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("serif", Font.PLAIN, 60));
                g2d.drawString("WELCOME TO MUMBAI INDIANS MERCHANDISE STORE", titleXPosition, getHeight() / 2);
            }
        };
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(getWidth(), 100));
        add(titlePanel, BorderLayout.NORTH);

        // Content panel (same as before)
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color startColor = new Color(0, 0, 255); // Bright Blue
                Color endColor = new Color(0, 0, 139);   // Dark Blue
                GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setLayout(new BorderLayout());

        // Loading image
        final URL imageUrl = getClass().getResource("/icons/mumbai-indians-uniform-and-helmet.png");
        if (imageUrl != null) {
            ImageIcon i1 = new ImageIcon(imageUrl);
            Image i2 = i1.getImage();
            Image scaledImage = i2.getScaledInstance(getWidth(), getHeight() - titlePanel.getHeight(), Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(imageLabel, BorderLayout.CENTER);
        } else {
            System.err.println("Image not found. Ensure the image is in the correct location.");
        }

        // Button to continue
        JButton clickhere = new JButton("CLICK HERE TO CONTINUE");
        clickhere.setBackground(Color.BLUE);
        clickhere.setForeground(Color.WHITE);
        clickhere.setFont(new Font("Arial", Font.BOLD, 22));
        clickhere.setPreferredSize(new Dimension(350, 60));
        clickhere.addActionListener(e -> {
            setVisible(false);  // Close splash screen
            new Login();  // Open the login screen
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(clickhere, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);

        // Timer for sliding effect
        Timer timer = new Timer(20, e -> {
            titleXPosition += titleSpeed;
            if (titleXPosition > getWidth()) {
                titleXPosition = -600;  // Reset to left side
            }
            titlePanel.repaint();
        });
        timer.start();

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Splash());
    }
}
