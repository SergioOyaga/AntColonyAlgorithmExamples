package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Ant;

import lombok.Getter;
import org.soyaga.aco.Ant.Ant;
import org.soyaga.aco.Ant.EdgeSelector.EdgeSelector;
import org.soyaga.aco.world.Graph.Elements.Edge;
import org.soyaga.aco.world.World;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Edges.ACOEdge;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.*;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.MultiRouteSolution;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.ProblemStructures.ACORoute;

import java.util.*;

/**
 * Ant that builds the solution step by step.
 */
public class ACOAnt implements Ant {
    /**
     * Solution object built by the ant.
     */
    @Getter
    private MultiRouteSolution solution;
    /**
     * EdgeSelection with the edge selection methodology.
     */
    @Getter
    private final EdgeSelector edgeSelector;
    /**
     * A double value indicating the quantity of pheromone that an ant can distribute across the entire path.
     */
    @Getter
    private final Double pheromoneQuantity;

    /**
     * Constructor.
     *
     * @param solution          Solution Type this ant has to build incrementally.
     * @param edgeSelector      EdgeSelector Type that this ant uses to chose its next edge
     * @param pheromoneQuantity Double with the quantity of pheromone that an ant can distribute across the entire path.
     */
    public ACOAnt(MultiRouteSolution solution, EdgeSelector edgeSelector, Double pheromoneQuantity) {
        this.solution = solution;
        this.edgeSelector = edgeSelector;
        this.pheromoneQuantity = pheromoneQuantity;
    }

    /**
     * Constructor that creates a new empty instance of an ant.
     *
     * @return Ant an ant with an empty memory, new Solution, the same EdgeSelector, the same pheromone quantity, and
     * the routes map as in the new Solution.
     */
    @Override
    public ACOAnt createNewInstance() {
        return new ACOAnt(this.solution.createNewInstance(),this.edgeSelector,this.pheromoneQuantity);
    }

    /**
     * Function that builds a solution using the ant information, the world information (Graph, pheromone, ...) and any
     * other argument.
     *
     * @param world     world Object containing the "Graph" and "Pheromone" information.
     * @param buildArgs VarArgs containing the additional information needed to build a solution.
     */
    @Override
    public void buildSolution(World world, Object... buildArgs) {
        ArrayList<SelectorNode> selectorNodes = this.getSelectorNodes(world);
        for(SelectorNode selectorNode:selectorNodes){// go over selector nodes
            ACORoute currentRoute = new ACORoute(); //Route to build
            this.solution.getNodesVisited().add(selectorNode);
            HashSet<Object> edges = filterEdgesForTruck(world.getGraph().getOutputEdges(this.solution.getCurrentNode()), this.solution);
            if (edges.isEmpty()) continue;
            Object nextEdge = this.edgeSelector.apply(world, this.solution.getCurrentNode(), edges); // Edge selected from the selectorNode
            VirtualTruck selectedTruck = (VirtualTruck) world.getGraph().getNextNode(nextEdge); // Virtual truck connected with that Edge.
            this.solution.getRoutesMap().get(selectedTruck).add(currentRoute); // Add the new route to the solution map by truck
            this.solution.buildSolution(nextEdge,selectedTruck.getCurrentPosition(),currentRoute, 0., selectedTruck); // move the ant to the real node in the graph.
            if(this.solution.getCurrentNode() instanceof CDCNode & selectedTruck.getCurrentLoad()<selectedTruck.getCapacity()){ //Reload the truck if it starts the route in a CDC.
                this.solution.reloadedTruck(selectedTruck);
            }
            this.buildRoute(world, currentRoute, selectedTruck); // Build the truck in the graph.
            if(this.solution.stopBuild(world)) break; // Stop if condition is met.
        }
    }

