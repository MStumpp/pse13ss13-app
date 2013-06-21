package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.DisplayCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

import android.util.Log;
import android.view.Display;
import android.app.Activity;
import android.graphics.Point;

public final class CoordinateUtility {

	/**
	 * The average earth radius according to WGS84
	 */
	private static double EARTH_RADIUS = 6371.000785;

	private CoordinateUtility() {

	}

	public static DisplayCoordinate coordinateToDisplayCoordinate(
			Coordinate coord, Coordinate anchor) {
		return null;
	}

	public static Coordinate displayCoordinateToCoordinate(
			DisplayCoordinate dispCoord, Coordinate anchor) {
		return null;
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
		double zeta = Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1));
		return zeta*EARTH_RADIUS;
	}
}
