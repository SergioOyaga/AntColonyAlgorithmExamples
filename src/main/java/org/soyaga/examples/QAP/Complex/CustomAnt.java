package org.soyaga.examples.QAP.Complex;

import lombok.Getter;
import org.soyaga.aco.Ant.Ant;
import org.soyaga.aco.Ant.EdgeSelector.EdgeSelector;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;
import org.soyaga.examples.QAP.Complex.RunCustomQapOptimization.Location;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * This class represents an ant with memory. The special feature is that the ant only remembers certain nodes,
 * those stored in the memorizableNodes attribute.
 */
public class CustomAnt implements Ant {
    /**
     * An ArrayList of Nodes containing the already visited ones (ant's memory).
     */
    private ArrayList<Object> memory;
    /**
     * A HashSet of Nodes containing the nodes that the ant can store in its memory.
     */
    private final HashSet<Location> memorizableNodes;
    /**
     * Solution object built by the ant.
     */
    @Getter
    private Solution solution;
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
     * @param solution Solution Type this ant has to build incrementally.
     * @param edgeSelector EdgeSelector Type that this ant uses to chose its next edge
     * @param pheromoneQuantity Double with the quantity of pheromone that an ant can distribute across the entire path.
     * @param memorizableNodes HashSet of Nodes containing the nodes that the ant can store in its memory.
     */
    public CustomAnt(Solution solution, EdgeSelector edgeSelector, Double pheromoneQuantity, HashSet<Location> memorizableNodes) {
        this.solution = solution;
        this.edgeSelector = edgeSelector;
        this.pheromoneQuantity = pheromoneQuantity;
        this.memory=new ArrayList<>();
        this.memorizableNodes = memorizableNodes;
    }

    /**
     * Constructor that creates a new empty instance of an ant.
     *
     * @return Ant an ant with an empty memory, new Solution, the same EdgeSelector, the same pheromone quantity and
     * the same memorizable nodes.
     */
    @Override
    public CustomAnt createNewInstance() {
        return new CustomAnt(this.solution.createNewInstance(),this.edgeSelector,this.pheromoneQuantity, this.memorizableNodes);
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
        this.solution = this.solution.createNewInstance();
        this.solution.selectInitialNode(world,buildArgs);
        if(this.memorizableNodes.contains(this.solution.getCurrentNode())) {
            this.memory.add(this.solution.getCurrentNode());
        }
        do{
            Object currentNode=this.solution.getCurrentNode();
            HashSet<Object> edges = world.getGraph().getOutputEdges(currentNode);
            this.removeVisitedNodes(world,edges);
            Object nextEdge = this.edgeSelector.apply(world, currentNode, edges);
            this.solution.buildSolution(nextEdge,world.getGraph().getNextNode(nextEdge));
            if(this.memorizableNodes.contains(this.solution.getCurrentNode())) {
                this.memory.add(this.solution.getCurrentNode());
            }
        }
        while (!this.solution.stopBuild());
    }

    /**
     * This function resets the ant. It is used in every iteration to clean the Solution and other objects that the ant
     * might have modified during the solution build process.
     *
     * @param resetArgs VarArgs containing the additional information needed to perform the reset.
     */
    @Override
    public void resetAnt(Object... resetArgs) {
        this.solution = this.solution.createNewInstance();
        this.memory = new ArrayList<>();
    }

    /**
     * This function eliminates edges whose target node has already been visited. If no edge leading to an unvisited node is found,
     * all available edges are regarded as viable options.
     *
     * @param world The world in which to search for Nodes/Edges.
     * @param edges The edges to examine for visited target nodes.
     */
    private void removeVisitedNodes(World world, HashSet<Object> edges){
        HashSet<Object> aux = new HashSet<>(edges);
        for(Object edge :aux){
            if(this.memory.contains(world.getGraph().getNextNode(edge))) edges.remove(edge);
        }
        if(edges.size()==0) edges.addAll(aux);
    }

    /**
     * Function to verbose the optimization process.
     *
     * @return a string containing the Ant string representation.
     */
    @Override
    public String toString(){
        return memory.toString();
    }
}
