package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Edges;

import lombok.Getter;
import lombok.Setter;
import org.soyaga.aco.world.Graph.Elements.Edge;
import org.soyaga.aco.world.Graph.Elements.Node;

/**
 * Class that represents an Edge.
 */
public class ACOEdge extends Edge {
    /**
     * Double with the real-Edge distance.
     */
    @Getter @Setter
    private Double distanceMeters;
    /**
     * Double with the real-Edge duration.
     */
    @Getter @Setter
    private Double durationSeconds;

    /**
     * Constructor.
     *
     * @param origin           Node origin.
     * @param destination      Node destination.
     * @param distance         Double with the "distance."
     * @param initialPheromone Double with the "pheromone."
     * @param distanceMeters  Double with the real distance in meters.
     * @param durationSeconds  Double with the real duration in seconds.
     */
    public ACOEdge(Node origin, Node destination, Double distance, Double initialPheromone, Double distanceMeters, Double durationSeconds) {
        super(origin, destination, distance, initialPheromone);
        this.distanceMeters = distanceMeters;
        this.durationSeconds = durationSeconds;
    }
}
