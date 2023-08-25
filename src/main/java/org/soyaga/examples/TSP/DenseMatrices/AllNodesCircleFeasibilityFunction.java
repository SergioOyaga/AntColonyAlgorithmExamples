package org.soyaga.examples.TSP.DenseMatrices;

import org.soyaga.aco.Evaluable.Feasibility.FeasibilityFunction;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;

import java.util.HashSet;

/**
 * Class that evaluates if the built function is a real function. In this case, the function returns the number of not
 * visited nodes adding 1 if the solution doesn't start and finish in the same node.
 */
public class AllNodesCircleFeasibilityFunction implements FeasibilityFunction {

    /**
     * This function computes the value of the evaluable object.
     *
     * @param world           world Object containing the "Graph" and "PheromoneContainer" information.
     * @param solution        Solution object to evaluate.
     * @param feasibilityArgs VarArgs containing the additional information needed to evaluate.
     * @return A double containing the value of the evaluation.
     */
    @Override
    public Double evaluate(World world, Solution solution, Object... feasibilityArgs) {
        HashSet allNodes = world.getGraph().getNodes();
        solution.getNodesVisited().forEach(allNodes::remove);
        int notEqualStartEnd = (solution.getNodesVisited().get(0)==solution.getNodesVisited().get(solution.getNodesVisited().size()-1))?0:1;
        return (double) (allNodes.size()+notEqualStartEnd);
    }

}
