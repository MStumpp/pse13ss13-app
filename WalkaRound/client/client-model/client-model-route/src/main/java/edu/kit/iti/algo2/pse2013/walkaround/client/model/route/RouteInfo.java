package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public interface RouteInfo {
	
	public String getname();
	
	public Waypoint getStart();
	
	public Waypoint getEnd();
	
	public Waypoint getActiveWaypoint();
	
	public LinkedList<Route> getRoutes();
	
	public LinkedList<Waypoint> getWaypoints();
	
	public boolean containsWaypoint(Waypoint wp);
	
	public boolean isFavorite();
	
	
}
