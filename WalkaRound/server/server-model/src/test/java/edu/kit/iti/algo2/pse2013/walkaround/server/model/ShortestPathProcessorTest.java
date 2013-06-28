package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.EmptyListOfEdgesException;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Graph;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;

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
    public void testPriorityQueueOrderPreservation() {

        File verticesFile = new File("/Users/Matthias/Dropbox/PSE/pse13ss13-app/WalkaRound/server/server-model/src/test/resources/_nodes.txt");
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

        System.out.println(vertices.size());

        File edgesFile = new File("/Users/Matthias/Dropbox/PSE/pse13ss13-app/WalkaRound/server/server-model/src/test/resources/_edges.txt");
        try {
            input = new FileReader(edgesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        List<Edge> edges = new ArrayList<>();

        bufRead = new BufferedReader(input);
        try {
            while ((myLine = bufRead.readLine()) != null)
            {
                String[] array = myLine.split("\\s");
                edges.add(new Edge(vertices.get(Long.parseLong(array[1])), vertices.get(Long.parseLong(array[2]))));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println(edges.size());

        Graph graph;
        try {
            graph = Graph.getInstance(edges);
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
