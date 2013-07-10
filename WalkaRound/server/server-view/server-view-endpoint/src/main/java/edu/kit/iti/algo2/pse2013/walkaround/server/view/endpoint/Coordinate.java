package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Coordinate.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
@XmlRootElement
public class Coordinate {

    /**
     * latitude of this Coordinate.
     */
    private double lat;


    /**
     * longitude of this Coordinate.
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
    public Coordinate(final double lat, final double lon) {
        this(lat, lon, null);
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
    public double getLongitude() {
        return lon;
    }

}
