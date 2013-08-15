package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.ProtobufConverter;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGeometryData;

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
	 * @param root	GeometyNode as root of tree.
	 * @param numDimensions	Number of dimensions.
	 * @throws IllegalArgumentException	if root is null or number of dimensions is not greater than 0.
	 */
	public GeometryDataIO(GeometryNode root, int numDimensions) {
		if (root == null)
			throw new IllegalArgumentException("root node must not be null");
		if (numDimensions < 1)
			throw new IllegalArgumentException("number of dimensions must be greater than 0");
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

	/**
	 * Saves the GeometryDataIO object to an external file.
	 *
	 * @param objectToSave
	 *            GeometryDataIO object to save.
	 * @param destination
	 *            Location of output file on file system.
	 * @throws java.io.IOException
	 */
	public static void save(GeometryDataIO objectToSave, File destination) throws IOException {
		OutputStream out = new BufferedOutputStream(new FileOutputStream(destination));
		ProtobufConverter.getGeometryDataBuilder(objectToSave).build().writeTo(out);
		out.flush();
		out.close();
	}

	/**
	 * Loads and returns a GeometryDataIO object from a given file.
	 *
	 * @param source
	 *            Location of source file in file system.
	 * @throws java.io.IOException
	 * @throws ClassNotFoundException
	 */
	public static GeometryDataIO load(File source) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(source));
		GeometryDataIO geom = ProtobufConverter.getGeometryData(SaveGeometryData.parseFrom(in));
		in.close();
		return geom;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numDimensions;
		result = prime * result + ((root == null) ? 0 : root.hashCode());
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
		if (!(obj instanceof GeometryDataIO)) {
			return false;
		}
		GeometryDataIO other = (GeometryDataIO) obj;
		if (numDimensions != other.numDimensions) {
			return false;
		}
		if (root == null) {
			if (other.root != null) {
				return false;
			}
		} else if (!root.equals(other.root)) {
			return false;
		}
		return true;
	}
}