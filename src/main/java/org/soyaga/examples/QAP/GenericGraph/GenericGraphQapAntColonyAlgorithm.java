package org.soyaga.examples.QAP.GenericGraph;

import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.Ant.Ant;
import org.soyaga.aco.AntColonyAlgorithm.SimpleAntColonyAlgorithm;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution.Solution;
import org.soyaga.aco.SolutionConstructorPolicy.SolutionConstructorPolicy;
import org.soyaga.aco.SolutionEvaluatorPolicy.SolutionEvaluatorPolicy;
import org.soyaga.aco.StopingCriteriaPolicy.StoppingCriteriaPolicy;
import org.soyaga.aco.UpdatePheromonePolicy.UpdatePheromonePolicy;
import org.soyaga.aco.world.Graph.Elements.Edge;
import org.soyaga.aco.world.World;

import java.util.Set;

/**
 * Extends AntColonyAlgorithm and defines how we perform the optimization cycles and how we gather the results.
 */
public class GenericGraphQapAntColonyAlgorithm extends SimpleAntColonyAlgorithm {
    /**
     * Set with assignation nodes. I.E. facilities or locations depending on whether we assign facilities to locations
     * or vice-versa
     */
    private final Set assignation;

    /**
     * It receives all parameters needed to create an object of this class.
     *
     * @param ID String with the name of the ACO.
     * @param world World with the world defined.
     * @param colony Colony with the colony defined.
     * @param initialColonySize Integer with the initial colony size.
     * @param ant Ant type we want to use in the optimization.
     * @param stoppingCriteriaPolicy   StoppingCriteriaPolicy object with the criteria already defined.
     * @param acoInitializer  ACOInitializer object with the initializer defined.
     * @param solutionConstructorPolicy SolutionConstructorPolicy with the policy defined.
     * @param solutionEvaluatorPolicy SolutionEvaluatorPolicy with the policy defined.
     * @param updatePheromonePolicy UpdatePheromonePolicy with the policy defined
     * @param assignation Set used in the gathering of results. If the dest node is not in this set, then the edge is plotted.
     */
    public GenericGraphQapAntColonyAlgorithm(String ID, World world, Colony colony, Integer initialColonySize, Ant ant,
                                             StoppingCriteriaPolicy stoppingCriteriaPolicy, ACOInitializer acoInitializer,
                                             SolutionConstructorPolicy solutionConstructorPolicy,
                                             SolutionEvaluatorPolicy solutionEvaluatorPolicy,
                                             UpdatePheromonePolicy updatePheromonePolicy, Set assignation) {
        super(ID, world,colony,initialColonySize,ant,stoppingCriteriaPolicy,acoInitializer,solutionConstructorPolicy,
                solutionEvaluatorPolicy,updatePheromonePolicy);
        this.assignation = assignation;
    }

    /**
     * Method that returns from an optimized solution the actual result in the
     * form that is convenient for the problem. The assignation attribute is used as filter to don't see the
     * transitions that are not meaningful for the assignation.
     *
     * @return Object containing the result of the optimization. In theis case an image with the Path.
     */
    @Override
    public Object getResult() {
        Solution solution = this.getColony().getBestSolution();
        StringBuilder string= new StringBuilder();
        for(Object edge:solution.getPath()){
            if(!this.assignation.contains(((Edge)edge).getDestination())){
                string.append(((Edge) edge).getID()).append("\t");
            }
        }
        return string;
    }

}
