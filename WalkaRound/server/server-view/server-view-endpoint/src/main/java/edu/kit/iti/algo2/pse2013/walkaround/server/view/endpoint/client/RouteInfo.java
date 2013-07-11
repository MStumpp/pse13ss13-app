package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint.client;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public interface RouteInfo {

	public LinkedList<Waypoint> getWaypoints();

	public LinkedList<Coordinate> getCoordinates();

}