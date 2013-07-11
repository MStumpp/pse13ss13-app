package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import crosby.binary.file.BlockInputStream;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.pbf.PBF_FileBlockParser;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.ProtobufConverter;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveGraphData;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos.SaveLocationData;

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

		FileOutputStream graphOutput = new FileOutputStream(graphDestination);
		ProtobufConverter.getGraphDataBuilder(graphData).build().writeTo(graphOutput);
		graphOutput.flush();
		graphOutput.close();

		FileOutputStream locationOutput = new FileOutputStream(locationDestination);
		ProtobufConverter.getLocationDataBuilder(locationData).build().writeTo(locationOutput);
		locationOutput.flush();
		locationOutput.close();

		FileInputStream fis = new FileInputStream(graphDestination);
		GraphDataIO graph = ProtobufConverter.getGraphData(SaveGraphData.parseFrom(fis));
		fis.close();
		System.out.println(graph.getEdges().size() + " Edges are written to the file");
		for (Edge e : graph.getEdges()) {
			//System.out.println("Edge: " + e);
		}

		FileInputStream fis2 = new FileInputStream(locationDestination);
		LocationDataIO location = ProtobufConverter.getLocationData(SaveLocationData.parseFrom(fis2));
		fis2.close();
		System.out.println(location.getPOIs().size() + " POIs are written to the file");
		for (POI p : location.getPOIs()) {
			System.out.println("POI: " + p);
		}
	}

	public static void main(String[] args) throws IOException {
		OSMDataPreprocessor prep = new OSMDataPreprocessor(new File("/home/florian/OSM/Karten/2013-04-30-RegBez-KA.osm.pbf"), new File("/home/florian/Arbeitsfläche/locationData.io"), new File("/home/florian/Arbeitsfläche/graphData.io"));
		prep.parse();
	}
}