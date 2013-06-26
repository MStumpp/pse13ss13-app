package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import java.io.Serializable;
import java.util.ArrayList;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
/**
 * This class represents an egde contained in a graph. An edge has two coordinates,
 * each represented by longitude and latitude values.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
/**
 * This class represents an egde contained in a graph. An edge has two coordinates,
 * each represented by longitude and latitude values.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */

public final class Edge implements Serializable {
	/**
	 * Temporary Serial version ID as long as Java serialization is used
	 */
	private static final long serialVersionUID = 3182638429343198592L;

	private long id;
	private ArrayList<Vertex> vertices = new ArrayList<>();

	public Edge(long id, Vertex a, Vertex b) {
		this.id = id;
		vertices.add(a);
		vertices.add(b);
	}

	public long getId() {
		return id;
	}

	public double getLength() {
		return CoordinateUtility.calculateDifferenceInMeters(vertices.get(0), vertices.get(1));
	}

	public ArrayList<Vertex> getVertices() {
		return vertices;
	}


	public Vertex getTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}