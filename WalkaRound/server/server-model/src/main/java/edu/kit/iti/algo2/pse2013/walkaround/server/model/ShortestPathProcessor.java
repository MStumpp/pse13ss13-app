package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Graph;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.RouteInfoTransfer;

import java.util.LinkedList;
import java.util.List;

/**
 * ShortestPathProcessor which takes two Coordinates and computes the shortest path between
 * these coordinates using a Graph model. The result is represented as an ordered list of
 * Coordinates, whereas the first and last Coordinates equal the input Coordinates.
 * A route is represented as RouteInfoTransfer.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class ShortestPathProcessor {

    /**
     * ShortestPathProcessor instance.
     */
    private static ShortestPathProcessor instance;


    /**
     * Graph instance.
     */
    private Graph graph;


    /**
     * Creates an instance of ShortestPathProcessor.
     *
     * @param graph Graph used for shortest path computation.
     * @return ShortestPathProcessor.
     */
    private ShortestPathProcessor(Graph graph) {
        this.graph = graph;
    }


    /**
     * Instantiates and/or returns a singleton instance of ShortestPathProcessor.
     *
     * @param graph Graph used for shortest path computation.
     * @return ShortestPathProcessor.
     */
    // TODO: unschön, wenn man sich nur eine instance holen möchte, ohne die Graph instance zu kennen, muss getrennt werden
    public static ShortestPathProcessor getInstance(Graph graph) {
        if (graph == null)
            throw new IllegalArgumentException("graph must be provided");
        if (instance == null)
            instance = new ShortestPathProcessor(graph);
        return instance;
    }


    /**
     * Computes a shortest path between any given two Coordinates using the provided
     * graph.
     *
     * @param coordinate1 One end of the route to be computed.
     * @param coordinate2 One end of the route to be computed.
     * @return RouteInfoTransfer.
     */
    public RouteInfoTransfer computeShortestPath(Coordinate coordinate1, Coordinate coordinate2) {
        if (coordinate1 == null || coordinate2 == null)
            throw new IllegalArgumentException("coordinate1 and coordinate2 must be provided");

        List<Coordinate> coordinates = new LinkedList<Coordinate>();
        coordinates.add(new Coordinate(12.12, 12.12));
        coordinates.add(new Coordinate(13.13, 13.13));
        coordinates.add(new Coordinate(14.14, 14.14));
        // TODO: change constructor of route from LinkedList to List, then remove the cast
        return new RouteInfoTransfer((LinkedList<Coordinate>) coordinates);
    }

}
