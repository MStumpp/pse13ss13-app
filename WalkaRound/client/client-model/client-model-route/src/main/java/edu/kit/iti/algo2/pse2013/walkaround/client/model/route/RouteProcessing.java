package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

import java.util.LinkedList;
import java.util.List;

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
     *
     * @return RouteProcessing.
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
     * @return Route.
     */
    public RouteInfo computeShortestPath(Coordinate coordinate1, Coordinate coordinate2) {
        List<Coordinate> coordinates = new LinkedList<Coordinate>();
        coordinates.add(new Coordinate(12.12, 12.12));
        coordinates.add(new Coordinate(13.13, 13.13));
        coordinates.add(new Coordinate(14.14, 14.14));
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
     * @return Route.
     */
    public RouteInfo computeRoundtrip(Coordinate coordinate, int profile, int length) {
        throw new RuntimeException("to be implemented");
    }


    /**
     * Delegation method for computing an optimized route based on a given route.
     * The actual computation is done by an endpoint.
     *
     * @param routeInfo The route to be optimized.
     * @return Route.
     */
    public RouteInfo computeOptimizedRoute(RouteInfo routeInfo) {
        throw new RuntimeException("to be implemented");
    }

}
