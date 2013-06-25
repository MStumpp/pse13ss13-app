package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;

/**
 * Diese Klasse verindet eine DisplayCoordinate mit einem Waypoint
 * 
 * @author Ludwig Biermann
 *
 */
public class DisplayWaypoint extends DisplayCoordinate {
	
	/**
	 * Der Waypoint
	 */
	private int id;
	
	/**
	 * Erstellt ein Displaywaypoint.
	 * 
	 * @param x Koordinate
	 * @param y Koordinate
	 * @param waypoint der Wegpunkt
	 */
	public DisplayWaypoint(float x, float y, int id) {
		super(x, y);
		this.id = id;
	}
	
	/**
	 * Gibt den Wegpunkt zurück
	 * 
	 * @return Wegpunkt
	 */
	public int  getId() {
		return this.id;
	}

}
