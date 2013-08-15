package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.geometry;

import java.util.*;

import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometrizableWrapper;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.GeometryNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class takes some graph and location data and provides an api to query some
 * related info.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometryDataPreprocessor {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(GeometryDataPreprocessor.class.getName());


    /**
     * TreeSet.
     */
    private static final int NUMBER_GEOMETRIZABLES_PER_NODE = 1000;


    /**
     * TreeSet.
     */
    private static final TreeSet<Geometrizable> treeSet = new TreeSet<>();

    private static final ArrayList<Geometrizable> list = new ArrayList<>();


    /**
     * Preprocesses some data structure to be used by GeometryProcessor.
     *
     * @param geometrizables GraphDataIO object.
     * @return GeometryDataIO.
     * @throw IllegalArgumentException If graphDataIO or locationDataIO args invalid.
     */
    public static GeometryDataIO preprocessGeometryDataIO(List<Geometrizable> geometrizables) {
        return preprocessGeometryDataIO(geometrizables, NUMBER_GEOMETRIZABLES_PER_NODE);
    }


    /**
     * Preprocesses some data structure to be used by GeometryProcessor.
     *
     * @param geoms GraphDataIO object.
     * @param numberGeomPerNode Number of the Geometrizables to be stored in a GeometryNode.
     * @return GeometryDataIO.
     * @throw IllegalArgumentException If graphDataIO or locationDataIO args invalid.
     */
    public static GeometryDataIO preprocessGeometryDataIO(List<Geometrizable> geoms,
        int numberGeomPerNode) throws IllegalArgumentException {

        List<Geometrizable> geometrizables = geoms;

        // throw exception if number of geometrizables is not greater than 0
        if (geometrizables == null || geometrizables.size() == 0)
            throw new IllegalArgumentException("geometrizables must not be null and/or " +
                    "number of Geometrizables must be at least of size 1");

        // throw exception if number of geometrizables per node is not greater than 0
        if (numberGeomPerNode < 1)
            throw new IllegalArgumentException("number of Geomtrizables per " +
                    "GeometryNode must be at least of size 1");

        // number of dimensions, use first element of geometrizables
        int numDimensions = geometrizables.get(0).numberDimensions();

        logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! number geometrizables before: " + geometrizables.size());

        // if number of nodes in given geometrizables greater than 1,
        // wrap each geometrizable
        int numberNodes = geometrizables.get(0).numberNodes();
        if (numberNodes > 1) {
            List<Geometrizable> geometrizablesWrapped = new ArrayList<>();
            for (Geometrizable geometrizable : geometrizables) {
                for (int n = 0; n < numberNodes; n++) {
                    geometrizablesWrapped.add(new GeometrizableWrapper(geometrizable, n));
                }
            }
            geometrizables = geometrizablesWrapped;
        }

        logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! number geometrizables after: " + geometrizables.size());

        logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + geometrizables.get(0));

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

        if (data[0].length != data[1].length)
            throw new IllegalArgumentException("both lists must be of same size");
        if (data[0].length == 0)
            throw new IllegalArgumentException("list of vertices must be greater than 0");

        // build tree
        GeometryNode node = buildTree(data, null, 0, 0, data[0].length-1, numberGeomPerNode);
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
    private static GeometryNode buildTree(Geometrizable[][] data, GeometryNode parent,
                                          int depth, int start, int end, int numberGeomPerNode) {

        final int dim = depth % data.length;

        int size = end-start+1;

        // only one point in range, then take as leaf
        // eventually put more than one point in leaf
        if (size <= numberGeomPerNode)
            return new GeometryNode(parent, depth, Arrays.asList(Arrays.copyOfRange(data[dim], start, end+1)));

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
//            int tmpMedian = median+1;
//            while (tmpMedian <= end) {
//                if (data[dim][tmpMedian].valueForDimension(dim) == data[dim][median].valueForDimension(dim)) {
//                    if (tmpMedian == end) {
//                        median = tmpMedian;
//                        break;
//                    } else {
//                        tmpMedian += 1;
//                    }
//                } else {
//                    median = tmpMedian-1;
//                    break;
//                }
//            }
        }

        //treeSet.clear();
        //treeSet.addAll(Arrays.asList(Arrays.copyOfRange(data[dim], start, median+1)));

        list.clear();
        list.addAll(Arrays.asList(Arrays.copyOfRange(data[dim], start, median+1)));

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
                //if (treeSet.contains(geometrizable)) {
                //    treeSet.remove(geometrizable);
                if (list.contains(geometrizable)) {
                    list.remove(geometrizable);
                    data[i][leftIndex] = geometrizable;
                    leftIndex += 1;
                } else {
                    data[i][rightIndex] = geometrizable;
                    rightIndex += 1;
                }
            }
        }

        GeometryNode node = new GeometryNode(parent, depth, (data[dim][median].valueForDimension(dim)));
        node.setLeftNode(buildTree(data, node, depth+1, start, median, numberGeomPerNode));
        node.setRightNode(buildTree(data, node, depth+1, median+1, end, numberGeomPerNode));
        return node;
    }


    /**
     * Checks whether a certain Geometrizable is within an array of Geometrizables.
     *
     * @param array Array containing Geometrizables.
     * @param geometrizable Geometrizable to checks.
     * @param dim Current dimension.
     * @param start Start index for current processing.
     * @param end End index for current processing.
     * @return boolean Whether a certain Geometrizable is within an array of Geometrizables.
     */
    private static boolean contains(Geometrizable[] array, int dim, Geometrizable geometrizable, int start, int end) {
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
        boolean result = false;
        if (idx >= start && idx <= end)
            result = true;
        return result;
    }

}
