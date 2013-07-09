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

import android.util.Log;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Graph;

public class GraphDataIO implements Serializable {
	/**
	 * Temporary Serial version ID as long as Java serialization is used
	 */
	private static final long serialVersionUID = 3394680623853287035L;

	private ArrayList<Edge> edges = new ArrayList<Edge>();
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
	public Graph getGraph() {
		Log.d(this.getClass().getSimpleName(), "getGraph()-Stub!");
		return null;
	}
}