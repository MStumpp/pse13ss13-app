package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;


public class VisualTextNaviOutput implements NaviOutputInterface {

	private static VisualTextNaviOutput visualTextNaviOutput;

	private VisualTextNaviOutput() {
	}

	public static NaviOutputInterface getInstance() {
		if (visualTextNaviOutput == null) {
			visualTextNaviOutput = new VisualTextNaviOutput();
		}
		return visualTextNaviOutput;
	}

	public void deliverOutput(double turnAngle, double distToTurn) {
		//this.headUpControllerInstance.setNavigationsText("in " + distToTurn + "m ");
	}



}