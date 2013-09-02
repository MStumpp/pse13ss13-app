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
     * Initial Epsilon.
     */
    private final static double INITIAL_EPS = 0.001;


    /**
     * Maximal Epsilon.
     */
    private final static double MAXIMAL_EPS = 0.2;


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

            long startTime = System.currentTimeMillis();

            List<Vertex> roundtrip = new LinkedList<Vertex>();

            double currentEPS = INITIAL_EPS;
            int run = 1;

            while (roundtrip.isEmpty()) {

                // get the initial set of Vertices
                RouteSet ring_s = ShortestPathTreeProcessor.getInstance().computeShortestPathTree(source, categories, length/3, currentEPS, null);

                logger.info("ring_s: " + ring_s.getTargets().size());

                // for all Vertices
                RouteSet ring_u;
                Set<Vertex> intersect;

                Vertex currentBestU = null, currentBestV = null;
                List<Vertex> currentRouteUV = null;
                double weigthedLenghtSU, weightedLengthUVS, badLowerBound, badBestRoute = Double.POSITIVE_INFINITY, bestTotalWeightedLength = Double.POSITIVE_INFINITY;

                for (Vertex vertexU : ring_s.getTargets()) {
                    // stopping criterion #2 (route edges)
                    ring_u = ShortestPathTreeProcessor.getInstance().computeShortestPathTree(vertexU, categories, length/3, currentEPS, ring_s.getRouteEdges(vertexU));

                    logger.info("ring_u of size: " + ring_u.getTargets().size() + " for target: " + vertexU);

                    weigthedLenghtSU = ring_s.getWeigthedLength(vertexU);

                    // stopping criterion #1
                    badLowerBound = (2*weigthedLenghtSU)/((1+currentEPS)*length);
                    if (badLowerBound > badBestRoute)
                        break;

                    // compute intersection
                    intersect = Sets.intersection(ring_s.getTargets(), ring_u.getTargets());

                    logger.info("intersect of size: " + intersect.size());

                    // of the intersection, only consider the ones having greater weighted length
                    // then vertex_u
                    for (Vertex vertexV : intersect) {

                        // skip if Puv and Pvs share at least one Edge
                        // stopping criterion #2
                        if (!Sets.intersection(ring_u.getRouteEdges(vertexV), ring_s.getRouteEdges(vertexV)).isEmpty()) {
                            continue;
                        }

                        weightedLengthUVS = ring_u.getWeigthedLength(vertexV) + ring_s.getWeigthedLength(vertexV);
                        // stopping criterion #1
                        if (ring_s.getWeigthedLength(vertexU) <= ring_s.getWeigthedLength(vertexV)) {
                            if (currentBestU == null || (weigthedLenghtSU + weightedLengthUVS < bestTotalWeightedLength)) {
                                currentBestU = vertexU;
                                currentBestV = vertexV;
                                currentRouteUV = ring_u.getRouteVertices(vertexV);
                                badBestRoute = badLowerBound;
                                bestTotalWeightedLength = weigthedLenghtSU + weightedLengthUVS;
                            }
                        }
                    }
                }

                // if passed, at least 3 Vertices exist
                if (currentBestU == null || currentBestV == null || currentRouteUV == null) {
                    logger.info("no roundtrip found");

                    run++;
                    currentEPS *= run;
                    if (currentEPS > MAXIMAL_EPS) {
                        logger.info("updated eps (" + currentEPS + ") is greater than maximal eps (" + MAXIMAL_EPS + "), no roundtrip found");
                        break;
                    } else {
                        logger.info("updated eps (" + currentEPS + ") is smaller or equal than maximal eps (" + MAXIMAL_EPS + "), computing roundtrip...");
                        continue;
                    }
                }

                roundtrip.addAll(ring_s.getRouteVertices(currentBestU));
                roundtrip.addAll(currentRouteUV);
                roundtrip.addAll(RouteUtil.reverseList(ring_s.getRouteVertices(currentBestV)));
            }

            long stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;

            if (!roundtrip.isEmpty()) {
                logger.info("==============================================================");
                logger.info("computeRoundtrip: Source: " + roundtrip.get(0) + " Target: " + roundtrip.get(roundtrip.size()-1) +
                        " Length: " + RouteUtil.totalLength(roundtrip) + " Roundtrip Size: " + roundtrip.size() + " Run time: " + runTime);
                logger.info("==============================================================");
                return roundtrip;

            } else {
                throw new NoRoundtripExistsException("no roundtrip exists");
            }
        }
    }

}
