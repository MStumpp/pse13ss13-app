package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.util.Locale;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.speech.tts.TextToSpeech;
import android.util.Log;

/**
 * This class provides text-to-speech.
 *
 * @author Thomas Kadow
 * @version 1.0
 */
public final class TextToSpeechUtility implements OnSharedPreferenceChangeListener {

	private static String TAG_TTSUTIL = TextToSpeechUtility.class.getSimpleName();

	private static boolean sound;

	private static TextToSpeech tts;

	private static TextToSpeechUtility preferenceListener = new TextToSpeechUtility();

	public static void init(TextToSpeech tts) {
		TextToSpeechUtility.tts = tts;
		TextToSpeechUtility.sound = PreferenceUtility.getInstance().isSoundOn();
	}

	public static TextToSpeechUtility getPreferenceListener() {
		return preferenceListener;
	}

	private TextToSpeechUtility() {}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
		if (key.equals(PreferenceUtility.KEY_SOUND)) {
			sound = PreferenceUtility.getInstance().isSoundOn();
		}
	}

	/**
	 * Speaks a String
	 *
	 * @param text
	 * @return true, if the device has started speaking
	 */
	public static boolean speak(String text) {
		if (sound) {
			Log.d(TAG_TTSUTIL, "TextToSpeech is speaking");
			return tts.speak(text, TextToSpeech.QUEUE_ADD, null) == TextToSpeech.SUCCESS;
		}
		Log.e(TAG_TTSUTIL, "sound is off");
		return false;
	}


	/**
	 * Speaks a String
	 *
	 * @param text
	 * @param language
	 * @return
	 */
	public static boolean speak(String text, Locale language) {
		if (sound) {
			tts.setLanguage(language);
			tts.speak(text, TextToSpeech.QUEUE_ADD, null);
			tts.setLanguage(Locale.getDefault());
			return true;
		}
		Log.e(TAG_TTSUTIL, "sound is off");
		return false;
	}

	/**
	 * @return the current TextToSpeech-object used by the instance of TextToSpeechUtility
	 */
	public static TextToSpeech getTTS() {
		return tts;
	}

	public static void shutdown() {
		if (tts != null) {
			tts.shutdown();
		}
	}

	/**
	 * @return if the device has stopped speaking
	 */
	public static boolean stopSpeaking() {
		if(tts != null && tts.isSpeaking()){
			return tts.stop() == TextToSpeech.SUCCESS;
		}
		return false;
	}
}