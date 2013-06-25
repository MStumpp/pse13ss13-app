package edu.kit.iti.algo2.pse2013.walkaround.client.view.headup;

import android.app.Fragment;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.headup.HeadUpController;

/**
 * Diese Klasse ist f�r die Anzeige der sogenannten HeadUp Elemente zust�ndig.
 * 
 * Darunter fallen: Option Button Vergr��ern/ Verkleinern Button Play Button
 * PlayerLock Button statische Navigationsanzeige
 * 
 * @author Ludwig Biermann
 * 
 */
public class HeadUpView extends Fragment {

	private final static String HEADUP = "HEADUP";
	private final static String HEADUP_TOUCH = "HEADUP_TOUCH";

	private final static int user_lock = R.drawable.user_arrow_80x100_lock;
	private final static int user_unlock = R.drawable.user_arrow_80x100_unlock;

	private final static int play = R.drawable.play_100x100;
	private final static int pause = R.drawable.pause_100x100;

	private final static boolean defaultLockPosition = true;

	// M�gliche Piktogramme
	public final static int ARROW_RIGHT = R.drawable.pikto_rechts_100x100;

	HeadUpController headUpController;

	// Lock Bilder
	private Drawable lock;
	private Drawable unlock;
	private boolean lockPosition;

	// Steuer Elemente

	private ImageView plus;
	private ImageView minus;
	private ImageView option;
	private ImageView userLock;

	// Navigations Elemente

	private ImageView naviControll;

	private TextView naviText;
	private TextView speed;
	private TextView wayPassed;
	private TextView timePassed;
	private TextView wayToGo;
	private TextView timeToGo;

	private ImageView pikto;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// -----------------------------------------------
		Log.d(HEADUP, "Es wird mit dem headUpController verbunden");
		headUpController = HeadUpController.initializes(this);

		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		float height = (size.y - size.y / 10) - 50;

		Log.d(HEADUP, "H�he des head Up " + height);
		this.getActivity().findViewById(R.id.headup_container)
				.getLayoutParams().height = (int) height;

		// -----------------------------------------------
		Log.d(HEADUP,
				"Die Views werden den entsprechenden Variablen zugeteilt.");

		plus = (ImageView) this.getActivity()
				.findViewById(R.id.headup_lupeplus);
		minus = (ImageView) this.getActivity().findViewById(
				R.id.headup_lupeminus);
		option = (ImageView) this.getActivity()
				.findViewById(R.id.headup_option);
		userLock = (ImageView) this.getActivity().findViewById(
				R.id.headup_userlock);
		naviControll = (ImageView) this.getActivity().findViewById(
				R.id.headup_controll);
		naviText = (TextView) this.getActivity().findViewById(
				R.id.headup_navitext);
		speed = (TextView) this.getActivity().findViewById(R.id.headup_speed);
		wayPassed = (TextView) this.getActivity().findViewById(
				R.id.headup_waypassed);
		timePassed = (TextView) this.getActivity().findViewById(
				R.id.headup_timepassed);
		wayToGo = (TextView) this.getActivity().findViewById(
				R.id.headup_waytogo);
		timeToGo = (TextView) this.getActivity().findViewById(
				R.id.headup_timetogo);

		// -----------------------------------------------
		pikto = (ImageView) this.getActivity().findViewById(R.id.headup_pikto);

		Log.d(HEADUP, "View werden von der gr��e angepasst");

		plus.getLayoutParams().width = size.x / 5;
		plus.getLayoutParams().height = size.x / 10;

		minus.getLayoutParams().width = size.x / 5;
		minus.getLayoutParams().height = size.x / 10;

		option.getLayoutParams().width = size.x / 5;
		option.getLayoutParams().height = size.x / 5;

		userLock.getLayoutParams().width = size.x / 5;
		userLock.getLayoutParams().height = size.x / 5;

		// Navi Bar
		naviControll.getLayoutParams().height = size.y / 10;
		naviText.getLayoutParams().height = size.y / 10;
		pikto.getLayoutParams().height = size.y / 10;

		// zusatz Text
		speed.getLayoutParams().width = size.x / 5;
		wayPassed.getLayoutParams().width = size.x / 5;
		timePassed.getLayoutParams().width = size.x / 5;
		wayToGo.getLayoutParams().width = size.x / 5;
		timeToGo.getLayoutParams().width = size.x / 5;

		// -----------------------------------------------
		Log.d(HEADUP, "Listener werden hinzugef�gt");

		plus.setOnTouchListener(new ZoomPlusListener());
		minus.setOnTouchListener(new ZoomMinusListener());

		option.setOnTouchListener(new OptionListener());
		userLock.setOnTouchListener(new UserLockListener());

		// anfangs pausiert also nach Play listen
		naviControll.setOnTouchListener(new NavigationControllListener());

		// -----------------------------------------------
		Log.d(HEADUP, "initialisiere die Lock Position");
		lock = this.getActivity().getResources().getDrawable(user_lock);
		unlock = this.getActivity().getResources().getDrawable(user_unlock);

		lockPosition = defaultLockPosition;

