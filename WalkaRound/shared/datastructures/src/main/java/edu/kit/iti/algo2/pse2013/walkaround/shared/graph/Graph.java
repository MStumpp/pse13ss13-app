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
     * Array of Vertices.
     */
    public Vertex[] vertices;


    /**
     * Creates an instance of Graph.
     *
     * @param graphDataIO GaphDataIO data.
     * @throws EmptyListOfEdgesException if list of edges is empty.
     * @throws EmptyListOfEdgesException if list of edges is empty.
     */
    public Graph(GraphDataIO graphDataIO) throws IllegalArgumentException,
            EmptyListOfEdgesException {

        if (graphDataIO == null)
            throw new IllegalArgumentException("GraphDataIO must not be null");

        Set<Edge> edges = graphDataIO.getEdges();

        if (edges.size() == 0)
            throw new EmptyListOfEdgesException("list of edges must be at least of size 1");

        vertices = new Vertex[edges.size()*2];
        Vertex currentForTail, currentForHead;
        for (Edge edge : edges) {
            currentForTail = edge.getTail();
            if (vertices.length <= currentForTail.getID())
                vertices = increaseArray(vertices, 100000);
            if (vertices[currentForTail.getID()] == null)
                vertices[currentForTail.getID()] = currentForTail;
            currentForHead = edge.getHead();
            if (vertices.length <= currentForHead.getID())
                vertices = increaseArray(vertices, 100000);
            if (vertices[currentForHead.getID()] == null)
                vertices[currentForHead.getID()] = currentForHead;

            // for tail, reuse current edge object
            currentForTail.addOutgoingEdge(edge);
            // instantiate new Edge object for reverse egde
            Edge inverseEdge = new Edge(edge.getHead(), edge.getTail());
            currentForHead.addOutgoingEdge(inverseEdge);
        }
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
            return null;
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