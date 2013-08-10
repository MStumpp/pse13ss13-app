package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.util.List;


/**
 * This class represents CrossingInformation belonging to some Coordinate.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public final class CrossingInformation {

    /**
     * list of crossroad angles.
     */
    private float[] angles;
    private int nextCrossingAngleOnRoute;

    /**
     * Creates an instance of CrossingInformation.
     *
     * @param crossroadAngles An array of crossroad angles.
     */
    public CrossingInformation(float[] angles) {
        if (angles.length == 0)
            throw new IllegalArgumentException("number of crossing angles must be at least one");
        this.angles = angles;
	}

    public CrossingInformation(List<Float> angles) {
    	this.angles = new float[angles.size()];
    	for (int i = 0; i < angles.size(); i++) {
    		assert angles.get(i) != null;
    		if (angles.get(i) != null) {
        		this.angles[i] = angles.get(i);
    		}
    	}
    }


    /**
     * Returns all crossing angles.
     *
     * @return array of float.
     */
    public float[] getCrossingAngles() {
        return angles;
    }


    /**
     * Returns a certain crossroad angles at the a specific index.
     *
     * @return float.
     */
    public float getCrossroadAngle(int index) {
        if (index < 0 || index > angles.length-1)
            throw new IllegalArgumentException("index must be equal or greater " +
                    "than zero but not greater than " + (angles.length-1));
        return angles[index];
	}


    /**
     * Returns the number of all crossroad angles.
     *
     * @return int.
     */
    public int getNumCrossroads() {
        return angles.length;
    }

    public CrossingInformation clone() {
    	return new CrossingInformation(this.angles);
    }

    public boolean setNextCrossingAngleOnRoute(int index) {
    	if (index >= this.angles.length) {
    		return false;
    	}
    	this.nextCrossingAngleOnRoute = index;
    	return true;
    }

}
