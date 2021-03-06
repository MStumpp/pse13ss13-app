package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMElement;

public class OSMOrCategory extends OSMCategory {

	private OSMCategory decorated2;

	public OSMOrCategory(OSMCategory child1, OSMCategory child2) {
		if (child1 == null || child2 == null) {
			throw new IllegalArgumentException("OSMOrCategory can't be decorated with null");
		}
		this.decorated = child1;
		this.decorated2 = child2;
	}

	@Override
	public boolean accepts(OSMElement element) {
		return decorated.accepts(element) || decorated2.accepts(element);
	}

}