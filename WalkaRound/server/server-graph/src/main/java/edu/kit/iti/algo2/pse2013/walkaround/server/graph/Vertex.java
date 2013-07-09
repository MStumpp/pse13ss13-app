package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;


/**
 * This class represents an vertex contained in a graph.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Vertex extends Coordinate {

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
     * Parent Vertex.
     */
    private Vertex parent;


    /**
     * Current distance to source.
     */
    private double currentLength;


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
        id = idCounter;
        idCounter += 1;
        outgoingEdges = new ArrayList<Edge>();
        parent = null;
        currentLength = Double.POSITIVE_INFINITY;
        run = 0;
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

        Vertex vertex = (Vertex) o;

        if (id != vertex.id) return false;
        if (parent != null ? !parent.equals(vertex.parent) : vertex.parent != null) return false;

        return true;
    }


    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

}
