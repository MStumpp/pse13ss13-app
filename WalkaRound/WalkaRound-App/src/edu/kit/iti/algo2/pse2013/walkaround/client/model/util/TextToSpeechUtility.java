package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

/**
 * This class provides text-to-speech.
 *
 * @author Thomas Kadow
 * @version 1.0
 */
public final class TextToSpeechUtility implements OnInitListener,
		OnSharedPreferenceChangeListener {

	private static String TAG_TTSUTIL = TextToSpeechUtility.class
			.getSimpleName();

	private static TextToSpeechUtility ttsUtilInstance;

	boolean sound;

	private static TextToSpeech tts;
	private static boolean isReady;

	public static void initialize(Context context, boolean sound) {
		Log.d(TAG_TTSUTIL, "initialize(Context)");
		ttsUtilInstance = new TextToSpeechUtility(context, sound);
	}

	private TextToSpeechUtility(Context context, boolean sound) {
		tts = new TextToSpeech(context, this);
		this.sound = sound;
		isReady = true;
	}

	/**
	 * Speaks a String
	 *
	 * @param text
	 */
	public boolean speak(String text) {
		if (isReady && sound) {
			Log.d(TAG_TTSUTIL, "TextToSpeech is speaking");
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
			return true;
		} else {
			Log.e(TAG_TTSUTIL, "TextToSpeech is not ready");
			if (!sound) {
				Log.e(TAG_TTSUTIL, "sound is off");
			}
		}
		return false;
	}

	/**
	 * Speaks a String
	 *
	 * @param text
	 */
	public boolean speak(String text, Locale language) {
		if (isReady && sound) {
			tts.setLanguage(language);
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
			tts.setLanguage(Locale.getDefault());
			return true;
		} else {
			Log.e(TAG_TTSUTIL, "TextToSpeech is not ready");
			if (!sound) {
				Log.e(TAG_TTSUTIL, "sound is off");
			}
		}
		return false;
	}

	public static TextToSpeechUtility getInstance() {
		if (ttsUtilInstance == null) {
			return null;
		}
		return ttsUtilInstance;
	}

	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			isReady = true;
		} else if (status == TextToSpeech.ERROR) {
			isReady = false;
		}
	}

	public boolean isReady() {
		return isReady;
	}

	public void shutdown() {
		if (tts != null) {
			tts.shutdown();
		}
	}

	public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
		
		/*if (key.equals(PreferenceUtility.OPTION_SOUND)) {
			sound = pref.getBoolean(key, true);
		}*/
	}
	
	public boolean stopSpeaking(){
		if(tts != null){
			if(tts.isSpeaking()){
				tts.stop();
				return true;
			}
		}
		return false;
	}

}
