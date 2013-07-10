package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation;

public interface NaviInfo {
	
	public NaviInfo getInstance();
	
	public int getDistOnRouteInMeters();
	
	public int getDistLeftOnRouteInMeter();
	
	public int getTimeOnRouteInSec();
	
	public int getTimeLeftOnRoute();
	
}
