package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to transfer route info between client and server.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public final class RouteInfoTransfer {

    /**
     * list of coordinates.
     */
    private final List<Coordinate> coordinates;


    /**
     * Creates an instance of RouteInfoTransfer.
     *
     * @param coordinates List of coordinates representing this route.
     */
    public RouteInfoTransfer(List<Coordinate> coordinates) {
        if (coordinates == null || coordinates.size() < 2)
            throw new IllegalArgumentException("list of coordinates must be provided and at least two elements long");
        this.coordinates = new LinkedList<Coordinate>(coordinates);
    }


    /**
     * Returns a list of all Coordinates of a route.
     *
     * @return List<Coordinate>.
     */
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }


    /**
     * Returns a list of all Waypoints of a route.
     *
     * @return List<Waypoint>.
     */
    public List<Waypoint> getWaypoints() {
        List<Waypoint> result = new LinkedList<Waypoint>();
        for (Coordinate coordinate : coordinates)
            if (coordinate instanceof Waypoint)
                result.add((Waypoint) coordinate);
        return result;
    }

}