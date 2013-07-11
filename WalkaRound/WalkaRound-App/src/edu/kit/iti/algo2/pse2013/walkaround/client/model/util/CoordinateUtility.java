package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayWaypoint;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public final class CoordinateUtility {

	public static final String TAG = CoordinateUtility.class.getSimpleName();
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
	 * @param c1
	 *            the first coordinate
	 * @param c2
	 *            the the second coordinate
	 * @return the distance between the two coordinates in kilometers
	 * @see {@link https://de.wikipedia.org/wiki/Orthodrome#Strecke}
	 */
	public static double calculateDifferenceInMeters(Coordinate c1,
			Coordinate c2) {
		double lon1 = Math.toRadians(c1.getLongitude());
		double lon2 = Math.toRadians(c2.getLongitude());
		double lat1 = Math.toRadians(c1.getLatitude());
		double lat2 = Math.toRadians(c2.getLatitude());
		double zeta = Math.acos(Math.sin(lat1) * Math.sin(lat2)
				+ Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1));
		return zeta * EARTH_RADIUS;
	}

	/**
	 * Converts a given length in display-pixels into the corresponding
	 * geographical degrees.
	 *
	 * @param pixels
	 *            the given length in pixels
	 * @param levelOfDetail
	 *            the current level of detail
	 * @param one
	 *            of the constants {@link CoordinateUtility#HORIZONTAL} or
	 *            {@link CoordinateUtility#VERTICAL}
	 * @return the given length in degrees
	 */
	public static double convertPixelsToDegrees(float pixels,
			float levelOfDetail, boolean isHorizontal) {
		return (45 * pixels / Math.pow(2, levelOfDetail + 6))
				* (isHorizontal ? 2 : 1);
	}

	/**
	 * Converts given geographical degrees into the corresponding length in
	 * display-pixel
	 *
	 * @param degree
	 *            the given geographical degrees
	 * @param levelOfDetail
	 *            the current level of detail
	 * @return the given length in degrees
	 */
	public static float convertDegreesToPixels(double degree,
			float levelOfDetail, boolean isHorizontal) {
		return (float) ((degree * Math.pow(2, levelOfDetail + 6)) / 45)
				/ (isHorizontal ? 2 : 1);
	}

	/**
	 * Converts a given Display Coordinate to a geographical Coordinate
	 *
	 * @param dc
	 *            the given DisplayCoordinate
	 * @param upperLeft
	 *            the upper left Edge of the display
	 * @param levelOfDetail
	 *            the current Level of Detail
	 * @return a new Coordinate
	 */
	public static Coordinate convertDisplayCoordinateToCoordinate(
			DisplayCoordinate dc, Coordinate upperLeft, float levelOfDetail) {
		double deltaX = convertPixelsToDegrees(dc.getX(), levelOfDetail,
				CoordinateUtility.DIRECTION_X);
		double deltaY = convertPixelsToDegrees(dc.getY(), levelOfDetail,
				CoordinateUtility.DIRECTION_Y);

		Log.d(TAG, "DisplayCoordinate: " + dc);
		Log.d(TAG, "Delta upperLeft to DisplayCoordinate: x:" + deltaX + " y:"
				+ deltaY);
		Coordinate center = new Coordinate(upperLeft, -deltaY, deltaX);
		Log.d(TAG, "UpperLeft: " + upperLeft + " Center: " + center);

		return center;
	}

	/**
	 * berechnet die Display-Koordinate relativ zu oberen Ecke anhand einer
	 * Koordinate
	 *
	 * @param dc
	 *            die zu konvertierende DisplayCoordinate
	 * @return
	 */
	public static DisplayCoordinate convertDisplayCoordinateToCoordinate(
			Coordinate c, Coordinate upperLeft, float levelOfDetail) {
		double deltaX = CoordinateUtility.convertDegreesToPixels(
				c.getLongitude() - upperLeft.getLongitude(), levelOfDetail,
				CoordinateUtility.DIRECTION_LONGTITUDE);
		double deltaY = CoordinateUtility.convertDegreesToPixels(
				c.getLatitude() - upperLeft.getLatitude(), levelOfDetail,
				CoordinateUtility.DIRECTION_LATITUDE);
		return new DisplayCoordinate((float) deltaX, (float) deltaY);
	}

	/**
	 * Extract Display Waypoints out of the given Route Info
	 *
	 * @param currentRoute
	 *            the required RouteInfo
	 * @return DisplayWaypoints
	 */
	public static List<DisplayWaypoint> extractDisplayWaypointsOutOfRouteInfo(
			RouteInfo currentRoute, Coordinate upperLeft, float levelOfDetail) {

		LinkedList<DisplayWaypoint> dw = new LinkedList<DisplayWaypoint>();
		for (Waypoint value : currentRoute.getWaypoints()) {

			float x = (float) (value.getLongitude() - upperLeft.getLongitude());
			Log.d("wtf", "" + x);
			float y = (float) (value.getLatitude() - upperLeft.getLatitude());
			Log.d("wtf", "" + y);

			dw.add(new DisplayWaypoint(

			CoordinateUtility.convertDegreesToPixels(x, levelOfDetail, CoordinateUtility.DIRECTION_LONGTITUDE),

			-CoordinateUtility.convertDegreesToPixels(y, levelOfDetail,
					CoordinateUtility.DIRECTION_LATITUDE),

			value.getId()));
			Log.d("d x", "" + CoordinateUtility.convertDegreesToPixels(x, levelOfDetail,
					CoordinateUtility.DIRECTION_LONGTITUDE));
			Log.d("d y", "" + CoordinateUtility.convertDegreesToPixels(y, levelOfDetail,
					CoordinateUtility.DIRECTION_LATITUDE));
		}

		return dw;
	}

	/**
	 * Extract Display Coordinates out of the given Route Info
	 *
	 * @param currentRoute
	 *            the required RouteInfo
	 * @return DisplayWaypoints
	 */
	public static List<DisplayCoordinate> extractDisplayCoordinatesOutOfRouteInfo(
			RouteInfo currentRoute, Coordinate upperLeft, float levelOfDetail) {

		LinkedList<DisplayCoordinate> dw = new LinkedList<DisplayCoordinate>();

		for (Coordinate value : currentRoute.getCoordinates()) {

			float x = (float) (value.getLongitude() - upperLeft.getLongitude());
			float y = (float) (upperLeft.getLatitude() - value.getLatitude()) ;

			dw.add(new DisplayCoordinate(

			CoordinateUtility.convertDegreesToPixels(x, levelOfDetail,
					CoordinateUtility.DIRECTION_LONGTITUDE),

			CoordinateUtility.convertDegreesToPixels(y, levelOfDetail,
					CoordinateUtility.DIRECTION_LATITUDE)

			));
		}

		return dw;
	}

	/**
	 * compute and returns the current Tile size
	 *
	 * @return the current tile size in pixel
	 */
	public static float computeCurrentTileWidthInPixels(float levelOfDetail) {
		return (float) ((256 * Math.pow(2, levelOfDetail)) / Math
				.pow(2, Math.round(levelOfDetail)));
	}
}
