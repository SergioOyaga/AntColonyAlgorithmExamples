package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Popups;

import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.TruckWaypoint;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * Class that displays a popup to update the TruckWaypoint attributes.
 */
public class TruckWaypointPopup {
    /**
     * Function that displays the popup
     */
    public void display(TruckWaypoint waypoint) {
        // Create two text fields for numeric input
        JTextField capacity = new JTextField(String.valueOf(waypoint.getCapacity()));
        JTextField currentLoad = new JTextField(String.valueOf(waypoint.getCurrentLoad()));
        JTextField fixedCost = new JTextField(String.valueOf(waypoint.getFixedCost()));
        JTextField variableCost = new JTextField(String.valueOf(waypoint.getVariableCost()*1000));

        // Add double dc filter
        DoubleFilter filter =new DoubleFilter();
        ((PlainDocument)capacity.getDocument()).setDocumentFilter(filter);
        ((PlainDocument)currentLoad.getDocument()).setDocumentFilter(filter);
        ((PlainDocument)fixedCost.getDocument()).setDocumentFilter(filter);
        ((PlainDocument)variableCost.getDocument()).setDocumentFilter(filter);

        // Create a panel to hold the components
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel(waypoint.getID()));
        panel.add(new JLabel("Capacity"));
        panel.add(capacity);
        panel.add(new JLabel("Current Load"));
        panel.add(currentLoad);
        panel.add(new JLabel("Fixed Cost $"));
        panel.add(fixedCost);
        panel.add(new JLabel("Variable Cost $/Km"));
        panel.add(variableCost);

        // Show the popup and handle the user's choice
        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Truck Info",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            waypoint.setCapacityAndNotify(Double.parseDouble(capacity.getText()));
            waypoint.setCurrentLoadAndNotify(Double.parseDouble(currentLoad.getText()));
            waypoint.setFixedCostAndNotify(Double.parseDouble(fixedCost.getText()));
            waypoint.setVariableCostAndNotify(Double.parseDouble(variableCost.getText())/1000.);
            }
    }

}
