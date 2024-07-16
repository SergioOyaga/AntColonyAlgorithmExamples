package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Popups;

import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.ToVisitWaypoint;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * Class that displays a popup to update the ToVisitWaypoint attributes.
 */
 public class ToVisitWaypointPopup {
    /**
     * Function that displays the popup
     *
     * @return true if accepted, false otherwise
     */
    public boolean display(ToVisitWaypoint waypoint) {
        // Create two text fields for numeric input
        JTextField quantityField = new JTextField(String.valueOf(waypoint.getQuantity()));

        // Add double dc filter
        ((PlainDocument)quantityField.getDocument()).setDocumentFilter(new DoubleFilter());

        // Create a panel to hold the components
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel(waypoint.getID()));
        panel.add(new JLabel("Quantity to deliver"));
        panel.add(quantityField);

        // Show the popup and handle the user's choice
        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "ToVisitWaypoint Info",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            waypoint.setQuantityAndNotify(Double.parseDouble(quantityField.getText()));
            return true;
        }
        return false;
    }
}
