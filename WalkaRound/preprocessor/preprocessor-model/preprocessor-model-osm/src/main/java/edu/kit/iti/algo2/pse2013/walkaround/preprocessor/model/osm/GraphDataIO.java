package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Vector;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;

public class GraphDataIO {
	private Vector<Edge> edges = new Vector<>();
	/**
	 * Saves the GraphDataIO-object, which is given as parameter, as the {@code destination}-file.
	 * @param objectToSave
	 * @param destination
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void save(GraphDataIO objectToSave, File destination) throws FileNotFoundException, IOException {
		//TODO: Replace the simple java-serialization by a better one
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
		oos.writeObject(objectToSave);
		oos.flush();
		oos.close();
	}
	/**
	 * Loads a GraphDataIO-object from the given {@code source}-file
	 * @param destination
	 * @param objectToSave
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static GraphDataIO load(File source) throws FileNotFoundException, IOException, ClassNotFoundException {
		//TODO: Replace the simple java-serialization by a better one
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source)));
		GraphDataIO graph = (GraphDataIO) ois.readObject();
		ois.close();
		return graph;
	}
	public void addEdge(Edge e) {
		this.edges.add(e);
	}
	public List<Edge> getEdges() {
		return this.edges;
	}
}