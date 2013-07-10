package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

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
     * @throws InstantiationException If not instantiated before.
     */
    public static OptimizeRouteProcessor getInstance() throws InstantiationException {
        if (instance == null)
            throw new InstantiationException("singleton must be initialized first");
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
     * @param vertices The route to be optimized.
     * @return RouteInfoTransfer.
     */
    public List<Vertex> computeOptimizedRoute(List<Vertex> vertices) {
        if (vertices == null)
            throw new IllegalArgumentException("list of vertices must be provided");
        if (vertices.size() < 2)
            throw new IllegalArgumentException("number of vertices mist be equal to or greater than 2");
        List<Vertex> route = new LinkedList<Vertex>();
        route.add(new Vertex(12.12, 12.12));
        route.add(new Vertex(13.13, 13.13));
        route.add(new Vertex(14.14, 14.14));
        return route;
    }

}
