package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * GeometryDataIOTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryDataIOTest {

    private static String fileLocaton = System.getProperty("java.io.tmpdir") + File.separator + "geometryDataIO";

    @Test
    public void testSandAndLoad() {

        GeometryDataIO writeGeometryDataIO = getGeometryDataIO();
        GeometryNode node = writeGeometryDataIO.getRoot();

        try {
            GeometryDataIO.save(writeGeometryDataIO, new File(fileLocaton));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f = new File(fileLocaton);
        Assert.assertTrue(f.exists());

        GeometryDataIO readGeometryDataIO = null;
        try {
            readGeometryDataIO = GeometryDataIO.load(new File(fileLocaton));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Check, if something was read
        Assert.assertNotNull(readGeometryDataIO);
        // Check, if the same number of POI was written and read
        Assert.assertNotNull(readGeometryDataIO.getRoot());
    }

    private GeometryDataIO getGeometryDataIO() {

        GeometryDataIO geometryDataIO = new GeometryDataIO(new GeometryNode(new Vertex(1.d, 2.d)), 2);
        return geometryDataIO;
    }

}
