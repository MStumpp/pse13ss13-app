package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import java.util.HashMap;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

public class OSMRelation extends OSMElement {
	private HashMap<OSMElement, String> members = new HashMap<OSMElement, String>();
	public OSMRelation(long id) {
		super(id);
	}
	public void addMember(OSMElement ele, String role) {
		members.put(ele, role);
	}
	@Override
	public Coordinate getCenterCoordinate() {
		return null;// TODO Auto-generated method stub
	}
}
