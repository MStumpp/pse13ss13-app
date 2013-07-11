package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;

public class AudibleTextNaviOutput implements NaviOutput {
	
	private static AudibleTextNaviOutput audibleTextNaviOutput;
	private HeadUpController headUpControllerInstance;
	
	private AudibleTextNaviOutput() {
		this.headUpControllerInstance = HeadUpController.getInstance();
	}
	
	public static NaviOutput getInstance() {
		audibleTextNaviOutput = new AudibleTextNaviOutput();
		return audibleTextNaviOutput;
	}

	@Override
	public void deliverOutput(double turnAngle, int distToTurn) {
		// TODO Auto-generated method stub
		
	}

}
