package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * RouteInfoTransfer for server only.
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
     * Error message.
     */
    private String error;


    /**
     * Route length.
     */
    private double length;


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


    /**
     * Returns the error message.
     *
     * @return String Error message.
     */
    public String getError() {
        return error;
    }


    /**
     * Set the error message.
     *
     * @param error Error message.
     */
    public void setError(String error) {
        this.error = error;
    }


    /**
     * Returns the length of the route.
     *
     * @return double Length of the route.
     */
    public double getLength() {
        return length;
    }


    /**
     * Set the length of the route.
     *
     * @param length Length of the route.
     */
    public void setLength(double length) {
        this.length = length;
    }

}