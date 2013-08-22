package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final static int MAX_NUMBER_CALLS = 20000;


    /**
     * ExecutorService.
     */
    private ThreadPoolExecutor executor;


    /**
     * Creates an instance of GeometryProcessor.
     *
     * @param geometryDataIO GeometryDataIO used for geometry computation.
     * @param numberThreads The number of threads for parallel computation.
     */
    public GeometryProcessor(GeometryDataIO geometryDataIO, int numberThreads) {
        ArrayDeque<GeometryComputer> geometryComputerQueue =
                new ArrayDeque<GeometryComputer>();
        int idCounter = 0;
        for (int t=0; t<numberThreads; t++) {
            GeometryComputer computer = new GeometryComputer(idCounter,
                    geometryDataIO.getRoot(), geometryDataIO.getNumDimensions());
            geometryComputerQueue.add(computer);
            logger.info("Instantiated GeometryComputer: " + computer.getId());
            idCounter++;
        }

        executor = new ThreadPoolExecutorCustom(numberThreads, numberThreads, 9999,
                TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryCustom(geometryComputerQueue), geometryComputerQueue);
        executor.allowCoreThreadTimeOut(false);
    }


    /**
     * Computes the nearest Geometrizable for a given Geometrizable.
     *
     * @param search The Geometrizable the resulting Vertex has to be closest to.
     * @return Geometrizable Nearest Geometrizable Vertex.
     * @throws GeometryProcessorException If something goes wrong.
     * @throws IllegalArgumentException If geometrizable is null.
     */
    public Geometrizable getNearestGeometrizable(GeometrySearch search)
            throws GeometryProcessorException, GeometryComputationNoSlotsException,
            IllegalArgumentException {
        return getNearestGeometrizable(search, null);
    }


    /**
     * Computes the nearest Geometrizable for a given Geometrizable.
     *
     * @param search The Geometrizable the resulting Vertex has to be closest to.
     * @return Geometrizable Nearest Geometrizable Vertex.
     * @throws GeometryProcessorException If something goes wrong.
     * @throws IllegalArgumentException If geometrizable is null.
     */
    public Geometrizable getNearestGeometrizable(GeometrySearch search, GeometrizableConstraint constraint)
            throws GeometryProcessorException, GeometryComputationNoSlotsException,
            IllegalArgumentException {
        if (search == null)
            throw new IllegalArgumentException("geometrizable must not be null");

        logger.info("getNearestGeometrizable:");
        Future<Geometrizable> future = executor.submit(new GeometryNearestGeometrizableTask(search, constraint));

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
                    0, geometryComputerQueue.pollFirst());
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
     * Represents a task for nearest vertex computation.
     */
    private class GeometryNearestGeometrizableTask implements Callable<Geometrizable> {

        /**
         * Logger.
         */
    	private final Logger logger = LoggerFactory.getLogger(GeometryNearestGeometrizableTask.class);

        private GeometrySearch search;

        private GeometrizableConstraint constraint;

        GeometryNearestGeometrizableTask(GeometrySearch search, GeometrizableConstraint constraint) {
            this.search = search;
            this.constraint = constraint;
        }

        @Override
        public Geometrizable call() throws Exception {
            GeometryComputer computer =
                    ((ThreadCustom) Thread.currentThread()).getComputer();
            logger.info("call(): Thread: " + Thread.currentThread().getName() + " Reference: " +
                    Thread.currentThread().getClass().getName() + "@" + Integer.toHexString(Thread.currentThread().hashCode()) +
                    " and GeometryComputer: " + computer.getId() + " Reference: "
                   + computer.getClass().getName() + "@" + Integer.toHexString(computer.hashCode()));
            if (computer == null) {
            	logger.info("GeometryComputer is null in GeometryNearestVertexTask");
                throw new GeometryProcessorException(
                        "GeometryComputer is null in GeometryNearestVertexTask");
            }
            return computer.getNearestGeometrizable(search, constraint);
        }
    }


    /**
     * GeometryComputer.
     */
    private class GeometryComputer {

        /**
         * Logger.
         */
    	private final Logger logger = LoggerFactory.getLogger(GeometryComputer.class);


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
         * @param search The Geometrizable the resulting Vertex has to be closest to.
         * @return Geometrizable Nearest Geometrizable Vertex.
         * @throws GeometryProcessorException If something goes wrong.
         */
        public Geometrizable getNearestGeometrizable(GeometrySearch search, GeometrizableConstraint constraint)
                throws GeometryProcessorException {

            long startTime = System.currentTimeMillis();

            Geometrizable nearestGeometrizable = search(root, search, constraint);

            long stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;

            logger.info("getNearestGeometrizable: Start: Run time: " + runTime);

            return nearestGeometrizable;
        }


        /**
         * Computes whether and Edge is contained in an Area of a certain Category..
         *
         * @param root Root node of search tree.
         * @param search Geometrizable a projected Geometrizable to look for.
         * @return Geometrizable Projected Geometrizable.
         * @throws GeometryProcessorException If something goes wrong.
         */
        private Geometrizable search(GeometryNode root, GeometrySearch search,
                                     GeometrizableConstraint constraint)
                throws GeometryProcessorException {
            numberMethodCalls = 0;
            stop = false;
            GeometrizableHolder holder = new GeometrizableHolder(null);
            searchTreeDown(root, search, constraint, holder);

            Geometrizable geometrizable = holder.getGeometrizable();
            if (geometrizable == null) {
                return null;
            } else {
                if (geometrizable instanceof GeometrizableWrapper) {
                    return ((GeometrizableWrapper) geometrizable).getGeometrizable();
                } else {
                    return geometrizable;
                }
            }
        }


        /**
         * Computes whether and Edge is contained in an Area of a certain Category..
         *
         * @param node Current node to be processed while searching down the tree.
         * @param search Geometrizable a projected Geometrizable to look for.
         * @param currentBest Current best Geometrizable.
         * @throws GeometryProcessorException If something goes wrong.
         */
        private void searchTreeDown(GeometryNode node, GeometrySearch search, GeometrizableConstraint constraint,
                                    GeometrizableHolder currentBest)
                throws GeometryProcessorException {

            if (stop || numberMethodCalls > MAX_NUMBER_CALLS) {
                stop = true;
                return;
            }
            numberMethodCalls++;

            // compute dim
            int dim = node.getDepth() % numberDimensions;

            // if leaf, then check whether vertex of this node is better then current best
            // traverse up the tree
            Geometrizable currentGeometrizable;
            if (node.isLeaf()) {

                if (currentBest.getGeometrizable() == null) {
                    currentGeometrizable = node.getNearestGeometrizable(search.valueForDimension(dim), constraint, dim);
                } else {
                    currentGeometrizable = node.getNearestGeometrizable(currentBest.getGeometrizable().valueForDimension(dim), constraint, dim);
                }

                if (currentGeometrizable != null) {
                    if (currentBest.getGeometrizable() == null) {
                        currentBest.setGeometrizable(currentGeometrizable);
                    } else if (currentBest.getGeometrizable() != null && currentGeometrizable.valueForDimension(dim) < currentBest.getGeometrizable().valueForDimension(dim) ) {
                        currentBest.setGeometrizable(currentGeometrizable);
                    }
                }

                if (!node.isRoot()) {
                    searchTreeUp(node.getParent(), search, constraint, currentBest, node);
                }

            // otherwise, traverse further down the tree
            } else {

                // either further visit left or right child
                // here we check for less or equal
                if (search.valueForDimension(dim) <= node.getSplitValue() && node.getLeftNode() != null) {
                    searchTreeDown(node.getLeftNode(), search, constraint, currentBest);
                } else if (node.getRightNode() != null) {
                    searchTreeDown(node.getRightNode(), search, constraint, currentBest);
                }
            }

            return;
        }


        /**
         * Computes whether and Edge is contained in an Area of a certain Category..
         *
         * @param node Current node to be processed while searching down the tree.
         * @param search Geometrizable a projected Geometrizable to look for.
         * @param currentBest Current best Geometrizable.
         * @param child Previously visited node while searching up the tree.
         * @throws GeometryProcessorException If something goes wrong.
         */
        private void searchTreeUp(GeometryNode node, GeometrySearch search, GeometrizableConstraint constraint,
                                  GeometrizableHolder currentBest, GeometryNode child)
                throws GeometryProcessorException {

            if (stop || numberMethodCalls > MAX_NUMBER_CALLS) {
                stop = true;
                return;
            }
            numberMethodCalls++;

            // compute dim
            int dim = node.getDepth() % numberDimensions;

            // distance between the splitting coordinate of search point and current node
            if (node.getSplitValue() == Double.NaN) {
                throw new GeometryProcessorException("searchTreeUp: split value is NaN");
            }
            double distSearchAndCurrentNode = Math.abs(search.valueForDimension(dim) - node.getSplitValue());

            // distance between the splitting coordinate of search point end current best
            double distSearchAndCurrentBest = Math.abs(search.valueForDimension(dim) - currentBest.getGeometrizable().valueForDimension(dim));

            if (distSearchAndCurrentNode < distSearchAndCurrentBest) {
                if (child == node.getLeftNode() && node.getRightNode() != null) {
                    searchTreeDown(node.getRightNode(), search, constraint, currentBest);
                } else if (node.getLeftNode() != null) {
                    searchTreeDown(node.getLeftNode(), search, constraint, currentBest);
                }
            }

            if (!node.isRoot()) {
                searchTreeUp(node.getParent(), search, constraint, currentBest, node);
            }

            return;
        }

        private int getId() {
            return id;
        }
    }


    /**
     * A wrapper class the keep the current best Geometrizable.
     */
    private class GeometrizableHolder {

        private Geometrizable geometrizable;

        GeometrizableHolder (Geometrizable geometrizable) {
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
