package edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter;

import java.util.Locale;

import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.PreferenceUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TextToSpeechUtility;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TextToSpeechStarter extends SingleStarter implements OnInitListener {
	private Context context;
	private TextToSpeech tts;

	public TextToSpeechStarter(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		tts = new TextToSpeech(context, this);
	}

	@Override
	public void onInit(int status) {
		tts.setLanguage(Locale.GERMAN);
		tts.setSpeechRate(1.2f);
		TextToSpeechUtility.init(tts);
		PreferenceUtility.getInstance().registerOnSharedPreferenceChangeListener(
			TextToSpeechUtility.getPreferenceListener()
		);
		makeStep(0);
		finish();
	}

	@Override
	public void finish() {
		listener.onStarterFinished();
	}

	@Override
	public int[] getSteps() {
		return new int[]{250};
	}
}
