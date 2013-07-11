package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;
import edu.kit.iti.algo2.pse2013.walkaround.shared.route.RouteInfo;

public interface RouteListener {
	
	
	public void onRouteChange(RouteInfo currentRoute);
	
	
}
