package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints;

import lombok.Getter;
import lombok.Setter;
import org.jxmapviewer.beans.AbstractBean;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import java.awt.image.BufferedImage;

/**
 * Class that allow to draw custom waypoints
 */
public abstract class GUIWaypoint extends AbstractBean implements Waypoint {
    /**
     * String with the unique ID identification of the waypoint.
     */
    @Getter @Setter
    private String ID;
    /**
     * GeoPosition of the waypoint in the map
     */
    @Getter
    private GeoPosition mapPosition;
    /**
     * GeoPosition of the waypoint.
     */
    @Getter
    private GeoPosition position;
    /**
     * Buffered image used for the waypoint.
     */
    @Getter
    BufferedImage image;

    /**
     * Setter
     * @param mapPosition new GeoPosition
     */
    public void setMapPosition(GeoPosition mapPosition) {
        this.mapPosition = mapPosition;
    }

    /**
     * Setter with fire event.
     * @param position new GeoPosition
     */
    public void setPositionAndNotify(GeoPosition position) {
        if(!this.position.equals(position)) {
            this.position = position;
            this.firePropertyChange("position", this.getID(), this.position);
        }
    }

    /**
     * Constructor
     * @param ID String with the ID.
     * @param coord the geo coordinate
     */
    public GUIWaypoint(String ID, GeoPosition coord){
        this.ID=ID;
        this.position = coord;
        this.mapPosition = coord;
    }
}
