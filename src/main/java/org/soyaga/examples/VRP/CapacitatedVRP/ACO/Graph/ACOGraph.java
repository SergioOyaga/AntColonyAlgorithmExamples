package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph;

import org.soyaga.aco.world.Graph.Elements.Node;
import org.soyaga.aco.world.Graph.GenericGraph;

import java.util.HashMap;

/**
 * Identical to GenericGraph, but this class allows getting the nodes as objects with ID to edit the world in realtime.
 */
public class ACOGraph extends GenericGraph {
    /**
     * Constructor. Initializes an empty GenericGraph.
     *
     * @param initialPheromone Double with the initial amount of pheromone in the edges.
     */
    public ACOGraph(Double initialPheromone) {
        super(initialPheromone);
    }

    /**
     * Constructor.
     *
     * @param nodes            HashMap{@literal <Object, Node>}.
     * @param initialPheromone Double with the initial pheromone of the Edge.
     */
    public ACOGraph(HashMap<Object, Node> nodes, Double initialPheromone) {
        super(nodes, initialPheromone);
    }

    /**
     * Function that gets the Nodes object, so we can edit the Graph structure inplace.
     * @return HashMap<Object, Node> with the node structure.
     */
    public HashMap<Object, Node> getNodesObject(){
        return this.nodes;
    }

    /**
     * Function that removes a node from the graph and all the related edges
     * @param ID String with the node to remove
     */
    public void removeNode(String ID){
        Node nodeToRemove = this.getNode(ID);
        this.nodes.remove(ID); // Remove it from the graph
        for(Node node: this.getNodes()){ // Remove it as other nodes connections
            node.getOutputEdges().removeIf(edge-> edge.getDestination().equals(nodeToRemove));
        }
    }

}
