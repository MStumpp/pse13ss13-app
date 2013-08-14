package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;



import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable;

/**
 * GeometryProcessor provides some api to query nearest Edges and Vertices based on Coordinates.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryProcessor {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(GeometryProcessor.class);


    /**
     * TAG.
     */
    private static final String TAG = "GeometryProcessor";


    /**
     * MAX_NUMBER_CALLS.
     */
    private final static int MAX_NUMBER_CALLS = 500;


    /**
     * GeometryProcessor instance.
     */
    private static GeometryProcessor instance;


    /**
     * ExecutorService.
     */
    private ExecutorService executor;


    /**
     * ID counter for GeometryComputer.
     */
    private int idCounter;


    /**
     * Creates an instance of GeometryProcessor.
     *
     * @param geometryDataIO GeometryDataIO used for geometry computation.
     * @param numberThreads The number of threads for parallel computation.
     */
    private GeometryProcessor(GeometryDataIO geometryDataIO, int numberThreads) {
        ArrayDeque<GeometryComputer> geometryComputerQueue =
                new ArrayDeque<GeometryComputer>();
        idCounter = 0;
        for (int t=0; t<numberThreads; t++) {
            GeometryComputer computer = new GeometryComputer(idCounter,
                    geometryDataIO.getRoot(), geometryDataIO.getNumDimensions());
            geometryComputerQueue.add(computer);
            logger.info("Instantiated GeometryComputer: " + computer.getId());
            idCounter++;
        }

        executor = new ThreadPoolExecutorCustom(numberThreads, numberThreads, 1,
                TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryCustom(geometryComputerQueue), geometryComputerQueue);

    }


    /**
     * Returns a singleton instance of GeometryProcessor if available.
     *
     * @return GeometryProcessor.
     * @throws InstantiationException If not instantiated
     */
    public static GeometryProcessor getInstance() throws InstantiationException {
        if (instance == null)
            throw new InstantiationException("singleton must be initialized first");
        return instance;
    }


    /**
     * Instantiates and returns a singleton instance of GeometryProcessor.
     *
     * @param geometryDataIO GeometryDataIO containing relevant data.
     * @return GeometryProcessor.
     * @throws IllegalArgumentException If some input parameter are missing.
     */
    public static GeometryProcessor init(GeometryDataIO geometryDataIO)
            throws IllegalArgumentException {
        return init(geometryDataIO, 1);
    }


    /**
     * Instantiates and returns a singleton instance of GeometryProcessor.
     *
     * @param geometryDataIO GeometryDataIO containing relevant data.
     * @param numberThreads The number of threads for parallel computation.
     * @return GeometryProcessor.
     * @throws IllegalArgumentException If some input parameter are missing.
     */
    public static GeometryProcessor init(GeometryDataIO geometryDataIO, int numberThreads)
            throws IllegalArgumentException {
        if (geometryDataIO == null)
            throw new IllegalArgumentException("GeometryDataIO must be provided");
        if (numberThreads < 1)
            throw new IllegalArgumentException("Number of threads must be greater or equal to 1");
        if (instance != null)
            throw new IllegalArgumentException("GeometryProcessor already initialized");
        instance = new GeometryProcessor(geometryDataIO, numberThreads);
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
        final ArrayDeque<GeometryComputer> geometryComputerQueue;

        ThreadFactoryCustom(ArrayDeque<GeometryComputer> geometryComputerQueue) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null)? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
            this.geometryComputerQueue = geometryComputerQueue;
        }

        @Override
        public Thread newThread(Runnable r) {
            if (geometryComputerQueue.isEmpty())
                return null;
            Thread t = new ThreadCustom(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0, geometryComputerQueue.getFirst());
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

        private GeometryComputer computer;

        ThreadCustom(ThreadGroup group, Runnable target, String name, long stackSize, GeometryComputer computer) {
            super(group, target, name, stackSize);
            this.computer = computer;
            logger.info("New Thread: " + name);
        }

        private GeometryComputer getComputer() {
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

        private ArrayDeque<GeometryComputer> geometryComputerQueue;

        public ThreadPoolExecutorCustom(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                        TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactoryCustom threadFactory,
                                        ArrayDeque<GeometryComputer> geometryComputerQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
            this.geometryComputerQueue = geometryComputerQueue;
        }

        protected void afterExecute(Runnable r, Throwable t) {
            GeometryComputer computer =
                    ((ThreadCustom) Thread.currentThread()).getComputer();
            logger.info("afterExecute(): Thread: " + Thread.currentThread().getName() + " Reference: " +
                    Thread.currentThread().getClass().getName() + "@" + Integer.toHexString(Thread.currentThread().hashCode()) +
                    " and GeometryComputer: " + computer.getId() + " Reference: "
                    + computer.getClass().getName() + "@" + Integer.toHexString(computer.hashCode()));
            if (computer == null)
                logger.info("GeometryComputer is null in ThreadPoolExecutorCustom");
            //geometryComputerQueue.add(computer);
        }
    }


    /**
     * Computes the nearest Edge for a given Geometrizable.
     *
     * @param geometrizable The Geometrizable the resulting Edge has to be closest to.
     * @return Geometrizable Nearest Geometrizable Edge.
     * @throws GeometryProcessorException If something goes wrong.
     * @throws IllegalArgumentException If geometrizable is null.
     */
    public Geometrizable getNearestEdge(Geometrizable geometrizable)
            throws GeometryProcessorException {
        if (geometrizable == null)
            throw new IllegalArgumentException("geometrizable must not be null");

        return null;
    }


    /**
     * Computes the nearest Geometrizable for a given Geometrizable.
     *
     * @param geometrizable The Geometrizable the resulting Vertex has to be closest to.
     * @return Geometrizable Nearest Geometrizable Vertex.
     * @throws GeometryProcessorException If something goes wrong.
     * @throws IllegalArgumentException If geometrizable is null.
     */
    public Geometrizable getNearestVertex(Geometrizable geometrizable)
            throws GeometryProcessorException, GeometryComputationNoSlotsException,
            IllegalArgumentException {
        if (geometrizable == null)
            throw new IllegalArgumentException("geometrizable must not be null");

        logger.info("getNearestVertex:");
        Future<Geometrizable> future = executor.submit(new GeometryNearestVertexTask(geometrizable));

        if (future.isCancelled())
            throw new GeometryComputationNoSlotsException("no slots available");

        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new GeometryProcessorException(e.getMessage());
        } catch (ExecutionException e) {
            throw new GeometryProcessorException(e.getMessage());
        }
    }


    /**
     * Represents a task for nearest vertex computation.
     */
    private class GeometryNearestVertexTask implements Callable<Geometrizable> {

        /**
         * Logger.
         */
        private final Logger logger = LoggerFactory.getLogger(GeometryNearestVertexTask.class);

        private Geometrizable geometrizable;

        GeometryNearestVertexTask(Geometrizable geometrizable) {
            this.geometrizable = geometrizable;
        }

        @Override
        public Geometrizable call() throws Exception {
            GeometryComputer computer =
                    ((ThreadCustom) Thread.currentThread()).getComputer();
            logger.info("call(): Thread: " + Thread.currentThread().getName() + " Reference: " +
                    Thread.currentThread().getClass().getName() + "@" + Integer.toHexString(Thread.currentThread().hashCode()) +
                    " and GeometryComputer: " + computer.getId() + " Reference: "
                    + computer.getClass().getName() + "@" + Integer.toHexString(computer.hashCode()));
            if (computer == null)
                throw new GeometryProcessorException(
                        "GeometryComputer is null in GeometryNearestVertexTask");
            return computer.getNearestVertex(geometrizable);
        }
    }


    /**
     * Computes the distance between an Edge and the nearest POI of a certain Category.
     *
     * @param edge ID of the Edge.
     * @param category ID of the Category of the POI.
     * @return float Distance.
     */
    public float getDistanceToNearestPOIForEdge(int edge, int category) {
        return 1.f;
    }


    /**
     * Computes whether and Edge is contained in an Area of a certain Category..
     *
     * @param edge ID of the Edge.
     * @param category ID of the Category of the Area.
     * @return boolean True if contained, false otherwise.
     */
    public boolean isEdgeInArea(int edge, int category) {
        return false;
    }


    /**
     * GeometryComputer.
     */
    private class GeometryComputer {

        /**
         * id.
         */
        private int id;


        /**
         * Graph instance.
         */
        private GeometryNode root;


        /**
         * Number dimensions.
         */
        private int numberDimensions;


        /**
         * NumberMethodCalls.
         */
        private int numberMethodCalls;


        /**
         * Stop flag.
         */
        private boolean stop = false;


        /**
         * GeometryComputer.
         */
        GeometryComputer(int id, GeometryNode root, int numberDimensions) {
            this.id = id;
            this.root = root;
            this.numberDimensions = numberDimensions;
            numberMethodCalls = 0;
        }


        /**
         * Computes the nearest Geometrizable for a given Geometrizable.
         *
         * @param geometrizable The Geometrizable the resulting Vertex has to be closest to.
         * @return Geometrizable Nearest Geometrizable Vertex.
         * @throws GeometryProcessorException If something goes wrong.
         */
        public Geometrizable getNearestVertex(Geometrizable geometrizable)
                throws GeometryProcessorException {

            long startTime = System.currentTimeMillis();

            Geometrizable nearestVertex = search(root, geometrizable);

            long stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;

            logger.info("getNearestVertex: Start: Run time: " + runTime);

            return nearestVertex;
        }


        /**
         * Computes whether and Edge is contained in an Area of a certain Category..
         *
         * @param root Root node of search tree.
         * @param search Geometrizable a projected Geometrizable to look for.
         * @return Geometrizable Projected Geometrizable.
         * @throws GeometryProcessorException If something goes wrong.
         */
        private Geometrizable search(GeometryNode root, Geometrizable search)
                throws GeometryProcessorException {
            numberMethodCalls = 0;
            stop = false;
            GeometrizableWrapper wrapper = new GeometrizableWrapper(null);
            searchTreeDown(root, search, wrapper);
            return wrapper.getGeometrizable();
        }


        /**
         * Computes whether and Edge is contained in an Area of a certain Category..
         *
         * @param node Current node to be processed while searching down the tree.
         * @param search Geometrizable a projected Geometrizable to look for.
         * @param geometrizable Current best Geometrizable.
         * @throws GeometryProcessorException If something goes wrong.
         */
        private void searchTreeDown(GeometryNode node, Geometrizable search,
                                    GeometrizableWrapper geometrizable)
                throws GeometryProcessorException {

            if (stop || numberMethodCalls > MAX_NUMBER_CALLS) {
                stop = true;
                return;
            }
            numberMethodCalls++;

            // compute dim
            //logger.info("geometryDataIO.getNumDimensions(): " + geometryDataIO.getNumDimensions());
            int dim = node.getDepth() % numberDimensions;
            //logger.info("dim: " + dim);

            // if leaf, then check whether vertex of this node is better then current best
            // traverse up the tree
            if (node.isLeaf()) {
                if (node.getGeometrizable() != null) {
                    if (geometrizable.getGeometrizable() == null)
                        geometrizable.setGeometrizable(node.getGeometrizable());
                    else
                    if (node.getGeometrizable().valueForDimension(dim) <
                            geometrizable.getGeometrizable().valueForDimension(dim))
                        geometrizable.setGeometrizable(node.getGeometrizable());
                }
                searchTreeUp(node.getParent(), search, geometrizable, node);

                // otherwise, traverse further down the tree
            } else {

                // either further visit left or right child
                // here we check for less or equal
                if (search.valueForDimension(dim) <= node.getSplitValue())
                    searchTreeDown(node.getLeftNode(), search, geometrizable);
                else
                    searchTreeDown(node.getRightNode(), search, geometrizable);
            }

            return;
        }


        /**
         * Computes whether and Edge is contained in an Area of a certain Category..
         *
         * @param node Current node to be processed while searching down the tree.
         * @param search Geometrizable a projected Geometrizable to look for.
         * @param geometrizable Current best Geometrizable.
         * @param child Previously visited node while searching up the tree.
         * @throws GeometryProcessorException If something goes wrong.
         */
        private void searchTreeUp(GeometryNode node, Geometrizable search,
                                  GeometrizableWrapper geometrizable, GeometryNode child)
                throws GeometryProcessorException {

            if (stop || numberMethodCalls > MAX_NUMBER_CALLS) {
                stop = true;
                return;
            }
            numberMethodCalls++;

            // compute dim
            int dim = node.getDepth() % numberDimensions;

            // distance between the splitting coordinate of search point and current node
            if (node.getSplitValue() == Double.NaN)
                throw new GeometryProcessorException("searchTreeUp: split value is NaN");
            double distSearchAndCurrentNode = Math.abs(search.valueForDimension(dim) -
                    node.getSplitValue());

            // distance between the splitting coordinate of search point end current best
            double distSearchAndCurrentBest = Math.abs(search.valueForDimension(dim) -
                    geometrizable.getGeometrizable().valueForDimension(dim));

            if (distSearchAndCurrentNode < distSearchAndCurrentBest)
                if (child == node.getLeftNode())
                    searchTreeDown(node.getRightNode(), search, geometrizable);
                else
                    searchTreeDown(node.getLeftNode(), search, geometrizable);

            if (!node.isRoot())
                searchTreeUp(node.getParent(), search, geometrizable, node);

            return;
        }


        private int getId() {
            return id;
        }
    }


    /**
     * A wrapper class the keep the current best Geometrizable.
     */
    private class GeometrizableWrapper {

        private Geometrizable geometrizable;

        GeometrizableWrapper (Geometrizable geometrizable) {
            this.geometrizable = geometrizable;
        }

        Geometrizable getGeometrizable() {
            return geometrizable;
        }

        void setGeometrizable(Geometrizable geometrizable) {
            this.geometrizable = geometrizable;
        }
    }

}
