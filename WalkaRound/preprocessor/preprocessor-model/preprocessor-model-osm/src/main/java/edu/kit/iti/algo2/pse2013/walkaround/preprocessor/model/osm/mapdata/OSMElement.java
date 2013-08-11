package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategory;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategoryFactory;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Category;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

public abstract class OSMElement {
	private static final String KEY_WIKIPEDIA = "wikipedia";
	private static final String KEY_CITY = "addr:city";
	private static final String KEY_HOUSENUMBER = "addr:housenumber";
	private static final String KEY_STREET = "addr:street";
	private static final String KEY_POSTCODE = "addr:postcode";
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
	public abstract Coordinate getCenterCoordinate();
	public String getName() {
		return getTags().get("name");
	}
	public POI getPOI() {
		Coordinate c = getCenterCoordinate();
		int[] poiCats = getPOICategories();
		if (getName() != null && c != null && poiCats.length != 0) {
			return new POI(c.getLatitude(), c.getLongitude(), getName(), null, getWikipediaURL(), poiCats, getAddress());
		}
		return null;
	}
	private Address getAddress() {
		Integer postcode;
		try {
			postcode = getTags().containsKey(KEY_POSTCODE)?Integer.parseInt(getTags().get(KEY_POSTCODE)):null;
		} catch (NumberFormatException nfe) {
			postcode = null;
		}
		Address addr = new Address(
			getTags().containsKey(KEY_STREET)?getTags().get(KEY_STREET):null,
			getTags().containsKey(KEY_HOUSENUMBER)?getTags().get(KEY_HOUSENUMBER):null,
			getTags().containsKey(KEY_CITY)?getTags().get(KEY_CITY):null,
			postcode
		);
		return addr;
	}

	/**
	 * Returns the Wikipedia-URL of the element, which is stored in the OSM-Data
	 * @return a URL of the following form: http://[a-zA-Z]{2}.wikipedia.org/wiki/[.]+
	 */
	public URL getWikipediaURL() {
		String wikiValue = getTags().get(KEY_WIKIPEDIA);
		if (wikiValue == null || wikiValue.length() < 4 || !wikiValue.substring(0, 2).matches("[a-zA-Z]{2}")) {
			return null;
		}
		try {
			String s =  "http://" + getTags().get(KEY_WIKIPEDIA).substring(0, 2) + ".wikipedia.org/w/index.php?printable=yes&title=" + URLEncoder.encode(getTags().get(KEY_WIKIPEDIA).substring(3), "UTF-8");
			return new URL(s);
		} catch (MalformedURLException | UnsupportedEncodingException e) {
			return null;
		}
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