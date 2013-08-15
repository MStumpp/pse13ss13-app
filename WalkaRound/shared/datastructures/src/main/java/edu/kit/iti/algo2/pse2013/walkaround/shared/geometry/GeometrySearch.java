package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import java.util.HashMap;
import java.util.Map;

/**
 * GeometryProcessorException.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometrySearch {

    private Map<Integer, Double> search;

    public GeometrySearch(double[] values) {
        search = new HashMap<Integer, Double>();
        for (int dim = 0; dim < values.length; dim++) {
            search.put(dim, values[dim]);
        }
    }

    public int getNumberDimensions() {
        return search.size();
    }

    public double valueForDimension(int dim) {
        return search.get(dim);
    }

}
