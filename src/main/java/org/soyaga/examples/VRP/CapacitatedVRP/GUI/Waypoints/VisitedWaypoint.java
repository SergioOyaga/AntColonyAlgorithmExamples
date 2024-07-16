package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints;

import lombok.Getter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.image.BufferedImage;

/**
 * Class that represents a visited Waypoint
 */
@Getter
public class VisitedWaypoint extends GUIWaypoint {
    /**
     * Double with the quantity to be delivered.
     */
    private Double quantity;
    /**
     * VirtualTruck that delivered in this visited waypoint.
     */
    private final String truckID;

    /**
     * Constructor.
     * @param ID String with the waypoint ID.
     * @param coord the geo coordinate
     * @param quantity double with the quantity delivered.
     * @param truckID  String with the truckID that delivered in this visited waypoint.
     * @param img BufferedImage with the waypoint visited image.
     */
    public VisitedWaypoint(String ID, GeoPosition coord, Double quantity, String truckID,BufferedImage img){
        super(ID,coord);
        this.quantity = quantity;
        this.truckID = truckID;
        this.image = img;
    }

    /**
     * Setter with fire propertyChangeEvent.
     * @param quantity double with the new quantity.
     */
    public void setQuantityAndNotify(double quantity) {
        if(this.quantity!=quantity) {
            this.quantity = quantity;
            firePropertyChange("quantityVisited", this.truckID, this.getQuantity());
        }
    }
}
