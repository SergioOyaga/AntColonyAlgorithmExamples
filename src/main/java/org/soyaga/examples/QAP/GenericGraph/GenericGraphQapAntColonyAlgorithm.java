package org.soyaga.examples.QAP.GenericGraph;

import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.AntColonyAlgorithm.StatsAntColonyAlgorithm;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution;
import org.soyaga.aco.SolutionConstructorPolicy.SolutionConstructorPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.StatsRetrievalPolicy;
import org.soyaga.aco.StoppingPolicy.StoppingCriteriaPolicy;
import org.soyaga.aco.UpdatePheromonePolicy.UpdatePheromonePolicy;
import org.soyaga.aco.world.Graph.Elements.Edge;
import org.soyaga.aco.world.World;

import java.util.Set;

/**
 * Extends StatsAntColonyAlgorithm and defines how we perform the optimization cycles and how we gather the results.
 */
public class GenericGraphQapAntColonyAlgorithm extends StatsAntColonyAlgorithm {

    /**
     * This method receives all the parameters necessary to create an object of this class.
     *
     * @param ID                        A string representing the name of the ACO.
     * @param world                     A world object containing information about the "Graph" and "PheromoneContainer."
     * @param colony                    A Colony object containing the ants and the best solution found during optimization.
     * @param stoppingCriteriaPolicy    A StoppingCriteriaPolicy object with the predefined criteria.
     * @param acoInitializer            An ACOInitializer object with the initialization defined.
     * @param solutionConstructorPolicy A SolutionConstructorPolicy object responsible for constructing ants' solutions.
     * @param updatePheromonePolicy     An UpdatePheromonePolicy object that manages the addition and removal of pheromone in the world.
     * @param statsRetrievalPolicy      StatsRetrievalPolicy object with the stats defined.
     */
    public GenericGraphQapAntColonyAlgorithm(String ID, World world, Colony colony,
                                       StoppingCriteriaPolicy stoppingCriteriaPolicy, ACOInitializer acoInitializer,
                                       SolutionConstructorPolicy solutionConstructorPolicy,
                                       UpdatePheromonePolicy updatePheromonePolicy,
                                       StatsRetrievalPolicy statsRetrievalPolicy) {
        super(ID, world, colony, stoppingCriteriaPolicy, acoInitializer, solutionConstructorPolicy,
                updatePheromonePolicy, statsRetrievalPolicy);
    }

    /**
     * Method that returns from an optimized solution the actual result in the
     * form that is convenient for the problem. The assignation attribute is used as filter to don't see the
     * transitions that are not meaningful for the assignation.
     *
     * @return Object containing the result of the optimization. In theis case an image with the Path.
     */
    @Override
    public Object getResult(Object... resultArgs) {
        Set<Object> assignation = (Set<Object>) resultArgs[0];
        Solution solution = this.getColony().getBestSolution();
        StringBuilder string= new StringBuilder();
        for(Object edge:solution.getEdgesUtilized()){
            if(!assignation.contains(((Edge)edge).getDestination())){
                string.append(((Edge) edge).getID()).append("\t");
            }
        }
        return string;
    }

}
