package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

/**
 * This class represents a Waypoint.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Waypoint extends Location {

    /**
     * profile id.
     */
    private int profile;


    /**
     * POI a Waypoint is related to.
     */
    private POI poi;


    /**
     * Creates an instance of Waypoint.
     *
     * @param lat Latitude of Waypoint.
     * @param lon Longitude of Waypoint.
     * @param id ID of Waypoint.
     * @param name Name of Waypoint.
     */
    public Waypoint(double lat, double lon, int id, String name) {
        this(lat, lon, id, name, null);
    }


    /**
     * Creates an instance of Waypoint.
     *
     * @param lat Latitude of Waypoint.
     * @param lon Longitude of Waypoint.
     * @param id ID of Waypoint.
     * @param name Name of Waypoint.
     * @param address Address of Waypoint.
     */
    public Waypoint(double lat, double lon, int id, String name, Address address) {
        super(lat, lon, id, name, address);
        profile = -1;
        poi = null;
    }


    /**
     * Sets the Profile of Waypoint.
     *
     * @param profile Profile of Waypoint.
     */
    public void setProfile(int profile) {
        this.profile = profile;
    }


    /**
     * Returns the Profile id of Waypoint.
     *
     * @return int.
     */
    public int getProfile() {
        return profile;
    }


    /**
     * Sets the POI of Waypoint.
     *
     * @param poi POI of Waypoint.
     */
    public void setPOI(POI poi) {
        this.poi = poi;
    }


    /**
     * Returns the POI of Waypoint.
     *
     * @return POI.
     */
    public POI getPOI() {
        return poi;
    }


    /**
     * Returns whether this Waypoint is a POI.
     *
     * @return true if this Waypoint is a POI, false otherwise.
     */
    public boolean isPOI() {
        return poi != null;
    }

}
