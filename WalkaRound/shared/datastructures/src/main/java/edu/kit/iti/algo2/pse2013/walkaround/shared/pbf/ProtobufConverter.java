package edu.kit.iti.algo2.pse2013.walkaround.shared.pbf;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Area;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryNode;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveAddress;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveArea;
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
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveWaypoint;

public class ProtobufConverter {
	private static Logger logger = Logger.getLogger(ProtobufConverter.class.getSimpleName());
	private static Map<Integer, Vertex> tmp_vertices;
	public static Address getAddress(SaveAddress saveAddress) {
		if (saveAddress == null) {
			return null;
		}
		int zip = Address.NO_POSTAL_CODE;
		String street = null;
		String city = null;
		String housenumber = null;
		if (saveAddress.hasStreet()) {
			street = saveAddress.getStreet();
		}
		if (saveAddress.hasPostalCode()) {
			zip = saveAddress.getPostalCode();
		}
		if (saveAddress.hasCity()) {
			city = saveAddress.getCity();
		}
		if (saveAddress.hasHouseNumber()) {
			housenumber = saveAddress.getHouseNumber();
		}
		return new Address(street, housenumber, city, zip);
	}
	public static SaveAddress.Builder getAddressBuilder(Address addr) {
		if (addr == null) {
			return null;
		}
		SaveAddress.Builder builder = SaveAddress.newBuilder();
		if (addr.getCity() != null) {
			builder.setCity(addr.getCity());
		}
		if (addr.getHouseNumber() != null) {
			builder.setHouseNumber(addr.getHouseNumber());
		}
		if (addr.getPostalCode() != Address.NO_POSTAL_CODE) {
			builder.setPostalCode(addr.getPostalCode());
		}
		if (addr.getStreet() != null) {
			builder.setStreet(addr.getStreet());
		}
		return builder;
	}
	public static Area getArea(SaveArea saveArea) {
		if (saveArea == null) {
			return null;
		}
		int[] areaCats = new int[saveArea.getCategoryCount()];
		for (int i = 0; i < areaCats.length; i++) {
			areaCats[i] = saveArea.getCategory(i);
		}
		ArrayList<Coordinate> areaCoords = new ArrayList<Coordinate>();
		for (SaveCoordinate c : saveArea.getCoordinateList()) {
			areaCoords.add(getCoordinate(c));
		}
		return new Area(areaCats, areaCoords);
	}
	public static SaveArea.Builder getAreaBuilder(Area area) {
		if (area == null) {
			return null;
		}
		SaveArea.Builder builder = SaveArea.newBuilder();
		for (int cat : area.getAreaCategories()) {
			builder.addCategory(cat);
		}
		for (Coordinate c : area.getAreaCoordinates()) {
			builder.addCoordinate(getCoordinateBuilder(c));
		}
		return builder;
	}
	public static Coordinate getCoordinate(SaveCoordinate saveCoord) {
		if (saveCoord == null) {
			return null;
		}
		CrossingInformation crossInfo = null;
		if (saveCoord.getCrossingAngleCount() > 1) {
			crossInfo = new CrossingInformation(saveCoord.getCrossingAngleList());
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
		if (c.getCrossingInformation() != null && c.getCrossingInformation().getCrossingAngles() != null && c.getCrossingInformation().getNumCrossroads() > 1) {
			for (float angle : c.getCrossingInformation().getCrossingAngles()) {
				saveCoord.addCrossingAngle(angle);
			}
		}
		return saveCoord;
	}

	private static Edge getEdge(SaveEdge saveEdge) {
		if (saveEdge == null) {
			return null;
		}
		Edge e = new Edge(getVertex(saveEdge.getTail()), getVertex(saveEdge.getHead()), saveEdge.getID());
		return e;
	}
	private static SaveEdge.Builder getEdgeBuilder(Edge e) {
		if (e == null) {
			return null;
		}
		return SaveEdge.newBuilder()
			.setHead(getVertexBuilder(e.getHead()))
			.setTail(getVertexBuilder(e.getTail()))
			.setID(e.getID());
	}

	public static List<Geometrizable> getGeometrizables(List<SaveGeometrizable> saveGeoms) {
		ArrayList<Geometrizable> geoms = new ArrayList<Geometrizable>();
		for (SaveGeometrizable saveGeom : saveGeoms) {
			if (saveGeom.getCoordinate() != null) {
	    // TODO
	//			geoms.add(getCoordinate(saveGeom.getCoordinate()));
			} else if (saveGeom.getEdge() != null) {
				geoms.add(getEdge(saveGeom.getEdge()));
			}
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
		tmp_vertices = new TreeMap<Integer, Vertex>();
		return new GeometryDataIO(getGeometryNode(null, saveGeometryData.getRoot(), saveGeometryData.getRoot().getDepth()), saveGeometryData.getNumDimensions());
	}
	public static SaveGeometryData.Builder getGeometryDataBuilder(GeometryDataIO geometryData) {
		if (geometryData == null || geometryData.getRoot() == null) {
			return null;
		}
		return SaveGeometryData.newBuilder()
			.setRoot(getGeometryNodeBuilder(geometryData.getRoot()))
			.setNumDimensions(geometryData.getNumDimensions());
	}
	public static GeometryNode getGeometryNode(GeometryNode parent, SaveGeometryNode saveNode, int depth) {
		if (saveNode == null) {
			return null;
		}
		GeometryNode node;
		List<Geometrizable> geoms;
        // TODO Check
//		if (saveNode.getGeometrizables().size() > 0) {
//			geoms = getGeometrizables(saveNode.getGeometrizables());
//		}
		if (parent == null) {
//			node = new GeometryNode(saveNode.getSplitValue(), geoms);
		} else {
			node = new GeometryNode(parent, saveNode.getDepth(), saveNode.getSplitValue());
		}
		if (saveNode.hasLeft()) {
//			node.setLeftNode(getGeometryNode(node, saveNode.getLeft(), depth + 1));
		}
		if (saveNode.hasRight()) {
//			node.setRightNode(getGeometryNode(node, saveNode.getRight(), depth + 1));
		}
//		return node;
        return null;
	}
	public static SaveGeometryNode.Builder getGeometryNodeBuilder(GeometryNode node) {
		if (node == null) {
			return null;
		}
		SaveGeometryNode.Builder builder =  SaveGeometryNode.newBuilder()
			//.setDepth(node.getDepth())
			.setSplitValue(node.getSplitValue());
		if (node.getLeftNode() != null) {
			builder.setLeft(getGeometryNodeBuilder(node.getLeftNode()));
		}
		if (node.getRightNode() != null) {
			builder.setRight(getGeometryNodeBuilder(node.getLeftNode()));
		}
		if (node.getGeometrizables() != null && node.getGeometrizables().size() > 0) {
			for (Geometrizable g : node.getGeometrizables()) {
        // TODO
//				builder.addGeometrizable(getGeometrizableBuilder(g));
			}
		}
		return builder;
	}
	public static GraphDataIO getGraphData(SaveGraphData saveGraphData) {
		if (saveGraphData == null) {
			return null;
		}
		GraphDataIO graphData = new GraphDataIO();
		tmp_vertices = new TreeMap<Integer, Vertex>();
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
			saveLoc.getParentCoordinate().getLatitude(),
			saveLoc.getParentCoordinate().getLongitude(),
			saveLoc.getName(),
			getAddress(saveLoc.getAddress()));
	}
	public static SaveLocation.Builder getLocationBuilder(Location loc) {
		if (loc == null) {
			return null;
		}
		SaveLocation.Builder builder = SaveLocation.newBuilder()
				.setParentCoordinate(getCoordinateBuilder(loc))
				.setID(loc.getId())
				.setName(loc.getName());
		SaveAddress.Builder addr = getAddressBuilder(loc.getAddress());
		if (addr != null) {
			builder.setAddress(addr);
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
		for (SaveArea saveArea : saveLocationData.getAreaList()) {
			locationData.addArea(getArea(saveArea));
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
		for (Area a : locData.getAreas()) {
			builder.addArea(getAreaBuilder(a));
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
		URL url;
		try {
			url = new URL(savePOI.getURL());
		} catch (MalformedURLException e) {
			url = null;
		}
		return new POI(getLocation(savePOI.getParentLocation()), savePOI.getTextInfo(), url, cats);
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
			.setParentLocation(getLocationBuilder(p))
			.addAllPOICategory(poiList);
		if (p.getTextInfo() != null) {
			builder.setTextInfo(p.getTextInfo());
		}
		if (p.getURL() != null) {
			builder.setURL(p.getURL().toExternalForm());
		}
		return builder;
	}
	private static Vertex getVertex(SaveVertex saveVertex) {
		if (saveVertex == null) {
			return null;
		} else if (tmp_vertices.containsKey(saveVertex.getID())) {
			return tmp_vertices.get(saveVertex.getID());
		}
		Vertex v = new Vertex(saveVertex.getCoordinate().getLatitude(), saveVertex.getCoordinate().getLongitude(), saveVertex.getID());
		tmp_vertices.put(v.getID(), v);
		return v;
	}
	public static SaveVertex.Builder getVertexBuilder(Vertex vertex) {
		if (vertex == null) {
			return null;
		}
		SaveVertex.Builder builder = SaveVertex.newBuilder()
			.setCoordinate(getCoordinateBuilder(vertex))
			.setID(vertex.getID());
		return builder;
	}
	public static Waypoint getWaypoint(SaveWaypoint saWP) {
		SaveCoordinate coord = saWP.getParentLocation().getParentCoordinate();
		SaveAddress addr = saWP.getParentLocation().getAddress();
		Waypoint wp = new Waypoint(coord.getLatitude(), coord.getLongitude(), saWP.getParentLocation().getName(),
			new Address(addr.getStreet(), addr.getHouseNumber(), addr.getCity(), addr.getPostalCode()));
		try {
			wp.getClass().getField("crossInfo").set(wp, new CrossingInformation(saWP.getParentLocation().getParentCoordinate().getCrossingAngleList()));
			wp.getClass().getField("id").setInt(wp, saWP.getParentLocation().getID());
		} catch (NoSuchFieldException e) {
			logger.log(Level.WARNING, "Reflection failed!", e);
		} catch (IllegalArgumentException e) {
			logger.log(Level.WARNING, "Reflection failed!", e);
		} catch (IllegalAccessException e) {
			logger.log(Level.WARNING, "Reflection failed!", e);
		}
		wp.setProfile(saWP.getProfile());
		wp.setIsFavorite(saWP.hasFavorite());
		wp.setPOI(ProtobufConverter.getPOI(saWP.getPOI()));
		return wp;
	}
	public static SaveWaypoint.Builder getWaypointBuilder(Waypoint wp) {
		SaveLocation.Builder loc = ProtobufConverter.getLocationBuilder(wp);
		return SaveWaypoint.newBuilder()
				.setProfile(wp.getProfile())
				.setFavorite(wp.isFavorite())
				.setPOI(ProtobufConverter.getPOIBuilder(wp.getPOI()))
				.setParentLocation(loc);
	}
}