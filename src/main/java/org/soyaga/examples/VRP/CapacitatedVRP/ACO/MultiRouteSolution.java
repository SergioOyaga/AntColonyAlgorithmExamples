package org.soyaga.examples.VRP.CapacitatedVRP.ACO;

import lombok.Getter;
import org.soyaga.aco.BuilderEvaluator.BuilderEvaluator;
import org.soyaga.aco.Evaluable.Feasibility.FeasibilityFunction;
import org.soyaga.aco.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.aco.Solution;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Edges.ACOEdge;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.ACONode;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.WaypointNode;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.ProblemStructures.ACORoute;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.VirtualTruck;

import java.util.*;

@Getter
public class MultiRouteSolution extends Solution {
    /**
     * ArrayList<VirtualTruck> with all the available Trucks to build the solution.
     */
    private final ArrayList<VirtualTruck> virtualTrucks;
    /**
     * ACOGraph of the world.
     */
    private final HashMap<VirtualTruck, ArrayList<ACORoute>> routesMap;
    /**
     * HashMap<WaypointNode, Double> with the quantity delivered in each node.
     */
    private final HashMap<WaypointNode, Double> deliveredByNode;
    /**
     * HashMap<VirtualTruck,Double> with the visited waypoint nodes with delivery quantity.
     */
    private final HashMap<VirtualTruck,Double> visitedDeliveryNodes;
    /**
     * HashMap<VirtualTruck, Double> with the quantity delivered by each truck.
     */
    private final HashMap<VirtualTruck, Double> deliveredByTruck;
    /**
     * HashMap<VirtualTruck, Double> with the loads made by each truck.
     */
    private final HashMap<VirtualTruck, Double> loadedByTruck;

    /**
     * Function that creates a new instance of a solution.
     *
     * @param objectiveFunction   ObjectiveFunction object used to evaluate how well adapted a path is.
     * @param feasibilityFunction FeasibilityFunction Object used to evaluate if a path built matches problem requirements.
     * @param penalization        Double with the penalization that weights the feasibility of the solution.
     * @param maxEdges            Integer with the maximum number of Edges that a solution can contain.
     * @param builderEvaluator    BuilderEvaluator that checks if the built solution is an actual solution.
     * @param virtualTrucks              HashSet with all the Trucks in the scope.
     */
    public MultiRouteSolution(ObjectiveFunction objectiveFunction, FeasibilityFunction feasibilityFunction,
                              Double penalization, Integer maxEdges, BuilderEvaluator builderEvaluator,
                              ArrayList<VirtualTruck> virtualTrucks) {
        super(objectiveFunction, feasibilityFunction, penalization, maxEdges, builderEvaluator);
        this.virtualTrucks = virtualTrucks;
        this.routesMap = new HashMap<>();
        for (VirtualTruck virtualTruck :this.virtualTrucks){
            this.routesMap.put(virtualTruck, new ArrayList<>());
        }
        this.deliveredByNode = new HashMap<>();
        this.deliveredByTruck = new HashMap<>();
        this.visitedDeliveryNodes = new HashMap<>();
        this.loadedByTruck = new HashMap<>();
    }

    /**
     * Constructor that creates a new empty instance of a solution.
     *
     * @return MultiRouteSolution with empty fitness, objective and feasibility values; and empty edges used and nodes visited.
     */
    @Override
    public MultiRouteSolution createNewInstance() {
        return new MultiRouteSolution(this.getObjectiveFunction(), this.getFeasibilityFunction(),
                this.getPenalization(), this.getMaxEdges(), this.getBuilderEvaluator(), this.virtualTrucks);
    }

    /**
     * This function constructs a solution path step by step.
     *
     * @param edge     The Edge that contributes to forming a part of the solution.
     * @param destNode The Node that is going to be visited by the ant due to take a concrete Edge.
     * @param route ACORoute with the route that the ant is currently building.
     */
    public void buildSolution(Object edge, Object destNode, ACORoute route, Double toDeliver, VirtualTruck truck) {
        super.buildSolution(edge, destNode); //Add edge and node to the "edgesUtilized" and "nodesVisited".
        if( destNode instanceof WaypointNode waypointNode){
            this.deliveredByNode.put(waypointNode,this.deliveredByNode.getOrDefault(waypointNode,0.)+toDeliver);
            this.deliveredByTruck.put(truck, this.deliveredByTruck.getOrDefault(truck,0.)+toDeliver);
            if(toDeliver>0) this.visitedDeliveryNodes.put(truck, this.visitedDeliveryNodes.getOrDefault(truck,0.)+1.);
        }
        route.addToVisitEdge((ACOEdge) edge);
        route.addToVisitNode((ACONode) destNode);
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
        if (obj instanceof MultiRouteSolution multiRouteSolution) {
            return Objects.equals(this.getFitnessValue(), multiRouteSolution.getFitnessValue()) & this.routesMap.equals(multiRouteSolution.getRoutesMap());
        }
        return false;
    }

    /**
     * Function that reloads a truck;
     * @param selectedTruck VirtualTruck to reload;
     */
    public void reloadedTruck(VirtualTruck selectedTruck) {
        this.loadedByTruck.put(selectedTruck, this.loadedByTruck.getOrDefault(selectedTruck, 0.)+1.);
        selectedTruck.loadTruck();
    }

    /**
     * Function that clears a solution.
     */
    public void clearSolution(){
        this.routesMap.clear();
        for (VirtualTruck virtualTruck :this.virtualTrucks){
            this.routesMap.put(virtualTruck, new ArrayList<>());
        }
        this.deliveredByNode.clear();
        this.deliveredByTruck.clear();
        this.visitedDeliveryNodes.clear();
        this.loadedByTruck.clear();
    }
}
