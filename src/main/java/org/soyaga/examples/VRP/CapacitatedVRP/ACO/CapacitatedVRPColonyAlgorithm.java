package org.soyaga.examples.VRP.CapacitatedVRP.ACO;

import lombok.Getter;
import org.jxmapviewer.viewer.GeoPosition;
import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.Ant.EdgeSelector.RandomProportionalEdgeSelector;
import org.soyaga.aco.AntColonyAlgorithm.AntColonyAlgorithm;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Evaluable.Feasibility.ConstraintBasedFeasibilityFunction;
import org.soyaga.aco.Evaluable.Objective.MultiObjective.MultiObjective;
import org.soyaga.aco.Evaluable.Objective.MultiObjective.Scalarize.WeightedNormScalarize;
import org.soyaga.aco.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.aco.SolutionConstructorPolicy.SimpleConstructorPolicy;
import org.soyaga.aco.SolutionConstructorPolicy.SolutionConstructorPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.NIterationsStatsRetrievalPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.*;
import org.soyaga.aco.StatsRetrievalPolicy.StatsRetrievalPolicy;
import org.soyaga.aco.UpdatePheromonePolicy.AddPheromonePolicy.MaxGlobalBestFitnessProportionalAddPheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.EvaporatePheromonePolicy.MinPercentageEvaporatePheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.SimpleUpdatePheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.UpdatePheromonePolicy;
import org.soyaga.aco.world.GenericWorld;
import org.soyaga.aco.world.Graph.Elements.Edge;
import org.soyaga.aco.world.Graph.Elements.Node;
import org.soyaga.aco.world.World;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Ant.ACOAnt;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.BuilderEvaluator.NoPendingDemandClosedRoutesTrucksAtCDC;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Constraints.RouteCoherence;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Constraints.ServiceConstraint;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.ACOGraph;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Edges.ACOEdge;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.*;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Objectives.CostObjective;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Objectives.DistanceObjective;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Objectives.ReloadPenalization;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Objectives.TimeObjective;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Stats.ImageStat;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Stats.RouteChangeStat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * This class extends StatsAntColonyAlgorithm and specifies the method of result collection, which involves notifying
 * the controller the optimization has ended.
 */
public class CapacitatedVRPColonyAlgorithm implements AntColonyAlgorithm, Runnable, PropertyChangeListener {
    /**
     * String with the name of the ACO.
     */
    private final String ID;
    /**
     * world with the Graph and Pheromone container.
     */
    @Getter
    private final World world;
    /**
     * Colony with the Ants.
     */
    @Getter
    private final Colony colony;
    /**
     * AtomicBoolean shared with the controller that can be changed in runtime.
     */
    private volatile AtomicBoolean stopFlag;
    /**
     * AtomicBoolean shared with the controller that can be changed in runtime.
     */
    private volatile AtomicBoolean waitFlag;
    /**
     * ACOInitializer Object used to initialize the Ants that are going to be used in the optimization.
     */
    @Getter
    private final ACOInitializer acoInitializer;
    /**
     * SolutionConstructorPolicy Object used to build the solutions in each iteration.
     */
    private final SolutionConstructorPolicy solutionConstructorPolicy;
    /**
     * UpdatePheromonePolicy Object used to update the pheromone in the world each iteration.
     */
    private final UpdatePheromonePolicy updatePheromonePolicy;
    /**
     * StatsRetrievalPolicy object of the ACO.
     */
    @Getter
    private final StatsRetrievalPolicy statsRetrievalPolicy;
    /**
     * PropertyChangeSupport object to fire events when properties of this class change.
     */
    @Getter
    private final PropertyChangeSupport pcs;
    /**
     * HashMap with the Objective by name.
     */
    private final HashMap<String, ObjectiveFunction> objectives;
    /**
     * HashMap with the weight by objective. Same object that in the Scalarize.
     */
    private final HashMap<ObjectiveFunction, Double> weights;
    /**
     * Integer with the generation number
     */
    private int generation;
    /**
     * HashMap<String,String> that relates the external mapping with the internal names.
     */
    private final HashMap<String,String> externalMapping;


