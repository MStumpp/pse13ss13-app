package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

/**
 * GeometryProcessorEdge provides some api to query nearest Edges and Vertices based on Coordinates.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryProcessorEdge {

    /**
     * GeometryProcessor instance.
     */
    private static GeometryProcessorEdge instance;


    /**
     * GeometryProcessor instance.
     */
    private GeometryProcessor processor;


    /**
     * Creates an instance of GeometryProcessor.
     *
     * @param geometryDataIO GeometryDataIO used for geometry computation.
     * @param numberThreads The number of threads for parallel computation.
     */
    private GeometryProcessorEdge(GeometryDataIO geometryDataIO, int numberThreads) {
        processor = new GeometryProcessor(geometryDataIO, numberThreads);
    }


    /**
     * Returns a singleton instance of GeometryProcessorEdge if available.
     *
     * @return GeometryProcessor.
     * @throws InstantiationException If not instantiated
     */
    public static GeometryProcessorEdge getInstance() throws InstantiationException {
        if (instance == null)
            throw new InstantiationException("singleton must be initialized first");
        return instance;
    }


    /**
     * Instantiates and returns a singleton instance of GeometryProcessorEdge.
     *
     * @param geometryDataIO GeometryDataIO containing relevant data.
     * @return GeometryProcessor.
     * @throws IllegalArgumentException If some input parameter are missing.
     */
    public static GeometryProcessorEdge init(GeometryDataIO geometryDataIO)
            throws IllegalArgumentException {
        return init(geometryDataIO, 1);
    }


    /**
     * Instantiates and returns a singleton instance of GeometryProcessorEdge.
     *
     * @param geometryDataIO GeometryDataIO containing relevant data.
     * @param numberThreads The number of threads for parallel computation.
     * @return GeometryProcessor.
     * @throws IllegalArgumentException If some input parameter are missing.
     */
    public static GeometryProcessorEdge init(GeometryDataIO geometryDataIO, int numberThreads)
            throws IllegalArgumentException {
        if (geometryDataIO == null)
            throw new IllegalArgumentException("GeometryDataIO must be provided");
        if (numberThreads < 1)
            throw new IllegalArgumentException("Number of threads must be greater or equal to 1");
        if (instance != null)
            throw new IllegalArgumentException("GeometryProcessor already initialized");
        instance = new GeometryProcessorEdge(geometryDataIO, numberThreads);
        return instance;
    }


    /**
     * Computes the nearest Geometrizable for a given Geometrizable.
     *
     * @param search The Geometrizable the resulting Vertex has to be closest to.
     * @return Geometrizable Nearest Geometrizable Vertex.
     * @throws GeometryProcessorException If something goes wrong.
     * @throws GeometryComputationNoSlotsException
     * @throws IllegalArgumentException If geometrizable is null.
     */
    public Geometrizable getNearestGeometrizable(GeometrySearch search)
            throws GeometryProcessorException, GeometryComputationNoSlotsException,
            IllegalArgumentException {
        return processor.getNearestGeometrizable(search, null);
    }


    /**
     * Computes the nearest Geometrizable for a given Geometrizable.
     *
     * @param search The Geometrizable the resulting Vertex has to be closest to.
     * @param constraint
     * @return Geometrizable Nearest Geometrizable Vertex.
     * @throws edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryProcessorException If something goes wrong.
     * @throws GeometryComputationNoSlotsException
     * @throws IllegalArgumentException If geometrizable is null.
     */
    public Geometrizable getNearestGeometrizable(GeometrySearch search, GeometrizableConstraint constraint)
            throws GeometryProcessorException, GeometryComputationNoSlotsException,
            IllegalArgumentException {
        return processor.getNearestGeometrizable(search, constraint);
    }

}
