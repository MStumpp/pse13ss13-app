package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ShortestPathProcessor.class);


    /**
     * ShortestPathProcessor instance.
     */
    private static ShortestPathProcessor instance;


    /**
     * ExecutorService.
     */
    private ExecutorService executor;


    /**
     * ID counter for ShortestPathComputer.
     */
    private int idCounter;


    /**
     * Creates an instance of ShortestPathProcessor.
     *
     * @param graphDataIO GraphDataIO used for shortest path computation.
     * @param numberThreads The number of threads for parallel computation.
     */
    private ShortestPathProcessor(GraphDataIO graphDataIO, int numberThreads)
            throws EmptyListOfEdgesException {
        ArrayDeque<ShortestPathComputer> shortestPathComputerQueue =
                new ArrayDeque<ShortestPathComputer>();
        idCounter = 0;
        for (int t=0; t<numberThreads; t++) {
            ShortestPathComputer computer = new ShortestPathComputer(idCounter, new Graph(graphDataIO));
            shortestPathComputerQueue.add(computer);
            logger.info("Instantiated ShortestPathComputer: " + computer.getId());
            idCounter++;
        }

        executor = new ThreadPoolExecutorCustom(numberThreads, numberThreads, 1,
                TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryCustom(shortestPathComputerQueue), shortestPathComputerQueue);
    }


    /**
     * Instantiates and/or returns a singleton instance of ShortestPathProcessor.
     *
     * @return ShortestPathProcessor.
     * @throws InstantiationException If not instantiated before.
     */
    public static ShortestPathProcessor getInstance() throws InstantiationException {
        if (instance == null)
            throw new InstantiationException("singleton must be initialized first");
        return instance;
    }


    /**
     * Instantiates and returns a singleton instance of ShortestPathProcessor.
     *
     * @param graphDataIO GraphDataIO used for shortest path computation.
     * @return ShortestPathProcessor.
     * @throws IllegalArgumentException If some input parameter are missing.
     * @throws EmptyListOfEdgesException If there are no edges.
     */
    public static ShortestPathProcessor init(GraphDataIO graphDataIO)
            throws IllegalArgumentException, EmptyListOfEdgesException {
        return init(graphDataIO, 1);
    }


    /**
     * Instantiates and returns a singleton instance of ShortestPathProcessor.
     *
     * @param graphDataIO GraphDataIO used for shortest path computation.
     * @param numberThreads The number of threads for parallel computation.
     * @return ShortestPathProcessor.
     * @throws IllegalArgumentException If some input parameter are missing.
     * @throws EmptyListOfEdgesException If there are no edges.
     */
    public static ShortestPathProcessor init(GraphDataIO graphDataIO, int numberThreads)
            throws IllegalArgumentException, EmptyListOfEdgesException {
        if (graphDataIO == null)
            throw new IllegalArgumentException("GraphDataIO must be provided");
        if (numberThreads < 1)
            throw new IllegalArgumentException("Number of threads must be greater or equal to 1");
        if (instance != null)
            throw new IllegalArgumentException("ShortestPathProcessor already initialized");
        instance = new ShortestPathProcessor(graphDataIO, numberThreads);
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
        final ArrayDeque<ShortestPathComputer> shortestPathComputerQueue;

        ThreadFactoryCustom(ArrayDeque<ShortestPathComputer> shortestPathComputerQueue) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null)? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
            this.shortestPathComputerQueue = shortestPathComputerQueue;
        }

        @Override
        public Thread newThread(Runnable r) {
            if (shortestPathComputerQueue.isEmpty())
                return null;
            Thread t = new ThreadCustom(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0, shortestPathComputerQueue.getFirst());
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

        private ShortestPathComputer computer;

        ThreadCustom(ThreadGroup group, Runnable target, String name, long stackSize, ShortestPathComputer computer) {
            super(group, target, name, stackSize);
            this.computer = computer;
            logger.info("New Thread: " + name);
        }

        private ShortestPathComputer getComputer() {
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

        private ArrayDeque<ShortestPathComputer> shortestPathComputerQueue;

        public ThreadPoolExecutorCustom(int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactoryCustom threadFactory,
            ArrayDeque<ShortestPathComputer> shortestPathComputerQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
            this.shortestPathComputerQueue = shortestPathComputerQueue;
        }

        protected void afterExecute(Runnable r, Throwable t) {
            ShortestPathComputer computer =
                    ((ThreadCustom) Thread.currentThread()).getComputer();
            logger.info("afterExecute(): Thread: " + Thread.currentThread().getName() + " Reference: " +
                    Thread.currentThread().getClass().getName() + "@" + Integer.toHexString(Thread.currentThread().hashCode()) +
                    " and ShortestPathComputer: " + computer.getId() + " Reference: "
                    + computer.getClass().getName() + "@" + Integer.toHexString(computer.hashCode()));
            if (computer == null)
                logger.info("ShortestPathComputer is null in ThreadPoolExecutorCustom");
            //shortestPathComputerQueue.add(computer);
        }
    }


    /**
     * Computes a shortest path between any given two Coordinates using the provided
     * graph.
     *
     * @param source Source of the route to be computed.
     * @param target Target of the route to be computed.
     * @return RouteInfoTransfer.
     * @throws IllegalArgumentException Some input parameter is missing.
     * @throws ShortestPathComputationNoSlotsException If no slots available for computing shortest path.
     * @throws NoShortestPathExistsException If no shortest path between given Coordinates exists.
     * @throws ShortestPathComputeException If something during computation goes wrong.
     */
    public List<Vertex> computeShortestPath(Vertex source,
                                            Vertex target)
            throws NoShortestPathExistsException, ShortestPathComputeException,
            ShortestPathComputationNoSlotsException {

        if (source == null || target == null)
            throw new IllegalArgumentException("source and/or target must not be null");

        logger.info("computeShortestPath: Source: " + source.toString() + " Target: " + target.toString());
        Future<List<Vertex>> future = executor.submit(new ShortestPathTask(source, target));

        if (future.isCancelled())
            throw new ShortestPathComputationNoSlotsException("no slots available");

//        if (!future.isDone())
//            throw new ShortestPathComputeException("something went wrong internally");

        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new ShortestPathComputeException(e.getMessage());
        } catch (ExecutionException e) {
            throw new ShortestPathComputeException(e.getMessage());
        }
    }


    /**
     * Represents a task for shortest path computation.
     */
    private class ShortestPathTask implements Callable<List<Vertex>> {

        /**
         * Logger.
         */
        private final Logger logger = LoggerFactory.getLogger(ShortestPathTask.class);

        private Vertex source;
        private Vertex target;

        ShortestPathTask(Vertex source, Vertex target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public List<Vertex> call() throws Exception {
            ShortestPathComputer computer =
                    ((ThreadCustom) Thread.currentThread()).getComputer();
            logger.info("call(): Thread: " + Thread.currentThread().getName() + " Reference: " +
                    Thread.currentThread().getClass().getName() + "@" + Integer.toHexString(Thread.currentThread().hashCode()) +
                    " and ShortestPathComputer: " + computer.getId() + " Reference: "
                    + computer.getClass().getName() + "@" + Integer.toHexString(computer.hashCode()));
            if (computer == null)
                throw new ShortestPathComputeException(
                        "ShortestPathComputer is null in ShortestPathTask");
            return computer.computeShortestPath(source, target);
        }
    }


    /**
     * This class actually computes the shortest path.
     */
    private class ShortestPathComputer {

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
         * ShortestPathWorker.
         */
        ShortestPathComputer(int id, Graph graphIO) {
            this.id = id;
            graph = graphIO;
            runCounter = 0;
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
         * Computes a shortest path between any given two Coordinates using the provided
         * graph.
         *
         * @param source Source of the route to be computed.
         * @param target Target of the route to be computed.
         * @throws ShortestPathComputeException If something during computation goes wrong.
         */
        public List<Vertex> computeShortestPath(Vertex source, Vertex target)
                throws ShortestPathComputeException, NoShortestPathExistsException {

            if (source == null || target == null)
                throw new IllegalArgumentException("source and target must be provided");

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

            long startTime = System.currentTimeMillis();

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
                    currentHead = edge.getOtherVertex(current);
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

        private int getId() {
            return id;
        }
    }

}
