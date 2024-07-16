package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Panels;

import lombok.Getter;
import org.jxmapviewer.viewer.*;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.*;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.GUIWorld;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Listeners.JXMouseListener;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Painters.WorldPainter;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Custom JXMap object that allows the problem to dynamically interact with the user.
 */
public class JXMapPanel extends JPanel {
    /**
     * JXMapViewer (JPanel) with the map view.
     */
    private final JXMapViewer mainMap;
    /**
     * GUIWorld object with the routes and waypoints to paint in the map.
     */
    @Getter
    private final GUIWorld GUIWorld;

    /**
     * Constructor.
     * @param GUIWorld GUIWorld object with the routes to paint in the map
     * @param toVisit ToVisitWaypoint image to use in the waypoint creation.
     */
    public JXMapPanel(GUIWorld GUIWorld, BufferedImage toVisit){
        //Class Objects
        this.GUIWorld = GUIWorld;
        this.mainMap = new JXMapViewer();

        // Layout formatting.
        initComponents();

        // Create Map listeners
        createListeners(toVisit);

        // Create Map painters.
        createPainters();

        //Initialize Map Tile
        initializeMainMap();
    }

    /**
     * Function that initializes the visual components parameters.
     */
    private void initComponents(){
        this.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        this.add(this.mainMap, gridBagConstraints);

    }

    /**
     * Function that creates the listeners that handle events in the map.
     * @param toVisit BufferedImage used to create new ToVisitWaypoints.
     */
    private void createListeners(BufferedImage toVisit) {
        JXMouseListener listener = new JXMouseListener(this.mainMap, this.GUIWorld, toVisit);
        this.mainMap.addMouseMotionListener(listener);
        this.mainMap.addMouseListener(listener);
        this.mainMap.addMouseWheelListener(listener);
    }

    /**
     * Function that creates and adds the map painters.
     */
    private void createPainters() {
        WorldPainter worldPainter = new WorldPainter(this.GUIWorld);
        List<Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(worldPainter);
        this.mainMap.setOverlayPainter(new CompoundPainter<>(painters));
    }

    /**
     * Function that sets the maps tileFactory object.
     */
    private void initializeMainMap() {
        DefaultTileFactory tileFactory = new DefaultTileFactory(new OSMTileFactoryInfo()); // Create a TileFactoryInfo for OpenStreetMap
        this.mainMap.setTileFactory(tileFactory);
        this.mainMap.setRestrictOutsidePanning(true);
    }

    /**
     * Function that fits the zoom level in the map.
     * @param fraction Double with the fraction to apply to the zoom.
     */
    public void fitZoom(Double fraction) {
        Set<GeoPosition> allWaypoints= new HashSet<>();
        for(CDCWaypoint waypoint : GUIWorld.getGUICDCs()){
            allWaypoints.add(waypoint.getMapPosition());
        }
        for(VisitedWaypoint waypoint : GUIWorld.getVisitedWaypoints()){
            allWaypoints.add(waypoint.getMapPosition());
        }
        for(ToVisitWaypoint waypoint : GUIWorld.getToVisitWaypoints()){
            allWaypoints.add(waypoint.getMapPosition());
        }
        for(TruckWaypoint waypoint : GUIWorld.getGUITrucksByID().values()){
            allWaypoints.add(waypoint.getMapPosition());
        }
        this.mainMap.zoomToBestFit(allWaypoints,fraction);
    }
}