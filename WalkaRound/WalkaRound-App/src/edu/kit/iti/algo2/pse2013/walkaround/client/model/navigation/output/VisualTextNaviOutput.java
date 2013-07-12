package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;

public class VisualTextNaviOutput implements NaviOutput {
	
	private static VisualTextNaviOutput visualTextNaviOutput;
	private HeadUpController headUpControllerInstance;
	
	private VisualTextNaviOutput() {
		this.headUpControllerInstance = HeadUpController.getInstance();
	}
	
	public static NaviOutput getInstance() {
		visualTextNaviOutput = new VisualTextNaviOutput();
		return visualTextNaviOutput;
	}

	@Override
	public void deliverOutput(double turnAngle, double distToTurn) {
		
		
		// TODO Text setzen
		this.headUpControllerInstance.setNavigationsText("");
	}



}
