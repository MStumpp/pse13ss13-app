package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a Coordinate consisting of longitude and latitude.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Coordinate {

	/**
	 * latitude of this Coordinate.
	 */
	private double lat;


	/**
	 * longtitude of this Coordinate.
	 */
	private double lon;


	/**
	 * CrossingInformation for this Coordinate.
	 */
	private final CrossingInformation crossInfo;


	/**
	 * Creates an instance of Coordinate.
	 *
	 * @param lat Latitude of the Coordinate.
	 * @param lon Longitude of the Coordinate.
	 * @throws IllegalArgumentException If longitude or latitude is not within some common range.
	 */
	public Coordinate(double lat, double lon) {
		this(lat, lon, null);
	}
	public Coordinate(Coordinate reference, double latDelta, double lonDelta) {
		this(reference.getLatitude() + latDelta, reference.getLongtitude() + lonDelta);
	}


	/**
	 * Creates an instance of Coordinate.
	 *
	 * @param lat Latitude of the Coordinate.
	 * @param lon Longtitude of the Coordinate.
	 * @param crossInfo CrossingInformation for this Coordinate.
	 * @throws IllegalArgumentException If longitude or latitude is not within some common range.
	 */
	public Coordinate(double lat, double lon, CrossingInformation crossInfo) {
		setLatitude(lat);
		setLongtitude(lon);
		
		this.crossInfo = crossInfo;
	}

	/**
	 * Returns latitude of this Coordinate.
	 *
	 * @return double.
	 */
	public double getLatitude() {
		return lat;
	}

	/**
	 * Returns longitude of this Coordinate.
	 *
	 * @return double.
	 */
	public double getLongtitude() {
		return lon;
	}

	/**
	 * Returns latitude of this Coordinate.
	 *
	 * @return double.
	 */
	public void setLatitude(double lat) {
		this.lat = (lat + 90) % 180 - 90;
	}

	/**
	 * Returns longtitude of this Coordinate.
	 *
	 * @return double.
	 */
	public void setLongtitude(double lon) {
		this.lon = (lon + 180) % 360 - 180;
	}

	/**
	 * Returns CrossingInformation for this Coordinate.
	 *
	 * @return CrossingInformation.
	 */
	public CrossingInformation getCrossingInformation() {
		return crossInfo;
	}

	@Override
	public String toString() {
		return "Coordinate Latidude: " + this.lat + " Longitude " + this.lon;
	}
}