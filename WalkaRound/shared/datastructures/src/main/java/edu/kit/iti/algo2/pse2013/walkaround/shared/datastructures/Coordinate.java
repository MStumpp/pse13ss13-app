package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

public class Coordinate {

	private double latitude;

	private double longitude;

	public Coordinate(double lat, double lon) {
		if (Math.abs(lat) > 90) {
			throw new IllegalArgumentException(
					"Latitude has to be between -90 and 90");
		}
		if (Math.abs(lon) > 180) {
			throw new IllegalArgumentException(
					"Longtitude has to be between -180 and 180");
		}
		this.latitude = lat;
		this.longitude = lon;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongtitude() {
		return longitude;
	}
}