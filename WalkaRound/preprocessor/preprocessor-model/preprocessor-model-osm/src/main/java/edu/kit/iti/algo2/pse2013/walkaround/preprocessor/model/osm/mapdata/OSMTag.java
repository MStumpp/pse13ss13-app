package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

public class OSMTag {
	private String key;
	private String value;

	public OSMTag(String key, String value) {
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
}