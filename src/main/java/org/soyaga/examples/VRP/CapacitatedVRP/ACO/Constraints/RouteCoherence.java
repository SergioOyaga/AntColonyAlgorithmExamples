package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Constraints;

import org.soyaga.aco.Evaluable.Feasibility.Constraint.Constraint;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.ACONode;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.CDCNode;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.MultiRouteSolution;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.ProblemStructures.ACORoute;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.VirtualTruck;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class that implements Constraint and evaluates if all trucks end in a CDC.
 */
public class RouteCoherence implements Constraint {
    /**
     * This function computes the feasibility value of a solution.
     *
     * @param world          world Object containing the "Graph" and "PheromoneContainer" information.
     * @param solution       Solution object to evaluate.
     * @param constraintArgs VarArgs containing the additional information needed to evaluate the constraint.
     * @return A double containing the value of the evaluation.
     */
    @Override
    public Double evaluate(World world, Solution solution, Object... constraintArgs) {
        double errors = 0.;
        MultiRouteSolution multiRouteSolution = (MultiRouteSolution) solution;
        for(Map.Entry<VirtualTruck, ArrayList<ACORoute>> routeEntry: multiRouteSolution.getRoutesMap().entrySet()){
            ACONode previousCDC = null;
            for(ACORoute route: routeEntry.getValue()){
                ACONode firstRouteNode = route.getToVisitNodes().get(0);
                ACONode nextCDC = route.getToVisitNodes().get(route.getToVisitNodes().size()-1);
                if(previousCDC != null & !firstRouteNode.equals(previousCDC)){
                    errors+=1.;
                }
                if(!(nextCDC instanceof CDCNode)){
                    errors +=1.;
                }
                previousCDC = nextCDC;
            }
        }
        return errors;
    }
}
