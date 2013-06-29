package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
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

        GraphDataIO graphDataIO = getGraphDataIO();
        LocationDataIO locationDataIO = getLocationDataIO();
    }


    private GraphDataIO getGraphDataIO() {
        return new GraphDataIO();
    }


    private LocationDataIO getLocationDataIO() {
        return new LocationDataIO();
    }

}
