package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents an vertex contained in a graph.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Vertex extends Coordinate implements Comparable<Vertex> {

    /**
     * Internally used ID of that Vertex.
     */
    private int id;


    /**
     * ID counter for internal id.
     */
    private static int idCounter = 0;


    /**
     * List of outgoing Edges.
     */
    private List<Edge> outgoingEdges;


    /**
     * Parent Vertex.
     */
    private Vertex parent;


    /**
     * Current distance to source.
     */
    private double currentLength;


    /**
     * Current weighted distance to source.
     */
    private double currentWeightedLength;


    /**
     * Stores the current run.
     */
    private int run;


    /**
     * Creates an instance of Vertex.
     *
     * @param lat Latitude of Vertex
     * @param lon Longitude of Vertex.
     */
    public Vertex(double lat, double lon) {
        super(lat, lon);
        id = idCounter;
        idCounter += 1;
        outgoingEdges = new ArrayList<Edge>();
        parent = null;
        currentLength = Double.POSITIVE_INFINITY;
        run = 0;
    }


    public Vertex(double latitude, double longitude, int id) {
		this(latitude, longitude);
		this.id = id;
	}


    /**
     * Duplicate a Vertex.
     *
     * @param vertex The Vertex to be duplicated.
     */
    public Vertex(Vertex vertex) {
        this(vertex.getLatitude(), vertex.getLongitude(), vertex.getID());
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
     * Returns the Edge having the given
     * Vertex and this as endpoints.
     *
     * @return Edge.
     */
    public Edge getOutgoingEdge(Vertex vertex) {
        if (vertex == null || this.equals(vertex))
            return null;

        for (Edge edge : outgoingEdges) {
            if (edge.getHead().equals(vertex) ||
                edge.getTail().equals(vertex))
                return edge;
        }
        return null;
    }


    /**
     * Adds an outgoing Edge to the list of outgoing Edges.
     */
    public void addOutgoingEdge(Edge edge) {
        outgoingEdges.add(edge);
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
     *
     * @param currentLength The current length from source.
     */
    public void setCurrentLength(double currentLength) {
        this.currentLength = currentLength;
    }


    /**
     * Returns the current weighted length.
     *
     * @return currentWeightedLength.
     */
    public double getCurrentWeightedLength() {
        return currentWeightedLength;
    }


    /**
     * Sets the current weigthed length.
     *
     * @param currentWeightedLength The current length from source.
     */
    public void setCurrentWeightedLength(double currentWeightedLength) {
        this.currentWeightedLength = currentWeightedLength;
    }


    /**
     * Returns the current run.
     *
     * @return int.
     */
    public int getRun() {
        return run;
    }


    /**
     * Sets the current run.
     *
     * @param run Set the current run.
     */
    public void setRun(int run) {
        this.run = run;
    }


    @Override
    public String toString() {
        return "Vertex - id: " + id + " - lat: " + getLatitude() + " - lon: " + getLongitude() + " - current length: " + getCurrentLength();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Vertex vertex = (Vertex) o;

        if (id != vertex.id) return false;

        return true;
    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        return result;
    }


    @Override
    public int compareTo(Vertex other) {
        if (this.getID() > other.getID()) {
            return 1;
        } else if (this.getID() < other.getID()) {
            return -1;
        }
        return 0;
    }

}
