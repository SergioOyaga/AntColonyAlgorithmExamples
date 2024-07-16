package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Renderers;

import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.*;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.GUIRoute;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.GUIWorld;
import org.jxmapviewer.JXMapViewer;

import java.awt.*;
import java.util.Map;

/**
 * Class that renders a GUIWorld object into a map.
 */
public class CustomWorldRenderer {
    /**
     * CustomWaypointRenderer that renders a GUIWaypoint.
     */
    private final CustomWaypointRenderer waypointRenderer;
    /**
     * CustomRouteRenderer that renders a route.
     */
    private final CustomRouteRenderer routeRenderer;

    /**
     * Constructor.
     */
    public CustomWorldRenderer() {
        this.waypointRenderer = new CustomWaypointRenderer();
        this.routeRenderer = new CustomRouteRenderer();
    }

    /**
     * Function that paints a GUIWorld object.
     * @param g Graphics2D context where to paint the routes and waypoints.
     * @param map JXMapViewer reference.
     * @param GUIWorld GUIWorld object with the routes and the waypoints.
     */
    public void paintWorld(Graphics2D g, JXMapViewer map, GUIWorld GUIWorld) {
        for(Map.Entry<String, GUIRoute> entry : GUIWorld.getGUIRoutesByTruckID().entrySet()){
            this.routeRenderer.paintRoute(g,map, entry.getValue(), GUIWorld.getGUITrucksByID().get(entry.getKey()).getRouteColor());
        }
        for(CDCWaypoint waypoint : GUIWorld.getGUICDCs()){
            this.waypointRenderer.paintWaypoint(g,map,waypoint);
        }
        for(VisitedWaypoint waypoint : GUIWorld.getVisitedWaypoints()){
            this.waypointRenderer.paintWaypoint(g,map,waypoint);
        }
        for(ToVisitWaypoint waypoint : GUIWorld.getToVisitWaypoints()){
            this.waypointRenderer.paintWaypoint(g,map,waypoint);
        }
        for(TruckWaypoint waypoint : GUIWorld.getGUITrucksByID().values()){
            this.waypointRenderer.paintWaypoint(g,map,waypoint);
        }
    }
}
