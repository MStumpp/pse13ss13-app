package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.view;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataPreprocessor;
import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.shared.FileUtil;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;

/**
 * PreprocessorAdminTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class PreprocessorAdminTest {
	private static final File GRAPH_DATA_FILE = FileUtil.getFile("graphData.pbf");
	private static final File GEOMETRY_DATA_FILE = FileUtil.getFile("geometryData.pbf");
	private static final File LOCATION_DATA_FILE = FileUtil.getFile("locationData.pbf");


    @Test
    public void testPreprocessGeometryDataIO() {

        GraphDataIO graphDataIO = null;
        try {
            graphDataIO = GraphDataIO.load(GRAPH_DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LocationDataIO locationDataIO = null;
        try {
            locationDataIO = LocationDataIO.load(LOCATION_DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(new LinkedList<Geometrizable>(graphDataIO.getEdges()));

        try {
            GeometryDataIO.save(geometryDataIO, GEOMETRY_DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
