package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.FileUtil;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

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
	public void testPreprocessGraphDataIO() {

		File verticesFile = FileUtil.getFile("_nodes.txt");
		FileReader input;
		try {
			input = new FileReader(verticesFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		Map<Long, Vertex> vertices = new HashMap<Long, Vertex>();

		BufferedReader bufRead = new BufferedReader(input);
		String myLine;
		Vertex current;
		long osmID;
		try {
			while ((myLine = bufRead.readLine()) != null) {
				String[] array = myLine.split("\\s");
				osmID = Long.parseLong(array[0]);
				current = new Vertex(Double.parseDouble(array[1]),
						Double.parseDouble(array[2]));
				vertices.put(osmID, current);
			}
			bufRead.close();
		} catch (IOException e) {
			try {
				bufRead.close();
			} catch (IOException e1) {
				// EMPTY
			}
			e.printStackTrace();
			return;
		}

        /*File geometryFile = new File("/Users/Matthias/Workspace/PSE/data/_geometry.txt");
        try {
            input = new FileReader(geometryFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Map<Long, String> geometry = new HashMap<>();*/

		File edgesFile = FileUtil.getFile("_edges.txt");
		try {
			input = new FileReader(edgesFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

        GraphDataIO graphDataIO = new GraphDataIO();

		bufRead = new BufferedReader(input);
		try {
			while ((myLine = bufRead.readLine()) != null) {
				String[] array = myLine.split("\\s");
				graphDataIO.addEdge(new Edge(vertices.get(Long
						.parseLong(array[1])), vertices.get(Long
						.parseLong(array[2]))));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			GraphDataIO.save(graphDataIO, GRAPH_DATA_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPreprocessLocationDataIO() {

		File verticesFile = FileUtil.getFile("restaurant.csv");
		FileReader input;
		try {
			input = new FileReader(verticesFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

        LocationDataIO locationDataIO = new LocationDataIO();

        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        BufferedReader bufRead = new BufferedReader(input);
        String myLine;
        try {
            while ((myLine = bufRead.readLine()) != null)
            {
                String[] array = myLine.split(";");
                locationDataIO.addPOI(new POI(format.parse(array[1]).doubleValue(),
                        format.parse(array[0]).doubleValue(), array[2].replace("\"", ""), array[2].replace("\"", ""), new URL("http://www.walkaround.com"), new int[]{0, 1}));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (ParseException e) {
			e.printStackTrace();
		}

        try {
            LocationDataIO.save(locationDataIO, LOCATION_DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPreprocessGeometryDataIO() throws IOException {

        GraphDataIO graphDataIO = null;
        graphDataIO = GraphDataIO.load(GRAPH_DATA_FILE);

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(new LinkedList<Geometrizable>(graphDataIO.getEdges()));

        try {
            GeometryDataIO.save(geometryDataIO, GEOMETRY_DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
