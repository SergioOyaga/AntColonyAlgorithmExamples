package org.soyaga.examples.VRP.CapacitatedVRP;


import org.jxmapviewer.viewer.GeoPosition;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.CapacitatedVRPColonyAlgorithm;
import org.soyaga.examples.VRP.CapacitatedVRP.ApiCall.GoogleRouteMatrixApiCall;
import org.soyaga.examples.VRP.CapacitatedVRP.ApiCall.GoogleRoutesApiCall;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.UI;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.CDCWaypoint;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.ToVisitWaypoint;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.TruckWaypoint;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.VisitedWaypoint;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class aims to manage the interaction between the frontend and backend.
 */
public class Controller implements PropertyChangeListener {
    /**
     * CapacitatedVRPColonyAlgorithm with the optimization problem
     */
    private final CapacitatedVRPColonyAlgorithm aco;
    /**
     * UI with the user interface.
     */
    private final UI ui;
    /**
     * GoogleRouteMatrixApiCall used obtain Aco distances and times.
     */
    private final GoogleRouteMatrixApiCall routeMatrixApiCall;
    /**
     * GoogleRoutesApiCall used to obtain real routes for the UI.
     */
    private final GoogleRoutesApiCall routesApiCall;
    /**
     * Frontend thread.
     */
    private final Thread frontendThread;
    /**
     * Backend thread.
     */
    private Thread backendThread;

    /**
     * Class that controls the interactions between frontend and backend.
     * @param apiKey String with the Googles' API key. If null, Haversine distances and straight lines are used.
     * @throws IOException Exception.
     */
    public Controller(String apiKey) throws IOException {
        this.ui = new UI();
        this.aco = new CapacitatedVRPColonyAlgorithm("CapacitatedAco");
        this.routeMatrixApiCall = new GoogleRouteMatrixApiCall(apiKey);
        this.routesApiCall = new GoogleRoutesApiCall(apiKey);
        this.frontendThread = new Thread(this.ui);// Frontend Thread.
        this.backendThread = new Thread(this.aco); // Backend Thread.
        this.subscribeToComponents();
    }

    /**
     * Function that subscribes the ui and the aco to the Controller
     */
    private void subscribeToComponents() {
        this.ui.addPropertyChangeListener(this);
        this.aco.addPropertyChangeListener(this);
    }

    /**
     * Function that starts the Thread for the frontend, the app.
     */
    public void startApp(){
        frontendThread.start();
    }

