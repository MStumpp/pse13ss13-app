package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.*;

import java.io.*;
import java.util.*;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.RouteInfoTransfer;
import org.junit.Ignore;
import org.junit.Test;

/**
 * ShortestPathProcessorTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class ShortestPathProcessorTest {

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

        Map<Long, Vertex> vertices = new HashMap<>();

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
    public void testLoadGraphDataIO() {

        GraphDataIO graphDataIO = null;
        try {
            graphDataIO = GraphDataIO.load(new File("/Users/Matthias/Workspace/PSE/data/graphDataIO"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Graph graph;
        try {
            graph = Graph.getInstance(graphDataIO.getEdges());
        } catch (EmptyListOfEdgesException e) {
            e.printStackTrace();
            return;
        }

        ShortestPathProcessor shortestPathProcessor = ShortestPathProcessor.getInstance(graph);
        RouteInfoTransfer path;
        try {
            path = shortestPathProcessor.computeShortestPath(new Coordinate(1.d, 1.d), new Coordinate(1.d, 1.d));
            for (Coordinate coor : path.getCoordinates())
                System.out.println(((Vertex)coor).getID());
        } catch (NoShortestPathExistsException e) {
            e.printStackTrace();
        }
    }

}
