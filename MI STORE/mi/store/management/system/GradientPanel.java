package mi.store.management.system;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        // Define gradient colors
        Color startColor = new Color(0, 51, 153); // Dark Blue
        Color endColor = new Color(51, 153, 255); // Light Blue

        GradientPaint gradientPaint = new GradientPaint(0, 0, startColor, 0, height, endColor);
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, width, height);
    }
}
