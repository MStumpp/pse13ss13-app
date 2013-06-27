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
            edge.getTail().addOutgoingEdge(edge);
        }
    }


    /**
     * Instantiates and/or returns a singleton instance of Graph.
     *
     * @param edges List of Edges to build a Graph.
     * @return Graph.
     */
    // TODO: unschön, wenn man sich nur eine instance holen möchte, ohne die Graph instance zu kennen, muss getrennt werden
    public static Graph getInstance(List<Edge> edges) {
        if (edges == null || edges.size() == 0)
            throw new IllegalArgumentException("list of edges must not be null and at least of size 1");
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
        return null;
    }


    /**
     * Returns the Vertex with the given id.
     *
     * @param id The id of the Vertex to be returned.
     * @return Vertex the Vertex with the given id.
     */
    public Vertex getVertexByID(int id) {
        if (id < 0 || id > Vertex.getIDCount()-1)
            throw new IllegalArgumentException("id must not be smaller then 0 and not greater than " + (Vertex.getIDCount()-1));
        return vertices[id];
    }


    /**
     * Resets parent and currentLength of each Vertex in Graph.
     */
    // TODO: muss anders gelöst werden, evt. statische Attribute von dynamischen trennen
    public void resetGraph() {
        for (Vertex vertex : vertices) {
            vertex.setCurrentLength(Double.POSITIVE_INFINITY);
            vertex.setParent(null);
        }
    }

}