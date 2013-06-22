package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import java.util.ArrayList;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;

public class Edge {
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
}