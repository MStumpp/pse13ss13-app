package edu.kit.iti.algo2.pse2013.walkaround.shared.pbf;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Area;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Category;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Waypoint;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryNode;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

public class TestProtobufConverter {

	@Test
	public void testConvertAddress() {
		assertEquals(null, ProtobufConverter.getAddressBuilder(null));
		assertEquals(null, ProtobufConverter.getAddress(null));
		Address a = new Address(null, null, null, Address.NO_POSTAL_CODE);
		Address a2 = new Address("Am Fasanengarten", "5", "Karlsruhe", 76131);
		assertEquals(a, ProtobufConverter.getAddress(ProtobufConverter.getAddressBuilder(a).build()));
		assertEquals(a2, ProtobufConverter.getAddress(ProtobufConverter.getAddressBuilder(a2).build()));
	}

	@Test
	public void testConvertArea() {
		assertEquals(null, ProtobufConverter.getAreaBuilder(null));
		assertEquals(null, ProtobufConverter.getArea(null));
		LinkedList<Coordinate> coords = new LinkedList<Coordinate>();
		coords.add(new Coordinate(48, 8));
		coords.add(new Coordinate(49, 8));
		coords.add(new Coordinate(49, 9));
		Area a = new Area(Category.getAllAreaCategories(), coords);
		assertEquals(a, ProtobufConverter.getArea(ProtobufConverter.getAreaBuilder(a).build()));
	}

	@Test
	public void testConvertCoordinate() {
		assertEquals(null, ProtobufConverter.getCoordinate(null));
		assertEquals(null, ProtobufConverter.getCoordinateBuilder(null));
		Coordinate c = new Coordinate(48, 8, new CrossingInformation(new float[]{1.0f, 2.0f, 42.0f}));
		assertEquals(c, ProtobufConverter.getCoordinate(ProtobufConverter.getCoordinateBuilder(c).build()));

		c = new Coordinate(48, 8);
		assertEquals(c, ProtobufConverter.getCoordinate(ProtobufConverter.getCoordinateBuilder(c).build()));
	}
	@Test
	public void testLocation() {
		Location loc = new Location(0, 0, "LocName", new Address(null, null, null, null));
		Location loc2 = new Location(0, 0, null, null);
		assertEquals(loc, ProtobufConverter.getLocation(ProtobufConverter.getLocationBuilder(loc).build()));
		Location reload = ProtobufConverter.getLocation(ProtobufConverter.getLocationBuilder(loc2).build());
		assertEquals(loc2, reload);
	}
	@Test
	public void testPOI() throws MalformedURLException {
		POI p = new POI(0, 0, null, null, null, null);
		POI reload = ProtobufConverter.getPOI(ProtobufConverter.getPOIBuilder(p).build());
		assertEquals(reload.getAddress(), reload.getAddress());
		assertTrue(((Location) p).equals(reload));
		assertEquals(p, reload);
		p = new POI(0, 0, "Name", "TextInfo", new URL("http://www.wikipedia.org"), new int[]{1, 2, 3});
		assertEquals(p, ProtobufConverter.getPOI(ProtobufConverter.getPOIBuilder(p).build()));
	}

	@Test
	public void testConvertGeometryData() throws MalformedURLException {
		assertEquals(null, ProtobufConverter.getGeometryData(null));
		assertEquals(null, ProtobufConverter.getGeometryDataBuilder(null));
		GeometryNode root = new GeometryNode(5);
		GeometryNode left = new GeometryNode(4);
		left.addGeometrizable(new Edge(new Vertex(4, 5), new Vertex(6, 7)));
		left.addGeometrizable(new POI(1, 2, "Name", "TextInfo", new URL("http://www.wikipedia.org"), new int[]{1, 2}));
		GeometryNode right = new GeometryNode(6);
		GeometryNode rightLeft = new GeometryNode(5.5);
		right.setLeftNode(rightLeft);
		root.setLeftNode(left);
		root.setRightNode(right);

		GeometryDataIO g = new GeometryDataIO(new GeometryNode(5), 2);
		assertEquals(g, ProtobufConverter.getGeometryData(ProtobufConverter.getGeometryDataBuilder(g).build()));
	}

	@Test
	public void testGraphData() {
		GraphDataIO graph = new GraphDataIO();
		graph.addEdge(new Edge(new Vertex(48,  8), new Vertex(49, 9)));
		graph.addEdge(new Edge(new Vertex(49,  8), new Vertex(49, 9)));
		graph.addEdge(new Edge(new Vertex(49,  9), new Vertex(49, 9)));
		graph.addEdge(new Edge(new Vertex(49,  9), new Vertex(50, 9)));
		graph.addEdge(new Edge(new Vertex(49,  9), new Vertex(50, 10)));
		assertEquals(graph, ProtobufConverter.getGraphData(ProtobufConverter.getGraphDataBuilder(graph).build()));
	}

	@Test
	public void testWaypoint() {
		Waypoint wp = new Waypoint(0, 0, null, null);
		assertEquals(wp, ProtobufConverter.getWaypoint(ProtobufConverter.getWaypointBuilder(wp).build()));
		wp.setPOI(new POI(0, 0, null, null, null, null));
		assertEquals(wp, ProtobufConverter.getWaypoint(ProtobufConverter.getWaypointBuilder(wp).build()));
	}
}