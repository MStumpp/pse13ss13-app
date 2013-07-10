package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryProcessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryProcessorException;

/**
 * This class provides a method to normalize a coordinate to a coordinate on a
 * graph.
 *
 * @author Thomas Kadow
 * @version 1.0
 */
public final class CoordinateNormalizer {

	private static final String TAG_COORDINATENORMALIZER = CoordinateNormalizer.class.getSimpleName();
	
	/**
	 * This class is an utility class which is not instantiated.
	 */
	private CoordinateNormalizer() {

	}

	/**
	 * Normalizes a coordinate to a coordinate on a graph.
	 *
	 * @param coord
	 *            coordinate to normalize
	 * @param levelOfDetail
	 *            the "tile zoom level" for which the calculation should be made
	 * @return a normalized coordinate on a graph
	 */
	public static Coordinate normalizeCoordinate(Coordinate coord, int levelOfDetail) {
		try {
			return (Coordinate) GeometryProcessor.getInstance().getNearestVertex(coord);
		} catch (GeometryProcessorException e) {
			Log.d(TAG_COORDINATENORMALIZER, e.toString());
		} catch (InstantiationException e) {
			Log.d(TAG_COORDINATENORMALIZER, e.toString());
		}
		return null;
	}
}
