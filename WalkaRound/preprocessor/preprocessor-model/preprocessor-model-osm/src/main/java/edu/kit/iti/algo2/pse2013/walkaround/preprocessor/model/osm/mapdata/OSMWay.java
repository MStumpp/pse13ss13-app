package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import java.util.ArrayList;
import java.util.List;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;


public class OSMWay extends OSMElement {
	private ArrayList<OSMNode> nodes = new ArrayList<OSMNode>();

	public OSMWay(final long id) {
		super(id);
	}

	public void addNode(final OSMNode osmNode) {
		nodes.add(osmNode);
	}
	public List<Edge> getEdges() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (int i = 0; i < nodes.size() - 1; i++) {
			edges.add(new Edge(nodes.get(i).convertToVertex(), nodes.get(i+1).convertToVertex(), id));
		}
		return edges;
	}
}