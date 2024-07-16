package org.soyaga.examples.VRP.CapacitatedVRP.GUI;

import lombok.Getter;
import org.jxmapviewer.viewer.GeoPosition;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Panels.JXMapPanel;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Panels.PheromonePanel;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Panels.StatsPanel;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Panels.WeightsPanel;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class that represents the visual interface and provides the point of contact with the outside.
 */
@Getter
public class UI extends JFrame implements Runnable, PropertyChangeListener, ActionListener, TableModelListener {
    /**
     * JXMapPanel with the map.
     */
    private final JXMapPanel mainMap;
    /**
     * PheromonePanel with the pheromone image.
     */
    private PheromonePanel pheromonePanel;
    /**
     * StatsPanel with the solution stats.
     */
    private StatsPanel statsPanel;
    /**
     * WeightsPanel with the weights.
     */
    private WeightsPanel weightsPanel;
    /**
     * JButton that refresh the ACO.
     */
    private JButton runButton;
    /**
     * JButton that stops the ACO.
     */
    private JButton stopButton;


    /**
     * Creates a new, initially invisible, <code>Frame</code> with the
     * specified title.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *                           returns true.
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     */
    public UI() throws HeadlessException, IOException {
        super("GUIRoute UI");
        this.mainMap = new JXMapPanel(new GUIWorld(), ImageIO.read(Objects.requireNonNull(RunTest.class.getResource("/VRP/Icons/ToVisitPlaceholder.png"))));
        this.initComponents();
        this.subscribeToComponents();
    }

