package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata;

import java.util.ArrayList;
import java.util.List;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategory;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategoryFactory;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Area;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Category;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;


public class OSMWay extends OSMElement {
	private ArrayList<OSMNode> nodes = new ArrayList<OSMNode>();
	private Area area;

	public OSMWay(final long id) {
		super(id);
	}

	public void addNode(final OSMNode osmNode) {
		nodes.add(osmNode);
	}
	public List<Edge> getEdges() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (int i = 0; i < nodes.size() - 1; i++) {
			edges.add(new Edge(nodes.get(i).getVertex(), nodes.get(i+1).getVertex()));
			//System.out.println(String.format("Add Edge from %d (%d) to %d (%d)", nodes.get(i).id, nodes.get(i).getVertex().getID(), nodes.get(i+1).id, nodes.get(i+1).getVertex().getID()));
		}
		return edges;
	}
	public List<Coordinate> getCoordinates() {
		ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
		for (OSMNode node : nodes) {
			coordinates.add(new Coordinate(node.getLatitude(), node.getLongitude()));
		}
		return coordinates;
	}
	public Area getArea() {
		if (area == null && getAreaCategories().length != 0 && getCoordinates().size() >= 3) {
			area = new Area(getAreaCategories(), getCoordinates());
		}
		return area;
	}
	public int[] getAreaCategories() {
		ArrayList<Integer> fitting = new ArrayList<Integer>();
		for (int catID : Category.getAllAreaCategories()) {
			OSMCategory cat = OSMCategoryFactory.createAreaCategory(catID);
			if (cat.accepts(this)) {
				fitting.add(catID);
			}
		}
		int[] fittingArray = new int[fitting.size()];
		for (int i = 0; i < fitting.size(); i++) {
			fittingArray[i] = fitting.get(i);
		}
		return fittingArray;
	}
}