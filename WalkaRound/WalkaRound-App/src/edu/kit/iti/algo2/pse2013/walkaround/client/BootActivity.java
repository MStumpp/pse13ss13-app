package edu.kit.iti.algo2.pse2013.walkaround.client;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.widget.ProgressBar;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.FavoriteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.HeadUpController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.OverlayController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.POIMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.SearchMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoriteManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteProcessing;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.CurrentMapStyleModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.PreferenceUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TextToSpeechUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TileUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

public class BootActivity extends Activity {
	protected static final int TOTALSTEPS = 1000;
	public static Coordinate defaultCoordinate = new Coordinate(49.0145, 8.419);
	public static String TAG = BootActivity.class.getSimpleName();

	protected boolean mbActive;
	protected ProgressBar mProgressBar;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.gc();
		setContentView(R.layout.progress_bar);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

		PreferenceManager.setDefaultValues(getApplicationContext(),
				R.xml.options, true);
		PreferenceUtility.initialize(getApplicationContext());
		final Thread timerThread = new BootHelper();
		timerThread.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void updateProgress(final int timePassed) {
		if (null != mProgressBar) {
			// Ignore rounding error here
			final int progress = mProgressBar.getMax() * timePassed
					/ TOTALSTEPS;
			mProgressBar.setProgress(progress);
		}
	}

	public void onContinue() {
		Intent intent = new Intent(this, MapView.class);
		this.startActivity(intent);
	}

	public void alert() {

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Unknown Error");
		alertDialog.setMessage("Close?");
		alertDialog.setButton(1, "OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		alertDialog.setIcon(R.drawable.large_logo);
		alertDialog.show();
	}

	private class BootHelper extends Thread implements TileListener {

		private int tiles = 0;
		private int progress = 0;
		private int stepSize;

		@Override
		public void run() {
			mbActive = true;
			try {

				// Controller initialisierung

				FavoriteMenuController.getInstance();
				OverlayController.getInstance();
				POIMenuController.getInstance();
				RouteController.getInstance();
				SearchMenuController.getInstance();
				CurrentMapStyleModel.getInstance();
				String mapStyle = PreferenceUtility.getInstance().getMapStyle();
				CurrentMapStyleModel.getInstance().setCurrentMapStyle(mapStyle);
				// 10 %
				progress = 100;
				updateProgress(progress);

				// Model initialisierung

				FavoriteManager.initialize(getApplicationContext());
				POIManager.initialize(getApplicationContext());
				RouteProcessing.getInstance();
				Looper.prepare();
				PositionManager.initialize(getApplicationContext());

				Log.d("debugFu", " "
						+ (PreferenceUtility.getInstance().isSoundOn() == true));
				TextToSpeechUtility.initialize(getApplicationContext(),
						PreferenceUtility.getInstance().isSoundOn());
				PreferenceUtility.getInstance()
						.registerOnSharedPreferenceChangeListener(
								TextToSpeechUtility.getInstance());

				while (!TextToSpeechUtility.getInstance().isReady()) {
					Log.d(TAG, "TextToSpeech ist noch nicht ready");
					sleep(50);
				}

				// 20%
				progress = 200;
				updateProgress(progress);

				System.gc();

				updateProgress(progress);

				/*
				String fileString = File.separatorChar + "walkaround"
						+ File.separatorChar + "geometryData.pbf";
				GeometryDataIO geometryDataIO;
				try {
					geometryDataIO = GeometryDataIO.load(new File(Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ fileString));
					GeometryProcessor.init(geometryDataIO);
				} catch (IOException e) {
					Log.e(TAG, "geometry konnte nicht initialisiert werden.");
				}*/

				// 35%
				progress = 350;
				updateProgress(progress);

				// TileFetcher
				float lod = CurrentMapStyleModel.getInstance().getCurrentMapStyle().getDefaultLevelOfDetail();
				Display display = getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);

				Location l = PositionManager.getInstance().getLastKnownPosition();
				BoundingBox coorBox;

				if (l != null) {
					coorBox = new BoundingBox(new Coordinate(l.getLatitude(), l.getLongitude()), size, lod);
				} else {
					coorBox = new BoundingBox(defaultCoordinate, size, lod);
				}

				TileFetcher tileFetcher = new TileFetcher();

				// 50 %
				progress = 500;
				updateProgress(progress);

				// TODO Offset löschen
				int[] amountTop = TileUtility.getXYTileIndex(
						coorBox.getTopLeft(), (int) lod);
				amountTop[0]--;
				amountTop[1]--;

				int[] amountBottom = TileUtility.getXYTileIndex(
						coorBox.getBottomRight(), (int) lod);

				amountBottom[0]++;
				amountBottom[1]++;

				int amount = (amountBottom[0] - amountTop[0])
						* (amountBottom[1] - amountTop[1]);

				stepSize = (int) (400 / amount);

				tileFetcher.requestTiles((int) lod, amountTop[0], amountTop[1],
						amountBottom[0], amountBottom[1], this);

				updateProgress(progress);
				while ((amount - 4) > tiles) {
					Log.d(TAG, "Tile Fetcher Schleife: " + amount + " > "
							+ tiles);
					updateProgress(progress);
					sleep(50);
				}

				MapController.initialize(tileFetcher, coorBox, coorBox.getCenter());
				progress += 50;
				updateProgress(progress);
				HeadUpController.initializes();

				progress = 1000;
				updateProgress(progress);
				Log.d(TAG, "alles geladen!!");
				if (TextToSpeechUtility.getInstance().speak("Willkommen bei !")) {
					TextToSpeechUtility.getInstance().speak("WalkaRound!",
							Locale.ENGLISH);
				} else {
					MediaPlayer mp = MediaPlayer.create(getBaseContext(),
							R.raw.hangout_dingtone);
					mp.setVolume(100, 100);
					mp.start();
					// mp.pause();
				}

			} catch (InterruptedException e) {
			} finally {
				onContinue();
			}
		}

		@Override
		public void receiveTile(Bitmap tile, int x, int y, int levelOfDetail) {
			progress += stepSize;
			tiles++;
		}

	}
}