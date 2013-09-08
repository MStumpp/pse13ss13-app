package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;

/**
 * 
 * Administered the preferences. Uses the SharedPreferences. For testing please
 * clear data store of the app in android. (a restart and sometimes a
 * re-installation doesn't reset the preferences)
 * 
 * 
 * sound on/off map type - Manik; Wanderkarte, MapQuest POI - audio Title on/off
 * Text on/off POI - text images - on/off Navigation typ of output Text To
 * Speech; Stereo Sounds time to target - on/off time gone - on/off way left -
 * on/off way gone - on/off speed - on/off
 * 
 * WalkaRound Server Shortest-Way - url Roundtrip - url
 * 
 * @author Ludwig Biermann
 * @version 3.5
 * 
 */
public class PreferenceUtility {

	/* General */
	public static boolean DEFAULT_SOUND = false;

	/* look and feel */
	public static boolean DEFAULT_SOUND_TITLE = true;
	public static boolean DEFAULT_SOUND_POI_OUTPUT = true;
	public static boolean DEFAULT_SOUND_POI_IMAGE_OUTPUT = true;
	public static String DEFAULT_MAP_TYP;

	/* navi */
	public static String DEFAULT_NAVI_SOUND_TYP;
	public static boolean DEFAULT_NAVI_TIME_TO_DESTINATION = true;
	public static boolean DEFAULT_NAVI_TIME_LEFT = true;
	public static boolean DEFAULT_NAVI_REMAINING_WAY = true;
	public static boolean DEFAULT_NAVI_WALKED_WAY = true;
	public static boolean DEFAULT_NAVI_SPEED = true;

	/* server */
	public static String DEFAULT_SERVER_SHORT;
	public static String DEFAULT_SERVER_ROUNDTRIP;

	/** Keys **/

	/* gernal */
	public static String KEY_SOUND;

	/* look and feel */
	public static String KEY_POI_SOUND_TITLE;
	public static String KEY_POI_SOUND_TEXT;
	public static String KEY_POI_IMAGE;
	public static String KEY_MAP_TYP;

	/* navi */
	public static String KEY_NAVI_SOUND_TYP;
	public static String KEY_NAVI_TIME_TO_DESTINATION;
	public static String KEY_NAVI_TIME_LEFT;
	public static String KEY_NAVI_REMAINING_WAY;
	public static String KEY_NAVI_WALKED_WAY;
	public static String KEY_NAVI_SPEED;

	/* server */
	public static String KEY_SERVER_SHORT;
	public static String KEY_SERVER_ROUNDTRIP;

	/* static */
	private static PreferenceUtility manager;
	private static String TAG = PreferenceUtility.class.getSimpleName();

	/* instance */
	private SharedPreferences sh;

	/**
	 * construct a new Preference Manager
	 * 
	 * @param context
	 */
	private PreferenceUtility(Context context) {
		sh = PreferenceManager.getDefaultSharedPreferences(context);

		DEFAULT_MAP_TYP = context.getResources().getStringArray(
				R.array.options_map_entries)[0];
		DEFAULT_NAVI_SOUND_TYP = context.getResources().getStringArray(
				R.array.options_navigation_audio_output_typ_entries)[0];

		DEFAULT_SERVER_SHORT = context.getResources().getString(
				R.string.options_server_url_short_default);
		DEFAULT_SERVER_ROUNDTRIP = context.getResources().getString(
				R.string.options_server_url_roundrip_default);

		// Keys

		KEY_SOUND = context.getResources().getString(
				R.string.options_navigation_audio_output_all);

		/* look and feel */
		KEY_POI_SOUND_TITLE = context.getResources().getString(
				R.string.options_poi_speech_output);
		KEY_POI_SOUND_TEXT = context.getResources().getString(
				R.string.options_poi_text_output);
		KEY_POI_IMAGE = context.getResources().getString(
				R.string.options_poi_image);
		KEY_MAP_TYP = context.getResources()
				.getString(R.string.options_map_typ);

		/* navi */
		KEY_NAVI_SOUND_TYP = context.getResources().getString(
				R.string.options_navigation_audio_output_typ);
		KEY_NAVI_TIME_TO_DESTINATION = context.getResources().getString(
				R.string.options_navigation_text_output_time_to_destination);
		KEY_NAVI_TIME_LEFT = context.getResources().getString(
				R.string.options_navigation_text_output_time_left);
		KEY_NAVI_REMAINING_WAY = context.getResources().getString(
				R.string.options_navigation_text_output_remaining_way);
		KEY_NAVI_WALKED_WAY = context.getResources().getString(
				R.string.options_navigation_text_output_walked_way);
		KEY_NAVI_SPEED = context.getResources().getString(
				R.string.options_navigation_text_output_speed);

		/* server */
		KEY_SERVER_SHORT = context.getResources().getString(
				R.string.options_server_url);
		KEY_SERVER_ROUNDTRIP = context.getResources().getString(
				R.string.options_server_url_roundrip);
	}

