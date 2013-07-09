package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.view;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import edu.kit.iti.algo2.pse2013.walkaround.pbf.ProtobufConverter;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGraphData;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveLocationData;

/**
 * PreprocessorAdminTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class PreprocessorAdminTest {
	private static final File GRAPH_DATA_FILE = new File(
			System.getProperty("java.io.tmpdir") + File.separatorChar
					+ "walkaround" + File.separatorChar + "graphData.io");
	private static final File GEOMETRY_DATA_FILE = new File(
			System.getProperty("java.io.tmpdir") + File.separatorChar
					+ "walkaround" + File.separatorChar + "geometryData.io");
	private static final File LOCATION_DATA_FILE = new File(
			System.getProperty("java.io.tmpdir") + File.separatorChar
					+ "walkaround" + File.separatorChar + "locationData.io");

	@Test
	@Ignore
	public void testPreprocessGraphDataIO() {

		File verticesFile = new File(
				"/Users/Matthias/Workspace/PSE/data/_nodes.txt");
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
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		/*
		 * File geometryFile = new
		 * File("/Users/Matthias/Workspace/PSE/data/_geometry.txt"); try { input
		 * = new FileReader(geometryFile); } catch (FileNotFoundException e) {
		 * e.printStackTrace(); return; }
		 *
		 * Map<Long, String> geometry = new HashMap<>();
		 */

		File edgesFile = new File(
				"/Users/Matthias/Workspace/PSE/data/_edges.txt");
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
			OutputStream fileOut = new BufferedOutputStream(new FileOutputStream(GRAPH_DATA_FILE));
			ProtobufConverter.getGraphDataBuilder(graphDataIO).build().writeTo(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void testPreprocessLocationDataIO() {

		File verticesFile = new File(
				"/Users/Matthias/Workspace/PSE/data/restaurant.csv");
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
		int idCounter = 1;
		try {
			while ((myLine = bufRead.readLine()) != null) {
				String[] array = myLine.split(";");
				locationDataIO.addPOI(new POI(format.parse(array[1])
						.doubleValue(), format.parse(array[0]).doubleValue(),
						array[2].replace("\"", ""), array[2].replace("\"", ""),
						"http://www.walkaround.com", new int[] { 0, 1 }));
				idCounter += 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try {
			OutputStream fileOut = new BufferedOutputStream(new FileOutputStream(LOCATION_DATA_FILE));
			ProtobufConverter.getLocationDataBuilder(locationDataIO).build().writeTo(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPreprocessGeometryDataIO() {

		GraphDataIO graphDataIO = null;
		try {
			InputStream graphIn = new BufferedInputStream(new FileInputStream(GRAPH_DATA_FILE));
			graphDataIO = ProtobufConverter.getGraphData(SaveGraphData.parseFrom(graphIn));
			graphIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		LocationDataIO locationDataIO = null;
		try {
			InputStream locationIn = new BufferedInputStream(new FileInputStream(LOCATION_DATA_FILE));
			locationDataIO = ProtobufConverter.getLocationData(SaveLocationData.parseFrom(locationIn));
			locationIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(graphDataIO, locationDataIO);
		try {
			OutputStream out = new BufferedOutputStream(new FileOutputStream(GEOMETRY_DATA_FILE));
			ProtobufConverter.getGeometryDataBuilder(geometryDataIO).build().writeTo(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
