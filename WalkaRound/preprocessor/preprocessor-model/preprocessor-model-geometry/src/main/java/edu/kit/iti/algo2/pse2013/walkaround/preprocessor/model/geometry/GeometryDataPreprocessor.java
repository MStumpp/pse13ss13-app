package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

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

        Set<Geometrizable> geometrizables = new HashSet<Geometrizable>();

        int duplicates = 0;
        // get all vertices from graphDataIO

        for (Geometrizable geometrizable : graphDataIO.getVertices())
            if (!geometrizables.contains(geometrizable))
                geometrizables.add(geometrizable);
            else
                duplicates++;

        // get all POIs from locationDataIO
//        for (POI poi : locationDataIO.getPOIs())
//            geometrizables.add(poi);

        System.out.println("number duplicates: " + duplicates);

        // throw exception if number of geometrizables is not greater than 0
        if (geometrizables.size() == 0)
            throw new IllegalArgumentException("number of geometrizables must be greater than 0");

        // number of dimensions, use first element of geometrizables
        int numDimensions = 2;

        // set up data
        Set[] sorted = new TreeSet[numDimensions];
        Geometrizable[][] data = new Geometrizable[numDimensions][];

        // fill and sort data
        for (int i = 0; i<numDimensions; i++) {
            final int dim = i;
            Set<Geometrizable> sortedGeometrizables = new TreeSet<Geometrizable>(new Comparator<Geometrizable>() {
                @Override
                public int compare(Geometrizable v1, Geometrizable v2) {
                    if (v1.valueForDimension(dim) > v2.valueForDimension(dim)) {
                        return 1;
                    } else if (v1.valueForDimension(dim) < v2.valueForDimension(dim)) {
                        return -1;
                    } else
                        return 0;
                }
            });
            sortedGeometrizables.addAll(geometrizables);
            sorted[i] = sortedGeometrizables;
            data[i] = sortedGeometrizables.toArray(new Geometrizable[0]);
        }

        System.out.println(data[0].length);
        System.out.println(data[1].length);

        if (data[0].length != data[1].length)
            throw new IllegalArgumentException("both lists must be of same size");
        if (data[0].length == 0)
            throw new IllegalArgumentException("list of vertices must be greater than 0");

        // build tree
        GeometryNode node = buildTree(data, sorted, null, 0, 0, data[0].length-1);
        return new GeometryDataIO(node, data.length);
    }


    /**
     * Builds a k-dimensional binary search tree.
     *
     * @param data Double array consisting of sorted arrays per dimension.
     * @param parent Parent node for current processing.
     * @param depth Depth for current processing.
     * @param start Start index for current processing.
     * @param end End index for current processing.
     * @return GeometryNode Node.
     */
    private static GeometryNode buildTree(Geometrizable[][] data, Set[] sorted, GeometryNode parent, int depth, int start, int end) {

        int dim = depth % data.length;

        int size = end-start+1;
        // only one point in range, then take as leaf
        // eventually put more than one point in leaf
        if (size == 1)
            return new GeometryNode(parent, depth, data[dim][start]);

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
            while (tmpMedian <= end+1 && data[dim][tmpMedian].valueForDimension(dim) == data[dim][median].valueForDimension(dim))
                tmpMedian += 1;
            median = tmpMedian-1;
        }

        Map<Integer, Geometrizable[]> backupArray = new HashMap<Integer, Geometrizable[]>();
        for (int i=0; i<data.length; i++) {
            if (i == dim)
                continue;
            Geometrizable[] array = new Geometrizable[size];
            System.arraycopy(data[i], start, array, 0, size);
            backupArray.put(i, array);
        }

        // median + 1 becuase to parameter is exclusive, but median must be in left tree by definition
        List<Geometrizable> geometrizableForLeft = Arrays.asList(Arrays.copyOfRange(data[dim], start, median+1));
        //Geometrizable[] geometrizableForLeft = Arrays.copyOfRange(data[dim], start, median+1);
        int leftIndex;
        int rightIndex;
        for (Map.Entry<Integer, Geometrizable[]> entry : backupArray.entrySet()) {
            Integer currentDim = entry.getKey();
            Geometrizable[] geometrizables = entry.getValue();
            leftIndex = start;
            rightIndex = median+1;
            for (Geometrizable geometrizable : geometrizables) {
                if (geometrizableForLeft.contains(geometrizable)) {
                //if (Arrays.binarySearch(geometrizableForLeft, geometrizable)) {
                /*System.out.println("geometrizable: " + geometrizable + " dim: " + dim + " start: " + start + " median: " + median);
                System.out.println("old: bool: " + geometrizableForLeft.contains(geometrizable));
                System.out.println(geometrizableForLeft);
                System.out.println("new: bool: " + contains(data[dim], dim, geometrizable, start, median));
                System.out.println(Arrays.toString(data[dim]));
                if (contains(data[dim], dim, geometrizable, start, median)) { */
                    data[currentDim][leftIndex] = geometrizable;
                    leftIndex += 1;
                } else {
                    data[currentDim][rightIndex] = geometrizable;
                    rightIndex += 1;
                }
            }
        }

        GeometryNode node = new GeometryNode(parent, depth, (data[dim][median].valueForDimension(dim)));
        node.setLeftNode(buildTree(data, sorted, node, depth+1, start, median));
        node.setRightNode(buildTree(data, sorted, node, depth+1, median+1, end));
        return node;
    }

    private static boolean contains(Geometrizable[] array, int dim, Geometrizable geometrizable, int start, int end) {
        final int i = dim;
        int idx = Arrays.binarySearch(array, geometrizable, new Comparator<Geometrizable>() {
            @Override
            public int compare(Geometrizable v1, Geometrizable v2) {
                if (v1.valueForDimension(i) > v2.valueForDimension(i)) {
                    return 1;
                } else if (v1.valueForDimension(i) < v2.valueForDimension(i)) {
                    return -1;
                } else
                    return 0;
            }
        });
        System.out.println("idx " + idx);
        boolean result = false;
        if (idx >= start && idx <= end)
            result = true;
        return result;
    }

}
