package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Graph;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.NoVertexForIDExistsException;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * ShortestPathProcessor which takes two Coordinates and computes the shortest path between
 * these coordinates using a Graph model. The result is represented as an ordered list of
 * Coordinates, whereas the first and last Coordinates equal the input Coordinates.
 * A route is represented as RouteInfoTransfer.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class ShortestPathProcessor_SingleThreaded {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ShortestPathProcessor_SingleThreaded.class);


    /**
     * ShortestPathProcessor instance.
     */
    private static ShortestPathProcessor_SingleThreaded instance;


    /**
     * Graph instance.
     */
    private Graph graph;


    /**
     * Priority queue.
     */
    private PriorityQueue<Vertex> queue;


    /**
     * Run counter.
     */
    private int runCounter;


    /**
     * Creates an instance of ShortestPathProcessor.
     *
     * @param graph Graph used for shortest path computation.
     */
    private ShortestPathProcessor_SingleThreaded(Graph graph) {
        this.graph = graph;
        runCounter = 0;
    }


    /**
     * Instantiates and/or returns a singleton instance of ShortestPathProcessor.
     *
     * @return ShortestPathProcessor.
     * @throws InstantiationException If not instantiated before.
     */
    public static ShortestPathProcessor_SingleThreaded getInstance() throws InstantiationException {
        if (instance == null)
            throw new InstantiationException("singleton must be initialized first");
        return instance;
    }


    /**
     * Instantiates and returns a singleton instance of ShortestPathProcessor.
     *
     * @param graph Graph used for shortest path computation.
     * @return ShortestPathProcessor.
     */
    public static ShortestPathProcessor_SingleThreaded init(Graph graph) {
        if (graph == null)
            throw new IllegalArgumentException("Graph must be provided");
        if (instance != null)
            throw new IllegalArgumentException("ShortestPathProcessor already initialized");
        instance = new ShortestPathProcessor_SingleThreaded(graph);
        return instance;
    }


    /**
     * Computes a shortest path between any given two Coordinates using the provided
     * graph.
     *
     * @param source Source of the route to be computed.
     * @param target Target of the route to be computed.
     * @return RouteInfoTransfer.
     * @throws edu.kit.iti.algo2.pse2013.walkaround.server.model.NoShortestPathExistsException If no shortest path between given Coordinates exists.
     * @throws edu.kit.iti.algo2.pse2013.walkaround.server.model.ShortestPathComputeException If something during computation goes wrong.
     */
    public List<Vertex> computeShortestPath(Vertex source,
                                            Vertex target)
            throws NoShortestPathExistsException, ShortestPathComputeException {
        if (source == null || target == null)
            throw new IllegalArgumentException("source and target must be provided");

        // set up the priority queue
        queue = new PriorityQueue<Vertex>(10, new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                if (v1.getCurrentLength() >  v2.getCurrentLength()){
                    return 1;
                } else if (v1.getCurrentLength() < v2.getCurrentLength()){
                    return -1;
                } else
                    return 0;
            }
        });

        long startTime = System.currentTimeMillis();

        Vertex sourceVertex;
        Vertex targetVertex;
        try {
            sourceVertex = graph.getVertexByID(source.getID());
            targetVertex = graph.getVertexByID(target.getID());
        } catch (NoVertexForIDExistsException e) {
            throw new ShortestPathComputeException("source and/or target vertex provided not in graph contained");
        }

        if (sourceVertex == null || targetVertex == null)
            throw new ShortestPathComputeException("source and/or target provided not in graph contained");

        // some init
        runCounter += 1;
        sourceVertex.setCurrentLength(0.d);
        queue.clear();
        queue.add(sourceVertex);

        // update parent and currentLengths
        Vertex current, currentHead;
        double distance;
        while (!queue.isEmpty()) {
            current = queue.poll();

            // stop criteria
            if (current.equals(targetVertex))
                break;

            for (Edge edge : current.getOutgoingEdges()) {
                currentHead = edge.getHead();
                distance = current.getCurrentLength() + edge.getLength();

                // not yet visited during current run
                if (currentHead.getRun() != runCounter) {
                    currentHead.setCurrentLength(distance);
                    currentHead.setParent(current);
                    queue.add(currentHead);
                    currentHead.setRun(runCounter);

                // edge head already in queue, eventually decrease key
                } else if (distance < currentHead.getCurrentLength()) {
                    currentHead.setCurrentLength(distance);
                    currentHead.setParent(current);
                    // remove head if its already in the queue, coz
                    // updating current length after added to queue doesn't
                    // lead to order change
                    queue.remove(currentHead);
                    queue.add(currentHead);
                }
            }
        }

        // free queue from memory
        queue = null;

        // get the list of coordinates
        LinkedList<Vertex> route = new LinkedList<Vertex>();
        route.add(targetVertex);
        Vertex currentParent = targetVertex.getParent();
        while (currentParent != null && !currentParent.equals(sourceVertex)) {
            route.addFirst(currentParent);
            currentParent = currentParent.getParent();
        }
        if (currentParent != null)
            route.addFirst(currentParent);

        // throw exception if no shortest path exists
        if (route.size() == 1)
            throw new NoShortestPathExistsException("no shortest path exists "
                    + "between source vertex with id: "
                    + sourceVertex.getID() + " and target vertex with id: "
                    + targetVertex.getID());

        long stopTime = System.currentTimeMillis();
        long runTime = stopTime - startTime;

        logger.info("computeShortestPath: Start: " + sourceVertex.toString() +
                " End: " + targetVertex.toString() + " Route Size: " + route.size() + " Run time: " + runTime);

        return route;
    }

}
