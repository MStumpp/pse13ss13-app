package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint;

import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Coordinate.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
@XmlRootElement
public class Coordinate implements Geometrizable {

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


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable#valueForDimension()
     */
    public int numberDimensions() {
        return 2;
    }


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable#valueForDimension(int)
     */
    public double valueForDimension(int dim) {
        if (dim == 0)
            return getLatitude();
        else
            return getLongitude();
    }

}
