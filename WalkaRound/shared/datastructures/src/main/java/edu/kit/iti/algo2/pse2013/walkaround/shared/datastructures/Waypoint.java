package walkaround.datastructures;

public class Waypoint extends Location {

	private int profile;

	private POI poi;

	public Waypoint(double longitude, double latitude, String name, int id,
			Address address, int profile, POI poi) {
		super(latitude, latitude, name, profile, address);
		this.profile = profile;
		this.poi = poi;
	}

	public int getProfile() {
		return profile;
	}

	public POI getPoi() {
		return poi;
	}
}
