package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

/**
 * Wrapper for Geometrizable to handle multiple points.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometrizableWrapper implements Geometrizable {

    private Geometrizable geometrizable;

    private int nodeNumber;

    public GeometrizableWrapper(Geometrizable geometrizable, int nodeNumber) {
        this.geometrizable = geometrizable;
        this.nodeNumber = nodeNumber;
    }

    @Override
    public int numberDimensions() {
        return geometrizable.numberDimensions();
    }

    @Override
    public double valueForDimension(int dim) {
        return geometrizable.valueForDimension(nodeNumber, dim);
    }

    @Override
    public final int numberNodes() {
        return 1;
    }

    @Override
    public double valueForDimension(int nodeNumber, int dim) {
        if (this.nodeNumber != nodeNumber)
            throw new IllegalArgumentException("wrapper configured for nodeNumber " +
                    this.nodeNumber + ", but " + nodeNumber + " requested");
        return geometrizable.valueForDimension(this.nodeNumber, dim);
    }

    public Geometrizable getGeometrizable() {
        return geometrizable;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

}
