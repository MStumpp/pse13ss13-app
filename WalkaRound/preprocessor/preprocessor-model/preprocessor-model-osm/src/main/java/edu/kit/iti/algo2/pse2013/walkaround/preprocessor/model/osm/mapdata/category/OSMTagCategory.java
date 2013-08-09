package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

import java.util.HashSet;
import java.util.Set;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMElement;

public class OSMTagCategory extends OSMCategory {

	private Set<Tag> tags = new HashSet<Tag>();

	public OSMTagCategory() {}
	public OSMTagCategory(boolean inverted) {
		this.inverted = inverted;
	}

	public void addTag(String key, String value) {
		this.tags.add(new Tag(key, value));
	}
	@Override
	public boolean accepts(OSMElement element) {
		for (Tag tag : tags) {
			if (element.hasTag(tag.getKey(), tag.getValue())) {
				return !inverted;
			}
		}
		return (decorated == null)? inverted:decoratedAccepts(element);
	}
}