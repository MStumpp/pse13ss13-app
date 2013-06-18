package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import android.speech.tts.TextToSpeech;

public final class TextToSpeechUtility {

	private static TextToSpeechUtility ttsu;

	private TextToSpeechUtility() {

	}

	public static TextToSpeechUtility getInstance() {
		if (ttsu == null) {
			ttsu = new TextToSpeechUtility();
		}
		return ttsu;
	}
}
