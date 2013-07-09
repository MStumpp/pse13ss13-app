package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

public class SpeedManager {

	private static boolean intanceExists;
	private static SpeedManager speedManager;
	
	private SpeedManager() {
		//TODO: Hole Instanz von GPS Zeug, und irgendwie Zeit
	}
	
	public static SpeedManager getInstance() {
		if (!intanceExists) {
			speedManager = new SpeedManager();
		}
		return speedManager;
	}
}
