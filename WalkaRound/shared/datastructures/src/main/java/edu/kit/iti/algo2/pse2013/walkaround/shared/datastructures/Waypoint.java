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
     * @param name Name of Waypoint.
     */
    public Waypoint(double lat, double lon, String name) {
        this(lat, lon, name, null);
    }
    
    /**
     * Creates an instance of Waypoint.
     *
     * @param wp the waypoint of Waypoint. ;)
     */
    public Waypoint(Waypoint wp) {
        this(wp.getLatitude(), wp.getLongitude(),
        wp.getName() == null ? null : new String(wp.getName()),
        wp.getAddress() == null ? null : wp.getAddress().clone());
    }


    /**
     * Creates an instance of Waypoint.
     *
     * @param lat Latitude of Waypoint.
     * @param lon Longitude of Waypoint.
     * @param name Name of Waypoint.
     * @param address Address of Waypoint.
     */
    public Waypoint(double lat, double lon, String name, Address address) {
        super(lat, lon, name, address);
        profile = -1;
        poi = null;
        this.setMoveability(true);
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
     * Checks whether the Waypoints represents a "BUUMeranpoint" (= Anchor to a Roundtrip)
     * @return boolean
     */
    public boolean isAnchorToRoundtrip() {
    	if (this.profile >= 0) {
    		return true;
    	}
    	return false;
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

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((poi == null) ? 0 : poi.hashCode());
		result = prime * result + profile;
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
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Waypoint)) {
			return false;
		}
		Waypoint other = (Waypoint) obj;
		if (poi == null) {
			if (other.poi != null) {
				return false;
			}
		} else if (!poi.equals(other.poi)) {
			return false;
		}
		if (profile != other.profile) {
			return false;
		}
		return true;
	}


	public Waypoint clone() {
		return new Waypoint(this);
	}
	
	/* OLD VERSION
	public Waypoint clone() {
    	String clonedName = null;
    	if (this.getName() != null) {
    		clonedName = this.getName().toString();
    	}
    	Address clonedAddress = null;
    	if (this.getAddress() != null) {
    		clonedAddress = this.getAddress().clone();
    	}
    	POI clonedPOI = null;
    	if (this.getPOI() != null) {
    		clonedPOI = this.getPOI().clone();
    	}
    	
    	Waypoint clonedWaypoint = new Waypoint(this.getLatitude(), this.getLongitude(), clonedName, clonedAddress);
    	clonedWaypoint.setCrossingInformation(this.getCrossingInformation());
    	clonedWaypoint.setPOI(clonedPOI);
    	clonedWaypoint.setProfile(this.getProfile());
    	return clonedWaypoint;
    }
    */


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Waypoint [profile=" + profile + ", poi=" + poi + ", location=" + super.toString() + "]";
	}

}
