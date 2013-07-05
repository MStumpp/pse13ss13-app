package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

public final class CoordinateUtility {
	/**
	 * Used when pixels are converted to degrees horizontally or vice versa
	 */
	public static boolean DIRECTION_HORIZONTAL = true;
	/**
	 * Used when pixels are converted to degrees vertically or vice versa
	 */
	public static boolean DIRECTION_VERTICAL = false;
	/**
	 * Alias for {@link this#DIRECTION_VERTICAL}.
	 */
	public static boolean DIRECTION_LATITUDE = DIRECTION_VERTICAL;
	/**
	 * Alias for {@link this#DIRECTION_HORIZONTAL}.
	 */
	public static boolean DIRECTION_LONGTITUDE = DIRECTION_HORIZONTAL;
	/**
	 * Alias for {@link this#DIRECTION_VERTICAL}.
	 */
	public static boolean DIRECTION_Y = DIRECTION_VERTICAL;
	/**
	 * Alias for {@link this#DIRECTION_HORIZONTAL}.
	 */
	public static boolean DIRECTION_X = DIRECTION_HORIZONTAL;

	/**
	 * The average earth radius according to WGS84
	 */
	private static double EARTH_RADIUS = 6371.000785;

	private CoordinateUtility() {

	}

	// parameter war im entwurf noch nicht drin!
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
	 * @param c1 the first coordinate
	 * @param c2 the the second coordinate
	 * @return the distance between the two coordinates in kilometers
	 * @see {@link https://de.wikipedia.org/wiki/Orthodrome#Strecke}
	 */
	public static double calculateDifferenceInMeters(Coordinate c1, Coordinate c2) {
		double lon1 = Math.toRadians(c1.getLongtitude());
		double lon2 = Math.toRadians(c2.getLongtitude());
		double lat1 = Math.toRadians(c1.getLatitude());
		double lat2 = Math.toRadians(c2.getLatitude());
		double zeta = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1));
		return zeta * EARTH_RADIUS;
	}

	/**
	 * Converts a given length in display-pixels into the corresponding geographical degrees.
	 *
	 * @param pixels the given length in pixels
	 * @param levelOfDetail the current level of detail
	 * @param one of the constants {@link CoordinateUtility#HORIZONTAL} or {@link CoordinateUtility#VERTICAL}
	 * @return the given length in degrees
	 */
	public static float convertPixelsToDegrees(float pixels, float levelOfDetail, boolean isHorizontal) {
		return (float) (45 * pixels / Math.pow(2, levelOfDetail + 6)) * (isHorizontal ? 2 : 1);
	}

	/**
	 * Converts a given corresponding geographical degrees into a length in
	 * display-pixel
	 *
	 * @param degree the given geographical degrees
	 * @param levelOfDetail the current level of detail
	 * @return the given length in degrees
	 */
	public static float convertDegreesToPixels(float degree, float levelOfDetail, boolean isHorizontal) {
		return (float) ((degree * Math.pow(2, levelOfDetail + 6)) / 45) / (isHorizontal ? 2 : 1);
	}
	
	/**
	 * Converts a given Display Coordinate to a geographical Coordinate
	 * 
	 * @param dc the given DisplayCoordinate
	 * @param levelOfDetail the current Level of Detail
	 * @return a new Coordinate 
	 */
	public static Coordinate convertDisplayCoordinateToCoordinate(DisplayCoordinate dc, Coordinate upperLeft, float levelOfDetail) {
		return new Coordinate(upperLeft.getLatitude() + convertDegreesToPixels(dc.getX(),levelOfDetail,CoordinateUtility.DIRECTION_HORIZONTAL),upperLeft.getLongtitude() + convertDegreesToPixels(dc.getY(), levelOfDetail, CoordinateUtility.DIRECTION_HORIZONTAL));
	}
}
