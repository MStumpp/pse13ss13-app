package edu.kit.iti.algo2.pse2013.walkaround.shared.pbf;

import java.util.ArrayList;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryNode;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveAddress;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveEdge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGeometrizable;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGeometryData;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGeometryNode;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGraphData;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveLocation;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveLocationData;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SavePOI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveVertex;

public class ProtobufConverter {
	public static Address getAddress(SaveAddress saveAddress) {
		if (saveAddress == null) {
			return null;
		}
		return new Address(saveAddress.getStreet(), saveAddress.getHouseNumber(), saveAddress.getCity(), saveAddress.getPostalCode());
	}
	public static SaveAddress.Builder getAddressBuilder(Address addr) {
		if (addr == null) {
			return null;
		}
		return SaveAddress.newBuilder()
				.setCity(addr.getCity())
				.setHouseNumber(addr.getHouseNumber())
				.setPostalCode(addr.getPostalCode())
				.setStreet(addr.getStreet());
	}
	public static Coordinate getCoordinate(SaveCoordinate saveCoord) {
		if (saveCoord == null) {
			return null;
		}
		float[] crossroads = new float[saveCoord.getCrossroadAngleCount()];
		for (int i = 0; i < saveCoord.getCrossroadAngleCount(); i++) {
			crossroads[i] = saveCoord.getCrossroadAngle(i);
		}
		CrossingInformation crossInfo = null;
		if (crossroads.length > 0) {
			crossInfo = new CrossingInformation(crossroads);
		}
		return new Coordinate(saveCoord.getLatitude(), saveCoord.getLongitude(), crossInfo);
	}
	public static SaveCoordinate.Builder getCoordinateBuilder(Coordinate c) {
		if (c == null) {
			return null;
		}
		SaveCoordinate.Builder saveCoord = SaveCoordinate.newBuilder()
				.setLatitude(c.getLatitude())
				.setLongitude(c.getLongitude());
		if (c.getCrossingInformation() != null && c.getCrossingInformation().getCrossingAngles() != null) {
			for (float angle : c.getCrossingInformation().getCrossingAngles()) {
				saveCoord.addCrossroadAngle(angle);
			}
		}
		return saveCoord;
	}

	public static Edge getEdge(SaveEdge saveEdge) {
		if (saveEdge == null) {
			return null;
		}
		Edge e = new Edge(getVertex(saveEdge.getTail()), getVertex(saveEdge.getHead()));
		e.setLength(saveEdge.getLength());
		return e;
	}
	public static SaveEdge.Builder getEdgeBuilder(Edge e) {
		if (e == null) {
			return null;
		}
		return SaveEdge.newBuilder()
			.setHead(getVertexBuilder(e.getHead()))
			.setTail(getVertexBuilder(e.getTail()))
			.setLength(e.getLength());
	}

