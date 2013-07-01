package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
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

        GeometryDataIO geometryDataIO = new GeometryDataIO(new GeometryNode(new Vertex(1.d, 2.d), null, null));

        try {
            GeometryDataIO.save(geometryDataIO, new File(fileLocaton));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f = new File(fileLocaton);
        Assert.assertTrue(f.exists());

        geometryDataIO = null;
        try {
            geometryDataIO = GeometryDataIO.load(new File(fileLocaton));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(geometryDataIO);
    }

}
