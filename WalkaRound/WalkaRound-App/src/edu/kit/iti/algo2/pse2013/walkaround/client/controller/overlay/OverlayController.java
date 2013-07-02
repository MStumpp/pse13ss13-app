package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

public class OverlayController {
	private static OverlayController me;
	private static final String TAG = OverlayController.class.getSimpleName();

	/**
	 * Private constructor to prevent instantiation
	 */
	private OverlayController() {}
	/**
	 * Singleton-method
	 * @return everytime the same instance of the overlay-controller
	 */
	public static OverlayController getInstance() {
		if (me == null) {
			me = new OverlayController();
		}
		return me;
	}
	public boolean showPOIInfo(POI poi) {
		Log.d(TAG, "Entered showPOIInfo(POI)-stub. Always returns false.");
		return false;
	}
	public boolean showFavorite() {
		Log.d(TAG, "Entered showFavorite()-stub. Always returns false.");
		return false;
	}
}
