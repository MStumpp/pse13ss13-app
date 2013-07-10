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

	/**
	 * Geometrizable.
	 */
	private Geometrizable geometrizable;

	/**
	 * Value for split plane.
	 */
	private double splitValue;

	/**
	 * Parent GeometryNode.
	 */
	private GeometryNode parent;

	/**
	 * Depth of node.
	 */
	private int depth;

	/**
	 * Left GeometryNode.
	 */
	private GeometryNode leftNode;

	/**
	 * Right GeometryNode.
	 */
	private GeometryNode rightNode;

	/**
	 * Initializes GeometryNode as leaf node.
	 *
	 * @param parent
	 *            Parent GeometryNode.
	 * @param depth
	 *            Depth of this GeometryNode.
	 * @param geometrizable
	 *            Geometrizable.
	 */
	public GeometryNode(GeometryNode parent, int depth,
			Geometrizable geometrizable) {
		this.geometrizable = geometrizable;
		this.splitValue = Double.NaN;
		this.parent = parent;
		this.depth = depth;
	}

	/**
	 * Initializes GeometryNode as inner node.
	 *
	 * @param parent
	 *            Parent GeometryNode.
	 * @param depth
	 *            Depth of this GeometryNode.
	 * @param splitValue
	 *            Split value.
	 */
	public GeometryNode(GeometryNode parent, int depth, double splitValue) {
		this.geometrizable = null;
		this.splitValue = splitValue;
		this.parent = parent;
		this.depth = depth;
	}

	/**
	 * Initializes GeometryNode.
	 *
	 * @param geometrizable
	 *            Geometrizable.
	 */
	public GeometryNode(Geometrizable geometrizable) {
		this.geometrizable = geometrizable;
		this.splitValue = Double.NaN;
		this.parent = null;
		this.depth = -1;
	}

	/**
	 * Initializes GeometryNode.
	 *
	 * @param splitValue
	 *            Split value.
	 */
	public GeometryNode(double splitValue) {
		this.geometrizable = null;
		this.splitValue = splitValue;
		this.parent = null;
		this.depth = -1;
	}

	/**
	 * Returns split value.
	 *
	 * @return double.
	 */
	public double getSplitValue() {
		return splitValue;
	}

	/**
	 * Returns parent.
	 *
	 * @return GeometryNode.
	 */
	public GeometryNode getParent() {
		return parent;
	}

	/**
	 * Returns the depth.
	 *
	 * @return int depth.
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Returns the left GeometryNode.
	 *
	 * @return GeometryNode.
	 */
	public GeometryNode getLeftNode() {
		return leftNode;
	}

	/**
	 * Returns the right GeometryNode.
	 *
	 * @return GeometryNode.
	 */
	public GeometryNode getRightNode() {
		return rightNode;
	}

	/**
	 * Sets the left Geometrizable.
	 *
	 * @param leftNode
	 *            Geometrizable.
	 */
	public void setLeftNode(GeometryNode leftNode) {
		this.leftNode = leftNode;
	}

	/**
	 * Sets the right Geometrizable.
	 *
	 * @param rightNode
	 *            Geometrizable.
	 */
	public void setRightNode(GeometryNode rightNode) {
		this.rightNode = rightNode;
	}

	/**
	 * Returns the Geometrizable.
	 *
	 * @return Geometrizable.
	 */
	public Geometrizable getGeometrizable() {
		return geometrizable;
	}

	/**
	 * Sets the Geometrizable.
	 *
	 * @param geometrizable
	 *            Geometrizable.
	 */
	public void setGeometrizable(Geometrizable geometrizable) {
		this.geometrizable = geometrizable;
	}

	/**
	 * Returns whether this is a root node or not.
	 *
	 * @return true if root node, false otherwise.
	 */
	public boolean isRoot() {
		return parent == null;
	}

	/**
	 * Returns whether this is a leaf node or not.
	 *
	 * @return true if leaf node, false otherwise.
	 */
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
