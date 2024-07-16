package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Renderers;

import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.CDCWaypoint;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.GUIWaypoint;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypointRenderer;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.TrackWaypoint;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.TruckWaypoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Class that renders waypoint in the map by getting the image directly from the waypoint.
 */
public class CustomWaypointRenderer{
    /**
     * Default image that is used in the case the waypoint doesn't have an image.
     */
    private BufferedImage defaultImage;

    /**
     * Builder Custom waypoint renderer that gets the image from the waypoint.
     * Each waypoint can have its own image.
     */
    public CustomWaypointRenderer() {
        try {
            defaultImage = ImageIO.read(Objects.requireNonNull(DefaultWaypointRenderer.class.getResource("/images/standard_waypoint.png")));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * paint the specified waypoint on the specified map and graphics context
     *
     * @param g        the graphics2D object
     * @param map      the map
     * @param waypoint the waypoint
     */
    public void paintWaypoint(Graphics2D g, JXMapViewer map, GUIWaypoint waypoint) {
        if(waypoint instanceof TrackWaypoint) return;
        //Try to get image from the waypoint.
        BufferedImage img = this.defaultImage;
        if (waypoint.getImage()!= null) {
            img = waypoint.getImage();
        }
        Point2D point = map.getTileFactory().geoToPixel(waypoint.getMapPosition(), map.getZoom());
        int x = (int)point.getX() -img.getWidth() / 2;
        int y = (int)point.getY() -img.getHeight();
        if((waypoint instanceof CDCWaypoint) | (waypoint instanceof TruckWaypoint)){
            y+=img.getHeight()/2;
        }

        g.drawImage(img, x, y, null);
    }
}
