package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.util.Arrays;
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
     * @param angles An array of crossroad angles.
     */
    public CrossingInformation(float[] angles) {
        if (angles == null || angles.length <= 0)
            throw new IllegalArgumentException("number of crossing angles must be at least one");
        this.angles = angles;
	}

    public CrossingInformation(List<Float> angles) {
    	if (angles == null || angles.size() <= 0)
        	throw new IllegalArgumentException("number of crossing angles must be at least one");
    	this.angles = new float[angles.size()];
    	int counter = 0;
    	for (int i = 0; i < angles.size(); i++) {
    		if (angles.get(i) != null) {
    			this.angles[counter++] = angles.get(i);
    		}
    	}
    	this.angles = Arrays.copyOf(this.angles, counter);
        if (this.angles.length <= 0)
        	throw new IllegalArgumentException("number of crossing angles must be at least one");
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
     * Returns a certain crossroad angle at the a specific index.
     *
     * @return float.
     */
    public float getCrossingAngle(int index) {
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
    	if (index >= this.angles.length || index < 0) {
    		return false;
    	}
    	this.nextCrossingAngleOnRoute = index;
    	return true;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(angles);
		result = prime * result + nextCrossingAngleOnRoute;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CrossingInformation)) {
			return false;
		}
		CrossingInformation other = (CrossingInformation) obj;
		if (!Arrays.equals(angles, other.angles)) {
			return false;
		}
		if (nextCrossingAngleOnRoute != other.nextCrossingAngleOnRoute) {
			return false;
		}
		return true;
	}

}
