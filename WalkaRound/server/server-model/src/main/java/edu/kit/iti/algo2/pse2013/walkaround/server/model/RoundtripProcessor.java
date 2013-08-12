package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Profile;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryProcessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.*;

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
    private GraphDataIO graphDataIO;

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
     * Epsilon.
     */
    private final static double epsilon = 0.05;


    /**
     * Creates an instance of RoundtripProcessor.
     *
     * @param graphDataIO GraphDataIO used for shortest path computation.
     */
    private RoundtripProcessor(GraphDataIO graphDataIO, GeometryProcessor geometryProcessor) {
        this.graphDataIO = graphDataIO;
        try {
            this.graph = new Graph(graphDataIO);
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
     * @param graphDataIO Graph used for shortest path computation.
     * @return RoundtripProcessor.
     */
    public static RoundtripProcessor init(GraphDataIO graphDataIO, GeometryProcessor geometryProcessor) {
        if (graphDataIO == null)
            throw new IllegalArgumentException("Graph must be provided");
        if (geometryProcessor == null)
            throw new IllegalArgumentException("Graph must be provided");
        if (instance != null)
            throw new IllegalArgumentException("RoundtripProcessor already initialized");
        instance = new RoundtripProcessor(graphDataIO, geometryProcessor);
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
        try {
           sourceVertex = graph.getVertexByID(source.getID());
        } catch (NoVertexForIDExistsException e) {
            throw new ShortestPathComputeException("source vertex provided not in graph contained");
        }

        if (sourceVertex == null)
            throw new ShortestPathComputeException("source provided not in graph contained");

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

            if (current.getRun() == runCounter &&
                 current.getCurrentLength() > (1+epsilon)*length/3)
                continue;

            for (Edge edge : current.getOutgoingEdges()) {
                distance = current.getCurrentLength() + edgeWeight(edge);
                currentHead = edge.getHead();

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
//        route.add(targetVertex);
//        Vertex currentParent = targetVertex.getParent();
//        while (currentParent != null && !currentParent.equals(sourceVertex)) {
//            route.addFirst(currentParent);
//            currentParent = currentParent.getParent();
//        }
//        if (currentParent != null)
//            route.addFirst(currentParent);
//
//        // throw exception if not shortest path exists
//        if (route.size() == 1)
//            throw new NoShortestPathExistsException("no shortest path exists "
//                    + "between source vertex with id: "
//                    + sourceVertex.getID() + " and target vertex with id: "
//                    + targetVertex.getID());

        return route;
    }


    /**
     * Computes weight for a given Edge considering its length and some badness
     * value. Badness value is based on distance to certain types of locations
     * and can take value between 0 and 1. In case of areas, badness is either
     * 0 or 1.
     *
     * @param edge Edge weight to be computed for.
     * @return double Weight for edge considering its length and badness.
     */
    private double edgeWeight(Edge edge) {
        double badness = 1.d;
        return edge.getLength()*badness;
    }

}
