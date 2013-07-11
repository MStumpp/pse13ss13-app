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
public interface RouteInfo {

    public Waypoint getStart();

    public Waypoint getEnd();

    public LinkedList<Waypoint> getWaypoints();

    public LinkedList<Coordinate> getCoordinates();

    public boolean containsWaypoint(Waypoint wp);

}
