package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.io.Serializable;

/**
 * This class represents a Coordinate consisting of longitude and latitude.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Coordinate implements Serializable {

    /**
     * Temporary Serial version ID as long as Java serialization is used
     */
    private static final long serialVersionUID = -4228234461207025121L;


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


    // TODO: Wofür brauchen wir das? (Matthias)
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
     * Sets the latitude attribute of this Coordinate.
     *
     * @param lat Latitude value.
     */
    public void setLatitude(double lat) {
        if (lat > 90) {
            this.lat = -90 + lat % 90;
        } else if (lat < -90) {
            this.lat = 90 + lat % 90;
        } else {
            this.lat = lat;
        }
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
	 * Sets the longtitude attribute of this Coordinate.
     *
     * @param lon Longitude value.
     */
	public void setLongtitude(double lon) {
		if (lon > 180) {
			this.lon = -180 + lon % 180;
		} else if (lon < -180) {
			this.lon = 180 + lon % 180;
		} else {
			this.lon = lon;
		}
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
		return "Coordinate latidude: " + lat + " longitude " + lon;
	}

}