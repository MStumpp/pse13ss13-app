package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public interface RouteInfo {

	//public String getName();

	public Waypoint getStart();

	public Waypoint getEnd();

	public Waypoint getActiveWaypoint();

	public LinkedList<Waypoint> getWaypoints();

	public LinkedList<Coordinate> getCoordinates();

	public boolean containsWaypoint(Waypoint wp);
	
	public RouteInfo clone();

	public Waypoint getWaypoint(int id);

}