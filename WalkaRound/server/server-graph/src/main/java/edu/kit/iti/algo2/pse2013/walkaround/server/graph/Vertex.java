package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * This class represents an vertex contained in a graph.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Vertex extends Coordinate implements Serializable {

	/**
	 * Temporary Serial version ID as long as Java serialization is used
	 */
	private static final long serialVersionUID = -4228194461207025121L;


    /**
     * Internally used ID of that Vertex.
     */
    private int id;


    /**
     * OSM ID of that Vertex.
     */
    private transient long osmID;


    /**
     * ID counter for internal id.
     */
    private static int idCounter = 0;


    /**
     * List of outgoing Edges.
     */
    private List<Edge> outgoingEdges;


    /**
     * Parent Edge.
     */
    private Vertex parent;


    /**
     * Current distance to source.
     */
    private double currentLength;


    /**
     * Creates an instance of Vertex.
     *
     * @param lat Latitude of Vertex
     * @param lon Longitude of Vertex.
     */
    public Vertex(double lat, double lon) {
        this(lat, lon, -1);
    }


    /**
     * Creates an instance of Vertex.
     *
     * @param lat Latitude of Vertex
     * @param lon Longitude of Vertex.
     * @param osmID Corresponding OSM ID of Vertex.
     */
    public Vertex(double lat, double lon, long osmID) {
		super(lat, lon);
		this.osmID = osmID;
        outgoingEdges = new ArrayList<>();
        parent = null;
        currentLength = Double.POSITIVE_INFINITY;
	}


    /**
     * Returns id of this Vertex.
     *
     * @return int.
     */
	public int getID() {
		return id;
	}


    /**
     * Returns the list of outgoing Edges.
     *
     * @return List<Edge>.
     */
	public List<Edge> getOutgoingEdges() {
		return outgoingEdges;
	}


    /**
     * Adds an outgoing Edge to the list of outgoing Edges.
     */
    public void addOutgoingEdge(Edge edge) {
        outgoingEdges.add(edge);
    }


    /**
     * Returns the current id count.
     *
     * @return int.
     */
    public static int getIDCount() {
        return idCounter;
    }


    /**
     * Returns the parent Vertex.
     *
     * @return Vertex.
     */
    public Vertex getParent() {
        return parent;
    }


    /**
     * Sets the parent Vertex.
     */
    public void setParent(Vertex parent) {
        this.parent = parent;
    }


    /**
     * Returns the current length.
     *
     * @return double.
     */
    public double getCurrentLength() {
        return currentLength;
    }


    /**
     * Sets the current length.
     */
    public void setCurrentLength(double currentLength) {
        this.currentLength = currentLength;
    }


    /**
     * Returns a String representation of Vertex.
     */
    @Override
    public String toString() {
        return "Vertex - id: " + id + " - lat: " +
                getLatitude() + " - lon: " + getLongtitude() + " - current length: " +
                getCurrentLength() + " - parent: " + getParent().getID();
    }

}
