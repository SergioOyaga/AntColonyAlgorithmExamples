package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints;

import lombok.Getter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.image.BufferedImage;

/**
 * Class that represents a route Waypoint
 */
public class ToVisitWaypoint extends GUIWaypoint {
    /**
     * Double with the quantity to be delivered.
     */
    @Getter
    private Double quantity;

    /**
     * Constructor.
     * @param ID String with the waypoint ID.
     * @param coord the geo coordinate
     * @param img BufferedImage with the waypoint to visit image.
     */
    public ToVisitWaypoint(String ID, GeoPosition coord, Double quantity, BufferedImage img){
        super(ID,coord);
        this.quantity = quantity;
        this.image = img;
    }

    /**
     * Setter with fire propertyChangeEvent.
     * @param quantity double with the new quantity.
     */
    public void setQuantityAndNotify(double quantity) {
        if(this.quantity!=quantity) {
            this.quantity = quantity;
            firePropertyChange("quantityToVisit", this.getID(), this.getQuantity());
        }
    }
}
