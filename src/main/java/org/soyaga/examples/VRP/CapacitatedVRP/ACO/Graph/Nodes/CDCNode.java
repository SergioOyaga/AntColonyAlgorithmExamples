package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes;

import lombok.Getter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.util.MercatorUtils;
import org.soyaga.aco.world.Graph.Elements.Edge;

import java.awt.geom.Point2D;
import java.util.HashSet;

/**
 * Class that represents a CDC Node. Node that has to be visited by the Ant to finish a route and if all Waypoints have
 * been visited, then the Solution is achieved.
 */
public class CDCNode extends ACONode {
    /**
     * GeoPosition projected to a map using mercator approach.
     */
    @Getter
    private Point2D.Double point;
    /**
     * Constructor.
     *
     * @param ID          Object with the node ID.
     * @param geoPosition GeoPosition of the ACONode
     */
    public CDCNode(Object ID, GeoPosition geoPosition) {
        super(ID, geoPosition);
        this.point = new Point2D.Double(MercatorUtils.longToX(geoPosition.getLongitude(),6378137.0),
                MercatorUtils.latToY(geoPosition.getLatitude(),6378137.0));
    }

    /**
     * Constructor.
     *
     * @param ID          Object with the node ID.
     * @param outputEdges HashSet{@literal <Edge>} with the output edges.
     * @param geoPosition GeoPosition of the ACONode
     */
    public CDCNode(Object ID, HashSet<Edge> outputEdges, GeoPosition geoPosition) {
        super(ID, outputEdges, geoPosition);
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
