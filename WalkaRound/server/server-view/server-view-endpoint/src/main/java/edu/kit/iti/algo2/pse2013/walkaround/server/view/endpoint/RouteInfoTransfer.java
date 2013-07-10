package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to transfer route info between client and server.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
@XmlRootElement
public final class RouteInfoTransfer {

    /**
     * list of coordinates.
     */
    private final List<Coordinate> coordinates;


    /**
     * Creates an instance of RouteInfoTransfer.
     */
    public RouteInfoTransfer() {
        coordinates = new ArrayList<Coordinate>();
    }


    /**
     * Creates an instance of RouteInfoTransfer.
     *
     * @param coordinates List of coordinates representing this route.
     */
    public RouteInfoTransfer(List<Coordinate> coordinates) {
        if (coordinates == null)
            throw new IllegalArgumentException("list of coordinates must be provided");
        this.coordinates = new ArrayList<Coordinate>();
        this.coordinates.addAll(coordinates);
    }


    /**
     * Adds a Coordinate to the list of Coordinates.
     */
    public void addCoordinates(Coordinate coordinate) {
        coordinates.add(coordinate);
    }


    /**
     * Returns a list of all Coordinates of a route.
     *
     * @return List<Coordinate>.
     */
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

}