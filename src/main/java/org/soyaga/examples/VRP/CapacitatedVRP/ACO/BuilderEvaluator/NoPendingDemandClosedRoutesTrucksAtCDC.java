package org.soyaga.examples.VRP.CapacitatedVRP.ACO.BuilderEvaluator;

import org.soyaga.aco.BuilderEvaluator.BuilderEvaluator;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.CDCNode;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.WaypointNode;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.VirtualTruck;


/**
 * Class that evaluates a solution. False if there is pending quantity to deliver or if we have trucks on the road.
 */
public class NoPendingDemandClosedRoutesTrucksAtCDC implements BuilderEvaluator {

    /**
     * This function verifies whether the constructed solution is a valid solution. In this case, True if the solution
     * starts and ends in specific nodes and specific nodes in the Graph have been visited.
     *
     * @param solution Solution to be evaluated.
     * @param buildArgs VarArgs that contains the extra information needed to evaluate the builder.
     * @return A boolean indicating the validity.
     */
    @Override
    public Boolean evaluate(Solution solution, Object... buildArgs) {
        World world = (World) buildArgs[0];
        for(Object node : world.getGraph().getNodes()){
            if(node instanceof WaypointNode waypointNode){
                if(waypointNode.getPendingQuantity()>0) return false;
            }
            if(node instanceof VirtualTruck virtualTruck){
                if(!(virtualTruck.getCurrentPosition() instanceof CDCNode)) return false;
            }
        }
        return true;
    }

    /**
     * Does nothing The truck selector process is in charge.
     *
     * @param world           world Object containing the "Graph" and "PheromoneContainer" information.
     * @param initialNodeArgs VarArgs containing the additional information needed to select the first Node.
     * @return Node to start the solution.
     */
    @Override
    public Object getInitialNode(World world, Object... initialNodeArgs) {
        return null;
    }
}
