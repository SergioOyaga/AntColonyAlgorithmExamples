package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints;

import lombok.Getter;
import lombok.Setter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Class that represents a VirtualTruck Waypoint
 */
@Getter
public class TruckWaypoint extends GUIWaypoint {
    /**
     * Double with the quantity that the truck is able to deliver.
     */
    private double capacity;
    /**
     * Double with the current load quantity.
     */
    private double currentLoad;
    /**
     * Double with the fixed cost of using this VirtualTruck.
     */
    private double fixedCost;
    /**
     * Double with the variable cost while using this VirtualTruck.
     */
    private double variableCost;
    /**
     * Color used to print the route of this truck.
     */
    private final Color routeColor;
    /**
     * Double with the delivered quantity.
     */
    @Setter
    private double deliveredQuantity;
    /**
     * Double with the ToDeliver quantity.
     */
    @Setter
    private double toDeliverQuantity;
    /**
     * Double with the visited waypoint number.
     */
    @Setter
    private double visitedWaypointNumber;
    /**
     * Double with the ToVisit waypoint number.
     */
    @Setter
    private double toVisitWaypointNumber;
    /**
     * Double with the loads made.
     */
    @Setter
    private double loadedNumber;
    /**
     * Double with the pending loads.
     */
    @Setter
    private double toLoadNumber;

    /**
     * Constructor.
     * @param ID String with the waypoint ID.
     * @param coord GeoPosition with the Truck current position.
     * @param capacity Double with the truck capacity.
     * @param currentLoad Double with the remaining current load of the truck.
     * @param fixedCost Double with the fixed cost of using this truck.
     * @param variableCost Double with the variable cost of using this truck.
     * @param img BufferedImage to be used.
     * @param routeColor Color to be used to draw the route.
     */
    public TruckWaypoint(String ID, GeoPosition coord, Double capacity, Double currentLoad, Double fixedCost, Double variableCost,
                         BufferedImage img, Color routeColor) {
        super(ID, coord);
        this.capacity = capacity;
        this.currentLoad = currentLoad;
        this.fixedCost = fixedCost;
        this.variableCost = variableCost;
        this.image = img;
        this.routeColor = routeColor;
        this.deliveredQuantity = 0.;
        this.toDeliverQuantity = 0.;
        this.visitedWaypointNumber = 0.;
        this.toVisitWaypointNumber = 0.;
    }

    /**
     * Setter with fire propertyChangeEvent.
     * @param capacity double with the new capacity.
     */
    public void setCapacityAndNotify(double capacity) {
        if(this.capacity!=capacity) {
            this.capacity = capacity;
            firePropertyChange("capacityTruck", this.getID(), this.getCapacity());
        }
    }

    /**
     * Setter with fire propertyChangeEvent.
     * @param currentLoad double with the new currentLoad.
     */
    public void setCurrentLoadAndNotify(double currentLoad) {
        if(this.currentLoad!=currentLoad) {
            this.currentLoad = currentLoad;
            firePropertyChange("currentLoadTruck", this.getID(), this.getCurrentLoad());
        }
    }

    /**
     * Setter with fire propertyChangeEvent.
     * @param fixedCost double with the new fixed cost.
     */
    public void setFixedCostAndNotify(double fixedCost) {
        if(this.fixedCost!=fixedCost) {
            this.fixedCost = fixedCost;
            firePropertyChange("fixedCostTruck", this.getID(), this.getFixedCost());
        }
    }

    /**
     * Setter with fire propertyChangeEvent.
     * @param variableCost double with the new variable cost.
     */
    public void setVariableCostAndNotify(double variableCost) {
        if (this.variableCost != variableCost){
            this.variableCost = variableCost;
            firePropertyChange("variableCostTruck", this.getID(), this.getVariableCost());
        }
    }
}
