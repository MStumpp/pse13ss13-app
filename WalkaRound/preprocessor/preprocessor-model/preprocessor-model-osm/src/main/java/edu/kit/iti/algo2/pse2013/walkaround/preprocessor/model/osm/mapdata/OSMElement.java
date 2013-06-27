package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import java.util.HashMap;

public abstract class OSMElement {
	protected long id;
	private HashMap<String, String> tags = new HashMap<>();
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
}