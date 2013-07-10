package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an egde contained in a graph. An edge has two coordinates,
 * each represented by longitude and latitude values.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public final class Edge implements Geometrizable, Serializable {

    /**
     * Internally used ID of Edge.
     */
    private int id;


    @Override
	public String toString() {
		return "Edge [id=" + id + ", "
				+ (tail != null ? "tail=" + tail + ", " : "")
				+ (head != null ? "head=" + head + ", " : "") + "osmID="
				+ osmID + ", length=" + length + "]";
	}


	/**
     * Tail of this Edge.
     */
    private final Vertex tail;


    /**
     * Head of this Edge.
     */
    private final Vertex head;


    /**
     * OSM ID of that Edge.
     */
    private transient long osmID;


    /**
     * ID counter for internal id.
     */
    private static int idCounter = 0;


    /**
     * Distance betwen tail and head in meters.
     */
    private transient double length;


    /**
     * Creates an instance of Edge.
     *
     * @param tail Tail of Edge.
     * @param head Head of Edge.
     */
    public Edge(Vertex tail, Vertex head) {
        this(tail, head, -1);
    }


    /**
     * Creates an instance of Edge.
     *
     * @param tail Tail of Edge.
     * @param head Head of Edge.
     * @param osmID Corresponding OSM ID of this Edge.
     * @throws IllegalArgumentException If tail or head are null.
     */
    public Edge(Vertex tail, Vertex head, long osmID) {
        if (tail == null || head == null)
            throw new IllegalArgumentException("tail and head must not be null");
        this.tail = tail;
        this.head = head;
        this.osmID = osmID;
        id = idCounter;
        idCounter += 1;
        length = computeLength();
    }


    /**
     * Returns id of this Edge.
     *
     * @return int.
     */
    public int getID() {
        return id;
    }

    /**
     * Returns osm-id of this Edge.
     *
     * @return long.
     */
    public long getOSMID() {
        return osmID;
    }

    /**
     * Returns tail of this Edge.
     *
     * @return Vertex.
     */
    public Vertex getTail() {
        return tail;
    }


    /**
     * Returns head of this Edge.
     *
     * @return Vertex.
     */
    public Vertex getHead() {
        return head;
    }


    /**
     * Returns tail and head as list.
     *
     * @return List<Vertex>.
     */
    public List<Vertex> getVertices() {
        List<Vertex> result = new ArrayList<Vertex>();
        result.add(tail);
        result.add(head);
        return result;
    }


    /**
     * Returns distance between tail and head.
     *
     * @return double.
     */
    public double getLength() {
        return length;
    }


    /**
     * Overwrites the length computed in constructor.
     *
     * @param length Length.
     */
    public void setLength(double length) {
        this.length = length;
    }


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable#valueForDimension()
     */
    public int numberDimensions() {
        throw new RuntimeException("not yet supported");
    }


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable#valueForDimension(int)
     */
    public double valueForDimension(int dim) {
        throw new RuntimeException("not yet supported");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (id != edge.id) return false;

        return true;
    }


    @Override
    public int hashCode() {
        return id;
    }


    /**
     * Returns distance between tail and head in meters using Haversine formula.
     * Source: http://stackoverflow.com/questions/120283/working-with-latitude-longitude-values-in-java
     *
     * FIXME: Dafür haben wir Utilities:
     * {@link edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility#calculateDifferenceInMeters}
     *
     * @return double.
     */
    private double computeLength() {
        double earthRadius = 6371009;
        double dLat = Math.toRadians(head.getLatitude() - tail.getLatitude());
        double dLng = Math.toRadians(head.getLongitude() - tail.getLongitude());
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(tail.getLatitude())) * Math.cos(Math.toRadians(head.getLatitude()));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }

}