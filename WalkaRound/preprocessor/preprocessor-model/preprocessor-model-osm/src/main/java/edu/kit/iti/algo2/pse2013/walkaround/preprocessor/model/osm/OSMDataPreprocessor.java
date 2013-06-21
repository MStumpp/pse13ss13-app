package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

public class OSMDataPreprocessor {
	private File graphDestination;
	private File locationDestination;
	private File osmSource;

	public void OSMDataPreprocessor(File osmSource, File locationDestination, File graphDestination) throws IOException {
		if (!osmSource.exists()) {
			throw new FileNotFoundException(String.format("The OSM-Data-File was not found at '%s'!", osmSource.getAbsolutePath()));
		}
		if (!locationDestination.exists()) {
			locationDestination.mkdirs();
			locationDestination.createNewFile();
		}
		if (!graphDestination.exists()) {
			graphDestination.mkdirs();
			graphDestination.createNewFile();
		}
		this.graphDestination = graphDestination;
		this.locationDestination = locationDestination;
		this.osmSource = osmSource;
	}
	public void parse() throws FileNotFoundException, IOException {
		GraphDataIO graphData = new GraphDataIO();
		GraphDataIO.save(graphDestination, graphData);
	}
	public void parseRectangle(Coordinate c1, Coordinate c2) throws FileNotFoundException, IOException {
		GraphDataIO graphData = new GraphDataIO();
		GraphDataIO.save(graphDestination, graphData);
	}
}
