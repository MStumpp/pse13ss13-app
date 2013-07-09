package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

/**
 * This class contains some preprocessed data by GeometryDataPreprocessor.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryDataIO {

	/**
	 * Root GeometryNode.
	 */
	private GeometryNode root;

	/**
	 * Number of dimensions.
	 */
	private int numDimensions;

	/**
	 * Initializes GeometryDataIO with GeometryNode as root.
	 *
	 * @param root
	 *            GeometyNode as root of tree.
	 * @param numDimensions
	 *            Number of dimensions.
	 * @throws IllegalArgumentException
	 *             if root is null or number of dimensions is not greater than
	 *             0.
	 */
	public GeometryDataIO(GeometryNode root, int numDimensions) {
		if (root == null)
			throw new IllegalArgumentException("root node must not be null");
		if (numDimensions < 1)
			throw new IllegalArgumentException(
					"number of dimensions must be greater than 0");
		this.root = root;
		this.numDimensions = numDimensions;
	}

	/**
	 * Returns the root GeometryNode.
	 *
	 * @return GeometryNode root of tree.
	 */
	public GeometryNode getRoot() {
		return root;
	}

	/**
	 * Returns the number of dimensions.
	 *
	 * @return int Number of dimensions.
	 */
	public int getNumDimensions() {
		return numDimensions;
	}
}
