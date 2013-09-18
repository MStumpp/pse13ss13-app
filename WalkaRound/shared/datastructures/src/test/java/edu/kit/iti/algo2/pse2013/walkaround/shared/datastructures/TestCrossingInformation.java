package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestCrossingInformation {
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalConstructor1() {
		new CrossingInformation((float[])null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalConstructor2() {
		new CrossingInformation((List<Float>)null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalConstructor3() {
		new CrossingInformation(new float[0]);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalConstructor4() {
		new CrossingInformation(new LinkedList<Float>());
	}
	@Test
	public void testCrossingAngles() {
		CrossingInformation ci = new CrossingInformation(new float[]{1.0f, 2.0f, 3.0f});
		assertEquals(1f, ci.getCrossingAngle(0), 1e-45f);
		assertEquals(2f, ci.getCrossingAngle(1), 1e-45f);
		assertEquals(3f, ci.getCrossingAngle(2), 1e-45f);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testAngleOverflow() {
		CrossingInformation ci = new CrossingInformation(new float[]{1.0f, 2.0f, 3.0f});
		ci.getCrossingAngle(-1);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testAngleOverflow2() {
		CrossingInformation ci = new CrossingInformation(new float[]{1.0f, 2.0f, 3.0f});
		ci.getCrossingAngle(3);
	}
	@Test
	public void testNextAngleOnRoute() {
		CrossingInformation ci = new CrossingInformation(new float[]{1.0f, 2.0f, 3.0f});
		assertFalse(ci.setNextCrossingAngleOnRoute(-1));
		assertTrue(ci.setNextCrossingAngleOnRoute(0));
		assertTrue(ci.setNextCrossingAngleOnRoute(2));
		assertFalse(ci.setNextCrossingAngleOnRoute(3));
	}
	@Test
	public void testEquals() {
		CrossingInformation ci = new CrossingInformation(new float[]{1.0f, 2.0f, 3.0f});
		CrossingInformation ci2 = new CrossingInformation(new float[]{1.0f, 2.0f, 3.0f});
		CrossingInformation ci3 = new CrossingInformation(new float[]{1.0f, 2.0f, 4.0f});
		CrossingInformation ci4 = new CrossingInformation(new float[]{1.0f, 2.0f, 3.0f});
		ci4.setNextCrossingAngleOnRoute(1);

		assertFalse(ci.equals(null));
		assertFalse(ci.equals("Test"));
		assertTrue(ci.equals(ci));
		assertTrue(ci.equals(ci2));
		assertFalse(ci.equals(ci3));
		assertFalse(ci.equals(ci4));
	}
	@Test(expected=IllegalArgumentException.class)
	public void testNullInList() {
		LinkedList<Float> list = new LinkedList<Float>();
		list.add(null);
		list.add(1.0f);
		CrossingInformation ci = new CrossingInformation(list);
		assertNotNull(ci);

		list = new LinkedList<Float>();
		list.add(null);
		ci = new CrossingInformation(list);
	}
}