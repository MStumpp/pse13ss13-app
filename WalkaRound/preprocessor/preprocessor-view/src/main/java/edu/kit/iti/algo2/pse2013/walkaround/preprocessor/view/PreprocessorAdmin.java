package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

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

	@Option(name = "--poi_out", usage="Location of the LocationData-output")
	public String poiOutput = FileUtil.getFile("poiData.pbf").getAbsolutePath();

	@Option(name = "--graph_out", usage="Location of the GraphData-output")
	public String graphOutput = FileUtil.getFile("graphData.pbf").getAbsolutePath();

	@Option(name = "--geometry_vertices_out", usage="Location of the GeometryData-output Vertices")
	public String geometryOutputVertices = FileUtil.getFile("geometryDataVertices.pbf").getAbsolutePath();

    @Option(name = "--geometry_edges_out", usage="Location of the GeometryData-output Edges")
    public String geometryOutputEdges = FileUtil.getFile("geometryDataEdges.pbf").getAbsolutePath();

    @Option(name = "--geometry_pois_out", usage="Location of the GeometryData-output POIs")
    public String geometryOutputPOIs = FileUtil.getFile("geometryDataPOIs.pbf").getAbsolutePath();

	public static void main(String[] args) {
		System.exit(new PreprocessorAdmin().run(args));
	}

	private int run(String[] args) {
		CmdLineParser p = new CmdLineParser(this);
		try {
			p.parseArgument(args);
			run();
			File osmFile = new File(input);
			File geomVerticesFile = new File(geometryOutputVertices);
            File geomEdgesFile = new File(geometryOutputEdges);
            File geomPOIsFile = new File(geometryOutputPOIs);
            File graphFile = new File(graphOutput);
			File locFile = new File(locationOutput);
			File poiFile = new File(poiOutput);
			OSMDataPreprocessor prep = new OSMDataPreprocessor(osmFile, locFile, graphFile);

			geomVerticesFile.getParentFile().mkdirs();
            geomEdgesFile.getParentFile().mkdirs();
            geomPOIsFile.getParentFile().mkdirs();
            graphFile.getParentFile().mkdirs();
			locFile.getParentFile().mkdirs();

			prep.parse();

			logger.info("Fetching Wikipedia-information...");
			LocationDataIO locData = LocationDataIO.load(locFile, null);
			WikipediaPreprocessor.preprocessWikipediaInformation(locData);
			logger.info("Writing POIData with Wikipedia-Information...");
			LocationDataIO.save(locData, locFile);
			locData.clearAreas();
			LocationDataIO.save(locData, poiFile);

			logger.info("Start building GeometryData Vertices...");
			GeometryDataIO geomDataVertices = GeometryDataPreprocessor.preprocessGeometryDataIO(
                    new ArrayList<Geometrizable>(GraphDataIO.load(graphFile).getVertices()));
            logger.info(geomDataVertices.getRoot().getGeometrizables().size() + " Vertex-Geometrizables in root");
			GeometryDataIO.save(geomDataVertices, geomVerticesFile);
			logger.info("Writing GeometryDataIO Vertices finished.");

            logger.info("Start building GeometryData Edges...");
            GeometryDataIO geomDataEdges = GeometryDataPreprocessor.preprocessGeometryDataIO(
                    new ArrayList<Geometrizable>(GraphDataIO.load(graphFile).getEdges()));
            logger.info(geomDataEdges.getRoot().getGeometrizables().size() + " Edge-Geometrizables in root");
            GeometryDataIO.save(geomDataEdges, geomEdgesFile);
            logger.info("Writing GeometryDataIO Edges finished.");

            logger.info("Start building GeometryData POIs...");
            GeometryDataIO geomDataPOIs = GeometryDataPreprocessor.preprocessGeometryDataIO(
                    new ArrayList<Geometrizable>(LocationDataIO.load(locFile, null).getPOIs()));
            logger.info(geomDataPOIs.getRoot().getGeometrizables().size() + " POI-Geometrizables in root");
            GeometryDataIO.save(geomDataPOIs, geomPOIsFile);
            logger.info("Writing GeometryDataIO POIs finished.");
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
			+ "\tGeometryData Vertices: %s\n"
            + "\tGeometryData Edges: %s\n"
            + "\tGeometryData POIs: %s\n"
            + "\tGraphData:    %s\n"
			+ "\tLocationData: %s\n",
			input, geometryOutputVertices, geometryOutputEdges, geometryOutputPOIs, graphOutput, locationOutput);
	}
}