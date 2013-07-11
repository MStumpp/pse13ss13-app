package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;

public class AudibleTextNaviOutput implements NaviOutput {
	
	private AudibleTextNaviOutput audibleTextNaviOutput;
	private HeadUpController headUpControllerInstance;
	
	private AudibleTextNaviOutput() {
		this.headUpControllerInstance = HeadUpController.getInstance();
	}
	
	@Override
	public NaviOutput getInstance() {
		this.audibleTextNaviOutput = new AudibleTextNaviOutput();
		return this.audibleTextNaviOutput;
	}

	@Override
	public void deliverOutput(CrossingInformation crossingInfo, int distToTurn) {
		// TODO Auto-generated method stub
		
	}

}
