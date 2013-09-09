package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;


public class StereoNaviOutput implements NaviOutputInterface {

	private static StereoNaviOutput stereoNaviOutput;

	private StereoNaviOutput() {
	}

	public static NaviOutputInterface getInstance() {
		if (stereoNaviOutput == null) {
			stereoNaviOutput = new StereoNaviOutput();
		}
		return stereoNaviOutput;
	}

	public void deliverOutput(double turnAngle, double distToTurn) {
		// TODO Android Sound dingens holen, je nach Turn Angle und Distance Töne ausgeben.
		// TODO: Parameter, von denen Töne abhängen sind Lautstärke, Frequenz des Abspielens, Tonart, Ausgabe links/rechts



	}

}
