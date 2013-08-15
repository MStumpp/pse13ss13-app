package edu.kit.iti.algo2.pse2013.walkaround.shared.geometry;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * GeometrizablePOIConstraint.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class GeometrizablePOIConstraint implements GeometrizableConstraint {

    private int[] poiCategories;

    public GeometrizablePOIConstraint(int[] poiCategories) {
        this.poiCategories = poiCategories;
    }

    @Override
    public boolean isValid(Geometrizable geometrizable) {
        POI poi = (POI) geometrizable;
        for (int i = 0; i < poi.getPOICategories().length; i++) {
            for (int j = 0; j < poiCategories.length; j++) {
                if(poi.getPOICategories()[i] == poiCategories[j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
