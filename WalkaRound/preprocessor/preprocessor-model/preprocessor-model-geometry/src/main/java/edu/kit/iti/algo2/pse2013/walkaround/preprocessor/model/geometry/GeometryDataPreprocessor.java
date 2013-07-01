package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;

import java.util.*;

/**
 * This class takes some graph and location data and provides an api to query some
 * related info.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryDataPreprocessor {

    /**
     * Preprocesses some data structure to be used by GeometryProcessor.
     *
     * @param graphDataIO GraphDataIO object.
     * @param locationDataIO LocationDataIO object.
     * @return GeometryDataIO.
     * @throw IllegalArgumentException If graphDataIO or locationDataIO args invalid.
     */
    public static GeometryDataIO preprocessGeometryDataIO(GraphDataIO graphDataIO, LocationDataIO locationDataIO) {
        if (graphDataIO == null || locationDataIO == null)
            throw new IllegalArgumentException("graphDataIO and locationDataIO must be provided");

        // get all vertices
        List<Vertex> vertices = new ArrayList<>();
        for (Edge edge : graphDataIO.getEdges())
            for (Vertex vertex : edge.getVertices())
                vertices.add(vertex);

        Set<Vertex> sortedLatitude = new TreeSet<>(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                if (v1.getLatitude() >  v2.getLatitude()){
                    return 1;
                } else if (v1.getLatitude() < v2.getLatitude()){
                    return -1;
                } else
                    return 0;
            }
        });
        sortedLatitude.addAll(vertices);

        Set<Vertex> sortedLongitude = new TreeSet<>(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                if (v1.getLongtitude() >  v2.getLongtitude()){
                    return 1;
                } else if (v1.getLongtitude() < v2.getLongtitude()){
                    return -1;
                } else
                    return 0;
            }
        });
        sortedLongitude.addAll(vertices);

        GeometryNode node = twoDtree(new ArrayList<>(sortedLatitude), new ArrayList<>(sortedLongitude));
        return new GeometryDataIO(node);
    }

    private static GeometryNode twoDtree(List<Vertex> sortedLatitude, List<Vertex> sortedLongitude) {
        if (sortedLatitude.size() != sortedLongitude.size())
            throw new IllegalArgumentException("both lists must be of same size");
        if (sortedLatitude.size() == 0)
            throw new IllegalArgumentException("list of vertices must be greater than 0");
        System.out.println(sortedLatitude.toString());
        System.out.println(sortedLongitude.toString());
        return twoDtree(sortedLatitude, sortedLongitude, 0, 0, sortedLatitude.size()-1);
    }

    private static GeometryNode twoDtree(List<Vertex> sortedLatitude, List<Vertex> sortedLongitude, int depth, int start, int end) {
        if (end-start < 0)
            return null;

        // select dimension
        // 0 latitude
        // 1 longitude
        int dim = depth % 2;
        List<Vertex> workingList;
        if (dim == 0)
            workingList = sortedLatitude;
        else
            workingList = sortedLongitude;

        System.out.println(dim);

        int median = indexOfMedian(workingList, dim, start, end);
        System.out.println("median -> start: " + start + " end: " + end + " median: " + median);
        if (median == -1)
            throw new RuntimeException("something went wrong internally");
        GeometryNode leftNode = twoDtree(sortedLatitude, sortedLongitude, depth+1, start, median-1);
        GeometryNode rightNode = twoDtree(sortedLatitude, sortedLongitude, depth+1, median+1, end);
        GeometryNode node = new GeometryNode(workingList.get(median), leftNode, rightNode);

        return node;
    }

    private static int indexOfMedian(List<Vertex> vertices, int dim, int start, int end) {
        if (end-start < 0)
            return -1;
        if (start == end)
            return start;

        int size = end-start+1;
        int median = start+size/2;
        int tmpMedian = median-1;
        switch (dim)
        {
            case 0:
            {
                while (tmpMedian >= start-1 && vertices.get(tmpMedian).getLatitude() == vertices.get(median).getLatitude())
                    tmpMedian -= 1;
                break;
            }

            case 1:
            {
                while (tmpMedian >= start-1 && vertices.get(tmpMedian).getLongtitude() == vertices.get(median).getLongtitude())
                    tmpMedian -= 1;
                break;
            }
        }
        return tmpMedian+1;
    }

    // Heapsort

    private static void sort(int[] a) {
        sort(a, a.length - 1);
    }

    private static void sort(int[] a, int end) {
        for (int i = end / 2; i >= 1; i--)
            fixHeap(a, i, end, a[i]);
        for (int i = end; i > 1; i--) {
            swap(a, 1, i);
            fixHeap(a, 1, i - 1, a[1]);
        }
    }

    private static void fixHeap(int[] a, int root, int end, int key) {
        int child = 2*root;
        if (child < end && a[child] < a[child + 1])
            child++;
        if (child <= end && key < a[child]) {
            a[root] = a[child];
            fixHeap(a, child, end, key);
        } else {
            a[root] = key;
        }
    }

    private static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

}
