package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.kit.iti.algo2.pse2013.walkaround.pbf.ProtobufConverter;
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

	public static void save(GeometryDataIO geometryData, File destination) throws FileNotFoundException, IOException {
		OutputStream out = new BufferedOutputStream(new FileOutputStream(destination));
		ProtobufConverter.getGeometryDataBuilder(geometryData).build().writeTo(out);
		out.flush();
		out.close();
	}

	public static GeometryDataIO load(File source) throws FileNotFoundException, IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(source));
		GeometryDataIO geom = ProtobufConverter.getGeometryData(SaveGeometryData.parseFrom(in));
		in.close();
		return geom;
	}
}
