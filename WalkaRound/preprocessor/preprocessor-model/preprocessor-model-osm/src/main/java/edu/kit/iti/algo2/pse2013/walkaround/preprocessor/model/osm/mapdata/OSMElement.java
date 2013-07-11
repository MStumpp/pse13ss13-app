package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import java.util.ArrayList;
import java.util.HashMap;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategory;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategoryFactory;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Category;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

public abstract class OSMElement {
	protected long id;
	protected HashMap<String, String> tags = new HashMap<String, String>();
	public OSMElement(long id) {
		this.id = id;
	}
	public long getID() {
		return id;
	}
	public void addTag(String key, String value) {
		this.tags.put(key, value);
	}
	public HashMap<String, String> getTags() {
		return tags;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	public boolean hasTag(String key, String value) {
		for (String curKey : tags.keySet()) {
			if (curKey.equals(key)) {
				String[] values = tags.get(curKey).split(";");
				for (String curValue : values) {
					if (curValue.trim().equals(value)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public abstract Vertex getCenterVertex();
	public String getName() {
		return getTags().get("name");
	}
	public POI getPOI() {
		Vertex v = getCenterVertex();
		int[] poiCats = getPOICategories();
		if (getName() != null && v != null && poiCats.length != 0) {
			return new POI(v.getLatitude(), v.getLongitude(), getName(), null, getWikipediaURL(), poiCats, getAddress());
		}
		return null;
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