package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * Diese Klasse verindet eine DisplayCoordinate mit einem Point of Interest
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
	 * Erstellt ein Displaywaypoint.
	 * 
	 * @param x Koordinate
	 * @param y Koordinate
	 * @param waypoint der Wegpunkt
	 */
	public DisplayPOI(float x, float y, int id) {
		super(x, y);
		this.id = id;
	}
	
	/**
	 * gibt den POI zurück
	 * 
	 * @return POI
	 */
	public int getId() {
		return this.id;
	}

}
