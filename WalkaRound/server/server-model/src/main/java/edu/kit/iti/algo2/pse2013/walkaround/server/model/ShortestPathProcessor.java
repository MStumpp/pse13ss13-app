package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Graph;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.NoVertexForIDExistsException;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.RouteInfoTransfer;

import java.util.Comparator;
import java.util.LinkedList;
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
    private ShortestPathProcessor(Graph graph) {
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
    }


    /**
     * Instantiates and/or returns a singleton instance of ShortestPathProcessor.
     *
     * @param graph Graph used for shortest path computation.
     * @return ShortestPathProcessor.
     */
    // TODO: unschön, wenn man sich nur eine instance holen möchte,
    // ohne die Graph instance zu kennen, muss getrennt werden
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
     * @throw NoShortestPathExistsException If no shortest path between given Coordinates exists
     */
    public RouteInfoTransfer computeShortestPath(Coordinate coordinate1, Coordinate coordinate2)
            throws NoShortestPathExistsException {
        if (coordinate1 == null || coordinate2 == null)
            throw new IllegalArgumentException("coordinate1 and coordinate2 must be provided");

        //int startVertexId = GeometryProcessor.getInstance(null).getNearestVertex(coordinate1);
        //int endVertexId = GeometryProcessor.getInstance(null).getNearestVertex(coordinate2);

        int sourceVertexId = 2295;
        int targetVertexId = 2441;

        // get source and target Vertex objects
        Vertex sourceVertex = null, targetVertex = null;
        try {
            sourceVertex = graph.getVertexByID(sourceVertexId);
            targetVertex = graph.getVertexByID(targetVertexId);
        } catch (NoVertexForIDExistsException e) {
            e.printStackTrace();
        }

        System.out.println(sourceVertex);
        System.out.println(targetVertex);

        if (sourceVertex == null || targetVertex == null)
            return null;

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
        LinkedList<Coordinate> result = new LinkedList<Coordinate>();
        result.add(targetVertex);
        Vertex currentParent = targetVertex.getParent();
        while (currentParent != null && !currentParent.equals(sourceVertex)) {
            result.addFirst(currentParent);
            currentParent = currentParent.getParent();
        }
        if (currentParent != null)
            result.addFirst(currentParent);

        // throw exception if not shortest path exists
        if (result.size() == 1)
            throw new NoShortestPathExistsException("no shortest path exists "
                    + "between source vertex with id: "
                    + sourceVertex.getID() + " and target vertex with id: "
                    + targetVertex.getID());

        // set up route transfer and return
        RouteInfoTransfer route = new RouteInfoTransfer(result);
        return route;
    }

}
