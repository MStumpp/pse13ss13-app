package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint.client;

import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

public class Route implements RouteInfo {

	private LinkedList<Coordinate> routeCoordinates;

    public Route(LinkedList<Coordinate> coordsOfNewRoute) {
        this.routeCoordinates = coordsOfNewRoute;
    }

	@Override
	public LinkedList<Coordinate> getCoordinates() {
		return routeCoordinates;
	}

    @Override
    public LinkedList<Waypoint> getWaypoints() {
        LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>();
        for (Coordinate coord : this.routeCoordinates) {
            if (coord instanceof Waypoint) {
                waypoints.add((Waypoint) coord);
            }
        }
        return waypoints;
    }

}