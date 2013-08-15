package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

/**
 * Interface for GeometryProcessor.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public interface Geometrizable {

    /**
     * Returns the number of dimensions.
     *
     * @return int Number of dimensions.
     */
    public int numberDimensions();


    /**
     * Returns the value for a certain dimension.
     *
     * @return double value.
     */
    public double valueForDimension(int dim);


    /**
     * Returns the number of relevant points to be
     * inserted as separate nodes into the geometry tree.
     *
     * @return int Number of nodes.
     */
    public int numberNodes();


    /**
     * Returns the number of relevant points to be
     * inserted as separate nodes into the geometry tree.
     *
     * @return int Number of nodes.
     */
    public Geometrizable getNode(int nodeNumber);

}
