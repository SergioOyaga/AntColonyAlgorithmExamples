package org.soyaga.examples.VRP.CapacitatedVRP.GUI;

import lombok.Getter;
import org.jxmapviewer.beans.AbstractBean;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.*;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Class that represents a GUIWorld object.
 */
@Getter
public class GUIWorld extends AbstractBean {
    /**
     * Hashmap<TruckWaypoint, GUIRoute> with the GUIRoutes in the world by truck.
     */
    private final HashMap<String, GUIRoute> GUIRoutesByTruckID;
    /**
     * Hashmap<TruckWaypoint> with the TruckWaypoint in the world.
     */
    private final HashMap<String, TruckWaypoint> GUITrucksByID;
    /**
     * HashSet<CDCWaypoint> with the CDCs in the world.
     */
    private final HashSet<CDCWaypoint> GUICDCs;
    /**
     * ArrayList<VisitedWaypoint> with the visited waypoints in the world.
     */
    private final HashSet<VisitedWaypoint> visitedWaypoints;
    /**
     * ArrayList<ToVisitWaypoint> with the waypoints to visit in the world.
     */
    private final HashSet<ToVisitWaypoint> toVisitWaypoints;

    /**
     * Constructor.
     */
    public GUIWorld() {
        this.GUIRoutesByTruckID = new HashMap<>();
        this.GUITrucksByID = new HashMap<>();
        this.GUICDCs = new HashSet<>();
        this.visitedWaypoints = new HashSet<>();
        this.toVisitWaypoints = new HashSet<>();
    }

    /**
     * Function that adds a TruckWaypoint to the truck set.
     * @param waypoint TruckWaypoint to be appended.
     */
    public void addGUITruck(TruckWaypoint waypoint){
        this.GUITrucksByID.put(waypoint.getID(), waypoint);
    }

    /**
     * Function that adds a waypoint to the CDC set.
     * @param waypoint CDCWaypoint to be appended.
     */
    public void addGUICDC(CDCWaypoint waypoint){
        this.GUICDCs.add(waypoint);
    }

    /**
     * Function that adds a visited waypoint to the visitedWaypoints set.
     * @param waypoint VisitedWaypoint to be appended.
     */
    public void addGUIVisitedWaypoint(VisitedWaypoint waypoint){
        this.visitedWaypoints.add(waypoint);
    }

    /**
     * Function that adds a waypoint to visit to the toVisitWaypoints set.
     * @param waypoint ToVisitWaypoint to be appended.
     */
    public void addGUIToVisitWaypoint(ToVisitWaypoint waypoint){
        this.toVisitWaypoints.add(waypoint);
    }

    /**
     * Function that adds a waypoint to visit to the toVisitWaypoints set, and fires an event.
     * @param waypoint ToVisitWaypoint to be appended.
     */
    public void addGUIToVisitWaypointAndNotify(ToVisitWaypoint waypoint){
        this.toVisitWaypoints.add(waypoint);
        this.firePropertyChange("createWaypoint", null, waypoint);
    }

    /**
     * Function that removes a ToVisitWaypoint from the toVisitWaypoints set and fires event.
     * @param waypoint ToVisitWaypoint to be removed.
     */
    public void removeGUIWaypointAndNotify(ToVisitWaypoint waypoint){
        this.toVisitWaypoints.remove(waypoint);
        this.firePropertyChange("removeWaypoint", waypoint, null);
    }
}
