package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.pbf;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.OSMDataPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.FileUtil;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;

public class TestOSMPreprocessor {

	@Test(expected=FileNotFoundException.class)
	public void testNull() throws IOException {
		OSMDataPreprocessor odp = new OSMDataPreprocessor(new File("42"),
				new File(System.getProperty("java.io.tmpdir") + File.separatorChar + "42"),
				new File(System.getProperty("java.io.tmpdir") + File.separatorChar + "42"));
	}

	@Test
	public void testRun() throws IOException {
		OSMDataPreprocessor odp = new OSMDataPreprocessor(
			new File(System.getProperty("user.home") + File.separatorChar + "OSM" + File.separatorChar + "Karten" + File.separatorChar + "2013-09-12-RegBez-KA.osm.pbf"),
			FileUtil.getFile("locationData.pbf"),
			FileUtil.getFile("graphData.pbf")
		);
		odp.parse();
		LocationDataIO loc = LocationDataIO.load(FileUtil.getFile("locationData.pbf"), null);
		assertTrue(loc.getPOIs().size() > 0);
		assertTrue(loc.getAreas().size() > 0);
		GraphDataIO graph = GraphDataIO.load(FileUtil.getFile("graphData.pbf"));
		assertTrue(graph.getEdges().size() > 0);
		assertTrue(graph.getVertices().size() > 0);
	}
}
