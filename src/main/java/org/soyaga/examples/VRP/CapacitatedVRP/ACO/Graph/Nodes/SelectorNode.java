package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes;

import org.soyaga.aco.world.Graph.Elements.Edge;
import org.soyaga.aco.world.Graph.Elements.Node;

import java.util.HashSet;

/**
 * Class that represents a Selector node. This node is the starting point of the Ant to create a route. It selects a
 * VirtualTruck and builds the route from there.
 */
public class SelectorNode extends Node {
    /**
     * Constructor.
     *
     * @param ID Object with the node ID.
     */
    public SelectorNode(Object ID) {
        super(ID);
    }

    /**
     * Constructor.
     *
     * @param ID          Object with the node ID.
     * @param outputEdges HashSet{@literal <Edge>} with the output edges.
     */
    public SelectorNode(Object ID, HashSet<Edge> outputEdges) {
        super(ID, outputEdges);
    }
}
