package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.view;

import java.io.File;
import java.io.IOException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

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
	@Option(name = "--input", required = true, usage="Location of the raw OSM-file")
	public String input;

	private String dropboxPath = System.getProperty("user.home") + File.separatorChar + "Dropbox" + File.separatorChar + "Studium" + File.separatorChar + "PSE" + File.separatorChar;

	@Option(name = "--location_out", usage="Location of the LocationData-output")
	public String locationOutput = dropboxPath + "locationData.pbf";

	@Option(name = "--graph_out", usage="Location of the GraphData-output")
	public String graphOutput = dropboxPath + "graphData.pbf";

	@Option(name = "--geometry_out", usage="Location of the GeometryData-output")
	public String geometryOutput = dropboxPath + "geometryData.pbf";

	public static void main(String[] args) {
		System.exit(new PreprocessorAdmin().run(args));
	}

	private int run(String[] args) {
		CmdLineParser p = new CmdLineParser(this);
		try {
			p.parseArgument(args);
			run();
			File osmFile = new File(input);
			File geomFile = new File(geometryOutput);
			File graphFile = new File(graphOutput);
			File locFile = new File(locationOutput);
			OSMDataPreprocessor prep = new OSMDataPreprocessor(osmFile, locFile, graphFile);

			geomFile.getParentFile().mkdirs();
			graphFile.getParentFile().mkdirs();
			locFile.getParentFile().mkdirs();

			prep.parse();
			WikipediaPreprocessor.preprocessWikipediaInformation(LocationDataIO.load(locFile));
			GeometryDataIO geomData = GeometryDataPreprocessor.preprocessGeometryDataIO(GraphDataIO.load(graphFile), LocationDataIO.load(locFile));
			GeometryDataIO.save(geomData, geomFile);
			return 0;
		} catch (CmdLineException | IOException e) {
			System.err.println(e.getMessage());
			p.printUsage(System.err);
			return 1;
		}
	}

	private void run() {
		System.out.format(
			"Input file:\n\t%s\n"
			+ "Output files:\n"
			+ "\tGeometryData: %s\n"
			+ "\tGraphData:    %s\n"
			+ "\tLocationData: %s\n",
			input, geometryOutput, graphOutput, locationOutput);
	}
}