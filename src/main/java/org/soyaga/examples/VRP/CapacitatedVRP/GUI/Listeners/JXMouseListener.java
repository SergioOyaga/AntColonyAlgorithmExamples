package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Listeners;

import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.*;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.GUIWorld;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Popups.ToVisitWaypointPopup;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Popups.TruckWaypointPopup;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Popups.VisitedWaypointPopup;
import org.jxmapviewer.JXMapViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Class tha creates a listener that handles all events in the JXMapViewer object of the JXMapPanel object.
 */
public class JXMouseListener implements MouseListener, MouseWheelListener, MouseMotionListener {
    /**
     * Auxiliary Point variable to compute deltas while dragging.
     */
    private Point prev;
    /**
     * Integer with the number of waypoints created.
     */
    private int createdCount;
    /**
     * JXMapViewer map object.
     */
    private final JXMapViewer viewer;
    /**
     * ToVisitWaypointPopup with the popup form for a road waypoint.
     */
    private final ToVisitWaypointPopup toVisitWaypointPopup;
    /**
     * ToVisitWaypointPopup with the popup form for a road waypoint.
     */
    private final VisitedWaypointPopup visitedWaypointPopup;
    /**
     * TruckWaypointPopup with the popup form for a truck.
     */
    private final TruckWaypointPopup truckWaypointPopup;
    /**
     * GUIWorld object with the GUIWorld to paint and interact (bridge with the backend).
     */
    private final GUIWorld GUIWorld;
    /**
     * Buffered toVisit image for the ToVisitWaypoint.
     */
    private final BufferedImage image;
    /**
     * GUIWaypoint with the current selected waypoint.
     */
    private GUIWaypoint currentWaypoint;

