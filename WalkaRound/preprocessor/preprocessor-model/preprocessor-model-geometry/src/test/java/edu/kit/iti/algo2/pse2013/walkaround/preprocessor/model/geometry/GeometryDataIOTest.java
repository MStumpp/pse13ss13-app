package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.pbf.ProtobufConverter;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGeometryData;

/**
 * GeometryDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryDataIOTest {

	private static String fileLocation = System.getProperty("java.io.tmpdir")
			+ File.separator + "geometryDataIO";

	@Test
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
		Assert.assertEquals(readGeometryDataIO.getNumDimensions(),
				numDimensions);
	}

	private GeometryDataIO getGeometryDataIO() {

		GeometryDataIO geometryDataIO = new GeometryDataIO(new GeometryNode(
				new Vertex(1.d, 2.d)), 2);
		return geometryDataIO;
	}

}
