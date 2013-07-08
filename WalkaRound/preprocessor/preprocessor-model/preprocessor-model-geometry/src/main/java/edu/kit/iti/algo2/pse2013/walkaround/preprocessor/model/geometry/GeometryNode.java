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

    private Vertex vertex;

    private GeometryNode leftNode;

    private GeometryNode rightNode;

    public GeometryNode(Vertex vertex, GeometryNode leftNode, GeometryNode rightNode) {
        if (vertex == null)
            throw new IllegalArgumentException("vertex must not be null");
        this.vertex = vertex;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public GeometryNode getLeftNode() {
        return leftNode;
    }

    public GeometryNode getRightNode() {
        return rightNode;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n" + vertex.toString() + "\n");
        if (leftNode != null)
            sb.append("-> leftNode: " + leftNode.getVertex().toString() + "\n");
        if (rightNode != null)
            sb.append("-> rightNode: " + rightNode.getVertex().toString() + "\n");

        if (leftNode != null)
            sb.append(leftNode.toString() + "\n");
        if (rightNode != null)
            sb.append(rightNode.toString() + "\n");

        return sb.toString();
    }

}