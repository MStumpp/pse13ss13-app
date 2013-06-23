package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

public class CrossingInformation {

	private float[] crossroadAngles;

	public CrossingInformation(float[] crossroadAngles) {
		this.crossroadAngles = crossroadAngles;
	}

	public float getCrossroadAngle(int index) {
		return crossroadAngles[index];
	}

	public int getNumCrossroads() {
		return crossroadAngles.length;
	}
}
