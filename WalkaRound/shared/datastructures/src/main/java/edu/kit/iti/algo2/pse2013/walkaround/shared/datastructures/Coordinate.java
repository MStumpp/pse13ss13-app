package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable;

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
	private double latitude;


	/**
	 * longitude of this Coordinate.
	 */
	private double longitude;


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


	/**
	 * Returns latitude of this Coordinate.
	 *
	 * @return double.
	 */
	public void setLatitude(double lat) {
		if (lat > 90) {
			this.latitude = -90 + lat % 90;
		} else if (lat < -90) {
			this.latitude = 90 + lat % 90;
		} else {
			this.latitude = lat;
		}
	}


	/**
	 * Sets the longitude attribute of this Coordinate.
     *
     * @param lon Longitude value.
     */
	public void setLongitude(double lon) {
		if (lon > 180) {
			this.longitude = -180 + lon % 180;
		} else if (lon < -180) {
			this.longitude = 180 + lon % 180;
		} else {
			this.longitude = lon;
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
		String crossroads = "";
		if (getCrossingInformation() != null) {
			for (float cross : getCrossingInformation().getCrossingAngles()) {
				crossroads += cross + " ";
			}
		}
		return String.format(
			"Coord(%.5f° %s %.5f° %s | Crossings: %s)",
			latitude,  latitude > 0 ? "N" : "S",
			longitude, longitude > 0 ? "E" : "W",
			crossroads.trim()
		);
	}


    @Override
    public Coordinate clone() {
    	Coordinate clonedCoordinate;
    	if (this.crossInfo == null) {
    		clonedCoordinate = new Coordinate (this.latitude, this.longitude);
    	} else {
    		clonedCoordinate = new Coordinate(this.latitude, this.longitude, this.crossInfo.clone());
    	}
    	return clonedCoordinate;
    }


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crossInfo == null) ? 0 : crossInfo.hashCode());
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Coordinate)) {
			return false;
		}
		Coordinate other = (Coordinate) obj;
		if (crossInfo == null) {
			if (other.crossInfo != null) {
				return false;
			}
		} else if (!crossInfo.equals(other.crossInfo)) {
			return false;
		}
		double epsilon = 1e-323;
		if (Math.abs(Math.abs(latitude) - Math.abs(other.latitude)) > epsilon) {
			return false;
		}
		if (Math.abs(Math.abs(longitude) - Math.abs(other.longitude)) > epsilon) {
			return false;
		}
		return true;
	}


    // Geometrizable Interface

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

        if (dim < 0 || dim > numberDimensions()-1)
            throw new IllegalArgumentException("dim out of range");

        if (dim == 0)
            return getLatitude();
        else
            return getLongitude();
    }


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable#numberNodes()
     */
    public int numberNodes() {
        return 1;
    }


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable#valueForDimension()
     */
    public double valueForDimension(int nodeNumber, int dim) {
        if (nodeNumber < 0 || (nodeNumber > numberNodes()-1))
            throw new IllegalArgumentException("node number out of range");
        return valueForDimension(dim);
    }

}