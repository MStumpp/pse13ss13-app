package edu.kit.iti.algo2.pse2013.walkaround.shared.pbf;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Area;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Category;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;

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
		System.out.println(c.getCrossingInformation().getCrossingAngles());
		assertEquals(c, ProtobufConverter.getCoordinate(ProtobufConverter.getCoordinateBuilder(c).build()));
	}
}