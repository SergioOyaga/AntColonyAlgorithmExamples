package org.soyaga.examples.QAP.Complex;

import lombok.AllArgsConstructor;
import org.soyaga.aco.BuilderEvaluator.BuilderEvaluator;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;

import java.util.HashSet;


/**
 * BuilderEvaluator that stops building the solution when one node is visited two times.
 */
@AllArgsConstructor
public class CustomBuilderEvaluator implements BuilderEvaluator {
    /**
     * Node to start.
     */
    private final Object startNode;


    /**
     * This function verifies whether the constructed solution is valid. In this case, it returns true if the solution
     * visits one node two times.
     *
     * @param solution  The solution to be evaluated.
     * @param buildArgs VarArgs containing the additional information needed to evaluate the builder.
     * @return A boolean indicating the validity.
     */
    @Override
    public Boolean evaluate(Solution solution, Object... buildArgs) {
        HashSet<Object> controlSet = new HashSet<>();
        for(Object node:solution.getNodesVisited()){
            if (!controlSet.add(node)){
                return true;
            }
        }
        return false;
    }

    /**
     * Function that returns the starting node to build a solution.
     *
     * @param world           world Object containing the "Graph" and "PheromoneContainer" information.
     * @param initialNodeArgs VarArgs containing the additional information needed to select the first Node.
     * @return Node to start the solution.
     */
    @Override
    public Object getInitialNode(World world, Object... initialNodeArgs) {
        return this.startNode;
    }


}
