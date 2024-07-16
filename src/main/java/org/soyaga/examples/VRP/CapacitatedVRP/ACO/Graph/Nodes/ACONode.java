package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes;

import lombok.Getter;
import org.jxmapviewer.viewer.GeoPosition;
import org.soyaga.aco.world.Graph.Elements.Edge;
import org.soyaga.aco.world.Graph.Elements.Node;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Class that represents a Graph/Pheromone container Node with GeoPosition information.
 */
public abstract class ACONode extends Node {
    // GeoPosition position of the node.
    @Getter
    private GeoPosition geoPosition;

    /**
     * Constructor.
     *
     * @param ID Object with the node ID.
     * @param geoPosition GeoPosition of the ACONode
     */
    public ACONode(Object ID, GeoPosition geoPosition) {
        super(ID);
        setPointLocation(geoPosition);
    }

    /**
     * Constructor.
     *
     * @param ID          Object with the node ID.
     * @param outputEdges HashSet{@literal <Edge>} with the output edges.
     * @param geoPosition GeoPosition of the ACONode
     */
    public ACONode(Object ID, HashSet<Edge> outputEdges, GeoPosition geoPosition) {
        super(ID, outputEdges);
        setPointLocation(geoPosition);
    }

    /**
     * Function that sets a GeoPosition.
     * @param geoPosition GeoPosition to be set.
     */
    public void setPointLocation(GeoPosition geoPosition){
        this.geoPosition = geoPosition;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     *
     * <p>
     * An equivalence relation partitions the elements it operates on
     * into <i>equivalence classes</i>; all the members of an
     * equivalence class are equal to each other. Members of an
     * equivalence class are substitutable for each other, at least
     * for some purposes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @implSpec The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * In other words, under the reference equality equivalence
     * relation, each equivalence class only has a single element.
     * @apiNote It is generally necessary to override the {@link #hashCode() hashCode}
     * method whenever this method is overridden, to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof ACONode) {
            return this.getID().equals(((ACONode) obj).getID());
        }
        return false;
    }
}
