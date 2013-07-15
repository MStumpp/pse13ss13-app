package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;


public class AudibleTextNaviOutput implements NaviOutput {
	
	private static AudibleTextNaviOutput audibleTextNaviOutput;
	
	private AudibleTextNaviOutput() {
	}
	
	public static NaviOutput getInstance() {
		audibleTextNaviOutput = new AudibleTextNaviOutput();
		return audibleTextNaviOutput;
	}

	@Override
	public void deliverOutput(double turnAngle, double distToTurn) {
		// TODO
		
	}

}
