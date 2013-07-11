package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import java.util.Set;

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
    public Vertex[] vertices;


    /**
     * Creates an instance of Graph.
     *
     * @param edges Set of Edges to build a Graph.
     */
    private Graph(Set<Edge> edges) {
        vertices = new Vertex[edges.size()*2];
        Vertex currentForTail;
        Vertex currentForHead;
        for (Edge edge : edges) {
            currentForTail = edge.getTail();
            if (vertices.length <= currentForTail.getID())
                vertices = increaseArray(vertices, 100000);
            if (vertices[currentForTail.getID()] == null)
                vertices[currentForTail.getID()] = edge.getTail();
            currentForHead = edge.getHead();
            if (vertices.length <= currentForHead.getID())
                vertices = increaseArray(vertices, 100000);
            if (vertices[currentForHead.getID()] == null)
                vertices[currentForHead.getID()] = edge.getHead();

            // for tail, reuse current edge object
            edge.getTail().addOutgoingEdge(edge);
            // instantiate new Edge object for reverse egde
            Edge inverseEdge = new Edge(edge.getHead(), edge.getTail());
            inverseEdge.setLength(edge.getLength());
            edge.getHead().addOutgoingEdge(inverseEdge);
        }
    }


    /**
     * Instantiates and/or returns a singleton instance of Graph.
     *
     * @return Graph.
     * @throws InstantiationException If class is not instatiated.
     */
    public static Graph getInstance() throws InstantiationException {
        if (instance == null)
            throw new InstantiationException("singleton must be initialized first");
        return instance;
    }


    /**
     * Instantiates and returns a singleton instance of Graph.
     *
     * @param edges List of Edges to build a Graph.
     * @return Graph.
     * @throws EmptyListOfEdgesException if list of edges is empty.
     */
    public static Graph init(Set<Edge> edges) throws EmptyListOfEdgesException {
        if (edges == null)
            throw new IllegalArgumentException("list of edges must not be null");
        if (edges.size() == 0)
            throw new EmptyListOfEdgesException("list of edges must be at least of size 1");
        if (instance != null)
            throw new IllegalArgumentException("GeometryProcessor already initialized");
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


    /**
     * Increase the size of an array containing Vertex objects.
     *
     * @param array The array to be increased.
     * @param increase The target size.
     * @return Vertex[] The increased array.
     */
    private Vertex[] increaseArray(Vertex[] array, int increase) {
        Vertex[] newArray = new Vertex[array.length + increase];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

}