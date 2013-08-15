package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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
    @Ignore
    public void testSaveAndLoad() {
		GeometryDataIO writeGeometryDataIO = getGeometryDataIO();
		int numDimensions = writeGeometryDataIO.getNumDimensions();
		try {
			GeometryDataIO.save(writeGeometryDataIO, file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Assert.assertTrue(file.exists());

		GeometryDataIO readGeometryDataIO = null;
		try {
			readGeometryDataIO = GeometryDataIO.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check, if something was read
		Assert.assertNotNull(readGeometryDataIO);
		// Check, if the same number of POI was written and read
		Assert.assertNotNull(readGeometryDataIO.getRoot());
		// Check, if the same number of POI was written and read
		Assert.assertNotNull(readGeometryDataIO.getRoot().getGeometrizable());
		// Check, if value for dimension 0 is the same
		Assert.assertEquals(readGeometryDataIO.getRoot().getGeometrizable().valueForDimension(0), writeGeometryDataIO.getRoot().getGeometrizable().valueForDimension(0), 0.d);
		// Check, if value for dimension 1 is the same
		Assert.assertEquals(readGeometryDataIO.getRoot().getGeometrizable().valueForDimension(1), writeGeometryDataIO.getRoot().getGeometrizable().valueForDimension(1), 0.d);

		// Check, if number of dimensions is the same
		Assert.assertEquals(readGeometryDataIO.getNumDimensions(), numDimensions);
	}


	private GeometryDataIO getGeometryDataIO() {
		LinkedList<Geometrizable> list = new LinkedList<Geometrizable>();
		list.add(new Vertex(1.d, 2.d));
        GeometryDataIO geometryDataIO = new GeometryDataIO(new GeometryNode(null, list), 2);
        return geometryDataIO;
    }

}
