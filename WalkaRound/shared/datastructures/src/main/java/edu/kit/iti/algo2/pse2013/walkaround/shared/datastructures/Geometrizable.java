package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

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

}
