package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class POIInfoController {
	private static POIInfoController me;
	private POIInfoController() {}
	public static POIInfoController getInstance() {
		if (me == null) {
			me = new POIInfoController();
		}
		return me;
	}
	public void addPOIToFavorites(POI poi) {
		RouteController.getInstance().addLocationToFavorites(poi, poi.getName());
	}
	public void addPOIToRoute(POI poi) {
		RouteController.getInstance().addWaypoint(new Waypoint(poi.getLatitude(), poi.getLongitude(), poi.getName(), poi.getAddress()));
	}
}
