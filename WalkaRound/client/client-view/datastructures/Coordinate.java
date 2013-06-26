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
    private final double lat;


	/**
	 * longitude of this Coordinate.
	 */
	private final double lon;


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


	/**
	 * Creates an instance of Coordinate.
	 *
     * @param lat Latitude of the Coordinate.
	 * @param lon Longitude of the Coordinate.
	 * @param crossInfo CrossingInformation for this Coordinate.
	 * @throws IllegalArgumentException If longitude or latitude is not within some common range.
	 */
	public Coordinate(double lat, double lon, CrossingInformation crossInfo) {
		if (Math.abs(lat) > 90.d)
			throw new IllegalArgumentException("latitude has to be between -90 and 90");
        if (Math.abs(lon) > 180.d)
            throw new IllegalArgumentException("longitude has to be between -180 and 180");
        this.lat = lat;
		this.lon = lon;
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
	 * Returns CrossingInformation for this Coordinate.
	 *
	 * @return CrossingInformation.
	 */
	public CrossingInformation getCrossingInformation() {
		return crossInfo;
	}

}