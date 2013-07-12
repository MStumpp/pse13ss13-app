package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.headup.HeadUpView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;

public class ArrowNaviOutput implements NaviOutput {

	private static ArrowNaviOutput arrowNaviOutput;
	private HeadUpController headUpControllerInstance;
	
	private ArrowNaviOutput() {
		this.headUpControllerInstance = HeadUpController.getInstance();
	}
	
	public static NaviOutput getInstance() {
		arrowNaviOutput = new ArrowNaviOutput();
		return arrowNaviOutput;
	}

	@Override
	public void deliverOutput(double turnAngle, double distToTurn) {
		// TODO Auto-generated method stub
		
		// HeadUpView.ARROW_RIGHT
	}


}
