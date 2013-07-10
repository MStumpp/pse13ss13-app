package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

/**
 * This class connect DisplayCoordinate with a Waypoint
 * 
 * @author Ludwig Biermann
 *
 */
public class DisplayWaypoint extends DisplayCoordinate {
	
	/**
	 * The id of the waypoint
	 */
	private int id;
	
	/**
	 * Construct a new DisplayWaypoint
	 * 
	 * @param x Coordinate
	 * @param y Coordinate
	 * @param id of the waypoint
	 */
	public DisplayWaypoint(float x, float y, int id) {
		super(x, y);
		this.id = id;
	}
	
	/**
	 * Gives the id of the Waypoint back
	 * 
	 * @return the id of the Waypoint
	 */
	public int  getId() {
		return this.id;
	}

}
