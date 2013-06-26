package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

/**
 * This class represents an egde contained in a graph. An edge has two coordinates,
 * each represented by longitude and latitude values.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public final class Edge {

    /**
     * latitude 1.
     */
    private final double lat1;


    /**
     * longitude 1.
     */
    private final double lon1;


    /**
     * latitude 2.
     */
    private final double lat2;


    /**
     * longitude 2.
     */
    private final double lon2;


    /**
     * id.
     */
    private final int id;


    /**
     * id counter.
     */
    private static int idCounter = 0;


    /**
     * Creates an instance of Edge.
     *
     * @param lat1 Latitude 1 of the Edge.
     * @param lon1 Longitude 1 of the Edge.
     * @param lat2 Latitude 2 of the Edge.
     * @param lon2 Longitude 2 of the Edge.
     * @throws IllegalArgumentException If longitude or latitude is not within some common range.
     */
    public Edge(double lat1, double lon1, double lat2, double lon2) {
        if (Math.abs(lat1) > 90.d || Math.abs(lat2) > 90.d)
            throw new IllegalArgumentException("latitude has to be between -90 and 90");
        if (Math.abs(lon1) > 180.d || Math.abs(lon2) > 180.d)
            throw new IllegalArgumentException("longitude has to be between -180 and 180");
        this.lat1 = lat1;
        this.lon1 = lon1;
        this.lat2 = lat2;
        this.lon2 = lon2;
        id = idCounter;
        idCounter++;
    }


    /**
     * Returns the id of Edge.
     *
     * @return int.
     */
    public int getID() {
        return id;
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
     * Returns longitude 1 of this Edge.
     *
     * @return double.
     */
    public double getLongitude1() {
        return lon1;
    }


    /**
     * Returns latitude 2 of this Edge.
     *
     * @return double.
     */
    public double getLatitude2() {
        return lat2;
    }


    /**
     * Returns longitude 2 of this Edge.
     *
     * @return double.
     */
    public double getLongitude2() {
        return lon2;
    }


	public int getLength() {
		// TODO Auto-generated method stub
		return 0;
	}


	public Vertex getTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
