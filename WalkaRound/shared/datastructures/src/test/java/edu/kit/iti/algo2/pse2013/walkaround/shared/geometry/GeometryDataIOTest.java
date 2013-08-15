package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

/**
 * GeometryDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryDataIOTest {

    private static File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "geometryData.pbf");

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field idCounter = Vertex.class.getDeclaredField("idCounter");
        idCounter.setAccessible(true);
        idCounter.setInt(null, 0);
    }


    @Test
    public void testSaveAndLoad() throws IOException {
		GeometryDataIO writeGeometryDataIO = getGeometryDataIO();
		int numDimensions = writeGeometryDataIO.getNumDimensions();
		GeometryDataIO.save(writeGeometryDataIO, file);

		Assert.assertTrue(file.exists());

		GeometryDataIO readGeometryDataIO = GeometryDataIO.load(file);

		// Check, if something was read
		Assert.assertNotNull(readGeometryDataIO);
		// Check, if the same number of POI was written and read
		Assert.assertNotNull(readGeometryDataIO.getRoot());
		// Check, if the same number of POI was written and read
		Assert.assertNotNull(readGeometryDataIO.getRoot().getGeometrizables());
		for (int i = 0; i < readGeometryDataIO.getRoot().getGeometrizables().size(); i++) {
			for (int j = 0; j < readGeometryDataIO.getRoot().getGeometrizables().get(i).numberDimensions(); j++) {
				try {
					Assert.assertEquals(
						writeGeometryDataIO.getRoot().getGeometrizables().get(i).valueForDimension(j),
						readGeometryDataIO.getRoot().getGeometrizables().get(i).valueForDimension(j),
						0
					);
				} catch (RuntimeException e) {}
			}
		}
		// Check, if number of dimensions is the same
		Assert.assertEquals(readGeometryDataIO.getNumDimensions(), numDimensions);
	}


	private GeometryDataIO getGeometryDataIO() {
		LinkedList<Geometrizable> list = new LinkedList<Geometrizable>();
		list.add(new Vertex(1.d, 2.d));
		list.add(new GeometrizableWrapper(new POI(0, 0, null, null, null, null), 0));
		list.add(new POI(0, 0, null, null, null, null));
		list.add(new Edge(new Vertex(0, 0), new Vertex(0, 1)));
        return new GeometryDataIO(new GeometryNode(5, list), 2);
    }

}
