package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;


public class OSMNode extends OSMElement {

	private double latitude;
	private double longitude;
	private Vertex vertex;
	private Coordinate coord;

	public OSMNode(long id) {
		this(id, Double.NaN, Double.NaN);
	}

	public OSMNode(final long id, final double lat, final double lon) {
		super(id);
		latitude = lat;
		longitude = lon;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public Vertex getVertex() {
		if (vertex == null) {
			return (vertex = new Vertex(latitude, longitude));
		}
		return vertex;
	}

	public void setLatitude(double lat) {
		latitude = lat;
	}

	public void setLongitude(double lon) {
		longitude = lon;
	}

	@Override
	public Coordinate getCenterCoordinate() {
		if (coord == null) {
			return (coord = new Coordinate(latitude, longitude));
		}
		return coord;
	}
}