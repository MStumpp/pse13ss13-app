package edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation;

public class CompassManager {

	private static boolean intanceExists;
	private static CompassManager compassManager;
	
	private CompassManager() {
		//TODO: Starte Direction Zeug
	}
	
	public static CompassManager getInstance() {
		if (!intanceExists) {
			compassManager = new CompassManager();
		}
		return compassManager;
	}

}
