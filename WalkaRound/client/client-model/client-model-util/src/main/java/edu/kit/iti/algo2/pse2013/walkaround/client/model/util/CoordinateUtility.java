package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

import android.view.Display;
import android.graphics.Point;

public final class CoordinateUtility {

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

	public static double calculateDifferenceInMeters(Coordinate firstCoord,
			Coordinate secondCoord) {
		return 0;
	}
}
