package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;


public class ArrowNaviOutput implements NaviOutputInterface {

	private static ArrowNaviOutput arrowNaviOutput;

	private ArrowNaviOutput() {
	}

	public static NaviOutputInterface getInstance() {
		if (arrowNaviOutput == null) {
			arrowNaviOutput = new ArrowNaviOutput();
		}
		return arrowNaviOutput;
	}

	public void deliverOutput(double turnAngle, double distToTurn) {
		// TODO
		// HeadUpView.ARROW_RIGHT

	}
}