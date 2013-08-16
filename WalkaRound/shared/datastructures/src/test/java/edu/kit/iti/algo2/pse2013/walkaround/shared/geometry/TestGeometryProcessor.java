/**
 *
 */
package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.FileUtil;

/**
 * @author Florian Sch&auml;fer
 *
 */
public class TestGeometryProcessor {

	@Test(expected=IllegalArgumentException.class)
	public void test() throws IOException, IllegalArgumentException, GeometryProcessorException, GeometryComputationNoSlotsException {
		GeometryProcessor gp = new GeometryProcessor(GeometryDataIO.load(FileUtil.getFile("geometryData.pbf")), 1);
		gp.getNearestGeometrizable(new GeometrySearch(new double[]{49.2, 8.5}));
		assertTrue(true);
		gp.getNearestGeometrizable(null);
	}
}