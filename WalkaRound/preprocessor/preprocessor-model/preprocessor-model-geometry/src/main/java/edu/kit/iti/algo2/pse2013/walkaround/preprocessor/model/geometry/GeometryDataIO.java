package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import java.io.*;

/**
 * This class contains some preprocessed data by GeometryDataPreprocessor.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryDataIO implements Serializable {

    /**
     * Temporary Serial version ID as long as Java serialization is used
     */
    private static final long serialVersionUID = 3394680623853287035L;


    /**
     * Root GeometryNode.
     */
    private GeometryNode root;


    /**
     * Initializes GeometryDataIO with GeometryNode as root.
     *
     * @param root GeometyNode as root of tree.
     */
    public GeometryDataIO(GeometryNode root) {
        this.root = root;
    }


    /**
     * Returns the root GeometryNode.
     *
     * @return GeometryNode root of tree.
     */
    public GeometryNode getRoot() {
        return root;
    }


    /**
     * Saves the GeometryDataIO object to an external file.
     *
     * @param objectToSave GeometryDataIO object to save.
     * @param destination Location of output file on file system.
     * @throws java.io.IOException
     */
    public static void save(GeometryDataIO objectToSave, File destination) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
        oos.writeObject(objectToSave);
        oos.flush();
        oos.close();
    }


    /**
     * Loads and returns a GeometryDataIO object from a given file.
     *
     * @param source Location of source file in file system.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static GeometryDataIO load(File source) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source)));
        GeometryDataIO geometryDataIO = (GeometryDataIO) ois.readObject();
        ois.close();
        return geometryDataIO;
    }

}
