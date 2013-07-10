package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;


public class OSMNode extends OSMElement {

	private double lat;
	private double lon;

	public OSMNode(long id) {
		this(id, Double.NaN, Double.NaN);
	}

	public OSMNode(final long id, final double lat, final double lon) {
		super(id);
		this.lat = lat;
		this.lon = lon;
	}
	public double getLatitude() {
		return lat;
	}
	public double getLongtitude() {
		return lon;
	}
	public Vertex convertToVertex() {
		return new Vertex(lat, lon, id);
	}

	public void setLatitude(double lat) {
		this.lat = lat;
	}

	public void setLongitude(double lon) {
		this.lon = lon;
	}

	public POI convertToPOI() {
		if (getName() == null) {
			throw new NullPointerException("Can't convert to POI, because name is null.");
		}
		return new POI(lat, lon, getName(), null, getWikipediaURL(), new int[0]);
	}
	public String getWikipediaURL() {
		if (getTags().get("wikipedia") == null || getTags().get("wikipedia").length() < 4) {
			return null;
		}
		return "http://" + getTags().get("wikipedia").substring(0, 2) + ".wikipedia.org/wiki/" + getTags().get("wikipedia").substring(3);
	}
	public String getName() {
		return getTags().get("name");
	}

	public int[] getPOICategories() {
		//TODO: Auto-generated method stub
		return new int[1];
	}
}