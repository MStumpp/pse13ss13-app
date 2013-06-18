package walkaround.datastructures;

import java.util.ArrayList;

public class CrossingInformation {

	private ArrayList<Float> crossroadAngles;

	public CrossingInformation() {
		crossroadAngles = new ArrayList<Float>();
	}

	public float getCrossroadAngle(int index) {
		return crossroadAngles.get(index);
	}

	public int getNumCrossroads() {
		return crossroadAngles.size();
	}
}
