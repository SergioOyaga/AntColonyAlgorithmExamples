package org.soyaga.examples.QAP.Complex;

import org.soyaga.aco.Evaluable.Feasibility.Constraint.Constraint;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;
import org.soyaga.examples.QAP.Complex.RunCustomQapOptimization.*;

import java.util.*;



/**
 * This constraint evaluates whether the resulting flow in the system falls within certain specified values.
 * These boundaries ensure that the resulting assignment meets the business requirements.
 */
public class CustomConstraint implements Constraint {
    /**
     * A double representing the minimum flow that the solution must have.
     */
    private final Double minFlow;
    /**
     * A double representing the maximum flow that the solution can have.
     */
    private final Double maxFlow;
    /**
     * A HashSet containing the facility nodes. (Assume that the assignment is made from these nodes to the rest).
     */
    private final HashSet<Facility> facilities;

    /**
     * Constructor.
     *
     * @param minFlow Double representing the minimum flow that the solution must have.
     * @param maxFlow Double representing the maximum flow that the solution can have.
     * @param facilities HashSet containing the facility Nodes.
     */
    public CustomConstraint(Double minFlow, Double maxFlow, HashSet<Facility> facilities) {
        this.minFlow = minFlow;
        this.maxFlow = maxFlow;
        this.facilities = facilities;
    }

    /**
     * This function computes the feasibility value of a solution.
     *
     * @param world          A World object containing the "Graph" and "PheromoneContainer" information.
     * @param solution       A Solution object to evaluate.
     * @param constraintArgs VarArgs containing additional information needed to evaluate the constraint.
     * @return A double containing the value of the evaluation.
     */
    @Override
    public Double evaluate(World world, Solution solution, Object... constraintArgs) {
        HashMap<Facility,Location> assignation = new HashMap<>();   //Assignation map (Facility -> Location)
        Object currentNode = solution.getNodesVisited().get(0);     //Initial Node (F1)
        for (Object edge : solution.getEdgesUtilized()) {           //Go over the edges
            if (this.facilities.contains(currentNode)) {            //Is the origin a Facility?
                Location destination = (Location) world.getGraph().getNextNode(edge);
                assignation.put((Facility) currentNode, destination);   //Store assignation
            }
            currentNode =  world.getGraph().getNextNode(edge);  //Move to des node.
        }
        Double flow = 0.;   //Flow var
        for(Map.Entry<Facility,Location > assignationEntry: assignation.entrySet()){    //Go over the assignation pairs
            Facility facilityOrigin = assignationEntry.getKey();
            Location locationOrigin = assignationEntry.getValue();
            //For all the possible outgoing flows from the origin facility.
            for( Map.Entry<Object,Double> flowEntry:facilityOrigin.facilityOutgoingFlow.entrySet()){
                Facility facilityDest = (Facility) flowEntry.getKey();
                //If the origin location is conected to the dest location.
                if(locationOrigin.locationDistances.keySet().contains(assignation.getOrDefault(facilityDest,null))){
                    flow += flowEntry.getValue();   //add flow
                }
            }
        }
        return (flow<minFlow || flow>maxFlow)?1.:0.;
    }
}
