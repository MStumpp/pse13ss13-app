package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint.client;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

import java.util.LinkedList;

/**
 * Primarily for testing purposes.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Route implements RouteInfo {

    private LinkedList<Coordinate> routeCoordinates;

    public Route(LinkedList<Coordinate> coordsOfNewRoute) {
        this.routeCoordinates = coordsOfNewRoute;
    }

    public Waypoint getStart() {
        if (this.routeCoordinates.size() > 0) {
            return this.getWaypoints().getFirst();
        }
        return null;
    }

    public Waypoint getEnd() {
        if (this.routeCoordinates.size() > 0) {
            return this.getWaypoints().getLast();
        }
        return null;
    }

    public LinkedList<Waypoint> getWaypoints() {
        LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>();
        for (Coordinate coord : this.routeCoordinates) {
            if (coord instanceof Waypoint) {
                waypoints.add((Waypoint) coord);
            }
        }
        return waypoints;
    }

    public boolean containsWaypoint(Waypoint wp) {
        if (this.getWaypoints().contains(wp)) {
            return true;
        }
        return false;
    }

    public LinkedList<Coordinate> getCoordinates() {
        return this.routeCoordinates;
    }

}
