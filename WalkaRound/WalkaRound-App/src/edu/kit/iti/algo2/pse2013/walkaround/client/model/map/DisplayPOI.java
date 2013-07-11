package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;

/**
 * This class connect a DisplayCoordinate with a Point of Interest
 *
 * @author Ludwig Biermann
 *
 */
public class DisplayPOI extends DisplayCoordinate {

	/**
	 * Der POI
	 */
	private int id;

	/**
	 * Construct a new DisplayCoordinate
	 *
	 * @param x Coordinate
	 * @param y Coordinate
	 * @param id Id of the Point of Interest
	 */
	public DisplayPOI(float x, float y, int id) {
		super(x, y);
		this.id = id;
	}

	/**
	 * gives the id of Point of Interest back
	 *
	 * @return the id of Point of Interest
	 */
	public int getId() {
		return this.id;
	}

}
