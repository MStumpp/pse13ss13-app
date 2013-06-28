package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

import android.util.Log;
import android.view.Display;
import android.app.Activity;
import android.graphics.Point;

/**
 * This class offers different kind of computations with coordinates.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public final class CoordinateUtility {

	/**
	 * The average earth radius according to WGS84
	 */
	private static double EARTH_RADIUS = 6371.000785;

	/**
	 * This class is an utility class which is not instantiated.
	 */
	private CoordinateUtility() {

	}

	/**
	 * Converts a display coordinate into a coordinate.
	 * 
	 * @param coord
	 *            coordinate to convert
	 * @param anchor
	 * @return
	 */
	public static DisplayCoordinate coordinateToDisplayCoordinate(
			Coordinate coord, Coordinate anchor) {
		return new DisplayCoordinate(0, 0);
	}

	/**
	 * Converts a coordinate into a display coordinate.
	 * 
	 * @param dispCoord
	 *            display coordinate to convert
	 * @param anchor
	 * @return
	 */
	public static Coordinate displayCoordinateToCoordinate(
			DisplayCoordinate dispCoord, Coordinate anchor) {
		return new Coordinate(0, 0);
	}
	
	// parameter war im entwurf noch nicht drin!
	/**
	 * Returns the measures of the display.
	 * 
	 * @param activity
	 *            current activity
	 * @return the measures of the display
	 */
	public static Point getDisplay(Activity activity) {
		Log.d("COORDINATE_UTILITY", "Rufe Display ab.");
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}

	/**
	 * Calculates the distance between two coordinates
	 * 
	 * @param c1
	 *            the first coordinate
	 * @param c2
	 *            the the second coordinate
	 * @return the distance between the two coordinates in kilometers
	 * @see {@link https://de.wikipedia.org/wiki/Orthodrome#Strecke}
	 */
	public static double calculateDifferenceInMeters(Coordinate c1,
			Coordinate c2) {
		double lon1 = Math.toRadians(c1.getLongtitude());
		double lon2 = Math.toRadians(c2.getLongtitude());
		double lat1 = Math.toRadians(c1.getLatitude());
		double lat2 = Math.toRadians(c2.getLatitude());
		double zeta = Math.acos(Math.sin(lat1) * Math.sin(lat2)
				+ Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1));
		return zeta * EARTH_RADIUS;
	}
}
