package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;

/**
 * 
 * Administrate the preferences.
 * sound on/off
 * map typ - Manik; Wanderkarte, MapQuest
 * 
 * 
 * POI - audio
 * Title on/off
 * Text on/off
 * 
 * POI - text
 * images - on/off
 * 
 * Navigation
 * typ of output  Text To Speech; Stero Sounds
 * time to target - on/off
 * time gone - on/off
 * way left - on/off
 * way gone - on/off
 * speed - on/off
 * 
 * WalkaRound Server
 * Shortest-Waay - url
 * Roundtrip - url
 * 
 * @author Ludwig Biermann
 *
 */
public class PreferenceUtility {

	public static boolean DEFAULT_SOUND = true;
	public static boolean DEFAULT_SOUND_TEXT_TO_SPEECH = true;
	public static boolean DEFAULT_SOUND_POI_OUTPUT = true;
	public static boolean DEFAULT_SOUND_POI_IMAGE_OUTPUT = true;
	public static int DEFAULT_MAP_TYP = R.string.options_map_typ_default;

	public static int DEFAULT_NAVI_SOUND_TYP = R.string.options_navigation_audio_output_typ_default;
	public static boolean DEFAULT_NAVI_TIME_TO_DESTINATION = true;
	public static boolean DEFAULT_NAVI_TIME_LEFT = true;
	public static boolean DEFAULT_NAVI_REMAINING_WAY = true;
	public static boolean DEFAULT_NAVI_WALKED_WAY = true;
	public static boolean DEFAULT_NAVI_SPEED = true;
	
	public static int DEFAULT_SERVER_SHORT = R.string.options_server_url_short_default;
	public static int DEFAULT_SERVER_ROUNDTRIP = R.string.options_server_url_roundrip_default;
	
	private static PreferenceUtility manager;
	private static String TAG = PreferenceUtility.class.getSimpleName();

	public static String OPTION_SOUND;
	public static String OPTION_MAP_TYP;

	private SharedPreferences sh;
	private Context context;

	private PreferenceUtility(Context context) {
		sh = PreferenceManager.getDefaultSharedPreferences(context);
		this.context = context;

		OPTION_SOUND = context.getString(R.string.options_navigation_audio_output_all);
		OPTION_MAP_TYP = context.getString(R.string.options_map_typ);
	}

	/**
	 *
	 * @param context
	 */
	public static void initialize(Context context) {
		Log.d(TAG, "Context is " + (context != null));
		manager = new PreferenceUtility(context);
	}

	/**
	 *
	 * @return
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
	 * 
	 * @param listener
	 */
	public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener){
		sh.registerOnSharedPreferenceChangeListener(listener);
	}
	
	/* ------------------------------ General ------------------------ */
	public boolean isSoundOn(){
		return  sh.getBoolean(context.getString(R.string.options_navigation_audio_output_all), DEFAULT_SOUND);
	}
	
	/* ------------------------------ look and feel ------------------------ */

	public String getMapStyle(){
		return sh.getString(context.getResources().getString(R.string.options_map_typ), context.getString(DEFAULT_MAP_TYP));
	}

	public boolean isPOITitleSoundOn(){
		return  sh.getBoolean(context.getString(R.string.options_poi_speech_output), DEFAULT_SOUND_TEXT_TO_SPEECH);
	}

	public boolean isPOITextSoundOn(){
		return  sh.getBoolean(context.getString(R.string.options_poi_text_output), DEFAULT_SOUND_POI_OUTPUT);
	}

	public boolean isPOIImageSoundOn(){
		return  sh.getBoolean(context.getString(R.string.options_poi_image), DEFAULT_SOUND_POI_IMAGE_OUTPUT);
	}

	/* ------------------------------ Navigation ------------------------ */

	public String getNaviSoundTyp(){
		return sh.getString(context.getResources().getString(R.string.options_navigation_audio_output_typ), context.getString(DEFAULT_NAVI_SOUND_TYP));
	}

	public boolean isTimeToDestination(){
		return  sh.getBoolean(context.getString(R.string.options_navigation_text_output_time_to_destination), DEFAULT_NAVI_TIME_TO_DESTINATION);
	}

	public boolean isTimeLeft(){
		return  sh.getBoolean(context.getString(R.string.options_navigation_text_output_time_left), DEFAULT_NAVI_TIME_LEFT);
	}

	public boolean isTimeRemaining(){
		return  sh.getBoolean(context.getString(R.string.options_navigation_text_output_remaining_way), DEFAULT_NAVI_REMAINING_WAY);
	}

	public boolean isWalkedWay(){
		return  sh.getBoolean(context.getString(R.string.options_navigation_text_output_walked_way), DEFAULT_NAVI_WALKED_WAY);
	}

	public boolean isSpeed(){
		return  sh.getBoolean(context.getString(R.string.options_navigation_text_output_speed), DEFAULT_NAVI_SPEED);
	}
	
	/* ------------------------------ Walkaround Server  ------------------------ */
	public String getShortestPathServerUrl(){
		return  sh.getString(context.getString(R.string.options_server_url), context.getString(DEFAULT_SERVER_SHORT));
	}

	public String getRoundtripPathServerUrl(){
		return  sh.getString(context.getString(R.string.options_server_url_roundrip), context.getString(DEFAULT_SERVER_ROUNDTRIP));
	}
	
}
