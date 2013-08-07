package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.OSMDataPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.wikipedia.WikipediaPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;

/**
 * This class controls several Preprocessors.
 *
 * @author Florian Sch&auml;fer
 * @version 1.0
 */
public class PreprocessorAdmin {

	private static final Object CMD_QUIT = "quit";

	public static void main(String[] args) {
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		boolean success = false;
		boolean isQuit = false;

		File osmFile = null;
		File locFile = null;
		File graphFile = null;
		File geomFile = null;

		try {
			while (!success || isQuit) {
				System.out.println("Please enter the path of the raw OSM-data:");
				String osmPath = inReader.readLine();
				osmFile = new File(osmPath);
				success = osmFile.exists() && osmFile.isFile();
				isQuit = osmPath.equals(CMD_QUIT) || osmPath.length() == 0;
			}
			success = false;
			while (!success || isQuit) {
				System.out.println("Please enter the destination-path where the location-data should be written:");
				String locPath = inReader.readLine();
				locFile = new File(locPath);
				success = locFile.getParentFile().exists() || locFile.getParentFile().mkdirs();
				isQuit = locPath.equals(CMD_QUIT) || locPath.length() == 0;
			}
			success = false;
			while (!success || isQuit) {
				System.out.println("Please enter the destination-path where the graph-data should be written:");
				String graphPath = inReader.readLine();
				graphFile = new File(graphPath);
				success = graphFile.getParentFile().exists() || graphFile.getParentFile().mkdirs();
				isQuit = graphPath.equals(CMD_QUIT) || graphPath.length() == 0;
			}
			success = false;
			while (!success || isQuit) {
				System.out.println("Please enter the destination-path where the geometry-data should be written:");
				String geomPath = inReader.readLine();
				geomFile = new File(geomPath);
				success = geomFile.getParentFile().exists() || geomFile.getParentFile().mkdirs();
				isQuit = geomPath.equals(CMD_QUIT) || geomPath.length() == 0;
			}
			if (success && !isQuit) {
				OSMDataPreprocessor prep = new OSMDataPreprocessor(osmFile, locFile, graphFile);
				prep.parse();
				WikipediaPreprocessor.preprocessWikipediaInformation(LocationDataIO.load(locFile));
				GeometryDataIO geomData = GeometryDataPreprocessor.preprocessGeometryDataIO(GraphDataIO.load(graphFile), LocationDataIO.load(locFile));
				GeometryDataIO.save(geomData, geomFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}