package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation;

import java.util.LinkedList;

import android.util.Log;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output.ArrowNaviOutput;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output.AudibleTextNaviOutput;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output.NaviOutput;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output.StereoNaviOutput;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output.VisualTextNaviOutput;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.CompassListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.SpeedListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Location;
import android.preference.PreferenceManager;

public class NaviModel implements OnSharedPreferenceChangeListener, RouteListener, PositionListener, CompassListener, SpeedListener {
	
	private static String TAG_NAVI_MODEL = NaviModel.class.getSimpleName();

	private SharedPreferences sharedPrefs;
	private Context applicationContext;
	
	private static NaviModel naviModel;
	
	private LinkedList<NaviOutput> naviOutputs;
	
	private boolean naviIsActive;
	
	// TODO:
	private int distOnRouteInMeters;
	private int distLeftOnRouteInMeter;
	private int timeOnRouteInSec;
	private int timeLeftOnRouteInSec;
	private double speed;
	
	private Location lastKnownUserLocation;
	
	public void initialize(Context context) {
		Log.d(TAG_NAVI_MODEL, "NavigationModel initialize(Context)");
		this.applicationContext = context.getApplicationContext();
	}

	private NaviModel() {
		Log.d(TAG_NAVI_MODEL, "NavigationModel Contructor");
		if (this.applicationContext != null) {
			this.naviOutputs = new LinkedList<NaviOutput>();
			this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.applicationContext);
			this.naviIsActive = false;
			this.distLeftOnRouteInMeter = 0;
			this.distOnRouteInMeters = 0;
			this.timeOnRouteInSec = 0;
			this.timeLeftOnRouteInSec = 0;
			this.speed = 0;
			// TODO: Je nach gesetzten Optionen die Navi Strategien in onSharedPref...() Methode aktivieren/deaktivieren
			this.addOutputStrategy(ArrowNaviOutput.getInstance());
			this.addOutputStrategy(AudibleTextNaviOutput.getInstance());
			this.addOutputStrategy(VisualTextNaviOutput.getInstance());
			this.addOutputStrategy(StereoNaviOutput.getInstance());
		} else {
			Log.e(TAG_NAVI_MODEL, "Navigtaion Model has to be initialized first.");
		}
	}
	
	public NaviModel getInstance() {
		Log.d(TAG_NAVI_MODEL, "getInstance()");
		if (naviModel == null) {
			naviModel = new NaviModel();
		}
		naviModel.sharedPrefs.registerOnSharedPreferenceChangeListener(this);
		return naviModel;
	}
	
	
	
	
	// Strategy-Pattern methods
	private boolean addOutputStrategy(NaviOutput naviOutput) {
		Log.d(TAG_NAVI_MODEL, "addOutputStrategy(NaviOutput naviOutput)");
		this.naviOutputs.add(naviOutput);
		return true;
	}
	
	private boolean removeOutputStrategy(NaviOutput naviOutput) {
		Log.d(TAG_NAVI_MODEL, "removeOutputStrategy(NaviOutput naviOutput)");
		this.naviOutputs.remove(naviOutput);
		return true;
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
	
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		this.sharedPrefs = sharedPreferences;
		//TODO: Strategien an / abmelden, je nach dem, welche Prefs aktiv sind.
	}

	@Override
	public void onSpeedChange(double speed) {
		this.speed = speed;
		
	}

	@Override
	public void onCompassChange(float direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPositionChange(Location androidLocation) {
		this.lastKnownUserLocation = androidLocation;
		
	}

	@Override
	public void onRouteChange(RouteInfo currentRoute) {
		// TODO Auto-generated method stub
		
	}
	
	// TODO: nächsten turnAngle berechnen. Dazu nächstliegenden Coord zur GPS Pos aus Route bestimmen(, dieser muss aber vor dem User auf der Route liegen), danach von dort die Route abgehen bis Coord mit CrossingInfo != null vorhanden.
	
	
	
}
