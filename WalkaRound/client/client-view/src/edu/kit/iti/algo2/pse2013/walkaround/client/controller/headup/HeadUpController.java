package edu.kit.iti.algo2.pse2013.walkaround.client.controller.headup;

import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.headup.HeadUpView;

/**
 * Diese klasse bündelt und kontrolliert die Aus/Eingabe des HeadUpView
 * Elementes
 * 
 * @author Ludwig Biermann
 * 
 */
public class HeadUpController {

	/**
	 * Die Instanz des headUpController
	 */
	private static HeadUpController headUpController;
	/**
	 * Der Log Tag
	 */
	private static String HEAD_CONTROLLER = "HEAD_CONTROLLER";

	private HeadUpView headUpView;
	private MapController mapController;
	private boolean navi;

	/**
	 * initialisiert den HeadUpController
	 * 
	 * @param headUpView
	 *            die nötige Referenz zum HeadUpView
	 */
	private HeadUpController(HeadUpView headUpView) {
		this.headUpView = headUpView;
		this.mapController = MapController.getInstance();
		navi = false;
	}

	/**
	 * initialisiert einmal den HeadUpController
	 * 
	 * @param headUpView
	 *            die nötige Instanz des HeadUpViews
	 * @return eine Instanz des headUpControllers
	 */
	public static HeadUpController initializes(HeadUpView headUpView) {
		if (headUpController == null) {
			headUpController = new HeadUpController(headUpView);
		}
		return headUpController;
	}

	/**
	 * gibt eine Instanz des HeadUpControllers zurück
	 * 
	 * @return HeadUpController oder null fall initializes nicht aufgerufen
	 *         wurde
	 */
	public static HeadUpController getInstance() {
		if (headUpController == null) {
			Log.d(HEAD_CONTROLLER, "bitte initialisieren sie zuerst MapView");
			return null;
		}
		return headUpController;
	}

	public void showOptions() {
		// TODO
	}

	/**
	 * vergrößert die Karte um 1 Level
	 */
	public void zoomInOneLevel() {
		mapController.onZoom((mapController.getCurrentLevelOfDetail() + 1.0F));
	}

	/**
	 * verkleinert die Karte um 1 Level
	 */
	public void zoomOutOneLevel() {
		mapController.onZoom((mapController.getCurrentLevelOfDetail() - 1.0F));
	}

	/**
	 * startet oder deaktiviert die Navigation
	 */
	public void toggleNavigation() {
		Log.d(HEAD_CONTROLLER ,"Navigation ist " + navi);
		// TODO fehlt die Referenzierung zur Navi Komponente
		if(!navi){
			headUpView.showNavigationElements();
			Log.d(HEAD_CONTROLLER ,"false->true");
			navi = true;
		} else {
			headUpView.hideNavigationElements();
			Log.d(HEAD_CONTROLLER ,"true->false");
			navi = false;
		}
	}

	/**
	 * Updatet das Piktogramm anhand einer Id.
	 * 
	 * @param piktogramm
	 *            id des Piktogramm.
	 */
	public void setPiktogram(int id) {
		headUpView.updatePiktogram(id);
	}

	/**
	 * Updatet den Navigationstext.
	 * 
	 * @param text
	 *            neuer Text
	 */
	public void setNavigationsText(String text) {
		headUpView.updateNavigationsText(text);
	}

	/**
	 * Updatet die Geschwindigkeit. Eingabe in m/s
	 * 
	 * @param speed
	 *            neue Geschwindigkeit
	 */
	public void setSpeed(double speed) {
		headUpView.updateSpeed(speed);
	}

	/**
	 * Updatet den noch zulaufenden Weg. Eingabe in m
	 * 
	 * @param waytogo
	 *            noch zulaufenenden Weg.
	 */
	public void setWayToGo(int waytogo) {
		headUpView.updateWayToGo(waytogo);
	}

	/**
	 * Updatet die gelaufenden Weg Eingabe in m
	 * 
	 * @param wayPassed
	 *            gelaufender Weg
	 */
	public void setWayPassed(int wayPassed) {
		headUpView.updateWayPassed(wayPassed);

	}

	/**
	 * Updatet die noch zu laufende Zeit Eingabe in s
	 * 
	 * @param timeToGo
	 *            noch zu laufende zeit
	 */
	public void setTimeToGo(double timeToGo) {
		headUpView.updateTimeToGo(timeToGo);
	}

	/**
	 * updatet die vergangene Zeit. Eingabe in s
	 * 
	 * @param timePassed
	 *            vergangene Zeit
	 */
	public void setTimePassed(int timePassed) {
		headUpView.updateWayPassed(timePassed);

	}

	/**
	 * wechselt zwischen den Ansichts Modi. true Karte ist auf User
	 * zentriert(default) false Karte ist frei beweglich
	 * 
	 * @param b
	 *            der Modi
	 */
	public void switchUserPositionLock(boolean b) {
		headUpView.switchUserPositionLock(b);
		if (b == true) {
			mapController.onLockUserPosition();
		}
	}

}
