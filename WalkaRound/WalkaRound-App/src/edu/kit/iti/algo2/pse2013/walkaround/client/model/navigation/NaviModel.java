package edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation;

import java.util.Iterator;
import java.util.LinkedList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Location;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.RouteController.RouteListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.navigation.output.NaviOutputInterface;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteInfo;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.CompassManager.CompassListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager.PositionListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.SpeedManager.SpeedListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.PreferenceUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

public class NaviModel implements OnSharedPreferenceChangeListener, RouteListener, PositionListener, CompassListener, SpeedListener {

	private static String TAG_NAVI = NaviModel.class.getSimpleName();

	private static Context applicationContext;

	private static NaviModel naviModel;

	// References to listeners and HeadUpController:
	private LinkedList<NaviOutputInterface> naviOutputs;

	private boolean naviIsActive;

	// Current information, used as input for navi-calculations:
	private Location lastKnownUserLocation;
	private Coordinate nextCrossing;
	private Coordinate nextNextCrossing;
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

	public static void initialize(Context context) {
		Log.d(TAG_NAVI, "NavigationModel initialize(Context)");
		applicationContext = context.getApplicationContext();
	}

	private NaviModel() {
		Log.d(TAG_NAVI, "NavigationModel Contructor");
		if (this.applicationContext != null) {
			this.naviOutputs = new LinkedList<NaviOutputInterface>();

			// bitte nutze Preference Utility
			//this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.applicationContext);

			this.naviIsActive = false;
			this.distLeftOnRouteInMeter = 0;
			this.distOnRouteInMeters = 0;
			this.timeOnRouteInSec = 0;
			this.timeLeftOnRouteInSec = 0;
			this.speed = 0;
			// TODO: Je nach gesetzten Optionen die Navi Strategien in onSharedPref...() Methode aktivieren/deaktivieren

			PreferenceUtility.getInstance().registerOnSharedPreferenceChangeListener(this);
		} else {
			Log.e(TAG_NAVI, "Navigtaion Model has to be initialized first.");
		}
	}

	public static NaviModel getInstance() {
		Log.d(TAG_NAVI, "getInstance()");
		if (naviModel == null) {
			naviModel = new NaviModel();
		}
		return naviModel;
	}


	private void notifyAllNaviOutputs() {
		Log.e(TAG_NAVI, "notifyAllNaviOutputs() METHOD START");
		// Sending information to controller of headUp display:
		//this.headUpControllerInstance.setSpeed(this.speed);
		//this.headUpControllerInstance.setTimePassed(this.timeOnRouteInSec);
		//this.headUpControllerInstance.setTimeToGo(this.timeLeftOnRouteInSec);
		//this.headUpControllerInstance.setWayPassed(this.distOnRouteInMeters);
		//this.headUpControllerInstance.setWayToGo(this.distLeftOnRouteInMeter);
		// TODO: this.headUpControllerInstance.setTurnAngle(this.turnAngle);
		// TODO: this.headUpControllerInstance.setDistToTurn(this.distToTurn);
		// notifying all Navi Outputs:
		for (NaviOutputInterface naviOutput : this.naviOutputs) {
			naviOutput.deliverOutput(this.turnAngle, this.distToTurn);
		}
	}

	// 2 Strategy-Pattern methods:
	public boolean addOutputStrategy(NaviOutputInterface naviOutput) {
		Log.d(TAG_NAVI, "addOutputStrategy(NaviOutput naviOutput)");
		this.naviOutputs.add(naviOutput);
		return true;
	}

	public boolean removeOutputStrategy(NaviOutputInterface naviOutput) {
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

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.d(TAG_NAVI, "onSharedPreferenceChanged(" + sharedPreferences + ", " + key + ")");
		// TODO:

		/*
		this.addOutputStrategy(ArrowNaviOutput.getInstance());
		this.addOutputStrategy(TTSNaviOutput.getInstance());
		this.addOutputStrategy(VisualTextNaviOutput.getInstance());
		this.addOutputStrategy(StereoNaviOutput.getInstance());
		*/
	}


