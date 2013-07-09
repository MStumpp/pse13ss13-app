package edu.kit.iti.algo2.pse2013.walkaround.server.model;


import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.RouteInfoTransfer;

import java.util.LinkedList;
import java.util.List;

/**
 * OptimizeRouteProcessor which takes a route and computes an optimized route regarding the
 * Waypoints contained in the given route. A route is represented as RouteInfoTransfer.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class OptimizeRouteProcessor {

    /**
     * OptimizeRouteProcessor instance.
     */
    private static OptimizeRouteProcessor instance;


    /**
     * ShortestPathProcessor instance.
     */
    private ShortestPathProcessor shortestPathProcessor;


    /**
     * Creates an instance of OptimizeRouteProcessor.
     *
     * @param shortestPathProcessor ShortestPathProcessor used for route optimization.
     */
    private OptimizeRouteProcessor(ShortestPathProcessor shortestPathProcessor) {
        this.shortestPathProcessor = shortestPathProcessor;
    }


    /**
     * Instantiates and/or returns a singleton instance of OptimizeRouteProcessor.
     *
     * @return OptimizeRouteProcessor.
     */
    public static OptimizeRouteProcessor getInstance() {
        if (instance == null)
            throw new IllegalArgumentException("singleton must be initialized first");
        return instance;
    }


    /**
     * Instantiates and returns a singleton instance of OptimizeRouteProcessor.
     *
     * @param shortestPathProcessor ShortestPathProcessor used for shortest path computation.
     * @return OptimizeRouteProcessor.
     */
    public static OptimizeRouteProcessor init(ShortestPathProcessor shortestPathProcessor) {
        if (shortestPathProcessor == null)
            throw new IllegalArgumentException("ShortestPathProcessor must be provided");
        if (instance != null)
            throw new IllegalArgumentException("OptimizeRouteProcessor already initialized");
        instance = new OptimizeRouteProcessor(shortestPathProcessor);
        return instance;
    }


    /**
     * Computes an optimized route based on a given route.
     *
     * @param routeInfoTransfer The route to be optimized.
     * @return RouteInfoTransfer.
     */
    public RouteInfoTransfer computeOptimizedRoute(RouteInfoTransfer routeInfoTransfer) {
        if (routeInfoTransfer == null)
            throw new IllegalArgumentException("routeInfoTransfer must be provided");

        List<Coordinate> coordinates = new LinkedList<Coordinate>();
        coordinates.add(new Coordinate(12.12, 12.12));
        coordinates.add(new Coordinate(13.13, 13.13));
        coordinates.add(new Coordinate(14.14, 14.14));
        return new RouteInfoTransfer(coordinates);
    }

}
