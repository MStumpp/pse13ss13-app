package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.GraphDataIO;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Geometrizable;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryNode;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;

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

        List<Geometrizable> geometrizables = new ArrayList<Geometrizable>(graphDataIO.getVertices());

        // get all POIs from locationDataIO
//        for (POI poi : locationDataIO.getPOIs())
//            geometrizables.add(poi);

        // throw exception if number of geometrizables is not greater than 0
        if (geometrizables.size() == 0)
            throw new IllegalArgumentException("number of geometrizables must be at least of size 1");

        // number of dimensions, use first element of geometrizables
        int numDimensions = 2;

        // set up data
        Geometrizable[][] data = new Geometrizable[numDimensions][];

        // sort data
        for (int i = 0; i<numDimensions; i++) {
            final int dim = i;
            Collections.sort(geometrizables, new Comparator<Geometrizable>() {
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
            data[i] = geometrizables.toArray(new Geometrizable[0]);
        }

//        System.out.println(Arrays.toString(data[0]));
//        System.out.println(Arrays.toString(data[1]));

        if (data[0].length != data[1].length)
            throw new IllegalArgumentException("both lists must be of same size");
        if (data[0].length == 0)
            throw new IllegalArgumentException("list of vertices must be greater than 0");

        // build tree
        GeometryNode node = buildTree(data, null, 0, 0, data[0].length-1);
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
    private static GeometryNode buildTree(Geometrizable[][] data, GeometryNode parent, int depth, int start, int end) {

        final int dim = depth % data.length;

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
            while (tmpMedian <= end) {
                if (data[dim][tmpMedian].valueForDimension(dim) == data[dim][median].valueForDimension(dim)) {
                    if (tmpMedian == end) {
                        median = tmpMedian;
                        break;
                    } else {
                        tmpMedian += 1;
                    }
                } else {
                    median = tmpMedian-1;
                    break;
                }
            }
        }

        Multiset<Geometrizable> geometrizablesForLeft = TreeMultiset.create(new Comparator<Geometrizable>() {
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
        geometrizablesForLeft.clear();
        Collections.addAll(geometrizablesForLeft, Arrays.copyOfRange(data[dim], start, median+1));

        //System.out.println("size: " + geometrizablesForLeft.size());

        Geometrizable[] currentBackupArray;
        int leftIndex;
        int rightIndex;
        for (int i=0; i<data.length; i++) {
            if (i == dim)
                continue;
            currentBackupArray = new Geometrizable[size];
            System.arraycopy(data[i], start, currentBackupArray, 0, size);
            leftIndex = start;
            rightIndex = median+1;
            for (Geometrizable geometrizable : currentBackupArray) {
//                System.out.println("------");
//                System.out.println("geometrizable: " + geometrizable + " dim: " + dim + " start: " + start + " median: " + median);
//                System.out.println("old: bool: " + geometrizableForLeft.contains(geometrizable));
//                System.out.println(geometrizableForLeft);
//                System.out.println("new: bool: " + contains(data[dim], dim, geometrizable, start, median));
//                System.out.println(Arrays.toString(data[dim]));
//                if (contains(data[dim], dim, geometrizable, start, median)) {
                if (geometrizablesForLeft.contains(geometrizable)) {
                    geometrizablesForLeft.remove(geometrizable);
                    data[i][leftIndex] = geometrizable;
                    leftIndex += 1;
                } else {
                    data[i][rightIndex] = geometrizable;
                    rightIndex += 1;
                }
            }
        }

        GeometryNode node = new GeometryNode(parent, depth, (data[dim][median].valueForDimension(dim)));
        node.setLeftNode(buildTree(data, node, depth+1, start, median));
        node.setRightNode(buildTree(data, node, depth+1, median+1, end));
        return node;
    }

    private static boolean contains(Geometrizable[] array, int dim, Geometrizable geometrizable, int start, int end) {
//        System.out.println("------>");
//        System.out.println(Arrays.toString(array));
//        System.out.println(dim);
//        System.out.println(geometrizable.toString());
//        System.out.println(start);
//        System.out.println(end);
//        System.out.println("------>");
        final int i = dim;
        int idx = Arrays.binarySearch(array, start, end+1, geometrizable, new Comparator<Geometrizable>() {
            @Override
            public int compare(Geometrizable v1, Geometrizable v2) {
                System.out.println("v1: " + v1.valueForDimension(i) + " v2: " + v2.valueForDimension(i));
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
