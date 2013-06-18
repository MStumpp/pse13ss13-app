package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

public class Coordinate {
	
	private double longitude;
	
	private double latitude;
	
	CrossingInformation crossing;

	public Coordinate(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public CrossingInformation getCrossing() {
		return crossing;
	}
}