    /**
     * Function that initializes and starts the Thread for the ACO.
     */
    private void startAco() {
        this.ui.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); //Set the cursor to wait as the update of the world might take a while.
        this.initializeAco();
        this.aco.setStopFlag(false);
        this.aco.setWaitFlag(false);
        this.backendThread.start();
        this.ui.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));// Set the cursor to default.
    }

    /**
     * Function that initializes the ACO using the front info.
     */
    private void initializeAco() {
        for(Map.Entry<String, HashMap<String,Object>> cdcEntry: this.ui.getCDCWaypointsInfo().entrySet()){
            this.aco.addNewCDC(cdcEntry.getKey(), (GeoPosition) cdcEntry.getValue().get("position"));
        }
        for(Map.Entry<String, HashMap<String,Object>> truckEntry: this.ui.getTruckWaypointsInfo().entrySet()){
            this.aco.addNewTruckNode(truckEntry.getKey(),
                    (GeoPosition) truckEntry.getValue().get("position"),
                    (Double) truckEntry.getValue().get("capacity"),
                    (Double) truckEntry.getValue().get("currentLoad"),
                    (Double) truckEntry.getValue().get("fixedCost"),
                    (Double) truckEntry.getValue().get("variableCost")
            );
        }
        for(Map.Entry<String, HashMap<String,Object>> toVisitEntry: this.ui.getToVisitWaypointsInfo().entrySet()){
            this.aco.addNewWaypointNode(toVisitEntry.getKey(),
                    (GeoPosition) toVisitEntry.getValue().get("position"),
                    (Double) toVisitEntry.getValue().get("quantity")
            );
        }
        this.updateWeights();
        this.updateAcoEdges();
        this.ui.reComputeAllStats();
    }

    /**
     * Function that updates the ACO weights using the front weights.
     */
    private void updateWeights() {
        for(Map.Entry<String, Double> weightEntry:this.ui.getWeights().entrySet()) {
            this.aco.setWeight(weightEntry.getKey(),weightEntry.getValue());
        }
    }

    /**
     * Function that initializes the edges coming from the front info.
     */
    private void updateAcoEdges() {
        HashMap<String, HashMap<String, HashMap<String, Double>>> response = this.routeMatrixApiCall.postRequest(
                this.ui.getACOWaypointsLocations()
        );
        for(Map.Entry<String,HashMap<String,HashMap<String,Double>>>origEntry: response.entrySet()){
            for(Map.Entry<String,HashMap<String,Double>> destEntry: origEntry.getValue().entrySet()){
                this.aco.editNodeEdge(origEntry.getKey(), destEntry.getKey(),
                        destEntry.getValue().get("distance"), destEntry.getValue().get("duration")
                );
            }
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
        if(evt.getPropertyName().equals("runButtonClick")){
            if(this.backendThread.getState() == Thread.State.TERMINATED) {
                this.aco.resetGenerationCounter();
                this.aco.resetSolution();
                this.aco.resetPheromoneContainer();
                this.backendThread = new Thread(aco);
            }
            if(this.backendThread.getState() == Thread.State.NEW){
                this.startAco();
            }
            else if(this.backendThread.getState() == Thread.State.RUNNABLE){
                this.aco.setWaitFlag(true);
                this.ui.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); //Set the cursor to wait as the update of the world might take a while.
                this.updateAcoEdges();
                this.ui.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));// Set the cursor to default.
                this.aco.setWaitFlag(false);
            }
            else if(this.backendThread.getState() == Thread.State.TIMED_WAITING){
                this.aco.setWaitFlag(!this.aco.getWaitFlag());
            }
        }
        else if (evt.getPropertyName().equals("stopButtonClick")) {
            if(this.backendThread.getState() == Thread.State.RUNNABLE){
                this.aco.setStopFlag(true);
            }
        }
        else if (evt.getPropertyName().equals("quantityVisited")) {
            String truckID = (String) evt.getOldValue();
            this.ui.changeDeliveredQuantity(truckID, (Double) evt.getNewValue());
            this.ui.reComputeStats(truckID);
        }
        else if (evt.getPropertyName().equals("colonyImage")){
            this.ui.setPheromoneImage((BufferedImage) evt.getNewValue());
        }
        else if (evt.getPropertyName().equals("routeChange")){
            this.updateUIRoutes((HashMap<String, HashMap<String, Object>>)evt.getNewValue());
        }
        else if(this.backendThread.getState() == Thread.State.RUNNABLE){
            if (evt.getPropertyName().equals("quantityToVisit")) {
                this.aco.changeRequestedQuantity((String)evt.getOldValue(), (Double)evt.getNewValue());
                this.aco.resetSolution();
            }
            else if (evt.getPropertyName().equals("position")) {
                this.aco.changeLocation((String)evt.getOldValue(), (GeoPosition) evt.getNewValue());
                this.editEdges((String) evt.getOldValue(), (GeoPosition) evt.getNewValue());
                this.aco.resetSolution();
            }
            else if (evt.getPropertyName().equals("capacityTruck")) {
                this.aco.changeTruckCapacity((String)evt.getOldValue(), (Double) evt.getNewValue());
                this.aco.resetSolution();
            }
            else if (evt.getPropertyName().equals("currentLoadTruck")) {
                this.aco.changeTruckInitialLoad((String)evt.getOldValue(), (Double) evt.getNewValue());
                this.aco.resetSolution();
            }
            else if (evt.getPropertyName().equals("fixedCostTruck")) {
                this.aco.changeTruckFixedCost((String)evt.getOldValue(), (Double) evt.getNewValue());
                this.aco.resetSolution();
            }
            else if (evt.getPropertyName().equals("variableCostTruck")) {
                this.aco.changeTruckVariableCost((String)evt.getOldValue(), (Double) evt.getNewValue());
                this.aco.resetSolution();
            }
            else if (evt.getPropertyName().equals("createWaypoint")) {
                ToVisitWaypoint waypoint = (ToVisitWaypoint) evt.getNewValue();
                this.aco.setWaitFlag(true);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.aco.addNewWaypointNode(waypoint.getID(), waypoint.getPosition(), waypoint.getQuantity());
                this.editEdges(waypoint.getID(), waypoint.getPosition());
                this.aco.resetSolution();
                this.aco.setWaitFlag(false);
            }
            else if (evt.getPropertyName().equals("removeWaypoint")) {
                this.aco.setWaitFlag(true);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.aco.removeWaypointNode((String) evt.getOldValue());
                this.aco.resetSolution();
                this.aco.setWaitFlag(false);
            }
            else if (evt.getPropertyName().equals("weightChange")){
                this.aco.setWeight((String) evt.getOldValue(), Double.valueOf((String)evt.getNewValue()));
                this.aco.resetSolution();
            }
        }
        else{
            System.out.println(evt.getPropertyName()+ " not handled by the Controller");
        }
    }

    /**
     * Function that computes and sets the edges' distances in the ACO for a new/moved Waypoint and the rest of the
     * points in the map.
     * @param id String with the new/moved waypoint.
     * @param position GeoPosition new location of the waypoint.
     */
    private void editEdges(String id, GeoPosition position) {
        HashMap<String, HashMap<String, HashMap<String, Double>>> response = this.routeMatrixApiCall.postRequest(
                new HashMap<>(){{put(id, position);}},
                this.ui.getACOWaypointsLocations()
        );
        for(Map.Entry<String,HashMap<String,HashMap<String,Double>>>origEntry: response.entrySet()){
            for(Map.Entry<String,HashMap<String,Double>> destEntry: origEntry.getValue().entrySet()){
                this.aco.editNodeEdge(origEntry.getKey(), destEntry.getKey(),
                        destEntry.getValue().get("distance"), destEntry.getValue().get("duration")
                );
            }
        }
    }

    /**
     * Function that updates the routes in the front along with the Stats table.
     * @param infoByTruck HashMap<String,HashMap<String, Object>> with the new info by truckID.
     */
    private void updateUIRoutes(HashMap<String,HashMap<String, Object>> infoByTruck) {
        for(Map.Entry<String, HashMap<String,Object>> routeEntry:infoByTruck.entrySet()) {
            HashMap<String,Object> response = this.routesApiCall.postRequest((ArrayList<GeoPosition>) routeEntry.getValue().get("trackToVisit"));
            String truckID = routeEntry.getKey();
            this.ui.addToVisitTrack(truckID,
                    (ArrayList<GeoPosition>)response.get("track"),
                    (Double) response.get("distance"),
                    (Double) response.get("duration")
                    );
            this.ui.changeToDeliverQuantity(truckID, (Double) routeEntry.getValue().getOrDefault("deliveredQuantity", 0.));
            this.ui.changeToVisitWaypointNumber(truckID, (Double) routeEntry.getValue().getOrDefault("visitedWaypoints", 0.));
            this.ui.changeToLoadNumber(truckID,  (Double) routeEntry.getValue().getOrDefault("loadsNumber",0.));
            this.ui.reComputeStats(truckID);
        }
        this.ui.refreshMap();
    }

    /**
     * Function that adds a truck to the GUI.
     * @param ID String with the truck ID
     * @param coord GeoPosition of the truck
     * @param capacity Double with the capacity of the truck
     * @param currentLoad Double with the load of the truck.
     * @param fixedCost Double with the fixed cost
     * @param variableCost Double with the variable cost
     * @param img BufferedImage of the truck
     * @param routeColor Color for the pending route.
     */
    public void addGUITruck(String ID, GeoPosition coord, Double capacity, Double currentLoad, Double fixedCost, Double variableCost,
                            BufferedImage img, Color routeColor){
        this.ui.addGUITruck(new TruckWaypoint(ID, coord, capacity, currentLoad, fixedCost, variableCost, img, routeColor));
    }

    /**
     * Function that adds aCDC to the GUI.
     * @param ID String with the CDC ID.
     * @param coord GeoPosition of the CDC.
     * @param img BufferedImage of the CDC.
     */
    public void addGUICDC(String ID, GeoPosition coord, BufferedImage img){
        this.ui.addGUICDC(new CDCWaypoint(ID, coord,  img));
    }

    /**
     * Function that adds a visited waypoint to the GUI.
     * @param ID String with the ID of the waypoint
     * @param coord GeoPosition of the waypoint
     * @param quantity Double with the quantity delivered
     * @param truckID String with the ID of the truck that served that node
     * @param img BufferedImage of the visited node.
     */
    public void addGUIVisited(String ID, GeoPosition coord, Double quantity, String truckID, BufferedImage img){
        this.ui.addGUIVisitedWaypoint(new VisitedWaypoint(ID, coord, quantity, truckID, img));
    }

    /**
     * Function that adds a to visit waypoint to the GUI.
     * @param ID String with the waypoint ID
     * @param coord GeoPosition of the waypoint
     * @param quantity Double with the quantity to deliver.
     * @param img BufferedImage of the waypoint.
     */
    public void addGUIToVisit(String ID, GeoPosition coord, Double quantity, BufferedImage img){
        this.ui.addGUIToVisitWaypoint(new ToVisitWaypoint(ID, coord, quantity, img));
    }

    /**
     * Function that sets the visited waypoint number of a truck to a value.
     * @param truckID String with the truckID.
     * @param value Double with the value to set.
     */
    public void changeVisitedWaypointNumber(String truckID, Double value) {
        this.ui.changeVisitedWaypointNumber(truckID, value);
    }

    /**
     * Function that adds a visited track to the GUIRoute associated to the truck.
     * @param truckID String with the ID of the truck.
     * @param visitedTrack ArrayList<TrackWaypoint> to add.
     * @param visitedDistance double with the visited distance.
     * @param visitedDuration double with the visited duration.
     */
    public void addVisitedTrack(String truckID, ArrayList<GeoPosition> visitedTrack, double visitedDistance,
                                double visitedDuration){
        this.ui.addVisitedTrack(truckID,visitedTrack,visitedDistance,visitedDuration);
    }
}
