package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Graph;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Profile;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.RouteInfoTransfer;

import java.util.LinkedList;
import java.util.List;

/**
 * RoundtripProcessor which takes a Coordinate, a Profile and a length, and computes a roundtrip.
 * The result is represented as an ordered list of Coordinates.
 * A roundtrip is represented as RouteInfoTransfer.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class RoundtripProcessor {

    /**
     * RoundtripProcessor instance.
     */
    private static RoundtripProcessor instance;


    /**
     * Graph instance.
     */
    private Graph graph;


    /**
     * Creates an instance of RoundtripProcessor.
     *
     * @param graph Graph used for shortest path computation.
     */
    private RoundtripProcessor(Graph graph) {
        this.graph = graph;
    }


    /**
     * Instantiates and/or returns a singleton instance of RoundtripProcessor.
     *
     * @return RoundtripProcessor.
     */
    public static RoundtripProcessor getInstance() {
        if (instance == null)
            throw new IllegalArgumentException("singleton must be initialized first");
        return instance;
    }


    /**
     * Instantiates and returns a singleton instance of RoundtripProcessor.
     *
     * @param graph Graph used for shortest path computation.
     * @return RoundtripProcessor.
     */
    public static RoundtripProcessor init(Graph graph) {
        if (graph == null)
            throw new IllegalArgumentException("Graph must be provided");
        if (instance != null)
            throw new IllegalArgumentException("RoundtripProcessor already initialized");
        instance = new RoundtripProcessor(graph);
        return instance;
    }


    /**
     * Computes a roundtrip based on a starting Coordinate, Profile id
     * and a roundtrip length using the provided Graph.
     *
     * @param coordinate The starting Coordinate of the roundtrip to be computed.
     * @param profile The id of the Profile of the roundtrip to be computed.
     * @param length The length of the roundtrip in meter to be computed.
     * @return RouteInfoTransfer.
     */
    public RouteInfoTransfer computeRoundtrip(Coordinate coordinate, int profile, int length) {
        if (coordinate == null)
            throw new IllegalArgumentException("coordinate must be provided");
        if (Profile.getByID(profile) == null)
            throw new IllegalArgumentException("profile for id unknown");
        if (length < 100)
            throw new IllegalArgumentException("length must be at least 100 meter");

        List<Coordinate> coordinates = new LinkedList<Coordinate>();
        coordinates.add(new Coordinate(12.12, 12.12));
        coordinates.add(new Coordinate(13.13, 13.13));
        coordinates.add(new Coordinate(14.14, 14.14));
        // TODO: change constructor of route from LinkedList to List, then remove the cast
        return new RouteInfoTransfer((LinkedList<Coordinate>) coordinates);
    }

}
