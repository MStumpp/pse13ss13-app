package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.GraphDataIO;

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
     * Creates an instance of Graph.
     *
     * @param graphDataIO GraphDataIO with edges used to initialize this Graph.
     */
    private Graph(GraphDataIO graphDataIO) {
        // initialize this graph with list of edges
        graphDataIO.getEdges();
    }


    /**
     * Instantiates and/or returns a singleton instance of Graph.
     *
     * @param graphDataIO GraphDataIO with edges used to initialize this Graph.
     * @return Graph.
     */
    // TODO: unschön, wenn man sich nur eine instance holen möchte, ohne die Graph instance zu kennen, muss getrennt werden
    public Graph getInstance(GraphDataIO graphDataIO) {
        if (graphDataIO == null)
            throw new IllegalArgumentException("GraphDataIO must not be null");
        if (instance == null)
            instance = new Graph(graphDataIO);
        return instance;
    }


    /**
     * Returns the Edge with the given id.
     *
     * @param id The id of the Edge to be returned.
     * @return Edge the Edge with the given id.
     */
    public Edge getEdgeByID(int id) {
        return new Edge(0.d, 0.d, 0.d, 0.d);
    }


    /**
     * Returns the Vertex with the given id.
     *
     * @param id The id of the Vertex to be returned.
     * @return Vertex the Vertex with the given id.
     */
    public Vertex getVertexByID(int id) {
        return new Vertex(1);
    }


    /**
     * Flags each Vertex contained in the Graph with the key/value.
     *
     * @param key Key for object.
     * @param value Object.
     */
    public void forEachVertexSetKey(String key, Object value) {

    }

}
