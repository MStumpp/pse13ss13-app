package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMElement;

public abstract class OSMCategory {
	protected boolean inverted;
	protected OSMCategory decorated;
	public OSMCategory() {
		this(false);
	}
	public OSMCategory(boolean invert) {
		this(null, invert);
	}
	public OSMCategory(OSMCategory decorated, boolean inverted) {
		this.decorated = decorated;
		this.inverted = inverted;
	}
	public abstract boolean accepts(OSMElement element);
}