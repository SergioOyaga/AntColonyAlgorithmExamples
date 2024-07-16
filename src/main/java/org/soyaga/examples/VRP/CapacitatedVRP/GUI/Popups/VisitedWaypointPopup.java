package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Popups;

import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.VisitedWaypoint;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * Class that displays a popup to update the VisitedWaypoint attributes.
 */
public class VisitedWaypointPopup {
    /**
     * Function that displays the popup
     */
    public void display(VisitedWaypoint waypoint) {
        // Create two text fields for numeric input
        JTextField quantityField = new JTextField(String.valueOf(waypoint.getQuantity()));

        // Add double doc filter
        ((PlainDocument)quantityField.getDocument()).setDocumentFilter(new DoubleFilter());

        // Create a panel to hold the components
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel(waypoint.getID()));
        panel.add(new JLabel("Quantity delivered"));
        panel.add(quantityField);

        // Show the popup and handle the user's choice
        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "VisitedWaypoint Info",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            waypoint.setQuantityAndNotify(Double.parseDouble(quantityField.getText()));
        }
    }

}
