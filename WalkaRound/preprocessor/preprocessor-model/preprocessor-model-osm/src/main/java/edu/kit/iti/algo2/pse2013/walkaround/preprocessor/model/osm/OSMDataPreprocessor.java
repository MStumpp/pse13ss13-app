package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

public class OSMDataPreprocessor {
	private File graphDestination;
	private File locationDestination;
	private File osmSource;

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
	public void parse() throws FileNotFoundException, IOException {
		GraphDataIO graphData = new GraphDataIO();
		GraphDataIO.save(graphDestination, graphData);
	}
	public void parseRectangle(Coordinate c1, Coordinate c2) throws FileNotFoundException, IOException {
		GraphDataIO graphData = new GraphDataIO();
		GraphDataIO.save(graphDestination, graphData);
	}
}
