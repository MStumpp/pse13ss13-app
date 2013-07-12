package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation;

import java.util.LinkedList;

import android.util.Log;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
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
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Location;
import android.preference.PreferenceManager;

public class NaviModel implements OnSharedPreferenceChangeListener, RouteListener, PositionListener, CompassListener, SpeedListener {
	
	private static String TAG_NAVI = NaviModel.class.getSimpleName();

	private SharedPreferences sharedPrefs;
	private Context applicationContext;
	
	private static NaviModel naviModel;
	
	// References to listeners and HeadUpController:
	private LinkedList<NaviOutput> naviOutputs;
	private HeadUpController headUpControllerInstance;
	
	private boolean naviIsActive;
	
	// Current information, used as input for navi-calculations:
	private Location lastKnownUserLocation;
	private Coordinate nextCrossing;
	private RouteInfo lastKnownRoute;
	
	// Navigation information for NaviOutput-Classes
	private double turnAngle;
	private double distToTurn;
	
	// additional information for head up display.
	private int distOnRouteInMeters;
	private int distLeftOnRouteInMeter;
	private int timeOnRouteInSec;
	private int timeLeftOnRouteInSec;
	private double speed;
	
	public void initialize(Context context) {
		Log.d(TAG_NAVI, "NavigationModel initialize(Context)");
		this.applicationContext = context.getApplicationContext();
	}
	
