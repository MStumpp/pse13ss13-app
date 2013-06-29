package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

/**
 * This class provides text to speech.
 * 
 * @author Thomas Kadow
 * @version 1.0
 */
public final class TextToSpeechUtility extends Activity implements
		OnInitListener {

	private static TextToSpeechUtility instance;

	private TextToSpeech tts;

	private TextToSpeechUtility() {
		tts = new TextToSpeech(this, this);
	}

	public static TextToSpeechUtility getInstance() {
		if (instance == null) {
			instance = new TextToSpeechUtility();
		}
		return instance;
	}

	public void speak(String text) {
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

	}
}
