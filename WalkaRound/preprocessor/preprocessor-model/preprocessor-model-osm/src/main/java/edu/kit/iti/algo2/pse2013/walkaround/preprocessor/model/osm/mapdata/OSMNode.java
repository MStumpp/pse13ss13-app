package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import java.util.ArrayList;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategory;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategoryFactory;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Category;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;


public class OSMNode extends OSMElement {

	private Vertex vertex;

	public OSMNode(long id) {
		this(id, Double.NaN, Double.NaN);
	}

	public OSMNode(final long id, final double lat, final double lon) {
		super(id);
		vertex = new Vertex(lat, lon);
	}
	public double getLatitude() {
		return vertex.getLatitude();
	}
	public double getLongitude() {
		return vertex.getLongitude();
	}
	public Vertex getVertex() {
		return vertex;
	}

	public void setLatitude(double lat) {
		vertex.setLatitude(lat);
	}

	public void setLongitude(double lon) {
		vertex.setLongitude(lon);
	}

	public POI convertToPOI() {
		if (getName() == null) {
			throw new NullPointerException("Can't convert to POI, because name is null.");
		}
		return new POI(vertex.getLatitude(), vertex.getLongitude(), getName(), null, getWikipediaURL(), getPOICategories(), getAddress());
	}
	private Address getAddress() {
		Integer postcode;
		try {
			postcode = getTags().containsKey("addr:postcode")?Integer.parseInt(getTags().get("addr:postcode")):null;
		} catch (NumberFormatException nfe) {
			postcode = null;
		}
		Address addr = new Address(
			getTags().containsKey("addr:street")?getTags().get("addr:street"):null,
			getTags().containsKey("addr:housenumber")?getTags().get("addr:housenumber"):null,
			getTags().containsKey("addr:city")?getTags().get("addr:city"):null,
			postcode
		);
		return addr;
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