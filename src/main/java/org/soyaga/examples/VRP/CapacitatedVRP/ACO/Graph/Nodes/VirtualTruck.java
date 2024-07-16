package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes;

import lombok.Getter;
import lombok.Setter;
import org.soyaga.aco.world.Graph.Elements.Node;

/**
 * Class that represents a VirtualTruck.
 */
@Getter @Setter
public class VirtualTruck extends Node {
    /**
     * ACONode with the initial position of the truck.
     */
    private final ACONode startingPosition;
    /**
     * ACONode with the current position of the truck.
     */
    private ACONode currentPosition;
    /**
     * Double with the capacity of the truck.
     */
    private Double capacity;
    /**
     * Double with the initial load of the truck.
     */
    private Double initialLoad;
    /**
     * Double with the current load of the truck.
     */
    private Double currentLoad;
    /**
     * Double with the fixed cost of utilizing this VirtualTruck.
     */
    private Double fixedCost;
    /**
     * Double with the variable cost (price/meter)
     */
    private Double variableCost;

    /**
     * Constructor.
     *
     * @param ID           String with the truck ID.
     * @param startingPosition ACONode with the starting location of the truck.
     * @param capacity Double with the Capacity of the VirtualTruck.
     * @param initialLoad Double with the initial load of the truck.
     * @param fixedCost Double with the fixed cost.
     * @param variableCost Double with the variable cost.
     */
    public VirtualTruck(String ID, ACONode startingPosition, Double capacity, Double initialLoad, Double fixedCost,
                        Double variableCost) {
        super(ID);
        this.startingPosition = startingPosition;
        this.currentPosition = startingPosition;
        this.capacity = capacity;
        this.initialLoad = initialLoad;
        this.currentLoad = initialLoad;
        this.fixedCost = fixedCost;
        this.variableCost = variableCost;
    }

    /**
     * Function that resets this truck to its initial state.
     */
    public void resetTruck(){
        this.currentPosition = this.startingPosition;
        this.currentLoad = this.initialLoad;
    }

    /**
     * Function that reloads the Truck.
     */
    public void loadTruck(){
        this.currentLoad = this.capacity;
    }
}