    /**
     * Function that initializes the JFrame components.
     */
    private void initComponents() {
        this.pheromonePanel = new PheromonePanel();
        this.statsPanel = new StatsPanel();
        this.weightsPanel = new WeightsPanel();
        this.runButton = new JButton("Run/Update Optimization");
        this.stopButton = new JButton("Stop Optimization");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 600);

        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(this.mainMap)
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(this.pheromonePanel,350,350,350)
                                        .addComponent(this.weightsPanel,310,310,310)
                                        .addComponent(this.statsPanel, 350,350,350)
                                        .addGroup(
                                                layout.createSequentialGroup()
                                                        .addComponent(this.runButton)
                                                        .addComponent(this.stopButton)
                                        )
                        )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(this.mainMap)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(this.pheromonePanel,350,350,350)
                                .addComponent(this.weightsPanel,50,50,50)
                                .addComponent(this.statsPanel, 60,200,400)
                                .addGroup(
                                        layout.createParallelGroup()
                                                .addComponent(this.runButton)
                                                .addComponent(this.stopButton)
                                )
                        )

        );
    }

    /**
     * Function that subscribes all GUIWorld, its components and the front components to the UI class in order to provide one
     * communication point to the outside
     */
    private void subscribeToComponents() {
        this.mainMap.getGUIWorld().addPropertyChangeListener(this);
        for(TruckWaypoint truckWaypoint:this.mainMap.getGUIWorld().getGUITrucksByID().values()){
            truckWaypoint.addPropertyChangeListener(this);
        }
        for(CDCWaypoint cdcWaypoint:this.mainMap.getGUIWorld().getGUICDCs()){
            cdcWaypoint.addPropertyChangeListener(this);
        }
        for(VisitedWaypoint visitedWaypoint:this.mainMap.getGUIWorld().getVisitedWaypoints()){
            visitedWaypoint.addPropertyChangeListener(this);
        }
        for(ToVisitWaypoint toVisitWaypoint:this.mainMap.getGUIWorld().getToVisitWaypoints()){
            toVisitWaypoint.addPropertyChangeListener(this);
        }
        this.runButton.addActionListener(this);
        this.stopButton.addActionListener(this);
        this.weightsPanel.getWeights().getModel().addTableModelListener(this);
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        this.setTrucks();
        this.reComputeAllStats();
        this.setVisible(true);
        this.mainMap.fitZoom(0.9);
    }

    /**
     * Function that sets derivative truck values.
     */
    private void setTrucks() {
        for(VisitedWaypoint visitedWaypoint:this.mainMap.getGUIWorld().getVisitedWaypoints()){
            TruckWaypoint truck = this.mainMap.getGUIWorld().getGUITrucksByID().get(visitedWaypoint.getTruckID());
            this.changeDeliveredQuantity(visitedWaypoint.getTruckID(), truck.getDeliveredQuantity()+ visitedWaypoint.getQuantity());
        }
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.getRunButton()){
            this.forwardEvent("runButtonClick", null,e);
        }
        else if (e.getSource() == this.getStopButton()) {
            this.forwardEvent("stopButtonClick", null,e);
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
        if(evt.getPropertyName().equals("createWaypoint")){
            ((ToVisitWaypoint)evt.getNewValue()).addPropertyChangeListener(this);
            forwardEvent(evt.getPropertyName(), null, evt.getNewValue());
            return;
        }
        else if (evt.getPropertyName().equals("removeWaypoint")) {
            ((ToVisitWaypoint)evt.getOldValue()).removePropertyChangeListener(this);
            forwardEvent(evt.getPropertyName(), ((ToVisitWaypoint)evt.getOldValue()).getID(), null);
            return;
        }
        forwardEvent(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }

    /**
     * This fine grain notification tells listeners the exact range
     * of cells, rows, or columns that changed.
     *
     * @param evt a {@code TableModelEvent} to notify listener that a table model
     *          has changed
     */
    @Override
    public void tableChanged(TableModelEvent evt) {
        int row = evt.getFirstRow();
        int column = evt.getColumn();
        String id = column==0?"distance":column==1?"duration":"cost";
        this.forwardEvent("weightChange", id, this.weightsPanel.getWeights().getValueAt(row,column));
    }

    /**
     * Function that forwards the events in the ui to the outside world
     * @param propertyName String with the property name
     * @param oldValue Object with the old value
     * @param newValue Object with the new value
     */
    private void forwardEvent(String propertyName, Object oldValue, Object newValue){
        this.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Function that adds a to visit track to the GUIRoute associated to the truck.
     * @param truckID String with the ID of the truck.
     * @param toVisitTrack ArrayList<TrackWaypoint> to add.
     * @param toVisitDistance double with the ToVisit distance.
     * @param toVisitDuration double with the ToVisit duration.
     */
    public void addToVisitTrack(String truckID, ArrayList<GeoPosition> toVisitTrack, double toVisitDistance,
                                double toVisitDuration){
        GUIRoute route = this.mainMap.getGUIWorld().getGUIRoutesByTruckID().get(truckID);
        route.clearToVisitTrack();
        for(GeoPosition trackGeoPosition:toVisitTrack) {
            route.appendTrackWaypoint(new TrackWaypoint("",trackGeoPosition));
        }
        route.setToVisitDistance(toVisitDistance);
        route.setToVisitDuration(toVisitDuration);
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
        GUIRoute route = this.mainMap.getGUIWorld().getGUIRoutesByTruckID().get(truckID);
        route.clearVisitedTrack();
        for(GeoPosition trackGeoPosition:visitedTrack) {
            route.appendVisitedTrackWaypoint(new TrackWaypoint("",trackGeoPosition));
        }
        route.setVisitedDistance(visitedDistance);
        route.setVisitedDuration(visitedDuration);
    }

    /**
     * Function that adds a CDCWaypoint to the GUIWorld and subscribe it to this listener.
     * @param cdcWaypoint CDCWaypoint to add and subscribe.
     */
    public void addGUICDC(CDCWaypoint cdcWaypoint){
        this.mainMap.getGUIWorld().addGUICDC(cdcWaypoint);
        cdcWaypoint.addPropertyChangeListener(this);
    }

    /**
     * Function that adds a VisitedWaypoint to the GUIWorld and subscribe it to this listener.
     * @param visitedWaypoint VisitedWaypoint to add and subscribe.
     */
    public void addGUIVisitedWaypoint(VisitedWaypoint visitedWaypoint){
        this.mainMap.getGUIWorld().addGUIVisitedWaypoint(visitedWaypoint);
        visitedWaypoint.addPropertyChangeListener(this);
    }

    /**
     * Function that adds a ToVisitWaypoint to the GUIWorld and subscribe it to this listener.
     * @param toVisitWaypoint ToVisitWaypoint to add and subscribe.
     */
    public void addGUIToVisitWaypoint(ToVisitWaypoint toVisitWaypoint){
        this.mainMap.getGUIWorld().addGUIToVisitWaypoint(toVisitWaypoint);
        toVisitWaypoint.addPropertyChangeListener(this);
    }

    /**
     * Function that adds a TruckWaypoint to the GUIWorld. Also, subscribes it to this listener and creates the GUIRoute
     * associated with the truck.
     * @param truckWaypoint TruckWaypoint to add and subscribe.
     */
    public void addGUITruck(TruckWaypoint truckWaypoint){
        this.mainMap.getGUIWorld().addGUITruck(truckWaypoint);
        truckWaypoint.addPropertyChangeListener(this);
        this.mainMap.getGUIWorld().getGUIRoutesByTruckID().putIfAbsent(truckWaypoint.getID(),new GUIRoute());
        this.statsPanel.addTruckToTable(truckWaypoint);
    }

    /**
     * Function that sets the image of the pheromone panel.
     * @param image BufferedImage with the image.
     */
    public void setPheromoneImage(BufferedImage image){
        this.pheromonePanel.setImage(image);
    }

    /**
     * Function that retrieves the information needed by the ACO from the GUIWorld Waypoints.
     * @return HashMap<String, HashMap<String, Object>> with the information.
     */
    public HashMap<String, GeoPosition> getACOWaypointsLocations(){
        HashMap<String, GeoPosition> info = new HashMap<>();
        GUIWorld world = this.mainMap.getGUIWorld();
        for(CDCWaypoint cdcWaypoint:world.getGUICDCs()){
            info.put(cdcWaypoint.getID(), cdcWaypoint.getPosition());
        }
        for(Map.Entry<String, TruckWaypoint> truckEntry:world.getGUITrucksByID().entrySet()){
            TruckWaypoint truck = truckEntry.getValue();
            info.put(truckEntry.getKey(), truck.getPosition());
        }
        for(ToVisitWaypoint toVisitWaypoint:world.getToVisitWaypoints()){
            info.put(toVisitWaypoint.getID(),toVisitWaypoint.getPosition());
        }
        return info;
    }

    /**
     * Function that retrieves the information needed by the ACO from the CDC Waypoints.
     * @return HashMap<String, HashMap<String, Object>> with the information.
     */
    public HashMap<String, HashMap<String, Object>> getCDCWaypointsInfo(){
        HashMap<String, HashMap<String, Object>> info = new HashMap<>();
        GUIWorld world = this.mainMap.getGUIWorld();
        for(CDCWaypoint cdcWaypoint:world.getGUICDCs()){
            info.put(cdcWaypoint.getID(), new HashMap<>(){{put("position",cdcWaypoint.getPosition());}});
        }
        return info;
    }

    /**
     * Function that retrieves the information needed by the ACO from the Truck Waypoints.
     * @return HashMap<String, HashMap<String, Object>> with the information.
     */
    public HashMap<String, HashMap<String, Object>> getTruckWaypointsInfo(){
        HashMap<String, HashMap<String, Object>> info = new HashMap<>();
        GUIWorld world = this.mainMap.getGUIWorld();
        for(Map.Entry<String, TruckWaypoint> truckEntry:world.getGUITrucksByID().entrySet()){
            TruckWaypoint truck = truckEntry.getValue();
            info.put(truckEntry.getKey(), new HashMap<>(){{
                put("position",truck.getPosition());
                put("capacity",truck.getCapacity());
                put("currentLoad",truck.getCurrentLoad());
                put("fixedCost",truck.getFixedCost());
                put("variableCost",truck.getVariableCost());
            }});
        }
        return info;
    }

    /**
     * Function that retrieves the information needed by the ACO from the ToVisit Waypoints.
     * @return HashMap<String, HashMap<String, Object>> with the information.
     */
    public HashMap<String, HashMap<String, Object>> getToVisitWaypointsInfo(){
        HashMap<String, HashMap<String, Object>> info = new HashMap<>();
        GUIWorld world = this.mainMap.getGUIWorld();
        for(ToVisitWaypoint toVisitWaypoint:world.getToVisitWaypoints()){
            info.put(toVisitWaypoint.getID(), new HashMap<>(){{
                put("position",toVisitWaypoint.getPosition());
                put("quantity",toVisitWaypoint.getQuantity());
            }});
        }
        return info;
    }

    /**
     * Function that sets the toDeliver quantity of a truck to a value.
     * @param truckID String with the truckID.
     * @param toDeliverQuantity Double with the value to deliver.
     */
    public void changeToDeliverQuantity(String truckID, Double toDeliverQuantity) {
        this.mainMap.getGUIWorld().getGUITrucksByID().get(truckID).setToDeliverQuantity(toDeliverQuantity);
    }

    /**
     * Function that sets the Delivered quantity of a truck to a value.
     * @param truckID String with the truckID.
     * @param deliveredQuantity Double with the value delivered.
     */
    public void changeDeliveredQuantity(String truckID, Double deliveredQuantity) {
        this.mainMap.getGUIWorld().getGUITrucksByID().get(truckID).setDeliveredQuantity(deliveredQuantity);
    }

    /**
     * Function that sets the toVisit waypoint number of a truck to a value.
     * @param truckID String with the truckID.
     * @param toVisitNumber Double with the value to visit.
     */
    public void changeToVisitWaypointNumber(String truckID, Double toVisitNumber) {
        this.mainMap.getGUIWorld().getGUITrucksByID().get(truckID).setToVisitWaypointNumber(toVisitNumber);
    }

    /**
     * Function that sets the visited waypoint number of a truck to a value.
     * @param truckID String with the truckID.
     * @param value Double with the value to set.
     */
    public void changeVisitedWaypointNumber(String truckID, Double value) {
        this.mainMap.getGUIWorld().getGUITrucksByID().get(truckID).setVisitedWaypointNumber(value);
    }

    /**
     * Function that sets the toLoad number of a truck to a value.
     * @param truckID String with the truckID.
     * @param loadsNumber Double with the number of pending loads.
     */
    public void changeToLoadNumber(String truckID, Double loadsNumber) {
        this.mainMap.getGUIWorld().getGUITrucksByID().get(truckID).setToLoadNumber(loadsNumber);

    }

    /**
     * Function that sets the values of specific stats to a specific truckID.
     * @param truckID String with the truck ID that the stat applies.
     * @param statValuesByID HashMap<String, Object> with the values by stat name.
     */
    public void setStats(String truckID, HashMap<String, Object> statValuesByID){
        this.statsPanel.setStats(truckID,statValuesByID);
    }

    /**
     * Function that recomputes the stats of a truck.
     * @param truckID String with the truck ID to recompute.
     */
    public void reComputeStats(String truckID){
        TruckWaypoint truck = this.mainMap.getGUIWorld().getGUITrucksByID().get(truckID);
        GUIRoute route = this.mainMap.getGUIWorld().getGUIRoutesByTruckID().get(truckID);
        double distance = (route.getVisitedDistance() + route.getToVisitDistance())/1000.;
        double duration = route.getVisitedDuration() + route.getToVisitDuration();
        double cost = distance*truck.getVariableCost() + (distance>0.?truck.getFixedCost():0.);
        double deliveredQuantity = truck.getDeliveredQuantity() + truck.getToDeliverQuantity();
        double nodesServed = truck.getVisitedWaypointNumber() + truck.getToVisitWaypointNumber();
        double timesLoaded = truck.getLoadedNumber() + truck.getToLoadNumber();
        this.statsPanel.setStats(truckID,
                new HashMap<>(){{
                    put("Distance Km",String.format("%.2f", distance) );
                    put("Duration", String.format("%d h:%d m:%d s", (int)duration / 3600, ((int)duration % 3600) / 60, (int)duration % 60));
                    put("Cost $", String.format("%.2f",cost));
                    put("Quantity Delivered", deliveredQuantity);
                    put("Nodes Served", nodesServed);
                    put("Times Load", timesLoaded);
                }});
    }

    /**
     * Function that recomputes all stats.
     */
    public void reComputeAllStats() {
        for(TruckWaypoint truck:this.mainMap.getGUIWorld().getGUITrucksByID().values()){
            this.reComputeStats(truck.getID());
        }
    }

    /**
     * Function that gets the weights by ID.
     * @return HashMap<String,Double> with the weights by ID.
     */
    public HashMap<String,Double> getWeights() {
        double dist = Double.parseDouble((String) this.weightsPanel.getWeights().getValueAt(0,0));
        double time = Double.parseDouble((String) this.weightsPanel.getWeights().getValueAt(0,1));
        double cost = Double.parseDouble((String) this.weightsPanel.getWeights().getValueAt(0,2));
        return new HashMap<>(){{
            put("distance",dist);
            put("duration",time);
            put("cost",cost);
        }};
    }

    /**
     * Function that refreshes the map
     */
    public void refreshMap() {
        this.mainMap.repaint();
    }
}
