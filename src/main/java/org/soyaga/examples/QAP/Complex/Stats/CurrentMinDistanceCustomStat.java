package org.soyaga.examples.QAP.Complex.Stats;

import org.soyaga.aco.Ant.Ant;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.Stat;
import org.soyaga.examples.QAP.Complex.RunCustomQapOptimization.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Stat that computes the current best solution's distance.
 */
public class CurrentMinDistanceCustomStat implements Stat {
    /**
     * ArrayList{@literal <String>} with the different column headers computed by this Stat.
     */
    private final ArrayList<String> header;
    /**
     * String with the formatting of the Double number produced by the Stat.
     */
    private final String decimalFormat;
    /**
     * ArrayList {@literal <Facility>} with the facilities.
     */
    private final ArrayList<Facility> facilities;

    /**
     * Constructor.
     * @param decimalNumber Integer with the number of decimal points to keep in the stat.
     * @param facilities ArrayList {@literal <Facility>} with the nodes that are facilities.
     */
    public CurrentMinDistanceCustomStat(Integer decimalNumber, ArrayList<Facility> facilities) {
        this.header = new ArrayList<>(){{add("CurrentMinDistance");}};
        this.decimalFormat = "%."+decimalNumber+"f";
        this.facilities = facilities;
    }

    /**
     * ArrayList of strings that compose the header for this stat.
     *
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> getHeader() {
        return this.header;
    }

    /**
     * This ArrayList of strings computes the values for this stat. In this case, the stat returns the distance
     * of the colony's best solution.
     *
     * @param world World used to measure the stats.
     * @param colony Colony used to measure the stats.
     * @param statArgs   VarArgs containing the additional information needed to apply the stat. (Not used)
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> apply(World world, Colony colony, Object... statArgs) {
        Solution currentBestSolution = colony.getAnts().stream().map(Ant::getSolution).sorted().findFirst().get();
        HashMap<Facility, Location> assignation = new HashMap<>();
        HashMap<Location, Facility> reverseAssignation = new HashMap<>();
        Object currentNode = currentBestSolution.getNodesVisited().get(0);
        for (Object edge : currentBestSolution.getEdgesUtilized()) {
            if (this.facilities.contains(currentNode)) {
                Location destination = (Location) world.getGraph().getNextNode(edge);
                assignation.put((Facility) currentNode, destination);
                reverseAssignation.put(destination,(Facility)currentNode);
            }
            currentNode =  world.getGraph().getNextNode(edge);
        }
        Double dist = 0.;
        for(Map.Entry<Facility, Location> assignationEntry: assignation.entrySet()){
            Facility facilityOrigin = assignationEntry.getKey();
            Location locationOrigin = assignationEntry.getValue();
            for( Map.Entry<Object,Double> locationEntry:locationOrigin.locationDistances.entrySet()){
                Location locationDest = (Location) locationEntry.getKey();
                if (reverseAssignation.keySet().contains(locationDest)){
                    dist += locationEntry.getValue()*facilityOrigin.facilityOutgoingFlow.getOrDefault(reverseAssignation.get(locationDest),0.);
                }
            }
        }
        String str = String.format(this.decimalFormat,dist);
        return new ArrayList<>(){{add(str);}};
    }
}
