package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains some preprocessed data by GeometryDataPreprocessor.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryNode {

	/**
	 * Geometrizable.
	 */
	private List<Geometrizable> geometrizables;

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
	public GeometryNode(GeometryNode parent, int depth, Geometrizable geometrizable) {
		this.geometrizables = new ArrayList<Geometrizable>();
        this.geometrizables.add(geometrizable);
		this.splitValue = Double.NaN;
		this.parent = parent;
		this.depth = depth;
	}

    /**
     * Initializes GeometryNode as leaf node.
     *
     * @param parent
     *            Parent GeometryNode.
     * @param depth
     *            Depth of this GeometryNode.
     * @param geometrizables
     *            Geometrizable.
     */
    public GeometryNode(GeometryNode parent, int depth, List<Geometrizable> geometrizables) {
        this.geometrizables = geometrizables;
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
		this.geometrizables = null;
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
		this.geometrizables = new ArrayList<Geometrizable>();
        this.geometrizables.add(geometrizable);
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
		this.geometrizables = null;
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
        if (geometrizables != null && geometrizables.size() == 1)
		    return geometrizables.get(0);
        return null;
	}

    /**
     * Returns the nearest Geometrizable.
     *
     * @return Geometrizable.
     */
    public Geometrizable getNearestGeometrizable(Geometrizable geometrizable, int dim) {

        if (geometrizable == null || dim < 0)
            throw new IllegalArgumentException("geometrizable must not " +
                    "be null and/or dim greater or equal to 0");

        if (geometrizables == null)
            return null;

        if (geometrizables.size() == 1)
            return geometrizables.get(0);

        Geometrizable currentBest = geometrizables.get(0);
        for (Geometrizable geom : geometrizables) {
            if (geom.valueForDimension(dim) <
                    currentBest.valueForDimension(dim))
                currentBest = geom;
        }

        return currentBest;
    }

    /**
     * Returns the Geometrizable.
     *
     * @return Geometrizable.
     */
    public List<Geometrizable> getGeometrizables() {
        if (geometrizables != null)
            return geometrizables;
        return new ArrayList<Geometrizable>();
    }

	/**
	 * Sets the Geometrizable.
	 *
	 * @param geometrizable
	 *            Geometrizable.
	 */
	public void setGeometrizable(Geometrizable geometrizable) {
		this.geometrizables = new ArrayList<Geometrizable>();
        this.geometrizables.add(geometrizable);
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
		return geometrizables != null;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n\n -> depth: " + depth + "\n");
		sb.append("-> split: " + splitValue + "\n");

		if (geometrizables != null)
			sb.append("\n geometrizable: " + geometrizables.toString() + "\n");
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
