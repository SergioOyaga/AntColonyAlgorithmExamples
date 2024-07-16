package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Renderers;

import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.TrackWaypoint;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.GUIRoute;
import org.jxmapviewer.JXMapViewer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Class that renders a route object.
 */
public class CustomRouteRenderer {
    /**
     * Function that paints a GUIRoute.
     * @param g Graphics2D context where to draw the GUIRoute.
     * @param map JXMapViewer with the map reference.
     * @param GUIRoute GUIRoute with the waypoints.
     * @param color Color for the GUIRoute.
     */
    public void paintRoute(Graphics2D g, JXMapViewer map, GUIRoute GUIRoute, Color color) {
        // do the drawing
        g.setColor(new Color(0,0,0, 200));
        g.setStroke(new BasicStroke(6, // Width
                BasicStroke.CAP_ROUND, // End-cap style
                BasicStroke.JOIN_MITER, // Join style
                10.0f));
        drawRoute(g, map, GUIRoute.getVisitedTrack());

        // do the drawing
        g.setColor(new Color(190,190,190,200));
        g.setStroke(new BasicStroke(4, // Width
                BasicStroke.CAP_ROUND, // End-cap style
                BasicStroke.JOIN_MITER, // Join style
                10.0f));
        drawRoute(g, map, GUIRoute.getVisitedTrack());

        // do the drawing
        g.setColor(new Color(0,0,0, 200));
        g.setStroke(new BasicStroke(6, // Width
                BasicStroke.CAP_ROUND, // End-cap style
                BasicStroke.JOIN_MITER, // Join style
                10.0f));
        drawRoute(g, map, GUIRoute.getToVisitTrack());

        // do the drawing
        g.setColor(color);
        g.setStroke(new BasicStroke(4, // Width
                BasicStroke.CAP_ROUND, // End-cap style
                BasicStroke.JOIN_MITER, // Join style
                10.0f));
        drawRoute(g, map, GUIRoute.getToVisitTrack());
    }

    /**
     * Function that actually draws the GUIRoute.
     * @param g the graphics object
     * @param map the map
     * @param track ArrayList<TrackWaypoint> to paint.
     */
    private void drawRoute(Graphics2D g, JXMapViewer map, ArrayList<TrackWaypoint> track) {
        int lastX = 0;
        int lastY = 0;
        boolean first = true;

        for (TrackWaypoint waypoint : track) {
            Point2D pt = map.getTileFactory().geoToPixel(waypoint.getMapPosition(), map.getZoom());
            if (first) {
                first = false;
            }
            else {
                g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
            }
            lastX = (int) pt.getX();
            lastY = (int) pt.getY();
        }
    }
}
