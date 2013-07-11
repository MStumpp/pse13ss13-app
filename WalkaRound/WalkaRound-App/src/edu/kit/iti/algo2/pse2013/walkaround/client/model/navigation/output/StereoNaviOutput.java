package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;

public class StereoNaviOutput implements NaviOutput {
	
	private StereoNaviOutput stereoNaviOutput;
	private HeadUpController headUpControllerInstance;
	
	private StereoNaviOutput() {
		this.headUpControllerInstance = HeadUpController.getInstance();
	}
	
	@Override
	public NaviOutput getInstance() {
		this.stereoNaviOutput = new StereoNaviOutput();
		return this.stereoNaviOutput;
	}

	@Override
	public void deliverOutput(CrossingInformation crossingInfo, int distToTurn) {
		// TODO Auto-generated method stub
		
	}

}
