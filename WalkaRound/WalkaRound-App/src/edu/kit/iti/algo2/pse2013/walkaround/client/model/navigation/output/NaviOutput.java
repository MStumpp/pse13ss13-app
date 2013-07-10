package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;

public interface NaviOutput {
	
	public NaviOutput getInstance();
	
	public void deliverOutput(CrossingInformation crossingInfo, int distToTurn);
	

}
