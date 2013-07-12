package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable;
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
     * MAX_NUMBER_CALLS.
     */
    private final static int MAX_NUMBER_CALLS = 300;


    /**
     * GeometryProcessor instance.
     */
    private static GeometryProcessor instance;


    /**
     * GeometryDataIO instance.
     */
    private GeometryDataIO geometryDataIO;


    /**
     * NumberMethodCalls.
     */
    private int numberMethodCalls = 0;


    /**
     * Running flag.
     */
    private boolean running = false;


    /**
     * Stop flag.
     */
    private boolean stop = false;


    /**
     * Creates an instance of GeometryProcessor.
     */
    private GeometryProcessor(GeometryDataIO geometryDataIO) {
        this.geometryDataIO = geometryDataIO;
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
     */
    public static GeometryProcessor init(GeometryDataIO geometryDataIO) {
        if (geometryDataIO == null)
            throw new IllegalArgumentException("GeometryDataIO must be provided");
        if (instance != null)
            throw new IllegalArgumentException("GeometryProcessor already initialized");
        instance = new GeometryProcessor(geometryDataIO);
        return instance;
    }


    /**
     * Computes the nearest Edge for a given Geometrizable.
     *
     * @param geometrizable The Geometrizable the resulting Edge has to be closest to.
     * @return Geometrizable Nearest Geometrizable Edge.
     * @throws GeometryProcessorException If something goes wrong.
     * @throws IllegalArgumentException If geometrizable is null.
     */
    // TODO use Graph object to select a random outgoing edge of projected Geometrizable
    public Geometrizable getNearestEdge(Geometrizable geometrizable)
            throws GeometryProcessorException {
        if (geometrizable == null)
            throw new IllegalArgumentException("geometrizable must not be null");
        if (running)
            throw new GeometryProcessorException("currently running");
        return search(geometryDataIO.getRoot(), geometrizable);
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
            throws GeometryProcessorException {
        if (geometrizable == null)
            throw new IllegalArgumentException("geometrizable must not be null");
        if (running)
            throw new GeometryProcessorException("currently running");
        return search(geometryDataIO.getRoot(), geometrizable);
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
        running = true;
        stop = false;
        GeometrizableWrapper wrapper = new GeometrizableWrapper(null);
        searchTreeDown(root, search, wrapper);
        running = false;
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
        int dim = node.getDepth() % geometryDataIO.getNumDimensions();
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
        int dim = node.getDepth() % geometryDataIO.getNumDimensions();

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
