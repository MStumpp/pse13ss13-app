package edu.kit.iti.algo2.pse2013.walkaround.client.model.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

/**
 * 
 * 
 * @author Thomas Kadow
 *
 */
public final class TextToSpeechUtility {

	/**
	 * 
	 */
	private static TextToSpeechUtility ttsu;
	
	/**
	 * 
	 */
	private TextToSpeech tts;

	/**
	 * 
	 * @param con
	 * @param oil
	 */
	private TextToSpeechUtility(Context con, OnInitListener oil) {
		tts = new TextToSpeech(con, oil);
	}
	
	/**
	 * 
	 * @return
	 */
	public TextToSpeech getTextToSpeech() {
		return tts;
	}

	/**
	 * 
	 * @param con
	 * @param oil
	 * @return
	 */
	public static TextToSpeechUtility getInstance(Context con, OnInitListener oil) {
		if (ttsu == null) {
			ttsu = new TextToSpeechUtility(con, oil);
		}
		return ttsu;
	}
}
