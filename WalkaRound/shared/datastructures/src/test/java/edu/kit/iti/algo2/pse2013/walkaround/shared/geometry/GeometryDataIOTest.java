package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Graph;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.kit.iti.algo2.pse2013.walkaround.pbf.ProtobufConverter;
import java.lang.reflect.Field;

/**
 * GeometryDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryDataIOTest {

	private static String fileLocation = System.getProperty("java.io.tmpdir")
			+ File.separator + "geometryDataIO";


    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = Graph.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);

        Field idCounter = Vertex.class.getDeclaredField("idCounter");
        idCounter.setAccessible(true);
        idCounter.setInt(null, 0);
    }


    @Test
    @Ignore
    public void testSandAndLoad() {
		GeometryDataIO writeGeometryDataIO = getGeometryDataIO();
		int numDimensions = writeGeometryDataIO.getNumDimensions();

		try {
			OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(fileLocation)));
			ProtobufConverter.getGeometryDataBuilder(writeGeometryDataIO).build().writeTo(out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File f = new File(fileLocation);
		Assert.assertTrue(f.exists());

		GeometryDataIO readGeometryDataIO = null;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(new File(fileLocation)));
			ProtobufConverter.getGeometryData(SaveGeometryData.parseFrom(in));
			in.close();
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
		Assert.assertEquals(readGeometryDataIO.getRoot().getGeometrizable()
				.valueForDimension(0), writeGeometryDataIO.getRoot()
				.getGeometrizable().valueForDimension(0), 0.d);
		// Check, if value for dimension 1 is the same
		Assert.assertEquals(readGeometryDataIO.getRoot().getGeometrizable()
				.valueForDimension(1), writeGeometryDataIO.getRoot()
				.getGeometrizable().valueForDimension(1), 0.d);

		// Check, if number of dimensions is the same
		Assert.assertEquals(readGeometryDataIO.getNumDimensions(), numDimensions);
	}


	private GeometryDataIO getGeometryDataIO() {
        GeometryDataIO geometryDataIO = new GeometryDataIO(new GeometryNode(new Vertex(1.d, 2.d)), 2);
        return geometryDataIO;
    }

}
