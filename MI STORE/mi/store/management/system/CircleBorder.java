package mi.store.management.system;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

/**
 * Custom Border class to create a circular border.
 */
public class CircleBorder implements Border {

    private final Color borderColor;  // Color of the border
    private final int thickness;      // Thickness of the border

    /**
     * Constructor to create a circular border.
     * 
     * @param borderColor The color of the border.
     * @param thickness The thickness of the border.
     */
    public CircleBorder(Color borderColor, int thickness) {
        this.borderColor = borderColor;
        this.thickness = thickness;
    }

    /**
     * Gets the insets for the border. The insets define the space around the component
     * where the border is drawn.
     * 
     * @param c The component to which the border is applied.
     * @return The insets defining the space around the component.
     */
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }

    /**
     * Determines whether the border is opaque or not.
     * 
     * @return True if the border is opaque, false otherwise.
     */
    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    /**
     * Paints the circular border on the given component.
     * 
     * @param c The component to paint the border for.
     * @param g The graphics object used for painting.
     * @param x The x-coordinate of the top-left corner where the border starts.
     * @param y The y-coordinate of the top-left corner where the border starts.
     * @param width The width of the component.
     * @param height The height of the component.
     */
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(borderColor);  // Set the color for the border
        // Draw the oval border with the specified thickness
        g.drawOval(x + thickness, y + thickness, width - 2 * thickness, height - 2 * thickness);
    }
}
