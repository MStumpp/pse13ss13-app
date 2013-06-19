package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

public class Location extends Coordinate {

	private String name;

	private int id;

	private Address address;

	public Location(double longitude, double latitude, String name, int id,
			Address address) {
		super(longitude, latitude);
		this.name = name;
		this.id = id;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public Address getAddress() {
		return address;
	}

	public boolean isMoveable() {
		return false;
	}

	public boolean isFavorite() {
		return false;
	}
}
