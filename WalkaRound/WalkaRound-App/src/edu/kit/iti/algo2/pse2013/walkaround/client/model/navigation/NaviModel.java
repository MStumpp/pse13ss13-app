package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation;

import java.util.LinkedList;

import android.util.Log;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output.NaviOutput;

public class NaviModel implements NaviInfo {
	
	private static String TAG_NAVI_MODEL = NaviModel.class.getSimpleName();
	
	private static NaviModel naviModel;
	
	private LinkedList<NaviOutput> naviOutputs;
	
	private boolean naviIsActive;
	
	private NaviInfo naviInfo;
	
	
	private int distOnRouteInMeters;
	private int distLeftOnRouteInMeter;
	private int timeOnRouteInSec;
	private int timeLeftOnRouteInSec;

	private NaviModel() {
		Log.d(TAG_NAVI_MODEL, "NavigationModel Contructor");
		this.naviOutputs = new LinkedList<NaviOutput>();
		this.naviIsActive = false;
		this.distLeftOnRouteInMeter = 0;
		this.distOnRouteInMeters = 0;
		this.timeOnRouteInSec = 0;
		this.timeLeftOnRouteInSec = 0;
	}
	
	public NaviModel getInstance() {
		Log.d(TAG_NAVI_MODEL, "getInstance()");
		if (naviModel == null) {
			naviModel = new NaviModel();
		}
		return naviModel;
	}
	
	
	public boolean addOutputStrategy(NaviOutput naviOutput) {
		Log.d(TAG_NAVI_MODEL, "addOutputStrategy(NaviOutput naviOutput)");
		this.naviOutputs.add(naviOutput);
		return true;
	}
	
	public boolean removeOutputStrategy(NaviOutput naviOutput) {
		Log.d(TAG_NAVI_MODEL, "removeOutputStrategy(NaviOutput naviOutput)");
		this.naviOutputs.remove(naviOutput);
		return true;
	}
	
	public LinkedList<NaviOutput> getOutputStrategies() {
		Log.d(TAG_NAVI_MODEL, "getOutputStrategies()");
		return this.naviOutputs;
	}
	
	public boolean isRunning() {
		Log.d(TAG_NAVI_MODEL, "isRunning()");
		return this.naviIsActive;
	}
	
	public boolean toggleNavigation() {
		Log.d(TAG_NAVI_MODEL, "toggleNavigation()");
		if (this.naviIsActive == false) {
			this.naviIsActive = true;
		} else {
			this.naviIsActive = false;
		}
		return this.naviIsActive;
	}

	public NaviInfo getNavigationInfo() {
		Log.d(TAG_NAVI_MODEL, "getNavigationInfo()");
		return this.naviInfo;
	}

	@Override
	public int getDistOnRouteInMeters() {
		Log.d(TAG_NAVI_MODEL, "getDistOnRouteInMeters()");
		return this.distOnRouteInMeters;
	}

	@Override
	public int getDistLeftOnRouteInMeter() {
		Log.d(TAG_NAVI_MODEL, "getDistLeftOnRouteInMeter()");
		return this.distLeftOnRouteInMeter;
	}

	@Override
	public int getTimeOnRouteInSec() {
		Log.d(TAG_NAVI_MODEL, "getTimeOnRouteInSec()");
		return this.timeOnRouteInSec;
	}

	@Override
	public int getTimeLeftOnRoute() {
		Log.d(TAG_NAVI_MODEL, "getTimeLeftOnRoute()");
		return this.timeLeftOnRouteInSec;
	}
	
}
