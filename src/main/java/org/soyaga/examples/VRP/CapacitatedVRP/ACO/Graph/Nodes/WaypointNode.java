package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes;

import lombok.Getter;
import lombok.Setter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.util.MercatorUtils;

import java.awt.geom.Point2D;

/**
 * Class that represents a Waypoint Node. Nodes that the ants have to visit to build a solution.
 */
@Getter
public class WaypointNode extends ACONode {
    /**
     * GeoPosition projected to a map using mercator approach.
     */
    @Getter
    private Point2D.Double point;
    /**
     * Double with the quantity that the node is requesting.
     */
    @Setter
    private Double requestedQuantity;
    /**
     * Double with the quantity that the node is requesting.
     */
    @Setter
    private Double pendingQuantity;
    /**
     * Constructor.
     *
     * @param ID          Object with the node ID.
     * @param geoPosition GeoPosition of the ACONode
     */
    public WaypointNode(Object ID, GeoPosition geoPosition, Double requestedQuantity) {
        super(ID, geoPosition);
        this.requestedQuantity = requestedQuantity;
        this.pendingQuantity = this.requestedQuantity;
        this.point = new Point2D.Double(MercatorUtils.longToX(geoPosition.getLongitude(),6378137.0),
                MercatorUtils.latToY(geoPosition.getLatitude(),6378137.0));
    }

    /**
     * Function that sets a GeoPosition to this Node and transform it to X, y coordinates.
     * @param geoPosition GeoPosition to be set.
     */
    @Override
    public void setPointLocation(GeoPosition geoPosition){
        super.setPointLocation(geoPosition);
        this.point = new Point2D.Double(MercatorUtils.longToX(geoPosition.getLongitude(),6378137.0),
                MercatorUtils.latToY(geoPosition.getLatitude(),6378137.0));
    }
}
