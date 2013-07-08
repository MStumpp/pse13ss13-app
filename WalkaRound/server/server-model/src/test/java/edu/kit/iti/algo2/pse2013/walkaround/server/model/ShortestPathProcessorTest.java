package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.*;

import java.io.*;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.RouteInfoTransfer;
import org.junit.Test;

/**
 * ShortestPathProcessorTest.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class ShortestPathProcessorTest {

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
