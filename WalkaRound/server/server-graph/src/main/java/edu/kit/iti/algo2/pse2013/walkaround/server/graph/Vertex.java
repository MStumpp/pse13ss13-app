package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import java.util.List;
import java.io.Serializable;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * This class represents an vertex contained in a graph.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class Vertex extends Coordinate implements Serializable {
	/**
	 * Temporary Serial version ID as long as Java serialization is used
	 */
	private static final long serialVersionUID = -4228194461207025121L;

	private long ID;
	public Vertex(long ID, double lat, double lon) {
		super(lat, lon);
		this.ID = ID;
	}

	public long getID() {
		return ID;
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
