package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

/**
 * This class provides text-to-speech.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public final class TextToSpeechUtility implements
		OnInitListener {

	private static String TAG_TTSUTIL = TextToSpeechUtility.class.getSimpleName();
	
	private static TextToSpeechUtility instance;

	private TextToSpeech tts;

	public void initialize(Context context) {
		
	}
	
	private TextToSpeechUtility(Context context) {
		tts = new TextToSpeech(context.getApplicationContext(), this);
		
	}
	
	public static TextToSpeechUtility getInstance() {
		if (instance == null) {
			return null;
		}
		return instance;
	}

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}

}
