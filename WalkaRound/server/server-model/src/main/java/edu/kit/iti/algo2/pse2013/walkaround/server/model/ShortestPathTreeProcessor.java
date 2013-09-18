package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.*;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ShortestPathTreeProcessor.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class ShortestPathTreeProcessor {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ShortestPathTreeProcessor.class);


    /**
     * ShortestPathTreeProcessor instance.
     */
    private static ShortestPathTreeProcessor instance;


    /**
     * ExecutorService.
     */
    private ThreadPoolExecutor executor;


    /**
     * Creates an instance of ShortestPathTreeProcessor.
     *
     * @param graphDataIO GraphDataIO used for shortest path computation.
     * @param numberThreads The number of threads for parallel computation.
     */
    private ShortestPathTreeProcessor(GraphDataIO graphDataIO, int numberThreads)
            throws EmptyListOfEdgesException {
        ArrayDeque<ShortestPathTreeComputer> shortestPathTreeComputerQueue =
                new ArrayDeque<ShortestPathTreeComputer>();
        int idCounter = 0;
        for (int t=0; t<numberThreads; t++) {
            ShortestPathTreeComputer computer = new ShortestPathTreeComputer(idCounter, new Graph(graphDataIO));
            shortestPathTreeComputerQueue.add(computer);
            logger.info("Instantiated ShortestPathTreeComputer: " + computer.getId());
            idCounter++;
        }

        executor = new ThreadPoolExecutorCustom(numberThreads, numberThreads, 9999,
                TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryCustom(shortestPathTreeComputerQueue), shortestPathTreeComputerQueue);
        // avoid termination of core threads
        executor.allowCoreThreadTimeOut(false);
    }


    /**
     * Instantiates and/or returns a singleton instance of ShortestPathTreeProcessor.
     *
     * @return ShortestPathTreeProcessor.
     * @throws InstantiationException If not instantiated before.
     */
    public static ShortestPathTreeProcessor getInstance() throws InstantiationException {
        if (instance == null)
            throw new InstantiationException("singleton must be initialized first");
        return instance;
    }


    /**
     * Instantiates and returns a singleton instance of ShortestPathTreeProcessor.
     *
     * @param graphDataIO Graph used for shortest path tree computation.
     * @param numberThreads The number of threads for parallel computation.
     * @return ShortestPathTreeProcessor.
     */
    public static ShortestPathTreeProcessor init(GraphDataIO graphDataIO, int numberThreads)
            throws EmptyListOfEdgesException {
        if (graphDataIO == null)
            throw new IllegalArgumentException("Graph must be provided");
        if (numberThreads < 1)
            throw new IllegalArgumentException("Number of threads must be greater or equal to 1");
        if (instance != null)
            throw new IllegalArgumentException("ShortestPathTreeProcessor already initialized");
        instance = new ShortestPathTreeProcessor(graphDataIO, numberThreads);
        return instance;
    }


    /**
     * ThreadFactoryCustom.
     */
    private class ThreadFactoryCustom implements ThreadFactory {

        /**
         * Logger.
         */
        private final Logger logger = LoggerFactory.getLogger(ThreadFactoryCustom.class);

        final AtomicInteger poolNumber = new AtomicInteger(1);
        final ThreadGroup group;
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix;
        final ArrayDeque<ShortestPathTreeComputer> shortestPathTreeComputerQueue;

        ThreadFactoryCustom(ArrayDeque<ShortestPathTreeComputer> shortestPathTreeComputerQueue) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null)? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
            this.shortestPathTreeComputerQueue = shortestPathTreeComputerQueue;
        }

        @Override
        public Thread newThread(Runnable r) {
            if (shortestPathTreeComputerQueue.isEmpty())
                return null;
            Thread t = new ThreadCustom(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0, shortestPathTreeComputerQueue.pollFirst());
            logger.info("New Thread: " + t.getName() + " Reference: " +
                    t.getClass().getName() + "@" + Integer.toHexString(t.hashCode()));
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);

            return t;
        }
    }


    /**
     * ThreadCustom.
     */
    private class ThreadCustom extends Thread {

        /**
         * Logger.
         */
        private final Logger logger = LoggerFactory.getLogger(ThreadCustom.class);

        private ShortestPathTreeComputer computer;

        ThreadCustom(ThreadGroup group, Runnable target, String name, long stackSize, ShortestPathTreeComputer computer) {
            super(group, target, name, stackSize);
            this.computer = computer;
            logger.info("New Thread: " + name);
        }

        private ShortestPathTreeComputer getComputer() {
            return computer;
        }
    }


    /**
     * ThreadPoolExecutorCustom.
     */
    private class ThreadPoolExecutorCustom extends ThreadPoolExecutor {

        /**
         * Logger.
         */
        private final Logger logger = LoggerFactory.getLogger(ThreadPoolExecutorCustom.class);

        private ArrayDeque<ShortestPathTreeComputer> shortestPathTreeComputerQueue;

        public ThreadPoolExecutorCustom(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                        TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactoryCustom threadFactory,
                                        ArrayDeque<ShortestPathTreeComputer> shortestPathTreeComputerQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
            this.shortestPathTreeComputerQueue = shortestPathTreeComputerQueue;
        }

        protected void afterExecute(Runnable r, Throwable t) {
            ShortestPathTreeComputer computer =
                    ((ThreadCustom) Thread.currentThread()).getComputer();
            logger.info("afterExecute(): Thread: " + Thread.currentThread().getName() + " Reference: " +
                    Thread.currentThread().getClass().getName() + "@" + Integer.toHexString(Thread.currentThread().hashCode()) +
                    " and ShortestPathTreeComputer: " + computer.getId() + " Reference: "
                    + computer.getClass().getName() + "@" + Integer.toHexString(computer.hashCode()));
            if (computer == null)
                logger.info("ShortestPathTreeComputer is null in ThreadPoolExecutorCustom");
            //shortestPathTreeComputerQueue.add(computer);
        }
    }


    /**
     * Computes a set of Vertices having a distance from source in interval
     * (1-eps)*length and (1+eps)*length, ordered by some weighted distance
     * based on a set of categories.
     *
     * @param source The starting Coordinate of the roundtrip to be computed.
     * @param categories The categories the badness to be computed for.
     * @param length The length of the roundtrip in meter to be computed.
     * @return RouteSet.
     * @throws ShortestPathTreeComputationNoSlotsException If no slot is available.
     * @throws ShortestPathTreeComputeException If something goes wrong internally.
     */
    public RouteSet computeShortestPathTree(Vertex source, int[] categories, int length, double eps, Set<Edge> forbiddenEdges)
            throws ShortestPathTreeComputationNoSlotsException, ShortestPathTreeComputeException {

        logger.info("computeShortestPathTree: Source: " + source.toString() + " Categories: " + categories.toString());
        Future<RouteSet> future = executor.submit(new ShortestPathTreeTask(source, categories, length, eps, forbiddenEdges));

        if (future.isCancelled())
            throw new ShortestPathTreeComputationNoSlotsException("no slots available");

        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new ShortestPathTreeComputeException(e.getMessage());
        } catch (ExecutionException e) {
            throw new ShortestPathTreeComputeException(e.getMessage());
        }
    }


    /**
     * Represents a task for shortest path tree computation.
     */
    private class ShortestPathTreeTask implements Callable<RouteSet> {

        /**
         * Logger.
         */
        private final Logger logger = LoggerFactory.getLogger(ShortestPathTreeTask.class);

        private Vertex source;
        private int[] categories;
        private int length;
        private double eps;
        private Set<Edge> forbiddenEdges;

        ShortestPathTreeTask(Vertex source, int[] categories, int length, double eps, Set<Edge> forbiddenEdges) {
            this.source = source;
            this.categories = categories;
            this.length = length;
            this.eps = eps;
            this.forbiddenEdges = forbiddenEdges;
        }

        @Override
        public RouteSet call() throws Exception {
            ShortestPathTreeComputer computer =
                    ((ThreadCustom) Thread.currentThread()).getComputer();
            logger.info("call(): Thread: " + Thread.currentThread().getName() + " Reference: " +
                    Thread.currentThread().getClass().getName() + "@" + Integer.toHexString(Thread.currentThread().hashCode()) +
                    " and ShortestPathTreeComputer: " + computer.getId() + " Reference: "
                    + computer.getClass().getName() + "@" + Integer.toHexString(computer.hashCode()));
            if (computer == null)
                throw new ShortestPathTreeComputeException(
                        "ShortestPathTreeComputer is null in ShortestPathTreeTask");
            return computer.computeShortestPathTree(source, categories, length, eps, forbiddenEdges);
        }
    }


    /**
     * This class actually computes the shortest path.
     */
    private class ShortestPathTreeComputer {

        /**
         * Logger.
         */
        private final Logger logger = LoggerFactory.getLogger(ShortestPathTreeComputer.class);


        /**
         * id.
         */
        private int id;


        /**
         * Graph instance.
         */
        private Graph graph;


        /**
         * Run counter.
         */
        private int runCounter;


        /**
         * Priority queue.
         */
        private PriorityQueue<Vertex> queue;


        /**
         * GeometryProcessorPOI.
         */
        private GeometryProcessorPOI proc;


        /**
         * NUMBER_WEIGHTED_LENGTH_REFERENCE.
         */
        private static final int NUMBER_WEIGHTED_LENGTH_REFERENCE = 1000;


        /**
         * ShortestPathTreeComputer.
         */
        ShortestPathTreeComputer(int id, Graph graphIO) {
            this.id = id;
            graph = graphIO;
            runCounter = 0;
            try {
                proc = GeometryProcessorPOI.getInstance();
                logger.info("GeometryProcessorPOI initialized");
            } catch (InstantiationException e) {
                logger.info("GeometryProcessorPOI with InstantiationException");
            }
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
        }


        /**
         * Computes a set of Vertices having a distance from source in interval
         * (1-eps)*length and (1+eps)*length, ordered by some weighted distance
         * based on a set of categories.
         *
         * @param source The starting Coordinate of the roundtrip to be computed.
         * @param categories The categories the badness to be computed for.
         * @param length The length of the roundtrip in meter to be computed.
         * @return A set of Vertices.
         * @throws ShortestPathTreeComputeException If something goes wrong internally.
         */
        public RouteSet computeShortestPathTree(Vertex source, int[] categories, int length, double eps, Set<Edge> forbiddenEdges)
                throws ShortestPathTreeComputeException {

            Vertex sourceVertex;
            try {
                sourceVertex = graph.getVertexByID(source.getID());
            } catch (NoVertexForIDExistsException e) {
                throw new ShortestPathTreeComputeException("source vertex provided not in graph contained");
            }

            if (sourceVertex == null)
                throw new ShortestPathTreeComputeException("source provided not in graph contained");

            // some init
            runCounter += 1;
            sourceVertex.setCurrentLength(0.d);
            queue.clear();
            queue.add(sourceVertex);
            GeometrizablePOIConstraint poiConstraint = new GeometrizablePOIConstraint(categories);

            // ring
            Set<Vertex> ring = new TreeSet<Vertex>(new Comparator<Vertex>() {
                @Override
                public int compare(Vertex v1, Vertex v2) {
                    if (v1.getCurrentWeightedLength() > v2.getCurrentWeightedLength()){
                        return 1;
                    } else if (v1.getCurrentWeightedLength() < v2.getCurrentWeightedLength()){
                        return -1;
                    } else
                        return 0;
                }
            });

            // update parent and currentLengths
            Vertex current, currentHead, indexZeroVertexOfEdge;
            double distance, weightedDistance, weightedLength, poiDistance;
            while (!queue.isEmpty()) {
                current = queue.poll();

                if (current.getRun() == runCounter && current.getCurrentLength() > (1+eps)*length)
                    continue;

                for (Edge edge : current.getOutgoingEdges()) {

                    // check if edge is contained in forbidden edges
                    if (forbiddenEdges != null && forbiddenEdges.contains(edge))
                        continue;

                    currentHead = edge.getOtherVertex(current);
                    distance = current.getCurrentLength() + edge.getLength();

                    indexZeroVertexOfEdge = edge.getVertices().get(0);
                    try {
                        if (proc != null && poiConstraint != null) {
                            Vertex nearestPOI = (Vertex) proc.getNearestGeometrizable(new GeometrySearch(new double[]{indexZeroVertexOfEdge.getLatitude(), indexZeroVertexOfEdge.getLongitude()}), poiConstraint);
                            poiDistance = RouteUtil.computeDistance(indexZeroVertexOfEdge.getLatitude(), indexZeroVertexOfEdge.getLongitude(), nearestPOI.getLatitude(), nearestPOI.getLongitude());
                            weightedLength = Math.min(1, Math.max(0, poiDistance / NUMBER_WEIGHTED_LENGTH_REFERENCE)) * edge.getLength();
                        } else {
                            weightedLength = edge.getLength();
                        }

                    } catch (GeometryProcessorException e) {
                        logger.info("GeometryProcessorException");
                        weightedLength = edge.getLength();
                    } catch (GeometryComputationNoSlotsException e) {
                        logger.info("GeometryComputationNoSlotsException");
                        weightedLength = edge.getLength();
                    }

                    //weightedDistance = current.getCurrentWeightedLength() + edge.getWeightedLength(categories);
                    weightedDistance = current.getCurrentWeightedLength() + weightedLength;

                    // not yet visited during current run
                    if (currentHead.getRun() != runCounter) {
                        currentHead.setCurrentLength(distance);
                        currentHead.setCurrentWeightedLength(weightedDistance);
                        currentHead.setParent(current);
                        queue.add(currentHead);
                        currentHead.setRun(runCounter);

                        // edge head already in queue, eventually decrease key
                    } else if (weightedDistance < currentHead.getCurrentWeightedLength()) {
                        currentHead.setCurrentLength(distance);
                        currentHead.setCurrentWeightedLength(weightedDistance);
                        currentHead.setParent(current);
                        // remove head if its already in the queue, coz
                        // updating current length after added to queue doesn't
                        // lead to order change
                        queue.remove(currentHead);
                        queue.add(currentHead);
                    }
                }

                // check if the current vertex belongs to the ring,
                // add accordingly
                if (current.getCurrentLength() >= (1-eps)*length)
                    ring.add(current);
            }

            return new RouteSet(source, ring);
        }

        private int getId() {
            return id;
        }
    }

}
