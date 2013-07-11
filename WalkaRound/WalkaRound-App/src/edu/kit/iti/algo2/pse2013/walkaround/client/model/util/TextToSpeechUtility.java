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

	private static TextToSpeechUtility instance;

	private TextToSpeech tts;

	public void initialize(Context context) {
		
	}
	
	private TextToSpeechUtility(Context context) {
		
		tts = new TextToSpeech();
		
		
	}

	public static TextToSpeechUtility getInstance() {
		if (instance == null) {
			instance = new TextToSpeechUtility();
		}
		return instance;
	}

}
