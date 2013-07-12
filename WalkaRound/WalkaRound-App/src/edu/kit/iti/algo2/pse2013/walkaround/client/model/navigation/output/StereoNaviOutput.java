package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;

public class StereoNaviOutput implements NaviOutput {
	
	private static StereoNaviOutput stereoNaviOutput;
	private HeadUpController headUpControllerInstance;
	
	private StereoNaviOutput() {
		this.headUpControllerInstance = HeadUpController.getInstance();
	}
	
	public static NaviOutput getInstance() {
		stereoNaviOutput = new StereoNaviOutput();
		return stereoNaviOutput;
	}

	@Override
	public void deliverOutput(double turnAngle, double distToTurn) {
		// TODO Android Sound dingens holen, je nach Turn Angle und Distance Töne ausgeben.
		// TODO: Parameter, von denen Töne abhängen sind Lautstärke, Frequenz des Abspielens, Tonart, Ausgabe links/rechts
		
		
	}

}
