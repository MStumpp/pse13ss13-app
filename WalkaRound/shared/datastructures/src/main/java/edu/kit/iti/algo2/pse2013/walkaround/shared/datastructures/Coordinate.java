package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;


/**
 * This class represents a Coordinate consisting of longitude and latitude.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Coordinate implements Geometrizable {

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
     * Creates an instance of Coordinate based on a reference Coordinate
     * and deltas for longitude and latitude.
     *
     * @param reference Reference Coordinate.
     * @param latDelta Latitude delta of the Coordinate.
     * @param lonDelta Longitude delta of the Coordinate.
     */
	public Coordinate(final Coordinate reference, final double latDelta, final double lonDelta) {
		this(reference.getLatitude() + latDelta, reference.getLongitude() + lonDelta);
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
		setLongitude(lon);
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


	/**
	 * Returns latitude of this Coordinate.
	 *
	 * @return double.
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
	 * Sets the longitude attribute of this Coordinate.
     *
     * @param lon Longitude value.
     */
	public void setLongitude(double lon) {
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


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable#valueForDimension()
     */
    public int numberDimensions() {
        return 2;
    }


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable#valueForDimension(int)
     */
    public double valueForDimension(int dim) {
        if (dim == 0)
            return getLatitude();
        else
            return getLongitude();
    }


	@Override
	public String toString() {
		return String.format("Coordinate latitude: %.8f° longitude %.8f°", lat, lon);
	}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (Double.compare(that.lat, lat) != 0) return false;
        if (Double.compare(that.lon, lon) != 0) return false;

        return true;
    }


    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public Coordinate clone() {
    	Coordinate clonedCoordinate;
    	if (this.crossInfo == null) {
    		clonedCoordinate = new Coordinate (this.lat, this.lon);
    	} else {
    		clonedCoordinate = new Coordinate(this.lat, this.lon, this.crossInfo.clone());
    	}
    	return clonedCoordinate;
    }
    
    
}