package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

import java.util.HashMap;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMElement;

public class OSMTagCategory extends OSMCategory {
	private HashMap<String, String> mustHaveTags = new HashMap<String, String>();

	public void addTag(String key, String value) {
		this.mustHaveTags.put(key, value);
	}

	@Override
	public boolean accepts(OSMElement element) {
		HashMap<String, String> eleTags = element.getTags();
		for (String key : mustHaveTags.keySet()) {

		}
		return false;
	}

}
