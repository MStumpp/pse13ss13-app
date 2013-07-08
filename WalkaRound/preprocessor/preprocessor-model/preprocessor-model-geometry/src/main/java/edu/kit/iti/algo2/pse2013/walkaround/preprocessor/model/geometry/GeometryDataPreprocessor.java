package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.server.LocationDataIO;

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
        List<Vertex> vertices = new ArrayList<Vertex>();
        for (Edge edge : graphDataIO.getEdges())
            for (Vertex vertex : edge.getVertices())
                vertices.add(vertex);

        Set<Vertex> sortedLatitude = new TreeSet<Vertex>(new Comparator<Vertex>() {
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

        Set<Vertex> sortedLongitude = new TreeSet<Vertex>(new Comparator<Vertex>() {
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

        Vertex[][] array = new Vertex[][]{(Vertex[]) sortedLatitude.toArray(), (Vertex[]) sortedLongitude.toArray()};
        GeometryNode node = buildtree(array);
        return new GeometryDataIO(node);
    }

    private static GeometryNode buildtree(Vertex[][] data) {
        if (data[0].length != data[1].length)
            throw new IllegalArgumentException("both lists must be of same size");
        if (data[0].length == 0)
            throw new IllegalArgumentException("list of vertices must be greater than 0");
        return buildtree(data, 0, 0, data[0].length-1);
    }

    private static GeometryNode buildtree(Vertex[][] data, int depth, int start, int end) {
        if (end-start < 0)
            return null;

        // select dimension
        // 0 latitude
        // 1 longitude
        int dim = depth % data.length;

        int size = end-start+1;
        // only one point in range, then take as leaf
        // eventually put more than one point in leaf
        if (size == 1)
            return new GeometryNode(data[dim][0]);

        // otherwise, compute median;
        int median;
        // if range of size 2, than take first point as median
        if (size == 2) {
            median = start;

        // otherwise compute median, if size of range is at least 3
        } else {
            if (size%2 == 0)
                median = start+size/2-1;
            else
                median = start+size/2;
            // to make sure all less or equal
            // points to median go to left tree
            int tmpMedian = median+1;
            if (dim == 0)
                while (tmpMedian <= end+1 && data[dim][tmpMedian].getLatitude() == data[dim][median].getLatitude())
                    tmpMedian += 1;
            else
                while (tmpMedian <= end+1 && data[dim][tmpMedian].getLongtitude() == data[dim][median].getLongtitude())
                    tmpMedian += 1;
            median = tmpMedian-1;
        }

//      System.out.println("median -> start: " + start + " end: " + end + " median: " + median);
        if (median == -1)
            throw new RuntimeException("something went wrong internally");

        // sort longitude
        int leftIndex = 0;
        int rightIndex = median+1;
        Vertex[] toReplaceArray;
        if (dim == 0) toReplaceArray = data[1];
        else toReplaceArray = data[0];
        Vertex[] tmpArray = new Vertex[toReplaceArray.length];
        List<Vertex> vertexForLeft = Arrays.asList(Arrays.copyOfRange(data[dim], start, median));
        for (Vertex vertex : toReplaceArray) {
            if (vertexForLeft.contains(vertex)) {
                tmpArray[leftIndex] = vertex;
                leftIndex += 1;
            } else {
                tmpArray[rightIndex] = vertex;
                rightIndex += 1;
            }
        }
        if (dim == 0) data[1] = tmpArray;
        else data[0] = tmpArray;

        GeometryNode node = new GeometryNode(median);
        node.setLeftNode(buildtree(data, depth+1, start, median));
        node.setRightNode(buildtree(data, depth+1, median+1, end));
        return node;
    }

//    private static int indexOfMedian(Vertex[] vertices, int start, int length) {
//        if (end-start < 0)
//            return -1;
//        if (start == end)
//            return start;
//
//        int size = end-start+1;
//        int median = start+size/2;
//        int tmpMedian = median-1;
//        switch (dim)
//        {
//            case 0:
//            {
//                while (tmpMedian >= start-1 && vertices.get(tmpMedian).getLatitude() == vertices.get(median).getLatitude())
//                    tmpMedian -= 1;
//                break;
//            }
//
//            case 1:
//            {
//                while (tmpMedian >= start-1 && vertices.get(tmpMedian).getLongtitude() == vertices.get(median).getLongtitude())
//                    tmpMedian -= 1;
//                break;
//            }
//        }
//        return tmpMedian+1;
//    }

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
