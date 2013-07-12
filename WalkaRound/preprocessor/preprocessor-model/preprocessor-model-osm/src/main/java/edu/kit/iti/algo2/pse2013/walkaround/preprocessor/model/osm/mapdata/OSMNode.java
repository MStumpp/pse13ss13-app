package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;


public class OSMNode extends OSMElement {

	private Vertex vertex;

	public OSMNode(long id) {
		this(id, Double.NaN, Double.NaN);
	}

	public OSMNode(final long id, final double lat, final double lon) {
		super(id);
		vertex = new Vertex(lat, lon);
	}
	public double getLatitude() {
		return vertex.getLatitude();
	}
	public double getLongitude() {
		return vertex.getLongitude();
	}
	public Vertex getVertex() {
		return vertex;
	}

	public void setLatitude(double lat) {
		vertex.setLatitude(lat);
	}

	public void setLongitude(double lon) {
		vertex.setLongitude(lon);
	}

	@Override
	public Coordinate getCenterCoordinate() {
		return vertex;
	}
}