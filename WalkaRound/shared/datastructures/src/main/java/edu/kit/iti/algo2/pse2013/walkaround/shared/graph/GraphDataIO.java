package edu.kit.iti.algo2.pse2013.walkaround.shared.graph;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.ProtobufConverter;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.Protos;

public class GraphDataIO {

    /**
     * Stores a set of Edge objects.
     */
    private Set<Edge> edges;


    /**
     * Initializes a GraphDataIO object.
     */
    public GraphDataIO() {
        edges = new HashSet<Edge>();
    }


    /**
	 * Adds an Edge to the list of all Edges.
	 *
	 * @param edge Edge to be added.
	 */
	public void addEdge(Edge edge) {
	    edges.add(edge);
	}


	/**
	 * Adds a list of Edges to the list of all Edges.
	 *
	 * @param edges List of Edges to be added.
	 */
	public void addEdges(List<Edge> edges) {
	    this.edges.addAll(edges);
	}


	/**
	 * Returns the set of all Edges.
	 *
	 * @return Set<Edge>.
	 */
	public Set<Edge> getEdges() {
	    return edges;
	}


	/**
     * Returns a set of all Vertex objects contained in all edges.
     *
     * @return Set<Vertex> List of all egdes.
     */
    public Set<Vertex> getVertices() {
        Set<Vertex> vertices = new HashSet<Vertex>();
        for (Edge edge : edges)
            for (Vertex vertex : edge.getVertices())
                vertices.add(vertex);
        return vertices;
    }


    /**
     * Saves the GraphDataIO  object to an external file.
     *
     * @param objectToSave GeometryDataIO object to save.
     * @param destination Location of output file on file system.
     * @throws java.io.IOException
     */
    public static void save(GraphDataIO objectToSave, File destination) throws IOException {
        OutputStream out = new BufferedOutputStream(new FileOutputStream(destination));
        ProtobufConverter.getGraphDataBuilder(objectToSave).build().writeTo(out);
        out.flush();
        out.close();
    }


    /**
     * Loads and returns a GraphDataIO  object from a given file.
     *
     * @param source Location of source file in file system.
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    public static GraphDataIO load(File source) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(source));
        GraphDataIO geom = ProtobufConverter.getGraphData(Protos.SaveGraphData.parseFrom(in));
        in.close();
        return geom;
    }

}