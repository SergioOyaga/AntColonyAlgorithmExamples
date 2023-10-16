package org.soyaga.examples.QAP.Complex;

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

import java.util.Collection;


/**
 * This class extends StatsAntColonyAlgorithm and defines how optimization cycles are performed and how results are gathered.
 */
public class CustomQapAntColonyAlgorithm extends StatsAntColonyAlgorithm {

    /**
     * Constructor. This method receives all the necessary parameters to create an object of this class.
     *
     * @param ID                        A string representing the name of the ACO.
     * @param world                     A World object containing information about the "Graph" and "PheromoneContainer."
     * @param colony                    A Colony object containing the ants and the best solution found during optimization.
     * @param stoppingCriteriaPolicy    A StoppingCriteriaPolicy object with predefined criteria.
     * @param acoInitializer            An ACOInitializer object with the defined initialization.
     * @param solutionConstructorPolicy A SolutionConstructorPolicy object responsible for constructing ants' solutions.
     * @param updatePheromonePolicy     An UpdatePheromonePolicy object that manages the addition and removal of pheromone in the world.
     * @param statsRetrievalPolicy      A StatsRetrievalPolicy object with defined stats.
     */
    public CustomQapAntColonyAlgorithm(String ID, World world, Colony colony,
                                       StoppingCriteriaPolicy stoppingCriteriaPolicy, ACOInitializer acoInitializer,
                                       SolutionConstructorPolicy solutionConstructorPolicy,
                                       UpdatePheromonePolicy updatePheromonePolicy,
                                       StatsRetrievalPolicy statsRetrievalPolicy) {
        super(ID, world, colony, stoppingCriteriaPolicy, acoInitializer, solutionConstructorPolicy,
                updatePheromonePolicy, statsRetrievalPolicy);
    }


    /**
     * This method returns the actual result from an optimized solution in a convenient format for the problem.
     * The assignation attribute is used as a filter to exclude transitions that are not meaningful for the assignment.
     *
     * @return An object containing the result of the optimization. In this case, a string representing the path.
     */
    @Override
    public Object getResult(Object... resultArgs) {
        Collection<Object> assignation = (Collection<Object>) resultArgs[0];
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