		headUpController.switchUserPositionLock(lockPosition);

		// -----------------------------------------------
		Log.d(HEADUP, "initialisiere die Navigation Position");
		// TODO
	}

	/**
	 * zeigt die NavigationsElemente
	 */
	public void showNavigationElements() {

		// aktualisiere die Steuerung
		naviControll.setImageDrawable(this.getActivity().getResources()
				.getDrawable(pause));

		// zeige Elemente
		naviText.setVisibility(View.VISIBLE);
		pikto.setVisibility(View.VISIBLE);
		speed.setVisibility(View.VISIBLE);
		wayPassed.setVisibility(View.VISIBLE);
		timePassed.setVisibility(View.VISIBLE);
		wayToGo.setVisibility(View.VISIBLE);
		timeToGo.setVisibility(View.VISIBLE);
	}

	/**
	 * versteckt die NavigationsElemente
	 */
	public void hideNavigationElements() {

		// aktualisiere die Steuerung
		naviControll.setImageDrawable(this.getActivity().getResources()
				.getDrawable(play));

		// verstecke Elemente
		naviText.setVisibility(View.GONE);
		pikto.setVisibility(View.GONE);
		speed.setVisibility(View.GONE);
		wayPassed.setVisibility(View.GONE);
		timePassed.setVisibility(View.GONE);
		wayToGo.setVisibility(View.GONE);
		timeToGo.setVisibility(View.GONE);
	}

	/**
	 * �ndert das Piktogramm anhand einer ID
	 * 
	 * @param id
	 *            des Piktogramms
	 */
	public void updatePiktogram(int id) {
		this.pikto.setImageDrawable(this.getActivity().getResources()
				.getDrawable(id));
	}

	/**
	 * Updatet den Navigationstext.
	 * 
	 * @param text
	 *            neuer Text
	 */
	public void updateNavigationsText(String text) {
		// TODO
		naviText.setText(text);
	}

	/**
	 * Updatet die Geschwindigkeit. Eingabe in m/s
	 * 
	 * @param speed
	 *            neue Geschwindigkeit
	 */
	public void updateSpeed(double speed) {
		// TODO
		this.speed.setText(speed + "m/s");
	}

	/**
	 * Updatet den noch zulaufenden Weg. Eingabe in m
	 * 
	 * @param waytogo
	 *            noch zulaufenenden Weg.
	 */
	public void updateWayToGo(int way) {
		// TODO
		this.wayToGo.setText(way + "m");
	}

	/**
	 * Updatet die gelaufenden Weg Eingabe in m
	 * 
	 * @param wayPassed
	 *            gelaufender Weg
	 */
	public void updateWayPassed(int way) {
		// TODO
		this.wayPassed.setText(way + "m");

	}

	/**
	 * Updatet die noch zu laufende Zeit Eingabe in s
	 * 
	 * @param timeToGo
	 *            noch zu laufende zeit
	 */
	public void updateTimeToGo(double time) {
		// TODO
		this.timeToGo.setText(time + "s");
	}

	/**
	 * updatet die vergangene Zeit. Eingabe in s
	 * 
	 * @param timePassed
	 *            vergangene Zeit
	 */
	public void updateTimePassed(int time) {
		// TODO
		this.timePassed.setText(time + "s");
	}

	/**
	 * wechselt zwischen den Ansichts Modi. true Karte ist auf User
	 * zentriert(default) false Karte ist frei beweglich
	 * 
	 * @param Modi
	 */
	public void switchUserPositionLock(boolean b) {
		if (!b) {
			userLock.setImageDrawable(unlock);
		} else {
			userLock.setImageDrawable(lock);
		}
	}

	/**
	 * Ein Listener der auf eine Optionen Eingabe wartet.
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class OptionListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(option)) {
				Log.d(HEADUP_TOUCH, "Optionen werden gestartet");
			}
			return false;
		}

	}

	/**
	 * Ein Listener der auf eine Vergr��ern Eingabe wartet.
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class ZoomPlusListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(plus)) {
				Log.d(HEADUP_TOUCH, "Plus wird gedr�ckt");
				headUpController.zoomInOneLevel();
			}
			return false;
		}

	}

	/**
	 * Ein Listener der auf eine Verkleinern Eingabe wartet.
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class ZoomMinusListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(minus)) {
				Log.d(HEADUP_TOUCH, "Minus wird gedr�ckt");
				headUpController.zoomOutOneLevel();
			}
			return false;
		}

	}

	/**
	 * Ein Listener der die Navigation einblendet oder ausblendet
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class NavigationControllListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(naviControll)) {
				Log.d(HEADUP_TOUCH, "Navigation wird aktiviert/deaktiviert");
				headUpController.toggleNavigation();
			}
			return false;
		}

	}


	/**
	 * Ein listener der auf eine UserLockEingabe wartet.
	 * 
	 * @author Ludwig Biermann
	 * 
	 */
	private class UserLockListener implements OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(userLock)) {
				Log.d(HEADUP_TOUCH, "UserLock wird ausgef�hrt");
				headUpController.switchUserPositionLock(true);
			}
			return false;
		}

	}
}