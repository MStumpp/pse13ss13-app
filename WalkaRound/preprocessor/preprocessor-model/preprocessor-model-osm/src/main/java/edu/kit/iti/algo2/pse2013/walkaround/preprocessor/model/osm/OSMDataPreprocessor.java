package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import crosby.binary.file.BlockInputStream;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.pbf.PBF_FileBlockParser;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

public class OSMDataPreprocessor {

	private static final Logger logger = Logger.getLogger(OSMDataPreprocessor.class.getSimpleName());

	private File graphDestination;
	private File locationDestination;
	private File osmSource;

	/**
	 *
	 * @param osmSource
	 * @param locationDestination
	 * @param graphDestination
	 * @throws IOException
	 */
	public OSMDataPreprocessor(File osmSource, File locationDestination, File graphDestination) throws IOException {
		this.graphDestination = graphDestination;
		this.locationDestination = locationDestination;
		this.osmSource = osmSource;
		if (!this.osmSource.exists()) {
			throw new FileNotFoundException(String.format("The OSM-Data-File was not found at '%s'!", osmSource.getAbsolutePath()));
		}
		if (!this.locationDestination.exists()) {
			this.locationDestination.getParentFile().mkdirs();
			this.locationDestination.createNewFile();
		}
		if (!this.graphDestination.exists()) {
			this.graphDestination.getParentFile().mkdirs();
			this.graphDestination.createNewFile();
		}
	}
	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void parse() throws FileNotFoundException, IOException {
		GraphDataIO graphData = new GraphDataIO();
		LocationDataIO locationData = new LocationDataIO();
		/* NOTE: Den FileInputStream <b>nicht</b> mit dem Dekorierer {@link java.io.BufferedInputStream} versehen!
		 * Das Programm crasht sonst unter Umständen (siehe {@link crosby.binary.file.BlockinputStream}, Zeile 25).
		 * Der InputStream muss "seekable" sein.
		 */
		PBF_FileBlockParser parser = new PBF_FileBlockParser(graphData, locationData);
		do {
			BlockInputStream blockStream = new BlockInputStream(new FileInputStream(osmSource), parser);
			blockStream.process();
			blockStream.close();
		} while (parser.needsFurtherRun());

		logger.info(String.format("Try writing %d edges (%d vertices) to GraphDataIO-file...", graphData.getEdges().size(), graphData.getVertices().size()));

		GraphDataIO.save(graphData, graphDestination);

		logger.info("Writing GraphDataIO finished (written data not yet validated).");

		logger.info(String.format("Try writing %d POIs and %d Areas to LocationDataIO-file...", locationData.getPOIs().size(), locationData.getAreas().size()));

		LocationDataIO.save(locationData, locationDestination);

		logger.info("Writing LocationDataIO finished (written data not yet validated).");

		logger.info("Start reading GraphDataIO...");

		GraphDataIO graph = GraphDataIO.load(graphDestination);

		logger.info(String.format("Read %d edges with %d vertices from file.", graph.getEdges().size(), graph.getVertices().size()));

		logger.info("Start reading LocationDataIO...");

		LocationDataIO location = LocationDataIO.load(locationDestination);

		logger.info(String.format("Read %d POI and %d areas from file.", location.getPOIs().size(), location.getAreas().size()));
	}

	public static void main(String[] args) throws IOException {
		OSMDataPreprocessor prep = new OSMDataPreprocessor(new File("/home/florian/OSM/Karten/2013-04-30-RegBez-KA.osm.pbf"), new File(System.getProperty("java.io.tmpdir") + File.separatorChar + "locationData.pbf"), new File(System.getProperty("java.io.tmpdir") + File.separatorChar + "graphData.pbf"));
		prep.parse();
	}
}