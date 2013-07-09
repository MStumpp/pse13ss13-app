package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

import android.location.Location;

public interface PositionListener {
	public void onPositionChange(Location androidLocation);
}
