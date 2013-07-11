package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.OSMDataPreprocessor;

/**
 * This class controls several Preprocessors.
 *
 * @author Matthias Stumpp
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
				success = locFile.mkdirs();
				isQuit = locPath.equals(CMD_QUIT) || locPath.length() == 0;
			}
			while (!success || isQuit) {
				System.out.println("Please enter the destination-path where the graph-data should be written:");
				String graphPath = inReader.readLine();
				graphFile = new File(graphPath);
				success = graphFile.mkdirs();
				isQuit = graphPath.equals(CMD_QUIT) || graphPath.length() == 0;
			}
			if (success && !isQuit) {
				OSMDataPreprocessor prep = new OSMDataPreprocessor(osmFile, locFile, graphFile);
				prep.parse();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}