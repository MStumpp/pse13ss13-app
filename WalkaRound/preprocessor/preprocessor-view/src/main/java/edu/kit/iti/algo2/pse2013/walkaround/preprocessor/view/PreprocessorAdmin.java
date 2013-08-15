package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.OSMDataPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.wikipedia.WikipediaPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.FileUtil;
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
	Logger logger = Logger.getLogger(PreprocessorAdmin.class.getSimpleName());

	@Option(name = "--input", required = true, usage="Location of the raw OSM-file")
	public String input;

	@Option(name = "--location_out", usage="Location of the LocationData-output")
	public String locationOutput = FileUtil.getFile("locationData.pbf").getAbsolutePath();

	@Option(name = "--graph_out", usage="Location of the GraphData-output")
	public String graphOutput = FileUtil.getFile("graphData.pbf").getAbsolutePath();

	@Option(name = "--geometry_out", usage="Location of the GeometryData-output")
	public String geometryOutput = FileUtil.getFile("geometryData.pbf").getAbsolutePath();

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

			logger.info("Fetching Wikipedia-information...");
			LocationDataIO locData = LocationDataIO.load(locFile);
			WikipediaPreprocessor.preprocessWikipediaInformation(locData);
			logger.info("Writing LocationDataIo again with Wikipedia-Information...");
			LocationDataIO.save(locData, locFile);

			logger.info("Start building GeometryData...");
			GeometryDataIO geomData = GeometryDataPreprocessor.preprocessGeometryDataIO(
                    new ArrayList<Geometrizable>(GraphDataIO.load(graphFile).getVertices()));
			GeometryDataIO.save(geomData, geomFile);
			logger.info("Writing GeometryDataIO finished.");
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