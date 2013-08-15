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
    private double latitude;


    /**
     * longitude of this Coordinate.
     */
    private double longitude;


    /**
     * CrossingInformation for this Coordinate.
     */
    private CrossingInformation crossingInformation;


    /**
     * Default constructor.
     */
    public Coordinate() {}


    /**
     * Creates an instance of Coordinate.
     *
     * @param latitude Latitude of the Coordinate.
     * @param longitude Longitude of the Coordinate.
     * @throws IllegalArgumentException If longitude or latitude is not within some common range.
     */
    public Coordinate(double latitude, double longitude) {
        this(latitude, longitude, null);
    }


    /**
     * Creates an instance of Coordinate.
     *
     * @param latitude Latitude of the Coordinate.
     * @param longitude Longtitude of the Coordinate.
     * @param crossingInformation CrossingInformation for this Coordinate.
     * @throws IllegalArgumentException If longitude or latitude is not within some common range.
     */
    public Coordinate(double latitude, double longitude, CrossingInformation crossingInformation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.crossingInformation = crossingInformation;
    }


    /**
     * Returns latitude of this Coordinate.
     *
     * @return double.
     */
    public double getLatitude() {
        return latitude;
    }


    /**
     * Returns longitude of this Coordinate.
     *
     * @return double.
     */
    public double getLongitude() {
        return longitude;
    }


    public CrossingInformation getCrossingInformation() {
        return crossingInformation;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public void setCrossingInformation(CrossingInformation crossingInformation) {
        this.crossingInformation = crossingInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.longitude, longitude) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
