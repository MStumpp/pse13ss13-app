package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.view;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry.GeometryDataPreprocessor;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;


/**
 * PreprocessorAdminTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class PreprocessorAdminTest {

    @Test
    @Ignore
    public void testPreprocessGraphDataIO() {

        File verticesFile = new File("/Users/Matthias/Workspace/PSE/data/_nodes.txt");
        FileReader input;
        try {
            input = new FileReader(verticesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Map<Long, Vertex> vertices = new HashMap<Long, Vertex>();

        BufferedReader bufRead = new BufferedReader(input);
        String myLine;
        Vertex current;
        long osmID;
        try {
            while ((myLine = bufRead.readLine()) != null)
            {
                String[] array = myLine.split("\\s");
                osmID = Long.parseLong(array[0]);
                current = new Vertex(Double.parseDouble(array[1]), Double.parseDouble(array[2]));
                vertices.put(osmID, current);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        /*File geometryFile = new File("/Users/Matthias/Workspace/PSE/data/_geometry.txt");
        try {
            input = new FileReader(geometryFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Map<Long, String> geometry = new HashMap<>();*/

        File edgesFile = new File("/Users/Matthias/Workspace/PSE/data/_edges.txt");
        try {
            input = new FileReader(edgesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        GraphDataIO graphDataIO = new GraphDataIO();

        bufRead = new BufferedReader(input);
        try {
            while ((myLine = bufRead.readLine()) != null)
            {
                String[] array = myLine.split("\\s");
                graphDataIO.addEdge(new Edge(vertices.get(Long.parseLong(array[1])), vertices.get(Long.parseLong(array[2]))));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            GraphDataIO.save(graphDataIO, new File("/Users/Matthias/Workspace/PSE/data/graphDataIO"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    @Ignore
    public void testPreprocessLocationDataIO() {

        File verticesFile = new File("/Users/Matthias/Workspace/PSE/data/restaurant.csv");
        FileReader input;
        try {
            input = new FileReader(verticesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        LocationDataIO locationDataIO = new LocationDataIO();

        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
        BufferedReader bufRead = new BufferedReader(input);
        String myLine;
        int idCounter = 1;
        try {
            while ((myLine = bufRead.readLine()) != null)
            {
                String[] array = myLine.split(";");
                locationDataIO.addPOI(new POI(format.parse(array[1]).doubleValue(),
                        format.parse(array[0]).doubleValue(), idCounter, array[2].replace("\"", ""), array[2].replace("\"", ""), "http://www.walkaround.com", new int[]{0, 1}));
                idCounter += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            LocationDataIO.save(locationDataIO, new File("/Users/Matthias/Workspace/PSE/data/locationDataIO"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testPreprocessGeometryDataIO() {

        GraphDataIO graphDataIO = null;
        try {
            graphDataIO = GraphDataIO.load(new File("/Users/Matthias/Workspace/PSE/data/graphDataIO"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LocationDataIO locationDataIO = null;
        try {
            locationDataIO = LocationDataIO.load(new File("/Users/Matthias/Workspace/PSE/data/locationDataIO"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GeometryDataIO geometryDataIO = GeometryDataPreprocessor.preprocessGeometryDataIO(graphDataIO, locationDataIO);

        try {
            GeometryDataIO.save(geometryDataIO, new File("/Users/Matthias/Workspace/PSE/data/geometryDataIO"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
