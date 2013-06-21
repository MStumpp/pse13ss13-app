package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a Coordinate consisting of longitude and latitude.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public final class Coordinate {

    /**
     * longitude of this Coordinate.
     */
    private final double lon;


    /**
     * latitude of this Coordinate.
     */
	private final double lat;


    /**
     * CrossingInformation for this Coordinate.
     */
    private final CrossingInformation crossInfo;


    /**
     * Creates an instance of Coordinate.
     *
     * @param lon Longitude of the Coordinate.
     * @param lat Latitude of the Coordinate.
     * @throws IllegalArgumentException If longitude or latitude is not within some common range.
     */
	public Coordinate(double lon, double lat) {
        this(lon, lat, null);
	}


    /**
     * Creates an instance of Coordinate.
     *
     * @param lon Longitude of the Coordinate.
     * @param lat Latitude of the Coordinate.
     * @param crossInfo CrossingInformation for this Coordinate.
     * @throws IllegalArgumentException If longitude or latitude is not within some common range.
     */
    public Coordinate(double lon, double lat, CrossingInformation crossInfo) {
        if (Math.abs(lon) > 180)
            throw new IllegalArgumentException("longitude has to be between -180 and 180");
        if (Math.abs(lat) > 90)
            throw new IllegalArgumentException("latitude has to be between -90 and 90");
        this.lon = lon;
        this.lat = lat;
        this.crossInfo = crossInfo;
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
	public double getLatitude() {
		return lat;
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