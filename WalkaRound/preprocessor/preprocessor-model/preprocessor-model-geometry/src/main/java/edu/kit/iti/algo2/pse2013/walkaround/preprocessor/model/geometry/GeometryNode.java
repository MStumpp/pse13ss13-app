package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable;

import java.io.Serializable;

/**
 * This class contains some preprocessed data by GeometryDataPreprocessor.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryNode implements Serializable {

    /**
     * Temporary Serial version ID as long as Java serialization is used
     */
    private static final long serialVersionUID = 3394680123853287035L;

    private Geometrizable geometrizable;

    private double splitValue;

    private GeometryNode parent;

    private int depth;

    private GeometryNode leftNode;

    private GeometryNode rightNode;

    public GeometryNode(GeometryNode parent, int depth, Geometrizable geometrizable) {
        this.geometrizable = geometrizable;
        this.splitValue = Double.NaN;
        this.parent = parent;
        this.depth = depth;
    }


    public GeometryNode(GeometryNode parent, int depth, double splitValue) {
        this.geometrizable = null;
        this.splitValue = splitValue;
        this.parent = parent;
        this.depth = depth;
    }

    public GeometryNode(Geometrizable geometrizable) {
        this.geometrizable = geometrizable;
        this.splitValue = Double.NaN;
        this.parent = null;
        this.depth = -1;
    }

    public GeometryNode(double splitValue) {
        this.geometrizable = null;
        this.splitValue = splitValue;
        this.parent = null;
        this.depth = -1;
    }

    public double getSplitValue() {
        return splitValue;
    }

    public GeometryNode getParent() {
        return parent;
    }

    public int getDepth() {
        return depth;
    }

    public GeometryNode getLeftNode() {
        return leftNode;
    }

    public GeometryNode getRightNode() {
        return rightNode;
    }

    public void setLeftNode(GeometryNode leftNode) {
        this.leftNode = leftNode;
    }

    public void setRightNode(GeometryNode rightNode) {
        this.rightNode = rightNode;
    }

    public Geometrizable getGeometrizable() {
        return geometrizable;
    }

    public void setGeometrizable(Geometrizable geometrizable) {
        this.geometrizable = geometrizable;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return geometrizable != null;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n\n -> depth: " + depth + "\n");
        sb.append("-> split: " + splitValue + "\n");

        if (geometrizable != null)
            sb.append("\n geometrizable: " + geometrizable.toString() + "\n");
        else
            sb.append("\n no vertex \n");

        if (parent != null)
            sb.append("-> parent: id: \n");
        else
            sb.append("-> no parent \n");

        if (leftNode != null)
            sb.append("-> leftNode: " + leftNode.toString() + "\n");
        if (rightNode != null)
            sb.append("-> rightNode: " + rightNode.toString() + "\n");
        return sb.toString();
    }

}
