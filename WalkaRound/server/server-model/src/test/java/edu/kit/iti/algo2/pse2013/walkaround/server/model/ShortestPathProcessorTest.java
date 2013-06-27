package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;

import java.util.Comparator;
import java.util.PriorityQueue;

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
    public void testPriorityQueueOrderPreservation() {
        Vertex v1 = new Vertex(1.d, 2.d);
        v1.setCurrentLength(10.d);
        Vertex v2 = new Vertex(1.d, 2.d);
        v2.setCurrentLength(11.d);

        // set up the queue with the source vertex
        PriorityQueue<Vertex> queue = new PriorityQueue<>(10, new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                if (v1.getCurrentLength() >  v2.getCurrentLength()){
                    return 1;
                } else if (v1.getCurrentLength() < v2.getCurrentLength()){
                    return -1;
                } else
                    return 0;
            }
        });
        queue.add(v1);
        queue.add(v2);
        System.out.println(v1.toString());
        System.out.println(v2.toString());

        System.out.println(queue.peek().toString());
        v1.setCurrentLength(12.d);
        System.out.println(queue.peek().toString());
    }

}
