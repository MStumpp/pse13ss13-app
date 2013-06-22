package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import java.util.ArrayList;

public abstract class OSMElement {
	protected long id;
	private ArrayList<OSMTag> tags;
	public OSMElement(long id) {
		this.id = id;
	}
	public long getID() {
		return id;
	}
	public void addTag(String key, String value) {
		this.tags.add(new OSMTag(key, value));
	}
	public OSMTag[] getTags() {
		return tags.toArray(new OSMTag[0]);
	}
}