	public static Geometrizable getGeometrizable(SaveGeometrizable geometrizable) {
		if (geometrizable.getCoordinate() != null) {
			return getCoordinate(geometrizable.getCoordinate());
		} else if (geometrizable.getEdge() != null) {
			return getEdge(geometrizable.getEdge());
		}
		return null;
	}
	public static SaveGeometrizable.Builder getGeometrizableBuilder(Geometrizable geometrizable) {
		if (geometrizable instanceof Edge) {
			return SaveGeometrizable.newBuilder().setEdge(getEdgeBuilder((Edge) geometrizable));
		} else if (geometrizable instanceof Coordinate) {
			return SaveGeometrizable.newBuilder().setCoordinate(getCoordinateBuilder((Coordinate) geometrizable));
		}
		return null;
	}
	public static GeometryDataIO getGeometryData(SaveGeometryData saveGeometryData) {
		if (saveGeometryData == null) {
			return null;
		}
		return new GeometryDataIO(getGeometryNode(saveGeometryData.getRoot()), saveGeometryData.getNumDimensions());
	}
	public static SaveGeometryData.Builder getGeometryDataBuilder(GeometryDataIO geometryData) {
		if (geometryData == null || geometryData.getRoot() == null) {
			return null;
		}
		return SaveGeometryData.newBuilder()
			.setRoot(getGeometryNodeBuilder(geometryData.getRoot()))
			.setNumDimensions(geometryData.getNumDimensions());
	}
	public static GeometryNode getGeometryNode(SaveGeometryNode saveNode) {
		if (saveNode == null) {
			return null;
		}
		GeometryNode node;
		//if (!saveNode.hasParent()) {
			SaveGeometryNode parent = saveNode.hasParent()?saveNode.getParent():null;
		//} else {
			node = new GeometryNode(getGeometryNode(parent), saveNode.getDepth(), saveNode.getSplitValue());
		//}
		if (saveNode.hasLeft()) {
			node.setLeftNode(getGeometryNode(saveNode.getLeft()));
		}
		if (saveNode.hasRight()) {
			node.setRightNode(getGeometryNode(saveNode.getRight()));
		}
		if (saveNode.hasGeometrizable()) {
			node.setGeometrizable(getGeometrizable(saveNode.getGeometrizable()));
		}
		return node;
	}
	public static SaveGeometryNode.Builder getGeometryNodeBuilder(GeometryNode node) {
		if (node == null) {
			return null;
		}
		SaveGeometryNode.Builder builder =  SaveGeometryNode.newBuilder()
			.setDepth(node.getDepth())
			.setSplitValue(node.getSplitValue());
		if (node.getParent() != null) {
			builder.setParent(getGeometryNodeBuilder(node.getParent()));
		}
		if (node.getLeftNode() != null) {
			builder.setLeft(getGeometryNodeBuilder(node.getLeftNode()));
		}
		if (node.getRightNode() != null) {
			builder.setRight(getGeometryNodeBuilder(node.getLeftNode()));
		}
		if (node.getGeometrizable() != null) {
			builder.setGeometrizable(getGeometrizableBuilder(node.getGeometrizable()));
		}
		return builder;
	}
	public static GraphDataIO getGraphData(SaveGraphData saveGraphData) {
		if (saveGraphData == null) {
			return null;
		}
		GraphDataIO graphData = new GraphDataIO();
		for (SaveEdge se : saveGraphData.getEdgeList()) {
			graphData.addEdge(getEdge(se));
		}
		return graphData;
	}
	public static SaveGraphData.Builder getGraphDataBuilder(GraphDataIO graphData) {
		if (graphData == null) {
			return null;
		}
		SaveGraphData.Builder builder = SaveGraphData.newBuilder();
		for (Edge e : graphData.getEdges()) {
			builder.addEdge(getEdgeBuilder(e));
		}
		return builder;
	}
	public static Location getLocation(SaveLocation saveLoc) {
		if (saveLoc == null) {
			return null;
		}
		return new Location(
			saveLoc.getParent().getLatitude(),
			saveLoc.getParent().getLongitude(),
			saveLoc.getName(),
			getAddress(saveLoc.getAddress()));
	}
	public static SaveLocation.Builder getLocationBuilder(Location loc) {
		if (loc == null) {
			return null;
		}
		SaveLocation.Builder builder = SaveLocation.newBuilder()
				.setParent(getCoordinateBuilder(loc))
				.setID(loc.getId())
				.setName(loc.getName());
		if (loc.getAddress() != null) {
			builder.setAddress(getAddressBuilder(loc.getAddress()));
		}
		return builder;
	}
	public static LocationDataIO getLocationData(SaveLocationData saveLocationData) {
		if (saveLocationData == null) {
			return null;
		}
		LocationDataIO locationData = new LocationDataIO();
		for (SavePOI savePOI : saveLocationData.getPOIList()) {
			locationData.addPOI(getPOI(savePOI));
		}
		return locationData;
	}
	public static SaveLocationData.Builder getLocationDataBuilder(LocationDataIO locData) {
		if (locData == null) {
			return null;
		}
		SaveLocationData.Builder builder = SaveLocationData.newBuilder();
		for (POI p : locData.getPOIs()) {
			builder.addPOI(getPOIBuilder(p));
		}
		return builder;
	}
	public static POI getPOI(SavePOI savePOI) {
		if (savePOI == null) {
			return null;
		}
		int[] cats = new int[savePOI.getPOICategoryList().size()];
		for (int i = 0; i < cats.length; i++) {
			cats[i] = savePOI.getPOICategory(i);
		}
		return new POI(getLocation(savePOI.getParent()), savePOI.getTextInfo(), savePOI.getImageURL(), cats);
	}
	public static SavePOI.Builder getPOIBuilder(POI p) {
		if (p == null) {
			return null;
		}
		int[] cats = p.getPOICategories();
		ArrayList<Integer> poiList = new ArrayList<Integer>();
		for (int i = 0; i < cats.length; i++) {
			poiList.add(cats[i]);
		}
		SavePOI.Builder builder = SavePOI.newBuilder()
			.setParent(getLocationBuilder(p))
			.addAllPOICategory(poiList);
		if (p.getTextInfo() != null) {
			builder.setTextInfo(p.getTextInfo());
		}
		if (p.getURL() != null) {
			builder.setImageURL(p.getURL());
		}
		return builder;
	}
	public static Vertex getVertex(SaveVertex saveVertex) {
		if (saveVertex == null) {
			return null;
		}
		Vertex v = new Vertex(saveVertex.getCoordinate().getLatitude(), saveVertex.getCoordinate().getLongitude());
		//v.setParent(getVertex(saveVertex.getParent())); // TODO: Aufpassen, dass das nicht zur Endlosschleife wird!
		//v.setRun(saveVertex.getRun());
		//v.setCurrentLength(saveVertex.getCurrentLength());
		for (int i = 0; i < saveVertex.getOutgoingEdgeCount(); i++) {
			v.addOutgoingEdge(getEdge(saveVertex.getOutgoingEdge(i)));
		}
		return v;
	}
	public static SaveVertex.Builder getVertexBuilder(Vertex vertex) {
		if (vertex == null) {
			return null;
		}
		SaveVertex.Builder builder = SaveVertex.newBuilder()
			.setCoordinate(getCoordinateBuilder(vertex))
			.setId(vertex.getID());
		for (Edge e : vertex.getOutgoingEdges()) {
			builder.addOutgoingEdge(getEdgeBuilder(e));
		}
		return builder;
	}
}