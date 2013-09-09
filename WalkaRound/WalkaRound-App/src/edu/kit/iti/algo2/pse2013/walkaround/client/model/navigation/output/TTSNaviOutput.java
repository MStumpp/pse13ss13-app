package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;


public class TTSNaviOutput implements NaviOutputInterface {

	private static TTSNaviOutput audibleTextNaviOutput;

	private TTSNaviOutput() {
	}

	public static NaviOutputInterface getInstance() {
		if (audibleTextNaviOutput == null) {
			audibleTextNaviOutput = new TTSNaviOutput();
		}
		return audibleTextNaviOutput;
	}

	public void deliverOutput(double turnAngle, double distToTurn) {
		// TODO

	}

}
