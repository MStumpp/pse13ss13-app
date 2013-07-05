package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import java.util.HashMap;

public class OSMRelation extends OSMElement {
	private HashMap<OSMElement, String> members = new HashMap<OSMElement, String>();
	public OSMRelation(long id) {
		super(id);
	}
	public void addMember(OSMElement ele, String role) {
		members.put(ele, role);
	}
}
