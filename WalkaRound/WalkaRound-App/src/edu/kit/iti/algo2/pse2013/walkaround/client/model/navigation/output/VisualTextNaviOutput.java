package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;

public class VisualTextNaviOutput implements NaviOutput {
	
	private VisualTextNaviOutput visualTextNaviOutput;
	private HeadUpController headUpControllerInstance;
	
	private VisualTextNaviOutput() {
		this.headUpControllerInstance = HeadUpController.getInstance();
	}
	
	@Override
	public NaviOutput getInstance() {
		this.visualTextNaviOutput = new VisualTextNaviOutput();
		return this.visualTextNaviOutput;
	}

	@Override
	public void deliverOutput(CrossingInformation crossingInfo, int distToTurn) {
		
		
		this.headUpControllerInstance.setNavigationsText("");
	}



}
