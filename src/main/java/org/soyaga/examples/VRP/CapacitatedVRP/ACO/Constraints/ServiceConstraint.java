package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Constraints;

import org.soyaga.aco.Evaluable.Feasibility.Constraint.Constraint;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.WaypointNode;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.MultiRouteSolution;

/**
 * Class that implements Constraint and evaluates the service level of the nodes in the Graph.
 */
public class ServiceConstraint implements Constraint {
    /**
     * This function computes the feasibility value of a solution.
     *
     * @param world          world Object containing the "Graph" and "PheromoneContainer" information.
     * @param solution       Solution object to evaluate.
     * @param constraintArgs VarArgs containing the additional information needed to evaluate the constraint.
     * @return A double containing the value of the evaluation.
     */
    @Override
    public Double evaluate(World world, Solution solution, Object... constraintArgs) {
        double pending = 0.;
        for(Object node : world.getGraph().getNodes()){
            if(node instanceof WaypointNode waypointNode){
                MultiRouteSolution multiRouteSolution = (MultiRouteSolution) solution;
                pending += waypointNode.getRequestedQuantity()-multiRouteSolution.getDeliveredByNode().getOrDefault(waypointNode, 0.);
            }
        }
        return pending;
    }
}
