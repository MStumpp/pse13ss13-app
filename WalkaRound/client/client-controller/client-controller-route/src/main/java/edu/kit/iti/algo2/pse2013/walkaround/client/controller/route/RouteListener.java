package edu.kit.iti.algo2.pse2013.walkaround.client.controller.route;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public interface RouteListener {
	
	
	public void onRouteChange(RouteInfo currentRoute, Waypoint activeWaypoint);
	
	
}