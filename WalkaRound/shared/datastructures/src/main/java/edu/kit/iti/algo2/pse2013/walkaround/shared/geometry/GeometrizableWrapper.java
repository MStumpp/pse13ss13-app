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
        return geometrizable.getNode(nodeNumber).valueForDimension(dim);
    }

    @Override
    public final int numberNodes() {
        return 1;
    }

    @Override
    public Geometrizable getNode(int nodeNumber) {
        return geometrizable.getNode(nodeNumber);
    }

    public Geometrizable getNode() {
        return geometrizable.getNode(nodeNumber);
    }

    public Geometrizable getGeometrizable() {
        return geometrizable;
    }

    @Override
    public int compareTo(Object o) {
        return getNode().compareTo(((GeometrizableWrapper)o).getNode());
    }

    public String toString() {
        return geometrizable.toString();
    }

}
