package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

/**
 * Diese Klasse repräsentiert ein Punkt auf dem Display des Android-Smartphones.
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
	 * @param x x-Koordinate (von oben nach unten, in dip)
	 * @param y y-Koordinate (von links nach rechts, in dip)
	 */
	public DisplayCoordinate(float x, float y) {
		setX(x);
		setY(y);
	}

	/**
	 * Setzt den x-Teil der Koordinate
	 *
	 * @param x x-Anteil der Koordinate
	 */
	public void setX(float x){
		this.x = x;
	}

	/**
	 * Setzt den y-Teil der Koordinate
	 *
	 * @param y y-Anteil der Koordinate
	 */
	public void setY(float y){
		this.y = y;
	}

	/**
	 * Gibt den x-Anteil der Koordinate zurück
	 *
	 * @return x x-Anteil der Koordinate
	 */
	public float getX(){
		return this.x;
	}

	/**
	 * Gibt den y-Anteil der Koordinate zurück
	 *
	 * @return y Achse
	 */
	public float getY(){
		return this.y;
	}
}