package edu.kit.iti.algo2.pse2013.walkaround.shared.pbf;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Logger;

//import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Area;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Categorizable;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometrizableWrapper;
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
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGeometrizableWrapper;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGeometryData;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGeometryNode;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGraphData;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveLocation;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveLocationCategory;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveLocationCategoryOrBuilder;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SavePOI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveVertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveWaypoint;

public class ProtobufConverter {
	private static final String TAG = ProtobufConverter.class.getSimpleName();
	@SuppressWarnings("unused")
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
		for (int cat : area.getCategories()) {
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
			if (saveGeom != null) {
				geoms.add(getGeometrizable(saveGeom));
			}
		}
		return geoms;
	}
	private static Geometrizable getGeometrizable(SaveGeometrizable saveGeom) {
		if (saveGeom.hasPOI()) {
			return getPOI(saveGeom.getPOI());
		} else if (saveGeom.hasEdge()) {
			return getEdge(saveGeom.getEdge());
		} else if (saveGeom.hasVertex()) {
			return getVertex(saveGeom.getVertex());
		} else if (saveGeom.hasWrapper()) {
			return getGeometrizableWrapper(saveGeom.getWrapper());
		}
		return null;
	}
	private static Geometrizable getGeometrizableWrapper(SaveGeometrizableWrapper wrapper) {
		return new GeometrizableWrapper(getGeometrizable(wrapper.getGeometrizable()), wrapper.getNumber());
	}
	public static SaveGeometrizable.Builder getGeometrizableBuilder(Geometrizable geometrizable) {
		SaveGeometrizable.Builder builder = SaveGeometrizable.newBuilder();
		if (geometrizable instanceof Edge) {
			return builder.setEdge(getEdgeBuilder((Edge) geometrizable));
		} else if (geometrizable instanceof POI) {
			return builder.setPOI(getPOIBuilder((POI) geometrizable));
		} else if (geometrizable instanceof Vertex) {
			return builder.setVertex(getVertexBuilder((Vertex) geometrizable));
		} else if (geometrizable instanceof GeometrizableWrapper) {
			return builder.setWrapper(getGeometrizableWrapperBuilder((GeometrizableWrapper) geometrizable));
		}
		assert false;
		return null;
	}
	private static SaveGeometrizableWrapper.Builder getGeometrizableWrapperBuilder(GeometrizableWrapper wrapper) {
		if (wrapper == null) {
			return null;
		}
		try {
			Field numberField = wrapper.getClass().getDeclaredField("nodeNumber");
			numberField.setAccessible(true);
			return SaveGeometrizableWrapper.newBuilder().setNumber(numberField.getInt(wrapper)).setGeometrizable(getGeometrizableBuilder(wrapper.getGeometrizable()));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static GeometryDataIO getGeometryData(SaveGeometryData saveGeometryData) {
		if (saveGeometryData == null) {
			return null;
		}
		tmp_vertices = new TreeMap<Integer, Vertex>();
		return new GeometryDataIO(getGeometryNode(null, saveGeometryData.getRoot()), saveGeometryData.getNumDimensions());
	}
	public static SaveGeometryData.Builder getGeometryDataBuilder(GeometryDataIO geometryData) {
		if (geometryData == null || geometryData.getRoot() == null) {
			return null;
		}
		return SaveGeometryData.newBuilder()
			.setRoot(getGeometryNodeBuilder(geometryData.getRoot()))
			.setNumDimensions(geometryData.getNumDimensions());
	}
	public static GeometryNode getGeometryNode(GeometryNode parent, SaveGeometryNode saveNode) {
		if (saveNode == null) {
			return null;
		}
		GeometryNode node;
		List<Geometrizable> geoms = new LinkedList<Geometrizable>();
		if (saveNode.getGeometrizableCount() > 0) {
			geoms = getGeometrizables(saveNode.getGeometrizableList());
		}
		if (parent == null) {
			node = new GeometryNode(saveNode.getSplitValue(), geoms);
		} else {
			node = new GeometryNode(parent, saveNode.getSplitValue(), geoms);
		}
		if (saveNode.hasLeft()) {
			node.setLeftNode(getGeometryNode(node, saveNode.getLeft()));
		}
		if (saveNode.hasRight()) {
			node.setRightNode(getGeometryNode(node, saveNode.getRight()));
		}
		return node;
	}
	public static SaveGeometryNode.Builder getGeometryNodeBuilder(GeometryNode node) {
		if (node == null) {
			return null;
		}
		SaveGeometryNode.Builder builder =  SaveGeometryNode.newBuilder().setSplitValue(node.getSplitValue());
		if (node.getLeftNode() != null) {
			builder.setLeft(getGeometryNodeBuilder(node.getLeftNode()));
		}
		if (node.getRightNode() != null) {
			builder.setRight(getGeometryNodeBuilder(node.getRightNode()));
		}
		if (node.getGeometrizables() != null && node.getGeometrizables().size() > 0) {
			for (Geometrizable g : node.getGeometrizables()) {
				builder.addGeometrizable(getGeometrizableBuilder(g));
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
			saveLoc.hasName()? saveLoc.getName(): null,
			saveLoc.hasAddress()?getAddress(saveLoc.getAddress()):null);
	}
	public static SaveLocation.Builder getLocationBuilder(Location loc) {
		if (loc == null) {
			return null;
		}
		SaveLocation.Builder builder = SaveLocation.newBuilder()
				.setParentCoordinate(getCoordinateBuilder(loc))
				.setID(loc.getId());
		if (loc.getName() != null) {
			builder.setName(loc.getName());
		}
		SaveAddress.Builder addr = getAddressBuilder(loc.getAddress());
		if (addr != null) {
			builder.setAddress(addr);
		}
		return builder;
	}
	public static LocationDataIO getLocationData(InputStream in, List<Integer> categories) throws IOException {
		if (in == null) {
			return null;
		}
		LocationDataIO locationData = new LocationDataIO();
		SaveLocationCategory currentCat = null;
		long time = System.currentTimeMillis();
		while ((currentCat = SaveLocationCategory.parseDelimitedFrom(in)) != null) {
			//Log.d(TAG, String.format("%d POIs and %d Areas", currentCat.getPOICount(), currentCat.getAreaCount()));
			boolean regardCat = categories == null;
			for (Integer id : currentCat.getIDList()) {
				regardCat = regardCat || categories.contains(id);
			}
			//Log.d(TAG, "Regard Category: " + regardCat);
			if (regardCat) {
				List<SavePOI> pois = currentCat.getPOIList();
				for (SavePOI poi : pois) {
					locationData.addPOI(getPOI(poi));
				}
				List<SaveArea> areas = currentCat.getAreaList();
				for (SaveArea area : areas) {
					locationData.addArea(getArea(area));
				}
			}
			//Log.d(TAG, "Category time: " + (System.currentTimeMillis() - time));
			time = System.currentTimeMillis();
		}
		return locationData;
	}
	public static List<SaveLocationCategory.Builder> getLocationCategoryBuilders(LocationDataIO locData) {
		if (locData == null) {
			return null;
		}
		Vector<SaveLocationCategory.Builder> categories = new Vector<SaveLocationCategory.Builder>();
		for (POI currentPOI : locData.getPOIs()) {
			boolean addedPOI = false;
			for (int i = 0; i < categories.size() && addedPOI == false; i++) {
				if (fitsInCategory(currentPOI, categories.get(i))) {
					categories.get(i).addPOI(getPOIBuilder(currentPOI));
					addedPOI = true;
				}
			}
			if (!addedPOI) {
				SaveLocationCategory.Builder catBuilder = SaveLocationCategory.newBuilder().addPOI(getPOIBuilder(currentPOI));
				for (int cat : currentPOI.getCategories()) {
					catBuilder.addID(cat);
				}
				categories.add(catBuilder);
			}
		}
		for (Area currentArea : locData.getAreas()) {
			boolean addedArea = false;
			for (int i = 0; i < categories.size() && addedArea == false; i++) {
				if (fitsInCategory(currentArea, categories.get(i))) {
					categories.get(i).addArea(getAreaBuilder(currentArea));
					addedArea = true;
				}
			}
			if (!addedArea) {
				SaveLocationCategory.Builder catBuilder = SaveLocationCategory.newBuilder().addArea(getAreaBuilder(currentArea));
				for (int cat : currentArea.getCategories()) {
					catBuilder.addID(cat);
				}
				categories.add(catBuilder);
			}
		}
		return categories;
	}
	private static boolean fitsInCategory(Categorizable categorizable, SaveLocationCategoryOrBuilder catBuilder) {
		if (categorizable.getCategories().length != catBuilder.getIDCount() || (catBuilder.getPOICount() + catBuilder.getAreaCount()) > 100) {
			return false;
		}
		for (int cat : categorizable.getCategories()) {
			if (!catBuilder.getIDList().contains(cat)) {
				return false;
			}
		}
		return true;
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
		String textInfo = null;
		if (savePOI.hasTextInfo()) {
			textInfo = savePOI.getTextInfo();
		}
		return new POI(getLocation(savePOI.getParentLocation()), textInfo, url, cats);
	}
	public static SavePOI.Builder getPOIBuilder(POI p) {
		if (p == null) {
			return null;
		}
		int[] cats = p.getCategories();
		ArrayList<Integer> poiList = new ArrayList<Integer>();
		for (int i = 0; cats != null && i < cats.length; i++) {
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
		Waypoint wp = new Waypoint(coord.getLatitude(), coord.getLongitude(), saWP.getParentLocation().getName());
		if (saWP.getParentLocation().hasAddress()) {
			SaveAddress sAddr = saWP.getParentLocation().getAddress();
			Address addr = new Address(sAddr.getStreet(), sAddr.getHouseNumber(), sAddr.getCity(), sAddr.getPostalCode());
			wp.setAddress(addr);
		}
		if (saWP.getParentLocation().getParentCoordinate().getCrossingAngleList().size() > 0) {
			wp.setCrossingInformation(new CrossingInformation(saWP.getParentLocation().getParentCoordinate().getCrossingAngleList()));
		}
		if (saWP.getParentLocation().hasName()) {
			wp.setName(saWP.getParentLocation().getName());
		} else {
			wp.setName(null);
		}
		wp.setProfile(saWP.getProfile());
		wp.setIsFavorite(saWP.getFavorite());
		if (saWP.hasPOI()) {
			wp.setPOI(ProtobufConverter.getPOI(saWP.getPOI()));
		}
		return wp;
	}
	public static SaveWaypoint.Builder getWaypointBuilder(Waypoint wp) {
		SaveLocation.Builder loc = ProtobufConverter.getLocationBuilder(wp);
		SaveWaypoint.Builder builder = SaveWaypoint.newBuilder()
			.setProfile(wp.getProfile())
			.setFavorite(wp.isFavorite())
			.setParentLocation(loc);
		if (wp.isPOI()) {
			builder.setPOI(ProtobufConverter.getPOIBuilder(wp.getPOI()));
		}
		return builder;
	}
}