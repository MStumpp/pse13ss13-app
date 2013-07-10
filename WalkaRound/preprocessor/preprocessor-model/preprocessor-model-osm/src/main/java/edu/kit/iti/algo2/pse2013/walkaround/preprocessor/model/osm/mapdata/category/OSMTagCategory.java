package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

import java.util.HashMap;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMElement;

public class OSMTagCategory extends OSMCategory {

	private HashMap<String, String> tags = new HashMap<String, String>();

	public void addTag(String key, String value) {
		this.tags.put(key, value);
	}
	@Override
	public boolean accepts(OSMElement element) {
		for (String key : tags.keySet()) {
			if (element.hasTag(key, tags.get(key))) {
				return true;
			}
		}
		return (decorated == null)? false:decoratedAccepts(element);
	}
}