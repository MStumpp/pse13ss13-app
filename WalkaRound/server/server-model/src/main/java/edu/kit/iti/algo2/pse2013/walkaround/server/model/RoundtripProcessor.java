package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Profile;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryProcessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Graph;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.NoVertexForIDExistsException;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

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
     * GeometryProcessor.
     */
    private GeometryProcessor geometryProcessor;


    /**
     * Priority queue.
     */
    private PriorityQueue<Vertex> queue;


    /**
     * Run counter.
     */
    private int runCounter;


    /**
     * Creates an instance of RoundtripProcessor.
     *
     * @param graph Graph used for shortest path computation.
     */
    private RoundtripProcessor(Graph graph, GeometryProcessor geometryProcessor) {
        this.graph = graph;
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
        runCounter = 0;
        this.geometryProcessor = geometryProcessor;
    }


    /**
     * Instantiates and/or returns a singleton instance of RoundtripProcessor.
     *
     * @return RoundtripProcessor.
     * @throws InstantiationException If not instantiated before.
     */
    public static RoundtripProcessor getInstance() throws InstantiationException {
        if (instance == null)
            throw new InstantiationException("singleton must be initialized first");
        return instance;
    }


    /**
     * Instantiates and returns a singleton instance of RoundtripProcessor.
     *
     * @param graph Graph used for shortest path computation.
     * @return RoundtripProcessor.
     */
    public static RoundtripProcessor init(Graph graph, GeometryProcessor geometryProcessor) {
        if (graph == null)
            throw new IllegalArgumentException("Graph must be provided");
        if (geometryProcessor == null)
            throw new IllegalArgumentException("Graph must be provided");
        if (instance != null)
            throw new IllegalArgumentException("RoundtripProcessor already initialized");
        instance = new RoundtripProcessor(graph, geometryProcessor);
        return instance;
    }


    /**
     * Computes a roundtrip based on a starting Coordinate, Profile id
     * and a roundtrip length using the provided Graph.
     *
     * @param source The starting Coordinate of the roundtrip to be computed.
     * @param profile The id of the Profile of the roundtrip to be computed.
     * @param length The length of the roundtrip in meter to be computed.
     * @return RouteInfoTransfer.
     */
    public List<Vertex> computeRoundtrip(Vertex source, int profile, int length)
        throws NoShortestPathExistsException, ShortestPathComputeException {
        if (source == null)
            throw new IllegalArgumentException("coordinate must be provided");
        // TODO: Uncomment once Profile returned
        /*if (Profile.getByID(profile) == null)
            throw new IllegalArgumentException("profile for id unknown");  */
        if (length < 100)
            throw new IllegalArgumentException("length must be at least 100 meter");

        Vertex sourceVertex;
        Vertex targetVertex;
        try {
            sourceVertex = graph.getVertexByID(source.getID());
            targetVertex = graph.getVertexByID(source.getID());
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

        // throw exception if not shortest path exists
        if (route.size() == 1)
            throw new NoShortestPathExistsException("no shortest path exists "
                    + "between source vertex with id: "
                    + sourceVertex.getID() + " and target vertex with id: "
                    + targetVertex.getID());

        return route;
    }

}
