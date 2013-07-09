package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GraphDataIO implements Serializable {

    /**
     * Temporary Serial version ID as long as Java serialization is used
     */
    private static final long serialVersionUID = 3394680623853287035L;


    /**
     * Stores a list of Edge objects.
     */
    private List<Edge> edges;


    /**
     * Initializes a GraphDataIO object.
     */
    public GraphDataIO() {
        edges = new ArrayList<Edge>();
    }


    /**
     * Returns a list of all Vertex objects contained in all edges.
     *
     * @return List<Vertex> List of all egdes.
     */
    public List<Vertex> getVertices() {
        List<Vertex> vertices = new ArrayList<Vertex>();
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
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
        oos.writeObject(objectToSave);
        oos.flush();
        oos.close();
    }


    /**
     * Loads and returns a GraphDataIO  object from a given file.
     *
     * @param source Location of source file in file system.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static GraphDataIO load(File source) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source)));
        GraphDataIO graph = (GraphDataIO) ois.readObject();
        ois.close();
        return graph;
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
        edges.addAll(edges);
    }


    /**
     * Returns the list of all Edges.
     *
     * @return List<Edge>.
     */
    public List<Edge> getEdges() {
        return edges;
    }

}