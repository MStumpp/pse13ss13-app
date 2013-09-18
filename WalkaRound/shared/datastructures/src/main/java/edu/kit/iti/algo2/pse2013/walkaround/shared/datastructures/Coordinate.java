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
	private double latitude;


	/**
	 * longitude of this Coordinate.
	 */
	private double longitude;


	/**
	 * CrossingInformation for this Coordinate.
	 */
	private CrossingInformation crossInfo;


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
		this.crossInfo = this.crossInfo == null ? null : this.crossInfo.clone();
	}
	
	/**
	 * Creates an instance of Coordinate with the position of the given waypoint.
	 * @param waypoint
	 */
	public Coordinate(Coordinate coord) {
		this(coord.getLatitude(), coord.getLongitude(),
				coord.getCrossingInformation() == null ? null : coord.getCrossingInformation().clone());
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
	 * Sets latitude and longitude attributes of the Coordinate to those of the given Coordinate.
	 * @param newPosition
	 */
	public void setPosition(Coordinate newPosition) {
		if (newPosition != null) {
			this.setLatitude(newPosition.getLatitude());
			this.setLongitude(newPosition.getLongitude());
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
	/**
	 * Returns CrossingInformation for this Coordinate.
	 *
	 * @return CrossingInformation.
	 */
	public void setCrossingInformation(CrossingInformation ci) {
		this.crossInfo = ci;
	}


	@Override
	public String toString() {
		String crossroads = "";
		if (getCrossingInformation() != null) {
			for (float cross : getCrossingInformation().getCrossingAngles()) {
				crossroads += cross + " ";
			}
		}
		String output = "Coord(" + this.latitude + " " + (latitude > 0 ? "N" : "S") + ", " 
				+ this.longitude + " " + (longitude > 0 ? "E" : "W") + " | Crossings: " + crossroads.trim() + ")";
		
		// Replaced old version, because %f was not exact enough for testing:
		/*
		return String.format(
			"Coord(%f %s %f %s | Crossings: %s)",
			latitude,  latitude > 0 ? "N" : "S",
			longitude, longitude > 0 ? "E" : "W",
			crossroads.trim()
		);
		*/
		return output;
	}


    @Override
    public Coordinate clone() {
    	return new Coordinate(this);
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
		double epsilon = 1e-323;
		if (Math.abs(Math.abs(latitude) - Math.abs(other.latitude)) > epsilon) {
			return false;
		}
		if (Math.abs(Math.abs(longitude) - Math.abs(other.longitude)) > epsilon) {
			return false;
		}
		return true;
	}

}