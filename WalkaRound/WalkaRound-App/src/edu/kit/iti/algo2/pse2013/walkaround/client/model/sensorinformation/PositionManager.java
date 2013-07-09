package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

public class PositionManager {

	private static boolean intanceExists;
	private static PositionManager positionManager;
	
	private PositionManager() {
		//TODO: Starte GPS
	}
	
	public static PositionManager getInstance() {
		if (!intanceExists) {
			positionManager = new PositionManager();
		}
		return positionManager;
	}
	
	
}
