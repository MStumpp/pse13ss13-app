package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * This class represents an egde contained in a graph. An edge has two coordinates,
 * each represented by longitude and latitude values.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public final class Edge {

    /**
     * longitude 1.
     */
    private final double lon1;


    /**
     * latitude 1.
     */
    private final double lat1;


    /**
     * longitude 2.
     */
    private final double lon2;


    /**
     * latitude 2.
     */
    private final double lat2;


    /**
     * Creates an instance of Edge.
     *
     * @param lon1 Longitude 1 of the Edge.
     * @param lat1 Latitude 1 of the Edge.
     * @param lon2 Longitude 2 of the Edge.
     * @param lat2 Latitude 2 of the Edge.
     * @throws IllegalArgumentException If longitude or latitude is not within some common range.
     */
    public Edge(double lon1, double lat1, double lon2, double lat2) {
        if (Math.abs(lon1) > 180.d || Math.abs(lon2) > 180.d)
            throw new IllegalArgumentException("longitude has to be between -180 and 180");
        if (Math.abs(lat1) > 90.d || Math.abs(lat2) > 90.d)
            throw new IllegalArgumentException("latitude has to be between -90 and 90");
        this.lon1 = lon1;
        this.lat1 = lat1;
        this.lon2 = lon2;
        this.lat2 = lat2;
    }


    /**
     * Creates an instance of Edge.
     *
     * @param coordinate1 Coordinate 1 of the Edge.
     * @param coordinate2 Coordinate 2 of the Edge.
     * @throws IllegalArgumentException If longitude or latitude is not within some common range.
     */
    public Edge(Coordinate coordinate1, Coordinate coordinate2) {
        if (coordinate1 == null || coordinate2 == null)
            throw new IllegalArgumentException("coordinate 1 and coordinate 2 must not be null");
        this.lon1 = coordinate1.getLongtitude();
        this.lat1 = coordinate1.getLatitude();
        this.lon2 = coordinate2.getLongtitude();
        this.lat2 = coordinate2.getLatitude();
    }


    /**
     * Returns longitude 1 of this Edge.
     *
     * @return double.
     */
    public double getLongitude1() {
        return lon1;
    }


    /**
     * Returns latitude 1 of this Edge.
     *
     * @return double.
     */
    public double getLatitude1() {
        return lat1;
    }


    /**
     * Returns longitude 2 of this Edge.
     *
     * @return double.
     */
    public double getLongitude2() {
        return lon2;
    }


    /**
     * Returns latitude 2 of this Edge.
     *
     * @return double.
     */
    public double getLatitude2() {
        return lat2;
    }

}
