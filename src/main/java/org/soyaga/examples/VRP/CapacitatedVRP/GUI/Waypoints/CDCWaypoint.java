package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints;

import org.jxmapviewer.viewer.GeoPosition;

import java.awt.image.BufferedImage;

/**
 * Class that represents a CDC Waypoint
 */
public class CDCWaypoint  extends GUIWaypoint {
    /**
     * Constructor.
     * @param ID String with the waypoint ID.
     * @param coord the geo coordinate
     */
    public CDCWaypoint(String ID, GeoPosition coord, BufferedImage img) {
        super(ID,coord);
        this.image = img;
    }
}
