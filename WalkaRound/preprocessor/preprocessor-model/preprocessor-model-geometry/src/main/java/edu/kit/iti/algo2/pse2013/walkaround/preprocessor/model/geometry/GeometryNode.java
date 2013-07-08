package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;

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

    private double splitValue;

    private GeometryNode parent;

    private int depth;

    private GeometryNode leftNode;

    private GeometryNode rightNode;

    private Vertex vertex;

    public GeometryNode(GeometryNode parent, int depth, Vertex vertex) {
        this.parent = parent;
        this.depth = depth;
        this.vertex = vertex;
        this.splitValue = Double.NaN;
    }

    public GeometryNode(GeometryNode parent, int depth, double splitValue) {
        this.parent = parent;
        this.depth = depth;
        this.splitValue = splitValue;
    }

    public GeometryNode(Vertex vertex) {
        this.vertex = vertex;
    }

    public GeometryNode(double splitValue) {
        this.splitValue = splitValue;
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

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return vertex != null;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n\n -> depth: " + depth + "\n");
        sb.append("-> split: " + splitValue + "\n");

        if (vertex != null)
            sb.append("\n vertex: " + vertex.toString() + "\n");
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
