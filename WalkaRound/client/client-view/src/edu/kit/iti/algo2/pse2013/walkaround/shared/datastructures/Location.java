package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a location.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Location extends Coordinate {

    /**
     * id of Location.
     */
    private final int id;


    /**
     * name of Location.
     */
    private final String name;


    /**
     * address of Location.
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
     * @param lat Latitude of Location.
     * @param lon Longitude of Location.
     * @param id ID of Location.
     * @param name Name of Location.
     */
    public Location(double lat, double lon, int id, String name) {
        this(lat, lon, id, name, null);
    }


    /**
     * Creates an instance of Location.
     *
     * @param lat Latitude of Location.
     * @param lon Longitude of Location.
     * @param id ID of Location.
     * @param name Name of Location.
     * @param address Address of Location.
     */
    public Location(double lat, double lon, int id, String name, Address address) {
        super(lat, lon);
        if (id <= 0)
            throw new IllegalArgumentException("id must be greater than zero");
        this.id = id;
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("name must not be null and not be empty");
        this.name = name;
        this.address = address;
        isMoveable = false;
        isFavorite = false;
    }


    /**
     * Returns the ID of Location.
     *
     * @return int.
     */
    public int getId() {
        return id;
    }


    /**
     * Returns the name of Location.
     *
     * @return String.
     */
    public String getName() {
        return name;
    }


    /**
     * Returns the Address of Location.
     *
     * @return Address.
     */
    public Address getAddress() {
        return address;
    }


    /**
     * Returns whether Location is moveable.
     *
     * @return true if Location is moveable, false otherwise.
     */
    public boolean isMoveable() {
        return isMoveable;
    }


    /**
     * Returns whether Location is a favorite.
     *
     * @return true if Location is a favorite, false otherwise.
     */
    public boolean isFavorite() {
        return isFavorite;
    }

}
