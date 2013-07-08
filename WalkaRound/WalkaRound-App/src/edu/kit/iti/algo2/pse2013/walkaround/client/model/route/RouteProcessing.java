package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.util.LinkedList;
import java.util.List;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * This class provides a set of delegation methods for computing a shortest path,
 * roundtrip and optimized route. The actual computation is done by an endpoint.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class RouteProcessing {

    /**
     * RouteProcessing instance.
     */
    private static RouteProcessing instance;


    /**
     * Creates a fresh instance of RouteProcessing.
     */
    private RouteProcessing() {
    }


    /**
     * Instantiates and/or returns a singleton instance of RouteProcessing.
     *
     * @return RouteProcessing.
     */
    public static RouteProcessing getInstance() {
        if (instance == null)
            instance = new RouteProcessing();
        return instance;
    }


    /**
     * Delegation method for computing a shortest path between any two Coordinates.
     * The actual computation is done by an endpoint.
     *
     * @param coordinate1 One end of the route to be computed.
     * @param coordinate2 One end of the route to be computed.
     * @return RouteInfo.
     */
    public RouteInfo computeShortestPath(Coordinate coordinate1, Coordinate coordinate2) {
        List<Coordinate> coordinates = new LinkedList<Coordinate>();
        coordinates.add(coordinate1);
        coordinates.add(coordinate2);
        // TODO: change constructor of route from LinkedList to List, then remove the cast
        return new Route((LinkedList<Coordinate>) coordinates);
    }


    /**
     * Delegation method for computing a roundtrip based on a starting Coordinate, Profile id
     * and a roundtrip length. The actual computation is done by an endpoint.
     *
     * @param coordinate The starting Coordinate of the roundtrip to be computed.
     * @param profile The id of the Profile of the roundtrip to be computed.
     * @param length The length of the roundtrip to be computed.
     * @return RouteInfo.
     */
    public RouteInfo computeRoundtrip(Coordinate coordinate, int profile, int length) {
        throw new RuntimeException("to be implemented");
    }


    /**
     * Delegation method for computing an optimized Route based on a given Route.
     * The actual computation is done by an endpoint.
     *
     * @param routeInfo The Route to be optimized.
     * @return RouteInfo.
     */
    public RouteInfo computeOptimizedRoute(RouteInfo routeInfo) {
        throw new RuntimeException("to be implemented");
    }

}
