package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import java.util.Locale;

import android.content.Context;
import android.location.Location;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

/**
 * This class provides text-to-speech.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public final class TextToSpeechUtility implements OnInitListener {

	private static String TAG_TTSUTIL = TextToSpeechUtility.class
			.getSimpleName();

	private static TextToSpeechUtility ttsUtilInstance;

	private static TextToSpeech tts;
	private static boolean isReady;
	public static void initialize(Context context) {
		Log.d(TAG_TTSUTIL, "initialize(Context)");
		ttsUtilInstance = new TextToSpeechUtility(context);
	}

	private TextToSpeechUtility(Context context) {
		tts = new TextToSpeech(context, this);
	}

	/**
	 * Speaks a String
	 * 
	 * @param text
	 */
	public void speak(String text) {
		if(isReady){
			tts.speak(text, TextToSpeech.QUEUE_ADD, null);
		} else {
			Log.e(TAG_TTSUTIL, "TextToSpeech is not ready");
		}
	}
	/**
	 * Speaks a String
	 * 
	 * @param text
	 */
	public void speak(String text, Locale language) {
		if(isReady){
			tts.setLanguage(language);
			tts.speak(text, TextToSpeech.QUEUE_ADD, null);
			tts.setLanguage(Locale.getDefault());
		} else {
			Log.e(TAG_TTSUTIL, "TextToSpeech is not ready");
		}
	}

	public static TextToSpeechUtility getInstance() {
		if (ttsUtilInstance == null) {
			return null;
		}
		return ttsUtilInstance;
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			isReady = true;
		} else if (status == TextToSpeech.ERROR) {
			isReady = false;
		}
	}

	public boolean isReady(){
		return isReady;
	}
	
	public void shutdown() {
		if (tts != null) {
			tts.shutdown();
		}
	}

}
