package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;

public class PreferenceUtility {

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
	
	
	public boolean isSoundOn(){
		return  sh.getBoolean(context.getString(R.string.options_navigation_audio_output_all), true);
	}
	
	public String getMapStyle(){
<<<<<<< HEAD
		return sh.getString(context.getResources().getString(R.string.options_map_typ), "Mapnik");
	}
	
	public boolean isPOITitleSoundOn(){
		return  sh.getBoolean(context.getString(R.string.options_poi_speech_output), true);
	}

	public boolean isPOITextSoundOn(){
		return  sh.getBoolean(context.getString(R.string.options_poi_text_output), true);
=======
		return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getResources().getString(R.string.options_map_typ), "Mapnik");
>>>>>>> 1f9bd8f0aa779c45e1e6cb8c29acc85b3f057a70
	}
	
	public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener){
		sh.registerOnSharedPreferenceChangeListener(listener);
	}
}
