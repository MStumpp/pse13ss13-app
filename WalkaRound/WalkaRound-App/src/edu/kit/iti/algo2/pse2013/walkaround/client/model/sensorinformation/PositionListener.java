package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

public interface PositionListener {
	public void onPositionChange(Coordinate coord);
}