    /**
     * This method receives all the parameters necessary to create an object of this class.
     *
     * @param ID                        A string representing the name of the ACO.
     */
    public CapacitatedVRPColonyAlgorithm(String ID) throws IOException {
        this.ID = ID;
        Double initialPheromone = 1.; // double with the initial pheromone of each edge.
        ACOGraph graph = new ACOGraph(initialPheromone);
        this.world = new GenericWorld(graph, graph);
        this.colony = new Colony();
        this.stopFlag = new AtomicBoolean(true);
        this.waitFlag = new AtomicBoolean(false);
        DistanceObjective distanceObjective = new DistanceObjective();
        TimeObjective timeObjective = new TimeObjective();
        CostObjective costObjective = new CostObjective();
        ReloadPenalization reloadPenalization = new ReloadPenalization();
        this.weights = new HashMap<>(){{
            put(distanceObjective,0.33);
            put(timeObjective,0.33);
            put(costObjective,0.33);
            put(reloadPenalization,1.);
        }};
        this.objectives = new HashMap<>(){{
            put("distance", distanceObjective);
            put("duration", timeObjective);
            put("cost", costObjective);
        }};
        int antNumber =20;  //Integer with the initial number of ants.
        Double antPheromoneQuantity=40000.; //Double with the amount of pheromone each ant can deposit in its track (same order of the problem optimal fitness).
        Double alpha = 1.;  //Double with the ants' Alpha (>0) parameter (importance of the edges pheromones against the edges "distances").
        Double beta = 2.;   //Double with the ants' Beta (>0) parameter (importance of the edges "distances" against the edges pheromones).
        Double penalization = 10000000.; //Double with the infeasibility penalization.
        Integer maxEdges = 200;  //Integer with the maximum number of edges an ant's solution can have.
        this.acoInitializer = new ACOInitializer(
                new ACOAnt(
                        new MultiRouteSolution(
                                new MultiObjective(
                                        new ArrayList<>(this.weights.keySet()),
                                        new WeightedNormScalarize(
                                                this.weights,
                                                1
                                        )
                                ),
                                new ConstraintBasedFeasibilityFunction(new ArrayList<>(){{
                                    add(new ServiceConstraint());
                                    add(new RouteCoherence());
                                }}),
                                penalization,
                                maxEdges,
                                new NoPendingDemandClosedRoutesTrucksAtCDC(),
                                new ArrayList<>()
                        ),
                        new RandomProportionalEdgeSelector(
                                alpha,
                                beta
                        ),
                        antPheromoneQuantity
                ),
                antNumber
        );
        this.solutionConstructorPolicy = new SimpleConstructorPolicy();
        Double persistence = .995;   //Double with pheromone persistence in the edge.
        Double minPheromone = .15;  //Double with min pheromone an edge can have.
        Double maxPheromone = 1.;   //Double with max pheromone an edge can have.
        this.updatePheromonePolicy = new SimpleUpdatePheromonePolicy(                        //UpdatePheromonePolicy, first evaporate then add:
                new MaxGlobalBestFitnessProportionalAddPheromonePolicy( //AddPheromonePolicy, prop to sol fitness:
                        maxPheromone                                        //Double, max pheromones.
                ),
                new MinPercentageEvaporatePheromonePolicy(          //EvaporatePheromonePolicy, percentage persistence:
                        persistence,                                    //Double, persistence.
                        minPheromone)                                   // Double, min pheromone.
        );
        Integer steps = 100;  //Integer with the steps between stat measures.
        Integer nOfDecimals = 4;   //Integer with the number of decimals to plot in the screen.
        Double imageWidth = 1000.;  // Double with the width of the image.
        Double imageHeight = 1000.; // Double with the height of the image
        Double pheromoneIntensity = 1.; //Double with the intensity of the pheromone in the graph.
        this.statsRetrievalPolicy = new NIterationsStatsRetrievalPolicy(                    //Stats retrieval policy, every n iterations:
                steps,                                              //Integer, steps between measures.
                new ArrayList<>(){{                                 //Array Of Stats
                    add(new CurrentMinFitnessStat(nOfDecimals));        //Min Fitness Stat.
                    add(new CurrentMaxFitnessStat(nOfDecimals));        //Max Fitness Stat.
                    add(new HistoricalMinFitnessStat(nOfDecimals));     //Hist Min Fitness Stat.
                    add(new MeanSdFitnessStat(nOfDecimals));            //Fitness Mean and Standard Dev Stat.
                    add(new MeanSdPheromoneStat(nOfDecimals));          //Fitness Mean and Standard Dev Stat.
                    add(new ImageStat(imageWidth,imageHeight,pheromoneIntensity)); //Image Stat
                    add(new RouteChangeStat());                         // Stat with the route change
                }},
                null,                                         //String, with the output path.
                true,                                               //Boolean, print in console.
                false                                                //Boolean, store in csv file.
        );
        this.subscribeToComponents();
        this.pcs = new PropertyChangeSupport(this);
        this.generation = 0;
        this.externalMapping = new HashMap<>();
    }

