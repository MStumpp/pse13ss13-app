package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import java.util.List;

/**
 * This class represents an vertex contained in a graph.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Vertex {

    /**
     * Vertex id.
     */
	private long id;

	public Vertex(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

	public List<Edge> getOutgoingEdges() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getDist() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setParent(Vertex current) {
		// TODO Auto-generated method stub

	}

	public void setDist(double distance) {
		// TODO Auto-generated method stub

	}

}
