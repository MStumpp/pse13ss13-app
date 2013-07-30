package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

/**
 * This class provides text-to-speech.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public final class TextToSpeechUtility implements
		OnInitListener {

	private static String TAG_TTSUTIL = TextToSpeechUtility.class.getSimpleName();
	
	private static TextToSpeechUtility ttsUtilInstance;
	
	private static TextToSpeech tts;
	private static boolean isReady;
	
	public static void initialize(Context context) {
		Log.d(TAG_TTSUTIL, "initialize(Context)");
		ttsUtilInstance = new TextToSpeechUtility(context.getApplicationContext());
	}
	
	private TextToSpeechUtility(Context context) {
		tts = new TextToSpeech(context.getApplicationContext(), this);
		
	}
	
	public static TextToSpeechUtility getInstance() {
		if (ttsUtilInstance == null || !isReady) {
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
	
	
	

}
