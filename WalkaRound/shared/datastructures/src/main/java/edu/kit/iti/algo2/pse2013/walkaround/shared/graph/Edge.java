package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import java.util.ArrayList;
import java.util.List;

import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an egde contained in a graph. An edge has two coordinates,
 * each represented by longitude and latitude values.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public final class Edge implements Geometrizable {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(Logger.class);


    /**
     * Internally used ID of Edge.
     */
    private int id;


    /**
     * Tail of this Edge.
     */
    private final Vertex tail;


    /**
     * Head of this Edge.
     */
    private final Vertex head;


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
        if (tail == null || head == null)
            throw new IllegalArgumentException("tail and head must not be null");
        this.tail = tail;
        this.head = head;
        id = idCounter;
        idCounter += 1;
        length = computeLength();
        //System.out.println(String.format("Creates edge %d->%d", tail.getID(), head.getID()));
    }

    public Edge(Vertex tail, Vertex head, int id) {
    	this(tail, head);
    	this.id = id;
    }

    /**
     * Duplicate an Edge. Doesn't duplicate the Vertices contained
     * in the Edge.
     *
     * @param edge The Edge to be duplicated.
     */
    public Edge(Edge edge) {
        this(edge.getTail(), edge.getHead(), edge.getID());
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
     * Returns the other end of the Egde than the given Vertex.
     *
     * @return Vertex.
     */
    public Vertex getOtherVertex(Vertex vertex) {
        if (tail == vertex)
            return head;
        return tail;
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
     * Returns weighted distance between tail and head
     * based on the set of Categories. It returns a value
     * between 0 and the length.
     *
     * @return double.
     */
    /*public double getWeightedLength(int[] categories) {
        return length * 1.0;
    } */


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


    public String toString() {
    	return "Edge(\n\t" + getID() + ",\n\t" + getTail() + ",\n\t" + getHead() + "\n)";
    }

	@Override
	public int compareTo(Object o) {
        if (this.getID() > ((Edge)o).getID()) {
            return 1;
        } else if (this.getID() < ((Edge)o).getID()) {
            return -1;
        }
        return 0;
	}


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable#valueForDimension()
     */
    public int numberDimensions() {
        return 2;
    }


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable#valueForDimension(int)
     */
    public double valueForDimension(int dim) {
        throw new RuntimeException("not supported");
    }


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable#numberNodes()
     */
    public int numberNodes() {
        return 2;
    }


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable#getNode(int nodeNumber)
     */
    public Geometrizable getNode(int nodeNumber) {

        if (nodeNumber < 0 || (nodeNumber > numberNodes()-1))
            throw new IllegalArgumentException("node number out of range");

        if (nodeNumber == 0)
            return tail;
        else
            return head;
    }

}