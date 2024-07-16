package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Stats;

import lombok.Getter;
import org.jxmapviewer.viewer.GeoPosition;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.Stat;
import org.soyaga.aco.world.World;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.ACONode;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.VirtualTruck;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.MultiRouteSolution;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.ProblemStructures.ACORoute;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RouteChangeStat implements Stat {
    /**
     * PropertyChangeSupport object to fire events when properties of this class change.
     */
    private final PropertyChangeSupport pcs;

    /**
     * Last solution sent.
     */
    @Getter
    private Solution currentSolution;

    /**
     * Constructor.
     */
    public RouteChangeStat() {
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * ArrayList of strings that compose the header for this stat.
     *
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> getHeader() {
        return new ArrayList<>(){{add("RouteChanged");}};
    }

    /**
     * ArrayList of strings that compose the values for this stat.
     *
     * @param world    world used to measure the stats.
     * @param colony   Colony used to measure the stats.
     * @param statArgs VarArgs containing the additional information needed to apply the stat.
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> apply(World world, Colony colony, Object... statArgs) {
        Solution solution = colony.getBestSolution();
        if(!solution.equals(this.currentSolution)){
            this.currentSolution = solution;
            this.pcs.firePropertyChange("routeChange", null, getInfoByTruck());
            return new ArrayList<>(){{add("true");}};
        }
        return new ArrayList<>(){{add("false");}};
    }

    /**
     * Computes the properties of the new solution.
     * @return HashMap<String, HashMap<String, Object>> with the info by truck
     */
    private HashMap<String, HashMap<String, Object>> getInfoByTruck() {
        HashMap<String,HashMap<String,Object>> infoByTruck = new HashMap<>();
        getSolutionMap(infoByTruck);
        getDeliveryByTruck(infoByTruck);
        getVisitedNodes(infoByTruck);
        getLoadedTimes(infoByTruck);
        return infoByTruck;
    }

    /**
     * Function that computes the number of loads made by truck.
     * @param infoByTruck object that encapsulates the info.
     */
    private void getLoadedTimes(HashMap<String, HashMap<String, Object>> infoByTruck) {
        for(Map.Entry<VirtualTruck,Double> truckDoubleEntry:
                ((MultiRouteSolution)this.currentSolution).getLoadedByTruck().entrySet()){
            HashMap<String,Object> infoRoute = infoByTruck.computeIfAbsent((String) truckDoubleEntry.getKey().getID(), k-> new HashMap<>());
            infoRoute.put("loadsNumber",truckDoubleEntry.getValue());
        }
    }

    /**
     * Function that computes the visited nodes by truck.
     * @param infoByTruck object that encapsulates the info.
     */
    private void getVisitedNodes(HashMap<String, HashMap<String, Object>> infoByTruck) {
        for(Map.Entry<VirtualTruck,Double> truckDoubleEntry:
                ((MultiRouteSolution)this.currentSolution).getVisitedDeliveryNodes().entrySet()){
            HashMap<String,Object> infoRoute = infoByTruck.computeIfAbsent((String) truckDoubleEntry.getKey().getID(), k-> new HashMap<>());
            infoRoute.put("visitedWaypoints",truckDoubleEntry.getValue());
        }
    }

    /**
     * Function that computes the delivery made by truck.
     * @param infoByTruck object that encapsulates the info.
     */
    private void getDeliveryByTruck(HashMap<String, HashMap<String, Object>> infoByTruck) {
        for(Map.Entry<VirtualTruck,Double> truckDoubleEntry:
                ((MultiRouteSolution)this.currentSolution).getDeliveredByTruck().entrySet()){
            HashMap<String,Object> infoRoute = infoByTruck.computeIfAbsent((String) truckDoubleEntry.getKey().getID(), k-> new HashMap<>());
            infoRoute.put("deliveredQuantity",truckDoubleEntry.getValue());
        }
    }

    /**
     * Function that computes the track expected by the controller.
     * @param infoByTruck object that encapsulates the info.
     */
    private void getSolutionMap(HashMap<String, HashMap<String, Object>> infoByTruck) {
        for (Map.Entry<VirtualTruck,ArrayList<ACORoute>> truckEntry:
                ((MultiRouteSolution)this.currentSolution).getRoutesMap().entrySet()){
            HashMap<String,Object> infoRoute = infoByTruck.computeIfAbsent((String) truckEntry.getKey().getID(), k-> new HashMap<>());
            ArrayList<GeoPosition> guiRoute = (ArrayList<GeoPosition>) infoRoute.computeIfAbsent("trackToVisit", k->new ArrayList<>());
            ACONode previousNode = null;
            for(ACORoute route:truckEntry.getValue()){
                for(ACONode node: route.getToVisitNodes()){
                    if(!node.equals(previousNode)){
                        guiRoute.add(node.getGeoPosition());
                    }
                    previousNode = node;
                }
            }
        }
    }

    /**
     * Function that forwards the addPropertyChangeListener to the PropertyChangeSupport Object.
     * @param listener The PropertyChangeListener to be added
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}
