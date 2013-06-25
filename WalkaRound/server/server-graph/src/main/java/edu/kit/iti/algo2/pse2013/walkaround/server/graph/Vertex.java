package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

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

	public Vertex(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

}
