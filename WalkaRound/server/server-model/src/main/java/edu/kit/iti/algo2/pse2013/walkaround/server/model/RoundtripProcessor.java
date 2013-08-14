package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import com.google.common.collect.Sets;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

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
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(RoundtripProcessor.class);


    /**
     * RoundtripProcessor instance.
     */
    private static RoundtripProcessor instance;


    /**
     * ExecutorService.
     */
    private ExecutorService executor;


    /**
     * Epsilon.
     */
    private final static double eps = 0.05;


    /**
     * Creates an instance of RoundtripProcessor.
     *
     * @param graphDataIO GraphDataIO used for shortest path computation.
     * @param numberThreads The number of threads for parallel computation.
     */
    private RoundtripProcessor(GraphDataIO graphDataIO, int numberThreads)
            throws EmptyListOfEdgesException {

        // initialize ShortestPathTreeProcessor
        ShortestPathTreeProcessor.init(graphDataIO, numberThreads);

        executor = Executors.newFixedThreadPool(numberThreads);
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
     * @param graphDataIO GraphDataIO used for shortest path computation.
     * @return RoundtripProcessor.
     * @throws IllegalArgumentException If some input parameter are missing.
     * @throws EmptyListOfEdgesException If there are no edges.
     */
    public static RoundtripProcessor init(GraphDataIO graphDataIO)
            throws IllegalArgumentException, EmptyListOfEdgesException {
        return init(graphDataIO, 1);
    }


    /**
     * Instantiates and returns a singleton instance of RoundtripProcessor.
     *
     * @param graphDataIO Graph used for roundtrip computation.
     * @param numberThreads The number of threads for parallel computation.
     * @return RoundtripProcessor.
     */
    public static RoundtripProcessor init(GraphDataIO graphDataIO, int numberThreads)
            throws EmptyListOfEdgesException {
        if (graphDataIO == null)
            throw new IllegalArgumentException("Graph must be provided");
        if (numberThreads < 1)
            throw new IllegalArgumentException("Number of threads must be greater or equal to 1");
        if (instance != null)
            throw new IllegalArgumentException("RoundtripProcessor already initialized");
        instance = new RoundtripProcessor(graphDataIO, numberThreads);
        return instance;
    }


    /**
     * Computes a shortest path between any given two Coordinates using the provided
     * graph.
     *
     * @param source Source of the route to be computed.
     * @param categories List of Categories.
     * @param length Length of the roundtrip.
     * @return List of Vertices.
     * @throws IllegalArgumentException Some input parameter is missing.
     * @throws RoundtripComputationNoSlotsException If no slots available for computing roundtrip.
     * @throws RoundtripComputeException If something during computation goes wrong.
     */
    public List<Vertex> computeRoundtrip(Vertex source, int[] categories, int length)
            throws IllegalArgumentException, RoundtripComputationNoSlotsException,
            RoundtripComputeException {

        if (source == null)
            throw new IllegalArgumentException("source must not be null");
        if (categories == null || categories.length == 0)
            throw new IllegalArgumentException("categories must not be null and/or of length 0");
        if (length < 1)
            throw new IllegalArgumentException("length must be at least 1 meter");

        logger.info("computeRoundtrip: Source: " + source.toString() +
                " Categories: " + categories + " Length: " + length);
        Future<List<Vertex>> future = executor.submit(new RoundtripTask(source, categories, length));

        if (future.isCancelled())
            throw new RoundtripComputationNoSlotsException("no slots available");

        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new RoundtripComputeException(e.getMessage());
        } catch (ExecutionException e) {
            throw new RoundtripComputeException(e.getMessage());
        }
    }


    /**
     * Represents a task for roundtrip computation.
     */
    private class RoundtripTask implements Callable<List<Vertex>> {

        /**
         * Logger.
         */
        private final Logger logger = LoggerFactory.getLogger(RoundtripTask.class);

        private Vertex source;
        private int[] categories;
        private int length;

        RoundtripTask(Vertex source, int[] categories, int length) {
            this.source = source;
            this.categories = categories;
            this.length = length;
        }

        @Override
        public List<Vertex> call() throws Exception {
            logger.info("call(): Thread: " + Thread.currentThread().getName() + " Reference: " +
                    Thread.currentThread().getClass().getName() + "@" + Integer.toHexString(Thread.currentThread().hashCode()));

            // get the initial set of Vertices
            RouteSet ring_s = ShortestPathTreeProcessor.getInstance().
                    computeShortestPathTree(source, categories, length/3, eps);

            // for all Vertices
            RouteSet ring_u;
            Set<Vertex> intersect;

            Vertex currentBestU = null,
                   currentBestV = null;
            List<Vertex> currentRouteUV = null;
            double weigthedLenghtSU, weightedLengthUVS,
                   bestTotalWeightedLength = Double.POSITIVE_INFINITY;

            for (Vertex vertexU : ring_s.getTargets()) {
                ring_u = ShortestPathTreeProcessor.getInstance().
                        computeShortestPathTree(vertexU, categories, length/3, eps);
                weigthedLenghtSU = ring_s.getWeithedLength(vertexU);

                // compute intersection
                intersect = Sets.intersection(ring_s.getTargets(), ring_u.getTargets());

                // of the intersection, only consider the ones having greater weighted length
                // then vertex_u
                for (Vertex vertexV : intersect) {
                    weightedLengthUVS = ring_u.getWeithedLength(vertexV) + ring_s.getWeithedLength(vertexV);
                    if (ring_s.getWeithedLength(vertexU) <= ring_s.getWeithedLength(vertexV)) {
                        if (currentBestU == null ||
                            weigthedLenghtSU + weightedLengthUVS < bestTotalWeightedLength) {
                            currentBestU = vertexU;
                            currentBestV = vertexV;
                            currentRouteUV = ring_u.getRoute(vertexV);
                            bestTotalWeightedLength = weigthedLenghtSU + weightedLengthUVS;
                        }
                    }
                }
            }

            if (currentBestU == null || currentBestU == null || currentRouteUV == null)
                throw new NoRoundtripExistsException("no roundtrip exists");

            List<Vertex> roundtrip = new LinkedList<Vertex>();
            roundtrip.addAll(ring_s.getRoute(currentBestU));
            roundtrip.addAll(currentRouteUV);
            roundtrip.addAll(reverseList(ring_s.getRoute(currentBestV)));

            return roundtrip;
        }
    }


    /**
     * Reverses a list of Vertices.
     *
     * @param list List of Vertices to be reversed.
     * @return List of Vertices.
     */
    private List<Vertex> reverseList(List<Vertex> list) {
        List<Vertex> invertedList = new LinkedList<Vertex>();
        for (int i = list.size() - 1; i >= 0; i--) {
            invertedList.add(list.get(i));
        }
        return invertedList;
    }

}
