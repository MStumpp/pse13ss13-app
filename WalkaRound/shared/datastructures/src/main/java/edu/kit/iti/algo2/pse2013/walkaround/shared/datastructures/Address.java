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
		return city + ", " + postalCode + ", " + street + " " + houseNumber;
	}


	public Address clone() {
		return new Address(
			getStreet() == null ? null : new String(getStreet()),
			getHouseNumber() == null ? null : new String(getHouseNumber()),
			getCity() == null ? null : new String(getCity()),
			getPostalCode()
		);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((houseNumber == null) ? 0 : houseNumber.hashCode());
		result = prime * result + postalCode;
		result = prime * result + ((street == null) ? 0 : street.hashCode());
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
		if (!(obj instanceof Address)) {
			return false;
		}
		Address other = (Address) obj;
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (houseNumber == null) {
			if (other.houseNumber != null) {
				return false;
			}
		} else if (!houseNumber.equals(other.houseNumber)) {
			return false;
		}
		if (postalCode != other.postalCode) {
			return false;
		}
		if (street == null) {
			if (other.street != null) {
				return false;
			}
		} else if (!street.equals(other.street)) {
			return false;
		}
		return true;
	}
}
