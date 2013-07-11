package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;

public class ArrowNaviOutput implements NaviOutput {

	private ArrowNaviOutput arrowNaviOutput;
	private HeadUpController headUpControllerInstance;
	
	private ArrowNaviOutput() {
		this.headUpControllerInstance = HeadUpController.getInstance();
	}
	
	@Override
	public NaviOutput getInstance() {
		this.arrowNaviOutput = new ArrowNaviOutput();
		return arrowNaviOutput;
	}

	@Override
	public void deliverOutput(CrossingInformation crossingInfo, int distToTurn) {
		// TODO Auto-generated method stub
		
	}


}
