package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import java.util.ArrayList;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategory;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategoryFactory;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Category;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;


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
		return new Vertex(lat, lon);
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
		return new POI(lat, lon, getName(), null, getWikipediaURL(), getPOICategories());
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
		ArrayList<Integer> fitting = new ArrayList<Integer>();
		for (int catID : Category.getAllPOICategories()) {
			OSMCategory cat = OSMCategoryFactory.createPOICategory(catID);
			if (cat.accepts(this)) {
				fitting.add(catID);
			}
		}
		int[] fittingArray = new int[fitting.size()];
		for (int i = 0; i < fitting.size(); i++) {
			fittingArray[i] = fitting.get(i);
		}
		return fittingArray;
	}
}