	private NaviModel() {
		Log.d(TAG_NAVI, "NavigationModel Contructor");
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
			Log.e(TAG_NAVI, "Navigtaion Model has to be initialized first.");
		}
	}
	
	public NaviModel getInstance() {
		Log.d(TAG_NAVI, "getInstance()");
		if (naviModel == null) {
			naviModel = new NaviModel();
		}
		naviModel.sharedPrefs.registerOnSharedPreferenceChangeListener(this);
		return naviModel;
	}
	
	
	private void notifyAllNaviOutputs() {
		// Sending information to headUp display:
		this.headUpControllerInstance.setSpeed(this.speed);
		this.headUpControllerInstance.setTimePassed(this.timeOnRouteInSec);
		this.headUpControllerInstance.setTimeToGo(this.timeLeftOnRouteInSec);
		this.headUpControllerInstance.setWayPassed(this.distOnRouteInMeters);
		this.headUpControllerInstance.setWayToGo(this.distLeftOnRouteInMeter);
		// notifying all Navi Outputs:
		for (NaviOutput naviOutput : this.naviOutputs) {
			naviOutput.deliverOutput(this.turnAngle, this.distToTurn);
		}
	}
	
	
	// 2 Strategy-Pattern methods:
	private boolean addOutputStrategy(NaviOutput naviOutput) {
		Log.d(TAG_NAVI, "addOutputStrategy(NaviOutput naviOutput)");
		this.naviOutputs.add(naviOutput);
		return true;
	}
	
	private boolean removeOutputStrategy(NaviOutput naviOutput) {
		Log.d(TAG_NAVI, "removeOutputStrategy(NaviOutput naviOutput)");
		this.naviOutputs.remove(naviOutput);
		return true;
	}
	
	
	
	
	
	public boolean isRunning() {
		Log.d(TAG_NAVI, "isRunning()");
		return this.naviIsActive;
	}
	
	public boolean toggleNavigation() {
		Log.d(TAG_NAVI, "toggleNavigation()");
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
		Log.d(TAG_NAVI, "onSharedPreferenceChanged(SharedPreferences, String)");
		this.sharedPrefs = sharedPreferences;
		//TODO: Strategien an / abmelden, je nach dem, welche Prefs aktiv sind.
	}

	@Override
	public void onSpeedChange(double speed) {
		Log.d(TAG_NAVI, "onSpeedChange(double)");
		this.speed = speed;
		
	}

	@Override
	public void onCompassChange(float direction) {
		Log.d(TAG_NAVI, "onCompassChange(float)");
		// TODO: Vor allem für Stereo Navi relevant
		// this.lastKnownUserLocation.setBearing(direction);
		
	}

	@Override
	public void onPositionChange(Location androidLocation) {
		Log.d(TAG_NAVI, "onPositionChange(Location)");
		this.lastKnownUserLocation = androidLocation;
		// Coordinate nextCrossing = 
		
	}

	@Override
	public void onRouteChange(RouteInfo currentRoute) {
		Log.d(TAG_NAVI, "onRouteCHange(RouteInfo)");
		this.computeNavi();
	}
	
	private void computeNavi() {
		this.nextCrossing = this.getNearestCoordinateWithRelevantCrossingInfo(this.lastKnownUserLocation);
		// TODO: Berechne TURN ANGLE, BRAUCHT METHODE
		
		this.distToTurn = CoordinateUtility.calculateDifferenceInMeters(this.nextCrossing, new Coordinate(this.lastKnownUserLocation.getLatitude(), this.lastKnownUserLocation.getLongitude()));
		this.notifyAllNaviOutputs();
	}
	
	private Coordinate getNearestCoordinateWithRelevantCrossingInfo(Location androidLocation) {
		Log.d(TAG_NAVI, "getNearestCoordinateWithRelevantCrossingInfo(Location) METHOD START input Coordinate: " + androidLocation.toString());
		float smallestDifference = Float.POSITIVE_INFINITY;
		Coordinate closestCoordinate = new Coordinate(androidLocation.getLatitude(), androidLocation.getLongitude());
		float[] results = new float[3];
		for (Coordinate coord : this.lastKnownRoute.getCoordinates()) {
			//TODO zweites Argument in if einbauen für relevante Crossings Prüfung, PRÜFUNG nach Reihenfolge
			if (coord.getCrossingInformation().getNumCrossroads() > 2) {
				
				Log.d(TAG_NAVI, "getNearestCoordinateWithRelevantCrossingInfo(Location) setting new nearest Coordinate");
				
				Location.distanceBetween(androidLocation.getLatitude(), androidLocation.getLongitude(), coord.getLatitude(), coord.getLongitude(), results);
				
				if (results[0] < smallestDifference) {
					closestCoordinate = coord;
				}
			}
		}
		
		Log.d(TAG_NAVI, "getNearestCoordinate(Location) METHOD END return Coordinate: " + closestCoordinate.toString());
		return closestCoordinate;
	}
	
	
	
	
	
	
	private Coordinate getNearestCoordinateOnRoute(Location androidLocation) {
		Coordinate nearestCoordinate = null;
		float smallestDifference = Float.POSITIVE_INFINITY;
		double tempDifference;
		
		for (Coordinate coord : this.lastKnownRoute.getCoordinates()) {
			 tempDifference = CoordinateUtility.calculateDifferenceInMeters(new Coordinate(androidLocation.getLatitude(), androidLocation.getLongitude()), coord);
			 if (tempDifference < smallestDifference) {
				 smallestDifference = (float) tempDifference;
				 nearestCoordinate = coord;
			 }
		}
		return nearestCoordinate;
	}
	
	
	
	
	
	
	
	/*
	private void flagRelevantCrossingAngle(RouteInfo ri) {
		// TODO: Gehe alle Knoten durch, berechne 
		CrossingInformation[] crossingAngleOfRouteCoordinates = new CrossingInformation[ri.getCoordinates().size()];
		for (int i = 0; i < crossingAngleOfRouteCoordinates.length; i++) {
			crossingAngleOfRouteCoordinates[i] = ri.getCoordinates().get(i).getCrossingInformation();
		}
		
		for (int i = 0; i < crossingAngleOfRouteCoordinates.length; i++) {
			
		}
		
		
		
	}
	
	*/
	
	
	
	
	
	// TODO: (OLD!?) nächsten turnAngle berechnen.
	// Dazu nächstliegenden Coord zur GPS Pos aus Route bestimmen(, dieser muss aber vor dem User auf der Route liegen), danach von dort die Route abgehen bis Coord mit CrossingInfo != null vorhanden.
	// 
	
	
}
