package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Class that extends JPanel and has a Buffered image that can be updated dynamically.
 */
public class PheromonePanel extends JPanel {
    /**
     * BufferedImage that can be updated.
     */
    private BufferedImage image;

    /**
     * Constructor Sets a default image for the initial UI display.
     */
    public PheromonePanel() {
        try {
            this.image = ImageIO.read(Objects.requireNonNull(PheromonePanel.class.getResource("/VRP/Images/Default_Image.png")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Function that sets the image to be displayed and repaints the object.
     * @param image BufferedImage to be displayed.
     */
    public void setImage(BufferedImage image){
        this.image = image;
        this.repaint();
    }

    /**
     * Function call in the graphical context to display the component.
     * @param g  the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(this.image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH), 0, 0, this); // Draw the image
    }
}

