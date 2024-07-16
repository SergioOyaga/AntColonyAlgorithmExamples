package org.soyaga.examples.VRP.CapacitatedVRP.ACO.ProblemStructures;

import lombok.Getter;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Edges.ACOEdge;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.ACONode;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.CDCNode;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.WaypointNode;

import java.util.ArrayList;

/**
 * Class that represents a route that a Truck follows.
 */
public class ACORoute {
    /**
     * ArrayList<ACONode> with to visit route Nodes.
     */
    @Getter
    private final ArrayList<ACONode> toVisitNodes;
    /**
     * ArrayList<ACOEdge> with to visit route Edges.
     */
    @Getter
    private final ArrayList<ACOEdge> toVisitEdges;


    /**
     * Constructor.
     */
    public ACORoute() {
        this.toVisitNodes = new ArrayList<>();
        this.toVisitEdges = new ArrayList<>();
    }

    /**
     * Function that adds an ACONode to the to-visit nodes. Defines the new route along with the "toVisitEdges" attribute
     * @param nodeToVisit ACONode to be added.
     */
    public void addToVisitNode(ACONode nodeToVisit){
        if(nodeToVisit instanceof CDCNode | nodeToVisit instanceof WaypointNode) {
            this.toVisitNodes.add(nodeToVisit);
        }
    }

    /**
     * Function that adds an Edge to the to-visit edges. Defines the new route along with the "toVisitNodes" attribute
     * @param edgeToVisit ACOEdge to be added.
     */
    public void addToVisitEdge(ACOEdge edgeToVisit){
        if ((edgeToVisit.getOrigin() instanceof CDCNode | edgeToVisit.getOrigin() instanceof WaypointNode) &
                (edgeToVisit.getDestination() instanceof CDCNode | edgeToVisit.getDestination() instanceof  WaypointNode)) {
            this.toVisitEdges.add(edgeToVisit);
        }
    }

    /**
     * Function overridden equals. It allows comparing Solutions via its visited nodes and utilized edges.
     *
     * @param obj Another Solution to compare with.
     * @return Boolean, <i>True</i> if both have equal Edges, <i>False</i> otherwise.
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj instanceof ACORoute) {
            return this.toVisitNodes.equals(((ACORoute)obj).getToVisitNodes()) &&
                    this.toVisitEdges.equals(((ACORoute)obj).getToVisitEdges());
        }
        return false;
    }
}
