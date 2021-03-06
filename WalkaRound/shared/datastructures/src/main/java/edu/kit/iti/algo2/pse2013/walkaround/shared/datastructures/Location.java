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
    private int id;


    /**
     * name of Location.
     */
    private String name;


    /**
     * address of Location.
     */
    private Address address;


    /**
     * flag for setting Location as moveable.
     */
    private boolean isMoveable;


    /**
     * flag for setting Location as favorite.
     */
    private boolean isFavorite;


    /**
     * id counter
     */
    private static int idCounter = 0;


    /**
     * Creates an instance of Location.
     *
     * @param lat Latitude of Location.
     * @param lon Longitude of Location.
     * @param name Name of Location.
     */
    public Location(double lat, double lon, String name) {
        this(lat, lon, name, null);
    }


    /**
     * Creates an instance of Location.
     *
     * @param lat Latitude of Location.
     * @param lon Longitude of Location.
     * @param name Name of Location.
     * @param address Address of Location.
     */
    public Location(double lat, double lon, String name, Address address) {
    	super(lat, lon);
		//Log.d(TAG_LOCATION, "Location Constructor: lat: " + lat + ", lon: " + lon + ", name: " + name + ", used id: " + idCounter + "");
        /*if (name == null || name.isEmpty())
            throw new IllegalArgumentException("name must not be null and not be empty");*/
        this.name = name;
        this.address = address;
        isMoveable = false;
        isFavorite = false;
        id = idCounter;
        idCounter++;
    }
    
    /**
     * Creates an instance of Location.
     * @param loca the location of Location...
     */
    public Location(Location loca) {
    	this(loca.getLatitude(), loca.getLongitude(), loca.getName() == null ? null : new String(loca.getName()),
    		loca.getAddress() == null ? null : loca.getAddress().clone());
    }


    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Location [name=" + name + ", address=" + address
				+ ", isMoveable=" + isMoveable + ", isFavorite=" + isFavorite
				+ ", coordinate=" + super.toString() + "]";
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

    public void setName(String name) {
    	this.name = name;
    }

    /**
     * Returns the Address of Location.
     *
     * @return Address.
     */
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
    	this.address = address;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + (isFavorite ? 1231 : 1237);
		result = prime * result + (isMoveable ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof Location)) {
			return false;
		}
		Location other = (Location) obj;
		if (address == null) {
			if (other.address != null) {
				return false;
			}
		} else if (!address.equals(other.address)) {
			return false;
		}
		if (isFavorite != other.isFavorite) {
			return false;
		}
		if (isMoveable != other.isMoveable) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public Location clone() {
		if (this instanceof Waypoint) {
			return (Waypoint) this.clone();
		} else if (this instanceof POI) {
			return (POI) this.clone();
		}
    	String clonedName = null;
    	if (this.getName() != null) {
    	clonedName = this.getName().toString();
    	}
    	Address clonedAddress = null;
    	if (this.getAddress() != null) {
    	clonedAddress = this.getAddress().clone();
    	}

		Location clonedLocation = new Location(this.getLatitude(), this.getLongitude(), clonedName, clonedAddress);
		clonedLocation.setMoveability(this.isMoveable());
		clonedLocation.setIsFavorite(this.isFavorite());
		return clonedLocation;
	}

	protected void setMoveability(boolean moveable) {
		this.isMoveable = moveable;
	}

	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
}