    /**
     * Function that returns the selector nodes.
     * @param  world World object with the Graph.
     * @return ArrayList<SelectorNode> with the SelectorNode.
     */
    private ArrayList<SelectorNode> getSelectorNodes(World world) {
        ArrayList<SelectorNode> selectorNodes = new ArrayList<>();
        for(Object node : world.getGraph().getNodes()){
            if(node instanceof SelectorNode selectorNode){
                selectorNodes.add(selectorNode);
            }
        }
        return selectorNodes;
    }

    /**
     * Function that filters the selector nodes to first select the trucks.
     * @param edges HashSet<Object> with the edges for this selector.
     * @param solution MultiRouteSolution to check if the trucks have been used.
     * @return HashSet<Object> With the new edges available for this selector.
     */
    private HashSet<Object> filterEdgesForTruck(HashSet<Object> edges, MultiRouteSolution solution) {
        HashSet<Object> edgesCopy = new HashSet<>(edges);
        for (Map.Entry<VirtualTruck, ArrayList<ACORoute>> entry :solution.getRoutesMap().entrySet()){
            if(entry.getValue().size()>0){
                edges.removeIf(edge -> ((ACOEdge)edge).getDestination().equals(entry.getKey()));
            }
        }
        if(edges.size()==0) return edgesCopy;
        else return edges;
    }

    /**
     * Function that builds a route.
     * @param world World.
     * @param route ACORoute object to be built.
     * @param truck VirtualTruck object with the current load.
     */
    private void buildRoute(World world, ACORoute route, VirtualTruck truck) {
        do{
            Object currentLocation = this.solution.getCurrentNode();
            HashSet<Object> edges = world.getGraph().getOutputEdges(currentLocation);
            filterEdgesByQuantity(edges);
            if(edges.isEmpty()) return;
            Object nextEdge = this.edgeSelector.apply(world, currentLocation, edges); // Edge selected from the selectorNode
            Object nextLocation = world.getGraph().getNextNode(nextEdge);
            if(nextLocation instanceof CDCNode cdcNode){
                this.solution.buildSolution(nextEdge,cdcNode,route, 0., truck); // move the ant in the graph.
                truck.setCurrentPosition(cdcNode);
            }
            else if (nextLocation instanceof WaypointNode waypointNode){
                Double toDeliver = Math.min(waypointNode.getPendingQuantity(),truck.getCurrentLoad());
                this.solution.buildSolution(nextEdge,waypointNode,route, toDeliver, truck); // move the ant in the graph.
                truck.setCurrentPosition(waypointNode);
                waypointNode.setPendingQuantity(waypointNode.getPendingQuantity()-toDeliver);
                truck.setCurrentLoad(truck.getCurrentLoad()-toDeliver);
            }
        }while (!(truck.getCurrentPosition() instanceof CDCNode));
    }

    /**
     * Function that filters the edges if they don't have pending quantity.
     * @param outputEdges HashSet<Edge> with the edges to filter.
     */
    private void filterEdgesByQuantity(HashSet<Object> outputEdges) {
        outputEdges.removeIf(e ->((((Edge)e).getDestination() instanceof WaypointNode && ((WaypointNode)((Edge)e).getDestination()).getPendingQuantity()==0.)));
    }

    /**
     * This function resets the ant. It is used in every iteration to clean the Solution and other objects that the ant
     * might have modified during the solution build process.
     *
     * @param resetArgs VarArgs containing the additional information needed to perform the reset.
     */
    @Override
    public void resetAnt(Object... resetArgs) { //World object as parameter
        this.solution = this.solution.createNewInstance();
        World world = (World) resetArgs[0];
        for(Object node: world.getGraph().getNodes()){
            if(node instanceof VirtualTruck virtualTruck){
                virtualTruck.resetTruck();
            }
            else if ( node instanceof WaypointNode waypointNode) {
                waypointNode.setPendingQuantity(waypointNode.getRequestedQuantity());
            }
        }
    }

    /**
     * Function to verbose the optimization process.
     *
     * @return a string containing the Ant string representation.
     */
    @Override
    public String toString(){
        return "ACOAnt";
    }
}
