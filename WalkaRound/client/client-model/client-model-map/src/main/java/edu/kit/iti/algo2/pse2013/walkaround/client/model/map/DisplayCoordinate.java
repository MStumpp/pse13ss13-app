package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

/**
 * Diese Klasse repräsentiert ein Punkt auf dem Display des Android Handy/ Tablet.
 * 
 * @author Ludwig Biermann
 *
 */
public class DisplayCoordinate {

	// X Coordinate
	private float x;
	
	// Y Coordinate
	private float y;
	
	/**
	 * Erstellt eine Display Koordinate
	 * 
	 * @param x Koordinate
	 * @param y Koordinate
	 */
	public DisplayCoordinate(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Setzt die x Achse
	 * 
	 * @param X Achse
	 */
	public void setX(float x){
		this.x = x;
	}
	
	/**
	 * Setzt die Y achse
	 * 
	 * @param y Achse
	 */
	public void setY(float y){
		this.y = y;
	}
	
	/**
	 * Gibt die X Achse zurück
	 * 
	 * @return x Achse
	 */
	public float getX(){
		return this.x;
	}
	
	/**
	 * Gibt die Y Achse zurück.
	 * 
	 * @return y Achse
	 */
	public float getY(){
		return this.y;
	}
}
