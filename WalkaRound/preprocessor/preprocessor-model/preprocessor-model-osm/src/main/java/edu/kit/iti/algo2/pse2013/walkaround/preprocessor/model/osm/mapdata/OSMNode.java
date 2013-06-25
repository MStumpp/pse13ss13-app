package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;


public class OSMNode extends OSMElement {

	private double lat;
	private double lon;

	public OSMNode(final long id, final double lat, final double lon) {
		super(id);
		this.lat = lat;
		this.lon = lon;
	}
	public double getLatitude() {
		return lat;
	}
	public double getLongtitude() {
		return lon;
	}
	public Vertex convertToVertex() {
		return new Vertex(id, lat, lon);
	}
}