	public void onSpeedChange(double speed) {
		Log.d(TAG_NAVI, "onSpeedChange(double)");
		this.speed = speed;
	}

	public void onCompassChange(float direction) {
		Log.d(TAG_NAVI, "onCompassChange(float)");
		// TODO: für Stereo Navi relevant
		// this.lastKnownUserLocation.setBearing(direction);

	}

	public void onPositionChange(Location androidLocation) {
		Log.d(TAG_NAVI, "onPositionChange(Location)");
		this.lastKnownUserLocation = androidLocation;
		this.computeNavi();
	}

	public void onRouteChange(RouteInfo currentRoute) {
		Log.d(TAG_NAVI, "onRouteCHange(RouteInfo)");
		this.computeNavi();
	}

	private void computeNavi() {
		Log.e(TAG_NAVI, "computeNavi()");
		this.computeNextTurnCoordinates();
		this.computeNewTurnAngle();
		this.computeNewDistanceToTurn();
		this.notifyAllNaviOutputs();
	}



	private void computeNextTurnCoordinates() {
		Coordinate tempNextCrossing = null;
		Coordinate tempNextNextCrossing = null;
		// Find the next crossing on Route:
		tempNextCrossing = this.computeNextCrossing();

		// Determine the next next crossing. (Yes, that is two next. ;-))
		if (tempNextCrossing != null) {
			Iterator<Coordinate> coordsIter = this.lastKnownRoute.getCoordinates().iterator();
			Coordinate tempCoord = null;
			while (coordsIter.hasNext() && !tempNextCrossing.equals(tempCoord)) {
				tempCoord = coordsIter.next();
			}
			if (coordsIter.hasNext()) {
				tempCoord = coordsIter.next();
			}
			while (coordsIter.hasNext() && !tempNextCrossing.equals(tempCoord) && tempCoord != null && !(tempCoord.getCrossingInformation().getCrossingAngles().length > 1)) {
				tempCoord = coordsIter.next();
			}
			tempNextNextCrossing = tempCoord;
		}

		this.nextCrossing = tempNextCrossing;
		this.nextNextCrossing = tempNextNextCrossing;

	}




	private void computeNewTurnAngle() {
		Log.e(TAG_NAVI, "computeNewTurnAngle()");
		double result = 0.00;
		// a turn angle can only exists if there's a direction to turn to:
		if (this.nextNextCrossing != null) {
			double[] vectorPosToNextCrossing = new double[2];
			double[] vectorNextCrossingToNextNextCrossing = new double[2];

			// Calculating x and y components of Vectors.
			// TODO: Test if this method works:
			// otherwise use CoordinateUtility.calculateDifferenceInMeters(c1, c2)
			vectorPosToNextCrossing[0] = this.nextCrossing.getLongitude() - this.lastKnownUserLocation.getLongitude();
			vectorPosToNextCrossing[1] = this.nextCrossing.getLatitude() - this.lastKnownUserLocation.getLatitude();
			vectorNextCrossingToNextNextCrossing[0] = this.nextNextCrossing.getLongitude() - this.nextCrossing.getLongitude();
			vectorNextCrossingToNextNextCrossing[1] = this.nextNextCrossing.getLatitude() - this.nextCrossing.getLatitude();

			double angleOfvectorPosToNextCrossingToXAxis = Math.cos(vectorPosToNextCrossing[1] / vectorPosToNextCrossing[0]);
			double angleOfvectorNextCrossingToNextNextCrossingToXAxis = Math.cos(vectorNextCrossingToNextNextCrossing[1] / vectorNextCrossingToNextNextCrossing[0]);

			this.turnAngle = angleOfvectorPosToNextCrossingToXAxis - angleOfvectorNextCrossingToNextNextCrossingToXAxis;
		}

		this.turnAngle = result;
	}


