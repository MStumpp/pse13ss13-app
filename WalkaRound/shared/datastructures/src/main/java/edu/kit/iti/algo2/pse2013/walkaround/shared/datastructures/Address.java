package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents an address.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public class Address {

	/**
	 * Name of the street.
	 */
	private String street;

	/**
	 * Number of the building in the street.
	 */
	private String streetNumber;

	/**
	 * Name of the city.
	 */
	private String city;

	/**
	 * Postal code of the address.
	 */
	private int postalCode;

	/**
	 * Constructs a new address.
	 * 
	 * @param street
	 *            name of the street
	 * @param streetNumber
	 *            number of the building in the street
	 * @param city
	 *            name of the city
	 * @param postalCode
	 *            postal code of the address
	 */
	public Address(String street, String streetNumber, String city,
			int postalCode) {
		this.street = street;
		this.streetNumber = streetNumber;
		this.city = city;
		this.postalCode = postalCode;
	}

	/**
	 * Returns the name of the street.
	 * 
	 * @return name of the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * Returns the number of the building in the street.
	 * 
	 * @return number of the building in the street
	 */
	public String getStreetNumber() {
		return streetNumber;
	}

	/**
	 * Returns the name of the city.
	 * 
	 * @return name of the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Returns the postal code of the address.
	 * 
	 * @return postal code of the address
	 */
	public int getPostalCode() {
		return postalCode;
	}

	@Override
	public String toString() {
		return city + ", " + postalCode + ", " + street + streetNumber;
	}
}