    /**
     * Constructor.
     *
     * @param viewer             JXMapViewer map object
     * @param GUIWorld           GUIWorld object with the GUIWorld to plot and interact (bridge with the backend).
     * @param image              Buffered toVisit image for the ToVisitWaypoint.
     */
    public JXMouseListener(JXMapViewer viewer, GUIWorld GUIWorld, BufferedImage image) {
        this.createdCount = 0;
        this.viewer = viewer;
        this.toVisitWaypointPopup = new ToVisitWaypointPopup();
        this.visitedWaypointPopup = new VisitedWaypointPopup();
        this.truckWaypointPopup = new TruckWaypointPopup();
        this.GUIWorld = GUIWorld;
        this.image = image;
    }

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  {@code MOUSE_DRAGGED} events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * {@code MOUSE_DRAGGED} events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param evt the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent evt) {
        if(SwingUtilities.isLeftMouseButton(evt)){
            if(this.currentWaypoint instanceof ToVisitWaypoint){ // Drag Waypoint
                dragWaypoint(evt);
            } else { //Pan view
                doPanning(evt);
            }
        }
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        // Do Nothing.
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        this.currentWaypoint = getWaypoint(e.getPoint()); //Get the clicked waypoint if any.
        if (SwingUtilities.isLeftMouseButton(e)){
            if(this.currentWaypoint!= null){ //Enable a waypoint edition.
                if(this.currentWaypoint instanceof ToVisitWaypoint){ // Change ToVisitWaypoint attributes.
                    this.toVisitWaypointPopup.display((ToVisitWaypoint) this.currentWaypoint);
                }
                else if(this.currentWaypoint instanceof VisitedWaypoint){ // Change ToVisitWaypoint attributes.
                    this.visitedWaypointPopup.display((VisitedWaypoint) this.currentWaypoint);
                }
                else if(this.currentWaypoint instanceof TruckWaypoint){ // Change TruckWaypoint attributes.
                    this.truckWaypointPopup.display((TruckWaypoint) this.currentWaypoint);
                }
            } else {//Add new ToVisitWaypoint.
                ToVisitWaypoint newWaypoint =  new ToVisitWaypoint("Created "+this.createdCount,
                        this.viewer.convertPointToGeoPosition(e.getPoint()),0.,this.image);
                if(this.toVisitWaypointPopup.display(newWaypoint)) {
                    this.GUIWorld.addGUIToVisitWaypointAndNotify(newWaypoint); // Fires node creation event.
                    this.createdCount++;
                }
            }
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            if(this.currentWaypoint!= null){
                if(this.currentWaypoint instanceof ToVisitWaypoint toVisitWaypoint) {
                    this.GUIWorld.removeGUIWaypointAndNotify(toVisitWaypoint); // Fires node removal event.
                }
            }
        }
        this.currentWaypoint =null; //Drop the waypoint
        this.viewer.repaint();
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        this.currentWaypoint = getWaypoint(e.getPoint()); //Get the clicked waypoint if any (only once for the dragged, need to be recomputed in the clicked).
        if (SwingUtilities.isLeftMouseButton(e)){
            // Set panning and dragging aux variables.
            this.prev = e.getPoint();
            this.viewer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(this.currentWaypoint!=null) {
            this.currentWaypoint.setPositionAndNotify(this.currentWaypoint.getMapPosition());
        }
        //Reset panning and dragging aux variables.
        this.prev = null;
        this.currentWaypoint = null; //Release clicked waypoint (only once for dragged, need to be re-dropped by clicked)
        this.viewer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // Do Nothing
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
        // Do Nothing
    }

    /**
     * Invoked when the mouse wheel is rotated.
     *
     * @param e the event to be processed
     * @see MouseWheelEvent
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //Zooming
        Point current = e.getPoint();
        Rectangle bound = this.viewer.getViewportBounds();
        double dx = current.x - bound.width / 2.;
        double dy = current.y - bound.height / 2.;
        Dimension oldMapSize = this.viewer.getTileFactory().getMapSize(this.viewer.getZoom());
        this.viewer.setZoom(this.viewer.getZoom() + e.getWheelRotation());
        Dimension mapSize = this.viewer.getTileFactory().getMapSize(this.viewer.getZoom());
        Point2D center = this.viewer.getCenter();
        double dzw = (mapSize.getWidth() / oldMapSize.getWidth());
        double dzh = (mapSize.getHeight() / oldMapSize.getHeight());
        double x = center.getX() + dx * (dzw - 1);
        double y = center.getY() + dy * (dzh - 1);
        this.viewer.setCenter(new Point2D.Double(x, y));
    }

    /**
     * Function that returns the waypoint that has been selected if any.
     * @param point Point on the screen where the click has happened.
     * @return GUIWaypoint with the waypoint or null.
     */
    private GUIWaypoint getWaypoint(Point point) {
        for(GUIWaypoint waypoint:this.GUIWorld.getGUITrucksByID().values()){
            if (this.pointInWaypoint(point, waypoint)) {
                return waypoint;
            }
        }
        for(GUIWaypoint waypoint:this.GUIWorld.getVisitedWaypoints()){
            if (this.pointInWaypoint(point, waypoint)) {
                return waypoint;
            }
        }
        for(GUIWaypoint waypoint:this.GUIWorld.getToVisitWaypoints()){
            if (this.pointInWaypoint(point, waypoint)) {
                return waypoint;
            }
        }
        for(GUIWaypoint waypoint:this.GUIWorld.getGUICDCs()){
            if (this.pointInWaypoint(point, waypoint)) {
                return waypoint;
            }
        }
        return null;
    }

    /**
     * Function that evaluates if a point is inside a waypoint image
     * @param point Point to evaluate.
     * @param waypoint GUIWaypoint to check.
     * @return boolean true if it is inside the image, false otherwise
     */
    private boolean pointInWaypoint(Point point, GUIWaypoint waypoint){
        // convert to GUIWorld bitmap
        Point2D worldPos = this.viewer.getTileFactory().geoToPixel(waypoint.getMapPosition(), this.viewer.getZoom());

        // convert to screen
        Rectangle rect = this.viewer.getViewportBounds();
        int sx = (int) worldPos.getX() - rect.x;
        int sy = (int) worldPos.getY() - rect.y- waypoint.getImage().getHeight()/2;
        if((waypoint instanceof CDCWaypoint) | (waypoint instanceof TruckWaypoint)){
            sy+=waypoint.getImage().getHeight()/2;
        }
        Point screenPos = new Point(sx, sy);
        return (screenPos.distance(point) < Math.max(waypoint.getImage().getWidth(),waypoint.getImage().getHeight())/2.);

    }

    /**
     * Function that does the panning.
     * @param evt MouseEvent
     */
    private void doPanning(MouseEvent evt){
        if (this.viewer.isPanningEnabled()) {// Check if panning is enabled in the map
            Point current = evt.getPoint(); // Where is the mouse
            double x = this.viewer.getCenter().getX(); //Where is the map center
            double y = this.viewer.getCenter().getY();
            if (this.prev != null) { //Compute new centre using mouse movement delta
                x += this.prev.x - current.x;
                y += this.prev.y - current.y;
            }
            this.prev = current; // Update old cursor position to the new
            this.viewer.setCenter(new Point2D.Double(x, y)); // Set new map centre
            this.viewer.repaint(); // Repaint
        }
    }

    /**
     * Function that drags the waypoint to a new location.
     * @param evt MouseEvent.
     */
    private void dragWaypoint(MouseEvent evt) {
        Point current = evt.getPoint();// Where is the mouse
        Point2D point = this.viewer.convertGeoPositionToPoint(this.currentWaypoint.getMapPosition()); // Where is the waypoint
        double x = point.getX();
        double y = point.getY();
        if (this.prev != null) { //Compute new waypoint position using mouse movement delta.
            x -= this.prev.x - current.x;
            y -= this.prev.y - current.y;
        }
        this.currentWaypoint.setMapPosition(this.viewer.convertPointToGeoPosition(new Point2D.Double(x,y))); // Set waypoint new location
        this.prev = current; // Update old cursor position.
        this.viewer.repaint(); // Repaint
    }
}
