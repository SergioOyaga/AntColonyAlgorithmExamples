package org.soyaga.examples.QAP.Complex;

import org.soyaga.aco.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;
import org.soyaga.examples.QAP.Complex.RunCustomQapOptimization.*;

import java.util.HashMap;
import java.util.Set;

public class CustomCostObjective implements ObjectiveFunction {
    /**
     * HasMap{@literal Node<HashMap<Node,Double>>} with the distances between locations.
     */
    private final HashMap<Object, HashMap<Object,Double>> locationTransportCosts;
    /**
     * HasMap{@literal Node<HashMap<Node,Double>>} with the flow between facilities.
     */
    private final HashMap<Object,HashMap<Object,Double>> facilityFlows;
    /**
     * Set{@literal <Node>} with the facilities.
     */
    private final Set<Object> facilities;

    /**
     * Constructor.
     *
     * @param locationTransportCosts HashMap{@literal <location,HashMap<location,transportationCosts>>}
     * @param facilityFlows   HashMap{@literal <facility,HashMap<facility,flow>>}
     */
    public CustomCostObjective(HashMap<Object, HashMap<Object, Double>> locationTransportCosts,
                                HashMap<Object, HashMap<Object, Double>> facilityFlows) {
        this.locationTransportCosts = locationTransportCosts;
        this.facilityFlows = facilityFlows;
        this.facilities = facilityFlows.keySet();
    }

    /**
     * Function that evaluates and sets the fitness of a Solution.
     *
     * @param world world Object containing the "Graph" and "PheromoneContainer" information.
     * @param solution Solution object to evaluate.
     * @param evaluationArgs VarArgs containing the additional information needed to evaluate.
     * @return double with the solution fitness.
     */
    @Override
    public Double evaluate(World world, Solution solution, Object... evaluationArgs) {
        double solutionObjective=0.;
        HashMap<Object, Object> assignation = new HashMap<>();
        Object currentNode = solution.getNodesVisited().get(0);
        for (Object edge : solution.getEdgesUtilized()) {
            if (this.facilities.contains(currentNode))
                assignation.put(currentNode, world.getGraph().getNextNode(edge));
            currentNode = world.getGraph().getNextNode(edge);
        }
        for (Object nodeOrig : solution.getNodesVisited()) {
            if(this.facilities.contains(nodeOrig)) {
                //Cost to build the facility.
                solutionObjective+= ((Facility)nodeOrig).costToBuild;
                //Cost to buy the location.
                solutionObjective+= ((Location)assignation.get(nodeOrig)).costToBuyLand;
                for (Object nodeDest : solution.getNodesVisited()) {
                    if(this.facilities.contains(nodeDest)) {
                        solutionObjective += (this.facilityFlows.getOrDefault(nodeOrig,new HashMap<>()).getOrDefault(nodeDest,0.) *
                                this.locationTransportCosts.getOrDefault(assignation.get(nodeOrig), new HashMap<>()).
                                        getOrDefault(assignation.get(nodeDest),0.));
                    }
                }
            }
        }
        return solutionObjective;
    }
}
