package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Objectives;

import org.soyaga.aco.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Edges.ACOEdge;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.MultiRouteSolution;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.ProblemStructures.ACORoute;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.VirtualTruck;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class that represents an objective function.
 */
public class CostObjective implements ObjectiveFunction {
    /**
     * This function computes the value of the evaluable object.
     *
     * @param world         world Object containing the "Graph" and "PheromoneContainer" information.
     * @param solution      Solution object to evaluate.
     * @param evaluableArgs VarArgs containing the additional information needed to evaluate.
     * @return A double containing the value of the evaluation.
     */
    @Override
    public Double evaluate(World world, Solution solution, Object... evaluableArgs) {
        MultiRouteSolution multiRouteSolution = (MultiRouteSolution) solution; // Cast to this solution
        double cost = 0.;
        for(Map.Entry<VirtualTruck, ArrayList<ACORoute>> entry:multiRouteSolution.getRoutesMap().entrySet()){
            double truckCost = 0.;
            for(ACORoute route : entry.getValue()){
                for(ACOEdge edge : route.getToVisitEdges()){
                    truckCost += edge.getDistanceMeters();
                }
            }
            if(truckCost>0.){
                cost += truckCost* entry.getKey().getVariableCost() + entry.getKey().getFixedCost();
            }
        }
        return cost;
    }
}