    /**
     * Function that performs the typical optimization for an ant colony optimization algorithm.
     */
    public synchronized void optimize() throws IOException {
        this.acoInitializer.initialize(this);
        while (!this.getStopFlag()){
            this.isUpdating();
            this.solutionConstructorPolicy.apply(this.world, this.colony, this.world);
            this.colony.evaluate(this.world);
            this.updatePheromonePolicy.apply(this.world, this.colony);
            this.generation++;
            this.statsRetrievalPolicy.apply(this.world,this.colony,this.generation);
        }
        this.statsRetrievalPolicy.closeWriter();
    }

    /**
     * Function that keeps the thread in a loop until the controller updates the world.
     */
    private void isUpdating(){

        while(this.getWaitFlag()){
            try {
                this.wait(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * AtomicBoolean getter. Stop flag.
     * @return Boolean with the Atomic value.
     */
    public Boolean getStopFlag() {
        return stopFlag.get();
    }

    /**
     * AtomicBoolean setter. Stop flag.
     * @param stopFlag Boolean for the Atomic value.
     */
    public void setStopFlag(Boolean stopFlag) {
        this.stopFlag.set(stopFlag);
    }

    /**
     * AtomicBoolean getter. Wait flag.
     * @return Boolean with the Atomic value.
     */
    public Boolean getWaitFlag() {
        return waitFlag.get();
    }

    /**
     * AtomicBoolean setter. Wait flag.
     * @param waitFlag Boolean for the Atomic value.
     */
    public void setWaitFlag(Boolean waitFlag) {
        this.waitFlag.set(waitFlag);
    }

    /**
     * Method that returns from an optimized solution the actual result in the
     * form that is convenient for the problem.
     *
     * @param resultArgs VarArgs containing the additional information needed to get the results.
     * @return Object containing the result of the optimization. Ej.:
     * <ul>
     *     <li>Best <b><i>Individual</i></b></li>
     *     <li>Set of best <b><i>Individuals</i></b></li>
     *     <li>Any format suitable for our problem <b><i>Object</i></b></li>
     * </ul>
     */
    @Override
    public Object getResult(Object... resultArgs) {
        return "Optimization finished";
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        try {
            this.optimize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        forwardEvent(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }

    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     * The same listener object may be added more than once, and will be called
     * as many times as it is added.
     * If {@code listener} is null, no exception is thrown and no action
     * is taken.
     *
     * @param listener  The PropertyChangeListener to be added
     */
    public void addPropertyChangeListener(PropertyChangeListener listener){
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * Function that forwards the events in the aco to the outside world
     * @param propertyName String with the property name
     * @param oldValue Object with the old value
     * @param newValue Object with the new value
     */
    private void forwardEvent(String propertyName, Object oldValue, Object newValue){
        this.pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Function that subscribes all relevant stats to the CapacitatedVRPColonyAlgorithm class in order to provide one
     * communication point to the outside
     */
    private void subscribeToComponents() {
        for(Stat stat: this.statsRetrievalPolicy.getStats()){
            if(stat instanceof ImageStat imageStat) imageStat.addPropertyChangeListener(this);
            if(stat instanceof RouteChangeStat routeChangeStat) routeChangeStat.addPropertyChangeListener(this);
        }
    }

    /**
     * Function that clears the current best solution by removing the routesMap entries.
     */
    public void resetSolution(){
        MultiRouteSolution solution = (MultiRouteSolution)this.colony.getBestSolution();
        if (solution==null) return;
        solution.clearSolution();
        this.colony.getBestSolution().evaluate(this.world);
    }

    /**
     * Function that resets all edges pheromone to the original value.
     */
    public void resetPheromoneContainer(){
        for(Object node: this.world.getGraph().getNodes()){
            for (Edge edge:((Node) node).getOutputEdges()){
                edge.setPheromone(((ACOGraph) this.world.getGraph()).getInitialPheromone());
            }
        }
    }

    /**
     * Function that reseats the generation counter to 0.
     */
    public void resetGenerationCounter(){
        this.generation = 0;
    }

    /**
     * Function that changes the location of an ACONode.
     * @param ID String with the node ID.
     * @param position String with the node new position.
     */
    public void changeLocation(String ID, GeoPosition position){
        ((ACONode)((ACOGraph)this.world.getGraph()).getNodesObject().get(this.externalMapping.get(ID))).setPointLocation(position);
    }

    /**
     * Function that changes the requested quantity of a WaypointNode
     * @param ID String with the ID of the node
     * @param quantity Double with the quantity to request.
     */
    public void changeRequestedQuantity(String ID, Double quantity){
        ((WaypointNode)((ACOGraph)this.world.getGraph()).getNodesObject().get(this.externalMapping.get(ID))).setRequestedQuantity(quantity);
    }

    /**
     * Function that changes the capacity of a VirtualTruck
     * @param ID String with the ID of the node
     * @param capacity Double with the capacity of the truck.
     */
    public void changeTruckCapacity(String ID, Double capacity){
        ((VirtualTruck)((ACOGraph)this.world.getGraph()).getNodesObject().get(ID)).setCapacity(capacity);
    }

    /**
     * Function that changes the initial load of a VirtualTruck
     * @param ID String with the ID of the node
     * @param initialLoad Double with the initial load of the truck.
     */
    public void changeTruckInitialLoad(String ID, Double initialLoad){
        ((VirtualTruck)((ACOGraph)this.world.getGraph()).getNodesObject().get(ID)).setInitialLoad(initialLoad);
    }

    /**
     * Function that changes the fixed cost of a VirtualTruck
     * @param ID String with the ID of the node
     * @param fixedCost Double with the fixed cost of the truck.
     */
    public void changeTruckFixedCost(String ID, Double fixedCost){
        ((VirtualTruck)((ACOGraph)this.world.getGraph()).getNodesObject().get(ID)).setFixedCost(fixedCost);
    }

    /**
     * Function that changes the variable cost of a VirtualTruck
     * @param ID String with the ID of the node
     * @param variableCost Double with the variable cost of the truck.
     */
    public void changeTruckVariableCost(String ID, Double variableCost){
        ((VirtualTruck)((ACOGraph)this.world.getGraph()).getNodesObject().get(ID)).setVariableCost(variableCost);
    }

    /**
     * Function that adds a new CDC to the graph.
     * @param ID String with the CDC ID
     * @param position GeoPosition of the CDC.
     */
    public void addNewCDC(String ID, GeoPosition position){
        ((ACOGraph)this.world.getGraph()).getNodesObject().putIfAbsent(ID, new CDCNode(ID,position));
        this.externalMapping.put(ID, ID);
    }

    /**
     * Function that creates a new truck node and the associated nodes (if it didn't exist).
     * @param ID String with the ID of the Truck.
     * @param position GeoPosition of the Truck.
     * @param capacity Double with the truck capacity.
     * @param initialLoad Double with the truck initialLoad.
     * @param fixedCost Double with the truck fixedCost.
     * @param variableCost Double with the truck variableCost.
     */
    public void addNewTruckNode(String ID, GeoPosition position, Double capacity, Double initialLoad,
                                Double fixedCost, Double variableCost){
        if(!((ACOGraph)this.world.getGraph()).getNodesObject().containsKey(ID)){
            WaypointNode waypointNode = new WaypointNode(ID+"Waypoint", position, 0.);
            VirtualTruck virtualTruck = new VirtualTruck(ID, waypointNode, capacity, initialLoad, fixedCost, variableCost);
            this.externalMapping.put(ID, ID+"Waypoint");
            ((ACOGraph)this.world.getGraph()).getNodesObject().put(waypointNode.getID(),waypointNode);
            ((ACOGraph)this.world.getGraph()).getNodesObject().put(virtualTruck.getID(),virtualTruck);
            ((MultiRouteSolution)this.acoInitializer.getAnt().getSolution()).getVirtualTrucks().add(virtualTruck);
            for(Object node: this.world.getGraph().getNodes()){
                if(node instanceof SelectorNode selectorNode){
                    selectorNode.getOutputEdges().add(new ACOEdge(selectorNode,virtualTruck,
                            1.,1.,1.,1.));
                }
            }
        }

    }

    /**
     * Function that creates a new waypointNode (if it didn't exist).
     * @param ID String with the ID of the Waypoint.
     * @param position GeoPosition of the waypoint.
     * @param requestedQuantity Double with the requested quantity.
     */
    public void addNewWaypointNode(String ID, GeoPosition position, Double requestedQuantity){
        if(!((ACOGraph)this.world.getGraph()).getNodesObject().containsKey(ID)) {
            ((ACOGraph) this.world.getGraph()).getNodesObject().putIfAbsent(ID, new WaypointNode(ID, position, requestedQuantity));
            this.externalMapping.put(ID, ID);
            int waypointCount = 0;
            int selectorCount = 0;
            HashSet<VirtualTruck> trucks = new HashSet<>();
            for(Object node:this.world.getGraph().getNodes()){
                if(node instanceof WaypointNode) waypointCount++;
                else if (node instanceof SelectorNode) selectorCount++;
                else if (node instanceof VirtualTruck virtualTruck) trucks.add(virtualTruck);
            }
            for(int i = 0; i< waypointCount-selectorCount;i++){
                SelectorNode selectorNode = new SelectorNode("Selector"+ID+i);
                ((ACOGraph)this.world.getGraph()).getNodesObject().put(selectorNode.getID(),selectorNode);
                for(VirtualTruck truck:trucks){
                    selectorNode.getOutputEdges().add(new ACOEdge(selectorNode, truck,
                            1., 1.,1., 1.));
                }
            }
            while(waypointCount > this.colony.getAnts().size()) this.colony.addAnt(this.acoInitializer.getAnt().createNewInstance());
        }
    }

    /**
     * Function that removes a WaypointNode from the graph along with the edges that use it.
     * @param ID String with the ID of the WaypointNode to remove.
     */
    public void removeWaypointNode(String ID){
        Node node = ((ACOGraph)this.getWorld().getGraph()).getNode(ID);
        if (node instanceof WaypointNode) {
            ((ACOGraph) this.getWorld().getGraph()).removeNode(ID);
            this.externalMapping.remove(ID);
            this.colony.getAnts().remove(this.colony.getAnts().size()-1);
        }

    }

    /**
     * Function that edits/creates an ACOEdge.
     * @param originID String with the ID of the origin node.
     * @param destID String with the ID of the destination node.
     * @param distance Double with the distance in meters of the edge.
     * @param duration Double with the duration in seconds of the edge.
     */
    public void editNodeEdge(String originID, String destID, Double distance, Double duration){
        HashSet<Edge> edges = ((ACOGraph)this.world.getGraph()).getNodesObject().get(this.externalMapping.get(originID)).getOutputEdges();
        for(Edge edge:edges){
            if(edge.getDestination().getID().equals(this.externalMapping.get(destID))){
                edge.setDistance(computeHeuristicDistance(distance,duration));
                ((ACOEdge) edge).setDistanceMeters(distance);
                ((ACOEdge) edge).setDurationSeconds(duration);
                return;
            }
        }
        edges.add(new ACOEdge(
                ((ACOGraph)this.world.getGraph()).getNodesObject().get(this.externalMapping.get(originID)),
                ((ACOGraph)this.world.getGraph()).getNodesObject().get(this.externalMapping.get(destID)),
                computeHeuristicDistance(distance,duration),
                ((ACOGraph) this.world.getGraph()).getInitialPheromone(),
                distance,
                duration));
    }

    /**
     * Function that computes the heuristic distance.
     * @param distanceMeters Double with the distance.
     * @param durationSeconds Double with the duration.
     * @return double with the heuristic distance.
     */
    private double computeHeuristicDistance(Double distanceMeters, Double durationSeconds){
        return Math.max(this.weights.get(this.objectives.get("distance"))*distanceMeters +
                this.weights.get(this.objectives.get("duration"))*durationSeconds+
                this.weights.get(this.objectives.get("cost"))*distanceMeters, 100);
    }

    /**
     * Function that changes the weight of an objective.
     * @param ID String with the ID of the objective.
     * @param value Double with the weight of the objective.
     */
    public void setWeight(String ID, Double value){
        this.weights.put(this.objectives.get(ID), value);
        for(Object node: this.world.getGraph().getNodes()) {
            if((node instanceof WaypointNode) | (node instanceof CDCNode)) {
                for (Object edge : this.world.getGraph().getOutputEdges(node)) {
                    ACOEdge acoEdge = (ACOEdge) edge;
                    acoEdge.setDistance(this.computeHeuristicDistance(acoEdge.getDistanceMeters(), acoEdge.getDurationSeconds()));
                }
            }
        }
    }
}
