package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents CrossingInformation belonging to some Coordinate.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
@XmlRootElement

public final class CrossingInformation {

    /**
     * list of crossroad angles.
     */
    private final float[] crossroadAngles;


    /**
     * Creates an instance of CrossingInformation.
     *
     * @param crossroadAngles An array of crossroad angles.
     */
    public CrossingInformation(float[] crossroadAngles) {
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

}
