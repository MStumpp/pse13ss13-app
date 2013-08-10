package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents an address.
 *
 * @author Thomas Kadow
 * @version 1.0
 */
public class Address {

	public static final int NO_POSTAL_CODE = -1;

	/**
	 * Name of the street.
	 */
	private String street;

	/**
	 * Number of the building in the street.
	 */
	private String houseNumber;

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
	 * @param houseNumber
	 *            number of the building in the street
	 * @param city
	 *            name of the city
	 * @param postalCode
	 *            postal code of the address
	 */
	public Address(String street, String houseNumber, String city, Integer postalCode) {
		this.street = street;
		this.houseNumber = houseNumber;
		this.city = city;
		if (postalCode != null) {
			this.postalCode = postalCode;
		} else {
			this.postalCode = NO_POSTAL_CODE;
		}
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
	public String getHouseNumber() {
		return houseNumber;
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
		return city + ", " + postalCode + ", " + street + houseNumber;
	}


	public Address clone() {
    	String clonedStreet = "";
    	if (this.getStreet() != null) {
    	clonedStreet = this.getStreet().toString();
    	}
    	
    	String clonedHouseNumber = "";
    	if (this.getHouseNumber() != null) {
    	houseNumber = this.getHouseNumber().toString();
    	}
    	
    	String clonedCity = "";
    	if (this.getCity() != null) {
    	clonedCity = this.getCity().toString();
    	}
		
		return new Address(clonedStreet, clonedHouseNumber, clonedCity, this.postalCode);
	}
}
