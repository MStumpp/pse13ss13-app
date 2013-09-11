package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * Logger.
     */
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(GeometryNode.class);

	/**
	 * Geometrizable.
	 */
	private List<Geometrizable> geometrizables = new ArrayList<Geometrizable>();

	/**
	 * Value for split plane.
	 */
	private double splitValue = Double.NaN;

	/**
	 * Parent GeometryNode.
	 */
	private GeometryNode parent = null;

	/**
	 * Depth of node.
	 */
	private int depth = 0;

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
	 * @param geometrizables
	 *            Geometrizable.
	 */
	public GeometryNode(GeometryNode parent, List<Geometrizable> geometrizables) {
		this.geometrizables = geometrizables;
		this.parent = parent;
	}

	/**
	 * Initializes GeometryNode as inner node.
	 *
	 * @param parent
	 *            Parent GeometryNode.
	 * @param splitValue
	 *            Split value.
	 */
	public GeometryNode(GeometryNode parent, double splitValue) {
		this.splitValue = splitValue;
		this.parent = parent;
	}

	/**
	 * Initializes GeometryNode as inner node.
	 *
	 * @param parent
	 *            Parent GeometryNode.
	 * @param splitValue
	 *            Split value.
	 * @param geometrizables
	 *           the geometrizable-list.
	 */
	public GeometryNode(GeometryNode parent, double splitValue, List<Geometrizable> geometrizables) {
		this.splitValue = splitValue;
		this.parent = parent;
		this.geometrizables = geometrizables;
	}

	/**
	 * Initializes GeometryNode.
	 *
	 * @param splitValue
	 *            Split value.
	 */
	public GeometryNode(double splitValue) {
		this.splitValue = splitValue;
	}
	/**
	 * Initializes GeometryNode.
	 *
	 * @param splitValue
	 *            Split value.
	 */
	public GeometryNode(double splitValue, List<Geometrizable> geometrizables) {
		this.splitValue = splitValue;
		this.geometrizables = geometrizables;
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

	private void setParent(GeometryNode parent) {
		this.parent = parent;
		setDepth(parent.getDepth() + 1);
	}

	/**
	 * Returns the depth.
	 *
	 * @return int depth.
	 */
	public int getDepth() {
		return depth;
	}

	private void setDepth(int depth) {
		this.depth = depth;
		if (leftNode != null) {
			leftNode.setDepth(depth + 1);
		}
		if (rightNode != null) {
			rightNode.setDepth(depth + 1);
		}
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
		leftNode.setParent(this);
	}

	/**
	 * Sets the right Geometrizable.
	 *
	 * @param rightNode
	 *            Geometrizable.
	 */
	public void setRightNode(GeometryNode rightNode) {
		this.rightNode = rightNode;
		rightNode.setParent(this);
	}

    /**
     * Returns the nearest Geometrizable.
     *
     * @return Geometrizable.
     */
    public Geometrizable getNearestGeometrizable(double value,
        GeometrizableConstraint constraint, int dim) {

        if (dim < 0) {
            throw new IllegalArgumentException("geometrizable must not " +
                    "be null and/or dim greater or equal to 0");
        }

        if (geometrizables.size() == 0) {
            return null;
        }

//        if (geometrizables.size() == 1) {
//            return geometrizables.get(0);
//        }

        Geometrizable currentBest = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Geometrizable geom : geometrizables) {
            if (constraint != null && !constraint.isValid(geom)) {
                continue;
            }

            double currentDistance = Math.abs(geom.valueForDimension(dim) - value);

            if (currentDistance < minDistance) {
                //logger.info("currentDistance < minDistance ");
                currentBest = geom;
                minDistance = currentDistance;
            }
        }

        return currentBest;
    }

    /**
     * Returns the Geometrizable.
     *
     * @return Geometrizable.
     */
    public List<Geometrizable> getGeometrizables() {
    	if (geometrizables == null) {
    		geometrizables = new ArrayList<Geometrizable>();
    	}
        return geometrizables;
    }

	/**
	 * Sets the Geometrizable.
	 *
	 * @param geometrizable
	 *            Geometrizable.
	 */
	public void addGeometrizable(Geometrizable geometrizable) {
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
        if (this.leftNode == null && this.rightNode == null) {
		    return true;
        }
        return false;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n\n -> depth: " + depth + "\n");
		sb.append("-> split: " + splitValue + "\n");

		if (geometrizables.size() > 0)
			sb.append("\n geometrizable: " + getGeometrizables().toString() + "\n");
		else
			sb.append("\n no vertex \n");

		if (parent != null)
			sb.append("-> parent: id: \n");
		else
			sb.append("-> no parent \n");

		if (leftNode != null)
			sb.append("-> leftNode: " + getLeftNode().toString() + "\n");
		if (rightNode != null)
			sb.append("-> rightNode: " + getRightNode().toString() + "\n");
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + depth;
		result = prime * result
				+ ((geometrizables == null) ? 0 : geometrizables.hashCode());
		result = prime * result
				+ ((leftNode == null) ? 0 : leftNode.hashCode());
		result = prime * result
				+ ((rightNode == null) ? 0 : rightNode.hashCode());
		long temp;
		temp = Double.doubleToLongBits(splitValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GeometryNode)) {
			return false;
		}
		GeometryNode other = (GeometryNode) obj;
		if (depth != other.depth) {
			return false;
		}
		if (geometrizables == null) {
			if (other.geometrizables != null) {
				return false;
			}
		} else if (!geometrizables.equals(other.geometrizables)) {
			return false;
		}
		if (leftNode == null) {
			if (other.leftNode != null) {
				return false;
			}
		} else if (!leftNode.equals(other.leftNode)) {
			return false;
		}
		if (rightNode == null) {
			if (other.rightNode != null) {
				return false;
			}
		} else if (!rightNode.equals(other.rightNode)) {
			return false;
		}
		if (Double.doubleToLongBits(splitValue) != Double
				.doubleToLongBits(other.splitValue)) {
			return false;
		}
		return true;
	}
}