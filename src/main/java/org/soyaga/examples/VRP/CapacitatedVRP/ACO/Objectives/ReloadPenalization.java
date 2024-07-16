package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Objectives;

import org.soyaga.aco.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.VirtualTruck;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.MultiRouteSolution;

import java.util.Map;

/**
 * Class that represents an objective function that penalizes the reload of the truck.
 */
public class ReloadPenalization implements ObjectiveFunction {
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
        double loadCount =0;
        for(Map.Entry<VirtualTruck,Double> loadEntry: multiRouteSolution.getLoadedByTruck().entrySet()){
            loadCount+=loadEntry.getValue();
        }
        return loadCount;
    }
}
