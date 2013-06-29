package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import java.util.List;

/**
 * This class represents a graph consisting of edges and vertices.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public final class Graph {

    /**
     * Graph instance.
     */
    private static Graph instance;


    /**
     * Array of Vertices.
     */
    private Vertex[] vertices;


    /**
     * Creates an instance of Graph.
     *
     * @param edges List of Edges to build a Graph.
     */
    private Graph(List<Edge> edges) {
        vertices = new Vertex[5000000];
        int currentID;
        for (Edge edge : edges) {
            currentID = edge.getTail().getID();
            if (vertices.length <= currentID)
                vertices = increaseArray(vertices, 1000000);
            if (vertices[currentID] == null)
                vertices[currentID] = edge.getTail();
            currentID = edge.getHead().getID();
            if (vertices.length <= currentID)
                vertices = increaseArray(vertices, 1000000);
            if (vertices[currentID] == null)
                vertices[currentID] = edge.getHead();
            edge.getTail().addOutgoingEdge(edge);
        }
    }


    private Vertex[] increaseArray(Vertex[] array, int increase) {
        Vertex[] newArray = new Vertex[array.length + increase];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }


    /**
     * Instantiates and/or returns a singleton instance of Graph.
     *
     * @param edges List of Edges to build a Graph.
     * @return Graph.
     * @throws EmptyListOfEdgesException if list of edges is empty.
     */
    // TODO: unschön, wenn man sich nur eine instance holen möchte, ohne die
    // Graph instance zu kennen, muss getrennt werden
    public static Graph getInstance(List<Edge> edges) throws EmptyListOfEdgesException {
        if (edges == null)
            throw new IllegalArgumentException("list of edges must not be null");
        if (edges.size() == 0)
            throw new EmptyListOfEdgesException("list of edges must be at least of size 1");
        if (instance == null)
            instance = new Graph(edges);
        return instance;
    }


    /**
     * Returns the Edge with the given id.
     *
     * @param id The id of the Edge to be returned.
     * @return Edge the Edge with the given id.
     */
    public Edge getEdgeByID(int id) {
        if (id < 0)
            throw new IllegalArgumentException("id must not be smaller then 0");
        return null;
    }


    /**
     * Returns the Vertex with the given id.
     *
     * @param id The id of the Vertex to be returned.
     * @return Vertex the Vertex with the given id.
     * @throws NoVertexForIDExistsException if no Vertex for given ID exists.
     */
    public Vertex getVertexByID(int id) throws NoVertexForIDExistsException {
        if (id < 0)
            throw new IllegalArgumentException("id must not be smaller then 0");
        if (id > vertices.length-1)
            throw new NoVertexForIDExistsException("id must not be greater than "
                    + (vertices.length-1));
        if (vertices[id] == null)
            throw new NoVertexForIDExistsException("no vertex exists for id: " + id);
        return vertices[id];
    }

}