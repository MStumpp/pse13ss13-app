package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

/**
 * Wrapper for constraining nearest Geometrizable search.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public interface GeometrizableConstraint {

    public boolean isValid(Geometrizable geometrizable);

}
