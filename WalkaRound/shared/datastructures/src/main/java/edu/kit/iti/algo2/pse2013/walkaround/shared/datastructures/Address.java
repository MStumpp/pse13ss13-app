package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

public class Address {

	private String street;

	private String streetNumber;

	private String city;

	private int postalCode;

	public Address(String street, String streetNumber, String city,
			int postalCode) {
		this.street = street;
		this.streetNumber = streetNumber;
		this.city = city;
		this.postalCode = postalCode;
	}

	public String getStreet() {
		return street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public String getCity() {
		return city;
	}

	public int getPostalCode() {
		return postalCode;
	}

}
