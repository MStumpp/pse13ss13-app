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
    private final Vertex[] vertices;


    /**
     * Creates an instance of Graph.
     *
     * @param edges List of Edges to build a Graph.
     */
    private Graph(List<Edge> edges) {
        vertices = new Vertex[Vertex.getIDCount()];
        for (Edge edge : edges) {
            if (vertices[edge.getTail().getID()] == null)
                vertices[edge.getTail().getID()] = edge.getTail();
            if (vertices[edge.getHead().getID()] == null)
                vertices[edge.getHead().getID()] = edge.getHead();
            edge.getTail().addOutgoingEdge(edge);
        }
    }


    /**
     * Instantiates and/or returns a singleton instance of Graph.
     *
     * @param edges List of Edges to build a Graph.
     * @return Graph.
     * @throws EmptyListOfEdgesException if list of edges is empty.
     */
    // TODO: unschön, wenn man sich nur eine instance holen möchte, ohne die Graph instance zu kennen, muss getrennt werden
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
        if (id > Vertex.getIDCount()-1)
            throw new NoVertexForIDExistsException("id must not be greater than " + (Vertex.getIDCount()-1));
        return vertices[id];
    }

}