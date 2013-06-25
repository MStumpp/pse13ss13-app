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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;

public class GraphDataIO implements Serializable {
	/**
	 * Temporary Serial version ID as long as Java serialization is used
	 */
	private static final long serialVersionUID = 3394680623853287035L;

	private ArrayList<Edge> edges = new ArrayList<>();
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
	public void addEdges(Collection<Edge> e) {
		Logger.getLogger(this.getClass().getName()).info(edges.size() + " edges");
		this.edges.addAll(e);
	}
	public List<Edge> getEdges() {
		return this.edges;
	}
}