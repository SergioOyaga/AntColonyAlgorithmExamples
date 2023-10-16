package org.soyaga.examples.QAP.Complex.Stats;

import org.soyaga.aco.Ant.Ant;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.Stat;
import org.soyaga.examples.QAP.Complex.RunCustomQapOptimization.*;

import java.util.*;

/**
 * Stat that computes the colony's current best solution flow.
 */
public class CurrentMinFlowCustomStat implements Stat {
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
     * @param facilities ArrayList {@literal <Facility>} with the facilities.
     */
    public CurrentMinFlowCustomStat(Integer decimalNumber, ArrayList<Facility> facilities) {
        this.header = new ArrayList<>(){{add("CurrentMinFlow");}};
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
     * This ArrayList of strings composes the values for this stat. In this case, the stat returns the flow
     * of the colony's best solution.
     *
     * @param world    World used to measure the stats.
     * @param colony   Colony used to measure the stats.
     * @param statArgs VarArgs containing the additional information needed to apply the stat.
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> apply(World world, Colony colony, Object... statArgs) {
        Solution currentBestSolution = colony.getAnts().stream().map(Ant::getSolution).sorted().findFirst().get();
        HashMap<Facility, Location> assignation = new HashMap<>();
        Object currentNode = currentBestSolution.getNodesVisited().get(0);
        for (Object edge : currentBestSolution.getEdgesUtilized()) {
            if (this.facilities.contains(currentNode)) {
                Location destination = (Location) world.getGraph().getNextNode(edge);
                assignation.put((Facility) currentNode, destination);
            }
            currentNode =  world.getGraph().getNextNode(edge);
        }
        Double flow = 0.;
        for(Map.Entry<Facility, Location> assignationEntry: assignation.entrySet()){
            Facility facilityOrigin = assignationEntry.getKey();
            Location locationOrigin = assignationEntry.getValue();
            for( Map.Entry<Object,Double> flowEntry:facilityOrigin.facilityOutgoingFlow.entrySet()){
                Facility facilityDest = (Facility) flowEntry.getKey();
                if(locationOrigin.locationDistances.keySet().contains(assignation.getOrDefault(facilityDest,null))){
                    flow += flowEntry.getValue();
                }
            }
        }
        String str = String.format(this.decimalFormat,flow);
        return new ArrayList<>(){{add(str);}};
    }
}
