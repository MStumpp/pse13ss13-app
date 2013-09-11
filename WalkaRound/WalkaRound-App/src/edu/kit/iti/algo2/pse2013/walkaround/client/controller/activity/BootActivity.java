package edu.kit.iti.algo2.pse2013.walkaround.client.controller.activity;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;
import edu.kit.iti.algo2.pse2013.walkaround.client.R;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter.MapStarter;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter.MultiStarter;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter.NaviStarter;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter.SingleStarter;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter.StepCounter;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter.TextToSpeechStarter;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.PreferenceUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TextToSpeechUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

/**
 * This class boots the WalkaRound app and shows a "Progress Bar"
 *
 * @author Ludwig Biermann
 * @version 6.3
 *
 */
public class BootActivity extends Activity implements StepCounter {
	protected static final int TOTALSTEPS = 1000;
	public static Coordinate defaultCoordinate = new Coordinate(49.0145, 8.419);
	public static String TAG = BootActivity.class.getSimpleName();

	private ProgressBar mProgressBar;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.gc();
		setContentView(R.layout.progress_bar);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		mProgressBar.setMax(TOTALSTEPS);

		PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.options, true);
		PreferenceUtility.initialize(getApplicationContext());

		BootStarter booSt = new BootStarter(this);
		SingleStarter[] starters = {
			new MapStarter(this),
			new NaviStarter(getApplicationContext()),
			new TextToSpeechStarter(getApplicationContext())
		};
		int sum = 0;
		for (SingleStarter siSta : starters) {
			for (int steps : siSta.getSteps()) {
				sum += steps;
			}
			Log.d(TAG, sum + " steps in total");
		}
		mProgressBar.setMax(sum);
		for (SingleStarter siSta : starters) {
			booSt.start(siSta);
		}

		booSt.noMoreStarters();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void makeStep(int stepSize) {
		Log.d(TAG, "make a " + stepSize + "-step");
		mProgressBar.setProgress(mProgressBar.getProgress() + stepSize);
	}

	/**
	 * is called if the progress has finished
	 */
	public void onContinue() {
		Intent intent = new Intent(this, WalkaRound.class);
		this.startActivity(intent);
	}

	/**
	 * shows a "unknown error" error
	 */
	public void alert() {

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Unknown Error");
		alertDialog.setMessage("Close?");
		alertDialog.setButton(1, "OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		alertDialog.setIcon(R.drawable.large_logo);
		alertDialog.show();
	}

	private class BootStarter extends MultiStarter {
		public BootStarter(StepCounter counter) {
			super(counter);
		}

		@Override
		public void actionWhenAllFinished() {
			if (PreferenceUtility.getInstance().isSoundOn()) {
				if (TextToSpeechUtility.speak("Willkommen bei")) {
					TextToSpeechUtility.speak("WalkaRound!", Locale.ENGLISH);
				} else {
					// TODO Klingelton einfügen^^
					// MediaPlayer mp = MediaPlayer.create(getBaseContext(),
					// R.raw.hangout_dingtone);
					// mp.setVolume(100, 100);
					// mp.start();
					// mp.pause();
				}
			}
			onContinue();
		}
	}
}