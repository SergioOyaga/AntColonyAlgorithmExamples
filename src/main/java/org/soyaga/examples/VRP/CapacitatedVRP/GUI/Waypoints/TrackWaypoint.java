package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints;

import org.jxmapviewer.viewer.GeoPosition;

import java.awt.image.BufferedImage;

/**
 * Class that represents a waypoint in a track. Used to draw the truck route.
 */
public class TrackWaypoint extends GUIWaypoint {
    /**
     * Constructor
     *
     * @param ID    String with the ID.
     * @param coord with the geo coordinate
     */
    public TrackWaypoint(String ID, GeoPosition coord) {
        super(ID, coord);
    }

    /**
     * Function that returns null, the track has no image associated.
     *
     * @return BufferedImage
     */
    @Override
    public BufferedImage getImage() {
        return null;
    }
}
