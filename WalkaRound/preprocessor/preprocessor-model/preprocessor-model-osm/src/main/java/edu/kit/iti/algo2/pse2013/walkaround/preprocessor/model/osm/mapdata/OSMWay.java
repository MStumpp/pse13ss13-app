package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import java.util.ArrayList;
import java.util.List;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;


public class OSMWay extends OSMElement {
	private ArrayList<OSMNode> nodes;

	public OSMWay(long id) {
		super(id);
		nodes = new ArrayList<>();
	}

	public void addNode(OSMNode node) {
		nodes.add(node);
	}
	public List<Edge> getEdges() {
		ArrayList<Edge> edges = new ArrayList<>();
		for (int i = 0; i < nodes.size() - 1; i++) {

		}
		return edges;
	}
}