	/**
	 * initialize a new PreferenceUtility
	 * 
	 * @param context
	 *            needed arguments
	 */
	public static void initialize(Context context) {
		Log.d(TAG, "Context is " + (context != null));
		manager = new PreferenceUtility(context);
	}

	/**
	 * gives the Preference Utility back
	 * 
	 * @param context
	 *            needed arguments
	 * 
	 * @return the Preference Utility
	 */
	public static PreferenceUtility getInstance(Context context) {
		Log.d(TAG, "PositionManager.getInstance()");
		if (manager == null) {
			PreferenceUtility.initialize(context);
			return manager;
		}
		return manager;
	}

	/**
	 * gives the Preference Utility back
	 * 
	 * @return the Preference Utility or null
	 */
	public static PreferenceUtility getInstance() {
		Log.d(TAG, "PositionManager.getInstance()");
		if (manager == null) {
			Log.e(TAG, "PositionManager not initialized");
			return null;
		}
		return manager;
	}

	/**
	 * Register a new PReferenceChangeListener
	 * 
	 * @param listener
	 *            the new Listener
	 */
	public void registerOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		sh.registerOnSharedPreferenceChangeListener(listener);
	}

	/* ------------------------------ General ------------------------ */
	/**
	 * Gives the State of general sound back
	 * 
	 * @return true if sound is enable
	 */
	public boolean isSoundOn() {
		return sh.getBoolean(KEY_SOUND, DEFAULT_SOUND);
	}

	/* ------------------------------ look and feel ------------------------ */

	/**
	 * Gives the current MapStyle back.
	 * 
	 * @return the current MapStyle back.
	 */
	public String getMapStyle() {
		return sh.getString(KEY_MAP_TYP, DEFAULT_MAP_TYP);
	}

	/**
	 * Gives the State of poi title sound back
	 * 
	 * @return true if sound is enable
	 */
	public boolean isPOITitleSoundOn() {
		return sh.getBoolean(KEY_POI_SOUND_TITLE, DEFAULT_SOUND_TITLE);
	}

	/**
	 * Gives the State of poi text sound back
	 * 
	 * @return true if sound is enable
	 */
	public boolean isPOITextSoundOn() {
		return sh.getBoolean(KEY_POI_SOUND_TEXT, DEFAULT_SOUND_POI_OUTPUT);
	}

	/**
	 * Gives back whether the user wants images
	 * 
	 * @return true if image is enable
	 */
	public boolean isPOIImageOn() {
		return sh.getBoolean(KEY_POI_IMAGE, DEFAULT_SOUND_POI_IMAGE_OUTPUT);
	}

	/* ------------------------------ Navigation ------------------------ */

	/**
	 * Gives the State of navigation sound back
	 * 
	 * @return text to speech or stereo sound
	 */
	public String getNaviSoundTyp() {
		return sh.getString(KEY_NAVI_SOUND_TYP, DEFAULT_NAVI_SOUND_TYP);
	}

	/**
	 * Gives the State of Time To Destination text field back
	 * 
	 * @return true if Time To Destination text field is enable
	 */
	public boolean isTimeToDestination() {
		return sh.getBoolean(KEY_NAVI_TIME_TO_DESTINATION,
				DEFAULT_NAVI_TIME_TO_DESTINATION);
	}

	/**
	 * Gives the State of Time Left text field back
	 * 
	 * @return true if Time Left text field is enable
	 */
	public boolean isTimeLeft() {
		return sh.getBoolean(KEY_NAVI_TIME_LEFT, DEFAULT_NAVI_TIME_LEFT);
	}

	/**
	 * Gives the State of Time Remaining text field back
	 * 
	 * @return true if Time Remaining text field is enable
	 */
	public boolean isTimeRemaining() {
		return sh
				.getBoolean(KEY_NAVI_REMAINING_WAY, DEFAULT_NAVI_REMAINING_WAY);
	}

	/**
	 * Gives the State of Walked Way text field back
	 * 
	 * @return true if Walked Way text field is enable
	 */
	public boolean isWalkedWay() {
		return sh.getBoolean(KEY_NAVI_WALKED_WAY, DEFAULT_NAVI_WALKED_WAY);
	}

	/**
	 * Gives the State of Speed text field back
	 * 
	 * @return true if Speed text field is enable
	 */
	public boolean isSpeed() {
		return sh.getBoolean(KEY_NAVI_SPEED, DEFAULT_NAVI_SPEED);
	}

	/* ------------------------------ Walkaround Server ------------------------ */

	/**
	 * Gives the url of the Shortest Path server back
	 * 
	 * @return the url of Shortest Path the Server
	 */
	public String getShortestPathServerUrl() {
		return sh.getString(KEY_SERVER_SHORT, DEFAULT_SERVER_SHORT);
	}

	/**
	 * Gives the url of the Roundtrip Path server back
	 * 
	 * @return the url of the Roundtrip Server
	 */
	public String getRoundtripPathServerUrl() {
		return sh.getString(KEY_SERVER_ROUNDTRIP, DEFAULT_SERVER_ROUNDTRIP);
	}

}
