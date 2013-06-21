package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a location.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Location extends Coordinate {

    /**
     * id of this Location.
     */
    private final int id;


    /**
     * name of this Location.
     */
    private final String name;


    /**
     * address of this Location.
     */
	private final Address address;


    /**
     * flag for setting Location as moveable.
     */
    private boolean isMoveable;


    /**
     * flag for setting Location as favorite.
     */
    private boolean isFavorite;


    /**
     * Creates an instance of Location.
     *
     * @param lon Longitude of the Location.
     * @param lat Latitude of the Location.
     * @param id ID of the Location.
     * @param name Name of the Location.
     */
    public Location(double lon, double lat, int id, String name) {
        this(lon, lat, id, name, null);
    }


    /**
     * Creates an instance of Location.
     *
     * @param lon Longitude of the Location.
     * @param lat Latitude of the Location.
     * @param id ID of the Location.
     * @param name Name of the Location.
     * @param address Address of the Location.
     */
    public Location(double lon, double lat, int id, String name, Address address) {
        super(lon, lat);
        if (id < 0)
            throw new IllegalArgumentException("id must be greater than zero");
        this.id = id;
        if (name.isEmpty())
            throw new IllegalArgumentException("name must not be empty");
        this.name = name;
        if (address == null)
            throw new IllegalArgumentException("address must be provided");
        this.address = address;
        isMoveable = false;
        isFavorite = false;
    }


    /**
     * Returns the ID of this Location.
     *
     * @return int.
     */
    public int getId() {
        return id;
    }


    /**
     * Returns the name of this Location.
     *
     * @return String.
     */
	public String getName() {
		return name;
	}


    /**
     * Returns the Address of this Location.
     *
     * @return Address.
     */
	public Address getAddress() {
		return address;
	}


    /**
     * Returns whether this Location is moveable.
     *
     * @return true if this Location is moveable, false otherwise.
     */
	public boolean isMoveable() {
		return isMoveable;
	}


    /**
     * Returns whether this Location is a favorite.
     *
     * @return true if this Location is a favorite, false otherwise.
     */
	public boolean isFavorite() {
		return isFavorite;
	}

}
