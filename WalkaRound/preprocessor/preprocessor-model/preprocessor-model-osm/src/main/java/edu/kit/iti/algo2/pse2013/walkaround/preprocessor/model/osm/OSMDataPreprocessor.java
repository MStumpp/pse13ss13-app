package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import crosby.binary.file.BlockInputStream;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.pbf.PBF_FileBlockParser;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;

public class OSMDataPreprocessor {
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
			this.locationDestination.mkdirs();
			this.locationDestination.createNewFile();
		}
		if (!this.graphDestination.exists()) {
			this.graphDestination.mkdirs();
			this.graphDestination.createNewFile();
		}
	}
	/**
	 *
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
		BlockInputStream blockStream = new BlockInputStream(new FileInputStream(osmSource), new PBF_FileBlockParser(graphData, locationData));
		blockStream.process();
		GraphDataIO.save(graphData, graphDestination);
		LocationDataIO.save(locationData, locationDestination);
	}
	/**
	 *
	 * @param c1
	 * @param c2
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void parseRectangle(Coordinate c1, Coordinate c2) throws FileNotFoundException, IOException {
		GraphDataIO graphData = new GraphDataIO();
		GraphDataIO.save(graphData, graphDestination);
	}
}
