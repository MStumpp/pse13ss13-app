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
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;

/**
 * Diese Klasse ist für die Anzeige der sogenannten HeadUp Elemente zuständig.
 *
 * Darunter fallen: Option Button Vergrößern/ Verkleinern Button Play Button
 * PlayerLock Button statische Navigationsanzeige
 *
 * @author Ludwig Biermann
 *
 */
public class HeadUpView extends Fragment {

	private final static String TAG_HEADUP_VIEW = HeadUpView.class.getSimpleName();
	private final static String TAG_HEADUP_VIEW_TOUCH = TAG_HEADUP_VIEW + "_touch";

	private final static int user_lock = R.drawable.user_arrow_lock;
	private final static int user_unlock = R.drawable.user_arrow_unlock;

	private final static int play = R.drawable.play;
	private final static int pause = R.drawable.pause;

	private final static boolean defaultLockPosition = true;

	// Mögliche Piktogramme
	public final static int ARROW_RIGHT = R.drawable.pikto_rechts;

	HeadUpController headUpController;

	// Lock Bilder
	private Drawable lock;
	private Drawable unlock;
	private boolean lockPosition;

	// Steuerelemente

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
		Log.d(TAG_HEADUP_VIEW, "Es wird mit dem headUpController verbunden");
		HeadUpController.getInstance().setHeadUpView(this);
		headUpController = HeadUpController.getInstance();
		Log.d(TAG_HEADUP_VIEW, "Controller wurde initialisiert: " + (headUpController != null));

		Display display = this.getActivity().getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		float height = (size.y - size.y / 10) - 50;

		Log.d(TAG_HEADUP_VIEW, "High of the HeadUP " + height);
		this.getActivity().findViewById(R.id.headup_container)
				.getLayoutParams().height = (int) height;

		// -----------------------------------------------
		Log.d(TAG_HEADUP_VIEW,
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

		Log.d(TAG_HEADUP_VIEW, "View wird angepasst");

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
		Log.d(TAG_HEADUP_VIEW, "Listener werden hinzugef�gt");

		plus.setOnTouchListener(new ZoomPlusListener());
		minus.setOnTouchListener(new ZoomMinusListener());

		option.setOnTouchListener(new OptionListener());
		userLock.setOnTouchListener(new UserLockListener());

		// anfangs pausiert also nach Play listen
		naviControll.setOnTouchListener(new NavigationControllListener());

		// -----------------------------------------------
		Log.d(TAG_HEADUP_VIEW, "initialisiere die Lock Position");
		lock = this.getActivity().getResources().getDrawable(user_lock);
		unlock = this.getActivity().getResources().getDrawable(user_unlock);

		lockPosition = defaultLockPosition;

		//headUpController.toggleUserPositionLock();

		// -----------------------------------------------
		Log.d(TAG_HEADUP_VIEW, "initialisiere die Navigation Position");
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
	public void setUserPositionLock(boolean status) {
		if(status){
			userLock.setImageDrawable(lock);
			Log.d(TAG_HEADUP_VIEW_TOUCH, "UNLOCK");
		} else {
			userLock.setImageDrawable(unlock);
			Log.d(TAG_HEADUP_VIEW_TOUCH, "LOCK");
		}
	}

	/**
	 * Ein Listener der auf eine Optionen Eingabe wartet.
	 *
	 * @author Ludwig Biermann
	 *
	 */
	private class OptionListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(option)) {
				Log.d(TAG_HEADUP_VIEW_TOUCH, "Optionen werden gestartet");
				headUpController.startOption();
			}
			return false;
		}

	}

	/**
	 * Ein Listener der auf eine Vergrößern Eingabe wartet.
	 *
	 * @author Ludwig Biermann
	 *
	 */
	private class ZoomPlusListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(plus)) {
				Log.d(TAG_HEADUP_VIEW_TOUCH, "plus i pressed");
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
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(minus)) {
				Log.d(TAG_HEADUP_VIEW_TOUCH, "Minus is pressed");
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
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(naviControll)) {
				Log.d(TAG_HEADUP_VIEW_TOUCH, "Navigation wird aktiviert/deaktiviert");
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
		public boolean onTouch(View view, MotionEvent arg1) {
			if (view.equals(userLock)) {
				Log.d(TAG_HEADUP_VIEW_TOUCH, "toggle UserLock");
				headUpController.toggleUserPositionLock();
			}
			return false;
		}
	}
}