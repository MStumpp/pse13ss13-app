package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import org.junit.Test;


/**
 * GeometryDataPreprocessorTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryDataPreprocessorTest {

    @Test
    public void testPreprocessGraphDataIO() {
        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(getGraphDataIO(), getLocationDataIO());
        System.out.println(geometryDataIO.getRoot().toString());
    }

    private GraphDataIO getGraphDataIO() {
        GraphDataIO graphDataIO = new GraphDataIO();
        graphDataIO.addEdge(new Edge(new Vertex(2.d, 3.d), new Vertex(5.d, 4.d)));
        graphDataIO.addEdge(new Edge(new Vertex(9.d, 6.d), new Vertex(4.d, 7.d)));
        graphDataIO.addEdge(new Edge(new Vertex(8.d, 1.d), new Vertex(7.d, 2.d)));
        return graphDataIO;
    }

    private LocationDataIO getLocationDataIO() {
        LocationDataIO locationDataIO = new LocationDataIO();
        locationDataIO.addPOI(new POI(1.d, 2.d, 1, "poi 1", "info 1", "url 1", new int[]{1}));
        locationDataIO.addPOI(new POI(3.d, 4.d, 2, "poi 2", "info 2", "url 2", new int[]{1}));
        locationDataIO.addPOI(new POI(5.d, 7.d, 3, "poi 3", "info 3", "url 3", new int[]{1}));
        return locationDataIO;
    }

}
