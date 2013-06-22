package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * This class represents an vertex contained in a graph.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Vertex extends Coordinate {
	private long ID;
	public Vertex(long ID, double lat, double lon) {
		super(lat, lon);
		this.ID = ID;
	}
	public long getID() {
		return ID;
	}
}