	private void computeNewDistanceToTurn() {
		this.distToTurn = CoordinateUtility.calculateDifferenceInMeters(this.nextCrossing, new Coordinate(this.lastKnownUserLocation.getLatitude(), this.lastKnownUserLocation.getLongitude()));
	}





	private Coordinate computeNextCrossing() {
		Coordinate output = null;

		// Find coordinate in front of user:
		Coordinate coordInfrontOfUser = this.computeNextCoordinateInfrontOfUser();
		Coordinate tempCoord = null;

		// forward from this coordinate to the next coordinate on route with a crossing:
		if (coordInfrontOfUser != null) {
			Iterator<Coordinate> coordsIter = this.lastKnownRoute.getCoordinates().iterator();

			// first forward iterator to coordinate in front of user:
			while (coordsIter.hasNext() && !coordInfrontOfUser.equals(tempCoord)) {
				tempCoord = coordsIter.next();
			}
			// now find the next Coordinate on route with a crossing:
			while (coordsIter.hasNext() && tempCoord != null && (tempCoord.getCrossingInformation() != null && !(tempCoord.getCrossingInformation() != null && tempCoord.getCrossingInformation().getNumCrossroads() > 1))) {
				tempCoord = coordsIter.next();
			}

		}
		output = tempCoord;

		return output;
	}


	private Coordinate computeNextCoordinateInfrontOfUser() {
		Coordinate output = null;
		// Principle: iterate through route, find
		Iterator<Coordinate> coordsIter = this.lastKnownRoute.getCoordinates().iterator();
		double minimalDistance = Double.POSITIVE_INFINITY;
		double tempDistance;

		// initializing iterator:
		Coordinate previousCoord = null;
		Coordinate tempCoord = null;

		if (coordsIter.hasNext()) {
			previousCoord = coordsIter.next();
		}
		if (coordsIter.hasNext()) {
			tempCoord = coordsIter.next();
		}

		if (tempCoord != null) {
			while (coordsIter.hasNext()) {
				previousCoord = tempCoord;
				tempCoord = coordsIter.next();
				tempDistance = this.computeDistanceOfUserPositionToLine(previousCoord, tempCoord);
				if (tempDistance < minimalDistance) {
					minimalDistance = tempDistance;
					output = tempCoord;
				}
			}
		}
		return output;
	}


	private double computeDistanceOfUserPositionToLine(Coordinate c1, Coordinate c2) {
		double output = -1.00;
		Coordinate user = new Coordinate(this.lastKnownUserLocation.getLatitude(), this.lastKnownUserLocation.getLongitude());

		double line = CoordinateUtility.calculateDifferenceInMeters(c1, c2);
		double userToC1 = CoordinateUtility.calculateDifferenceInMeters(user, c1);
		double userToC2 = CoordinateUtility.calculateDifferenceInMeters(user, c2);
		// determining angle through law of cosine: a²=b²+c²-2bc*cos(alpha) or cos(alpha) = (b²+c²-a²) / 2bc
		double angleAtC1 = Math.acos((Math.pow(line, 2) + Math.pow(userToC1, 2) - Math.pow(userToC2, 2)) / (2.00 * userToC1 * line));
		// userToC1 can be seen as a hypotenuse. Since the angleAtC1 is now known, the height or distance of the user to the line can be calculated:
		output = Math.sin(angleAtC1) * userToC1;
		return output;
	}



	private Coordinate getNearestCoordinateOnRoute(Coordinate inputCoord) {
		Log.d(TAG_NAVI, "getNearestCoordinateOnRoute(Location) METHOD START input Coordinate: " + inputCoord.toString());
		Coordinate nearestCoordinate = null;
		float smallestDifference = Float.POSITIVE_INFINITY;
		double tempDifference;
		for (Coordinate coord : this.lastKnownRoute.getCoordinates()) {
			 tempDifference = CoordinateUtility.calculateDifferenceInMeters(inputCoord, coord);
			 if (tempDifference < smallestDifference) {
				 smallestDifference = (float) tempDifference;
				 nearestCoordinate = coord;
			 }
		}
		return nearestCoordinate;
	}






}
