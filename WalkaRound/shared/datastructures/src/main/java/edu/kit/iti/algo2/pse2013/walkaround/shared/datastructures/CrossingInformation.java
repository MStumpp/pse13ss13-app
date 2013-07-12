package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;


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
    private float[] crossroadAngles;
    private int nextCrossingAngleOnRoute;
    
    /**
     * Creates an instance of CrossingInformation.
     *
     * @param crossroadAngles An array of crossroad angles.
     */
    public CrossingInformation(float[] crossroadAngles) {
        if (crossroadAngles.length == 0)
            throw new IllegalArgumentException("number of crossing angles must be at least one");
        this.crossroadAngles = crossroadAngles;
	}


    /**
     * Returns all crossing angles.
     *
     * @return array of float.
     */
    public float[] getCrossingAngles() {
        return crossroadAngles;
    }


    /**
     * Returns a certain crossroad angles at the a specific index.
     *
     * @return float.
     */
    public float getCrossroadAngle(int index) {
        if (index < 0 || index > crossroadAngles.length-1)
            throw new IllegalArgumentException("index must be equal or greater " +
                    "than zero but not greater than " + (crossroadAngles.length-1));
        return crossroadAngles[index];
	}


    /**
     * Returns the number of all crossroad angles.
     *
     * @return int.
     */
    public int getNumCrossroads() {
        return crossroadAngles.length;
    }

    public CrossingInformation clone() {
    	return new CrossingInformation(this.crossroadAngles);
    }
    
    public boolean setNextCrossingAngleOnRoute(int index) {
    	if (index >= this.crossroadAngles.length) {
    		return false;
    	}
    	this.nextCrossingAngleOnRoute = index;
    	return true;
    }
    
}
