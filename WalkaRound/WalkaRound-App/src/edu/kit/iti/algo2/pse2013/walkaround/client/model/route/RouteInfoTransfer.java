package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import java.util.LinkedList;
import java.util.List;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

/**
 * RouteInfoTransfer for client only.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class RouteInfoTransfer {

	/**
	 * list of coordinates.
	 */
	private List<Coordinate> coordinates;


	/**
	 * Error message.
	 */
	private String error;


	/**
	 * Creates an instance of RouteInfoTransfer.
	 */
	public RouteInfoTransfer() {
		coordinates = new LinkedList<Coordinate>();
	}


	/**
	 * Creates an instance of RouteInfoTransfer.
	 *
	 * @param coordinates List of coordinates representing this route.
	 */
	public RouteInfoTransfer(List<Coordinate> coordinates) {
		if (coordinates == null)
			throw new IllegalArgumentException("list of coordinates must be provided");
		this.coordinates = new LinkedList<Coordinate>();
		this.coordinates.addAll(coordinates);
	}


	/**
	 * Adds a Coordinate to the list of Coordinates.
	 * @param coordinate the coordinate to be added
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
	 * Sets the Coordinates.
	 *
	 * @param coordinates List of Coordinates.
	 */
	public void setCoordinates(List<Coordinate> coordinates) {
		this.coordinates = coordinates;
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
	 * Post processes shortest path.
	 * @param targetName
	 * @param sourceName
	 */
	public void postProcessShortestPath(String sourceName, String targetName) {
		if (coordinates.size() < 2)
			return;
		Coordinate source = coordinates.remove(0);
		coordinates.add(0, new Waypoint(source.getLatitude(), source.getLongitude(), sourceName));
		Coordinate target = coordinates.remove(coordinates.size()-1);
		coordinates.add(coordinates.size(), new Waypoint(target.getLatitude(), target.getLongitude(), targetName));
	}


	/**
	 * Post processes roundtrip.
	 */
	public void postProcessRoundtrip() {
		if (coordinates.size() < 2)
			return;

		Coordinate source = coordinates.remove(0);
		coordinates.add(0, new Waypoint(source.getLatitude(), source.getLongitude(), "Source"));
		// Coordinate target = coordinates.remove(coordinates.size()-1);
		// coordinates.add(coordinates.size(), coordinates.get(0));
	}

}
