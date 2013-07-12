package edu.kit.iti.algo2.pse2013.walkaround.server.view.endpoint;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents CrossingInformation belonging to some Coordinate.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
@XmlRootElement
public class CrossingInformation {

    /**
     * list of crossing angles.
     */
    private float[] crossingAngles;


    /**
     * Default constructor.
     */
    public CrossingInformation() {}


    /**
     * Creates an instance of CrossingInformation.
     *
     * @param crossingAngles An array of crossroad angles.
     */
    public CrossingInformation(float[] crossingAngles) {
        this.crossingAngles = crossingAngles;
	}


    /**
     * Returns all crossing angles.
     *
     * @return array of float.
     */
    public float[] getCrossingAngles() {
        return crossingAngles;
    }

}
