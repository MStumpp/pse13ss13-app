package edu.kit.iti.algo2.pse2013.walkaround.client.model.route;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

@RunWith(RobolectricTestRunner.class)
public class RouteInfoTransferTest {

	private RouteInfoTransfer routeInfoTrans;
	private RouteInfoTransfer routeInfoTransWithCoords;
	private List<Coordinate> coords;

	@Before
	public void setUp() {
		routeInfoTrans = new RouteInfoTransfer();
		coords = new ArrayList<>();
		coords.add(new Coordinate(2d, 4d));
		coords.add(new Coordinate(5d, 10d));
		coords.add(new Coordinate(8d, 16d));
		routeInfoTransWithCoords = new RouteInfoTransfer(coords);
	}

	@Test
	public void testGeneral() {
		assertTrue(routeInfoTransWithCoords.getCoordinates().size() == 3);
		routeInfoTrans.setError("test");
		assertEquals("test", routeInfoTrans.getError());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructException() {
		RouteInfoTransfer rt = new RouteInfoTransfer(null);
	}

	@Test
	public void testExecution() {
		routeInfoTrans.addCoordinates(new Coordinate(42d, 42d));
		routeInfoTrans.setCoordinates(routeInfoTrans.getCoordinates());
		routeInfoTrans.postProcessRoundtrip();
		routeInfoTrans.postProcessShortestPath();
		routeInfoTransWithCoords.postProcessRoundtrip();
		routeInfoTransWithCoords.postProcessShortestPath();
		assertTrue(true);
	}
}
