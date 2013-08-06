package edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay;

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.headup.HeadUpView;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.pullup.PullUpView;

/**
 * This class controls the data flow between the control Elements on the View and the System
 *
 * @author Ludwig Biermann
 *
 */
public class HeadUpController {

	/**
	 * Debug Information
	 */
	public static String TAG_HEADUP_CONTROLLER = HeadUpController.class.getSimpleName();
	
	/**
	 * The Controller
	 */
	private static HeadUpController headUpController;
	private MapController mapController;

	/**
	 * The Views
	 */
	private HeadUpView headUpView;
	
	/**
	 * permanent values
	 */
	private boolean navi;

	/*
	 * -----------------Initialization-----------------
	 */
	
	
	/**
	 * constructor of the HeadUpController
	 *
	 * @param headUpView the required HeadUpView
	 */
	private HeadUpController(HeadUpView headUpView) {
		this();
		this.headUpView = headUpView;
	}

	private HeadUpController(){
		this.mapController = MapController.getInstance();
		navi = false;		
	}
	
	public void setHeadUpView(HeadUpView headUpView) {
		this.headUpView = headUpView;
	}
	
	/**
	 * Initialize the unique HeadUpController
	 *
	 * @param headUpView the required HeadUpView
	 * @return Instance of headUpControllers
	 */
	public static HeadUpController initializes() {
		if (headUpController == null) {
			headUpController = new HeadUpController();
		}
		return headUpController;
	}
	
	
	/**
	 * Initialize the unique HeadUpController
	 *
	 * @param headUpView the required HeadUpView
	 * @return Instance of headUpControllers
	 */
	public static HeadUpController initializes(HeadUpView headUpView) {
		if (headUpController == null) {
			headUpController = new HeadUpController(headUpView);
		}
		return headUpController;
	}

	/**
	 * gives back the initialize of HeadUpControllers
	 *
	 * @return HeadUpController or {@code null}, if HeadUpController isn't initialize
	 */
	public static HeadUpController getInstance() {
		if (headUpController == null) {
			Log.d(TAG_HEADUP_CONTROLLER, "you have to initialice first");
			return null;
		}
		return headUpController;
	}

	/*
	 * -----------------Zoom Forwarding-----------------
	 */

	/**
	 * 	scales up the map by one step
	 */
	public void zoomInOneLevel() {
		Log.d(TAG_HEADUP_CONTROLLER, String.format("zoom step increased by one (%.2f => %.2f)", this.mapController.getCurrentLevelOfDetail(), this.mapController.getCurrentLevelOfDetail() + 1));
		this.mapController.onZoom(+1.0F);
	}

	/**
	 * scales down the map by one step 
	 */
	public void zoomOutOneLevel() {
		Log.d(TAG_HEADUP_CONTROLLER, String.format("zoom step decreasedby one (%.2f => %.2f)", this.mapController.getCurrentLevelOfDetail(), this.mapController.getCurrentLevelOfDetail() - 1));
		this.mapController.onZoom(-1.0F);
	}

	/*
	 * -----------------Position Lock Forwarding-----------------
	 */
	
	/**
	 * 
	 * toggle between lock on user position and free movable map
	 * true if lock (default) centers the map on user position
	 * false if unlock map is free moveable
	 *
	 * @param b
	 *           true if lock
	 */
	public void toggleUserPositionLock() {
		Log.d(TAG_HEADUP_CONTROLLER ,"Toggle user position lock!");
		this.mapController.toggleLockUserPosition();
		
	}
	
	/**
	 * 
	 * @param lock
	 */
	public void setUserPoisitionLock(boolean lock) {
		this.headUpView.setUserPositionLock(lock);
	}

	/*
	 * -----------------Option Forwarding-----------------
	 */
	
	/**
	 * forward the action to the pullup menu to start the option
	 */
	public void startOption() {
		Log.d(TAG_HEADUP_CONTROLLER ,"Start Option!");
		this.mapController.getPullUpView().changeView(PullUpView.CONTENT_OPTION);
		this.mapController.getPullUpView().setFullSizeHeight();
	}
	
	/*
	 * -----------------Navigation Forwarding-----------------
	 */
	
	/**
	 * stoogle Navigation
	 */
	public void toggleNavigation() {
		if(!this.navi){
			this.headUpView.showNavigationElements();
			this.navi = true;
		} else {
			this.headUpView.hideNavigationElements();
			this.navi = false;
		}
		Log.d(TAG_HEADUP_CONTROLLER ,"Navigation " + this.navi);
	}

	/**
	 * Updates pictogram by unique id
	 *
	 * @param piktogramm id of pictogram
	 */
	public void setPiktogram(int id) {
		this.headUpView.updatePiktogram(id);
	}

	/**
	 * Updates the navigation text.
	 *
	 * @param text
	 *            neuer Text
	 */
	public void setNavigationsText(String text) {
		this.headUpView.updateNavigationsText(text);
	}

	/**
	 * Updates the speed. Input must be in m/s
	 *
	 * @param speed the new speed
	 */
	public void setSpeed(double speed) {
		this.headUpView.updateSpeed(speed);
	}

	/**
	 * Updates the way to go. Input in meters
	 *
	 * @param waytogo
	 *            Way to go.
	 */
	public void setWayToGo(int waytogo) {
		this.headUpView.updateWayToGo(waytogo);
	}

	/**
	 * Updates way passed. Input in meters
	 *
	 * @param wayPassed
	 *             way passed
	 */
	public void setWayPassed(int wayPassed) {
		this.headUpView.updateWayPassed(wayPassed);

	}

	/**
	 * Updates time to go. Input in seconds
	 *
	 * @param timeToGo time to go
	 */
	public void setTimeToGo(double timeToGo) {
		this.headUpView.updateTimeToGo(timeToGo);
	}

	/**
	 * Updates time passed. Input in seconds
	 *
	 * @param timePassed time passed
	 */
	public void setTimePassed(int timePassed) {
		this.headUpView.updateWayPassed(timePassed);

	}
}
