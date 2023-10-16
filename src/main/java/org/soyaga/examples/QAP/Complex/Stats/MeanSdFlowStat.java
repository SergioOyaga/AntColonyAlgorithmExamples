package org.soyaga.examples.QAP.Complex.Stats;

import org.soyaga.aco.Ant.Ant;
import org.soyaga.aco.Colony;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.Stat;
import org.soyaga.aco.world.World;
import org.soyaga.examples.QAP.Complex.RunCustomQapOptimization;
import org.soyaga.examples.QAP.Complex.RunCustomQapOptimization.Facility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Stat that computes the mean and standard deviation of the facility flows for the colony.
 */
public class MeanSdFlowStat  implements Stat {
    /**
     * ArrayList{@literal <String>} with the different column headers computed by this Stat.
     */
    private final ArrayList<String> header;
    /**
     * String with the formatting of the Double number produced by the Stat.
     */
    private final String decimalFormat;
    /**
     * ArrayList{@literal <Facility> with the locations.
     */
    private final ArrayList<Facility> facilities;

    /**
     * Constructor.
     * @param decimalNumber Integer with the number of decimal points to keep in the stat.
     * @param facilities ArrayList{@literal <Facility> with the locations.
     */
    public MeanSdFlowStat(Integer decimalNumber, ArrayList<Facility> facilities) {
        this.header = new ArrayList<>(){{add("MeanFlow");add("StrDevFlow");}};
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
     * This ArrayList of strings composes the values for this stat. In this case, the stat returns the
     * colony mean flow and standard deviation values.
     *
     * @param world World used to measure the stats.
     * @param colony Colony used to measure the stats.
     * @param statArgs   VarArgs containing the additional information needed to apply the stat. (Not used)
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> apply(World world, Colony colony, Object... statArgs) {
        ArrayList<Double> flows = new ArrayList<>();
        for(Ant ant:colony.getAnts()){
            HashMap<Facility, RunCustomQapOptimization.Location> assignation = new HashMap<>();
            Object currentNode = ant.getSolution().getNodesVisited().get(0);
            for (Object edge : ant.getSolution().getEdgesUtilized()) {
                if (this.facilities.contains(currentNode)) {
                    RunCustomQapOptimization.Location destination = (RunCustomQapOptimization.Location) world.getGraph().getNextNode(edge);
                    assignation.put((Facility) currentNode, destination);
                }
                currentNode =  world.getGraph().getNextNode(edge);
            }
            Double flow = 0.;
            for(Map.Entry<Facility, RunCustomQapOptimization.Location> assignationEntry: assignation.entrySet()){
                Facility facilityOrigin = assignationEntry.getKey();
                RunCustomQapOptimization.Location locationOrigin = assignationEntry.getValue();
                for( Map.Entry<Object,Double> flowEntry:facilityOrigin.facilityOutgoingFlow.entrySet()){
                    Facility facilityDest = (Facility) flowEntry.getKey();
                    if(locationOrigin.locationDistances.keySet().contains(assignation.getOrDefault(facilityDest,null))){
                        flow += flowEntry.getValue();
                    }
                }
            }
            flows.add(flow);
        }
        Double mean = flows.stream().reduce(0., Double::sum)/flows.size();
        double sd = flows.stream().
                reduce(0.,(acc,val) -> acc+(val-mean)*(val-mean),Double::sum)/ colony.getAnts().size();
        String meanStr = String.format(this.decimalFormat, mean);
        String sdStr = String.format(this.decimalFormat, Math.sqrt(sd));
        return new ArrayList<>(){{add(meanStr);add(sdStr);}};
    }
}
