package org.soyaga.examples.VRP.CapacitatedVRP.GUI;

import lombok.Getter;
import lombok.Setter;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.TrackWaypoint;

import java.util.ArrayList;

/**
 * Class that represents a Route in the GUI.
 */
@Getter
public class GUIRoute {
    /**
     * ArrayList<TrackWaypoint> with the "visited" track to print in the map for this route.
     */
    private final ArrayList<TrackWaypoint> visitedTrack;
    /**
     * ArrayList<TrackWaypoint> with the "to visit track" to print in the map for this route.
     */
    private final ArrayList<TrackWaypoint> toVisitTrack;
    /**
     * Double with the visitedTrack distance
     */
    @Setter
    private double visitedDistance;
    /**
     * Double with the visitedTrack duration
     */
    @Setter
    private double visitedDuration;
    /**
     * Double with the toVisitTrack distance
     */
    @Setter
    private double toVisitDistance;
    /**
     * Double with the toVisitTrack duration
     */
    @Setter
    private double toVisitDuration;


    /**
     * Constructor.
     */
    public GUIRoute() {
        this.visitedTrack = new ArrayList<>();
        this.toVisitTrack = new ArrayList<>();
        this.visitedDistance = 0.;
        this.visitedDuration = 0.;
        this.toVisitDistance = 0.;
        this.toVisitDuration = 0.;
    }

    /**
     * Function that appends a TrackWaypoint to the end of the current track
     * @param trackWaypoint TrackWaypoint to append.
     */
    public void appendTrackWaypoint(TrackWaypoint trackWaypoint){
        this.toVisitTrack.add(trackWaypoint);
    }

    /**
     * Function that appends a TrackWaypoint to the end of the visited track
     * @param trackWaypoint TrackWaypoint to append.
     */
    public void appendVisitedTrackWaypoint(TrackWaypoint trackWaypoint){
        this.visitedTrack.add(trackWaypoint);
    }

    /**
     * Function that clears the toVisitTrack variable.
     */
    public void clearToVisitTrack(){
        this.toVisitTrack.clear();
    }

    /**
     * Function that clears the visitedTrack variable.
     */
    public void clearVisitedTrack(){
        this.visitedTrack.clear();
    }
}
