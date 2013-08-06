package edu.kit.iti.algo2.pse2013.walkaround.client;

import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.FavoriteMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.OverlayController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.POIInfoController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.POIMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.RouteController;
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.overlay.SearchMenuController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.FavoritesManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.route.RouteProcessing;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.CurrentMapStyleModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TileUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.view.map.MapView;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.widget.ProgressBar;

public class BootActivity extends Activity {
	protected static final int TIMER_RUNTIME = 10000; // in ms --> 10s
	public static Coordinate defaultCoordinate = new Coordinate(49.0145, 8.419);
	public static String TAG = BootActivity.class.getSimpleName();

	protected boolean mbActive;
	protected ProgressBar mProgressBar;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_bar);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

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
					/ TIMER_RUNTIME;
			mProgressBar.setProgress(progress);
		}
	}

	public void onContinue() {
	    Intent intent = new Intent(this, MapView.class);
	    this.startActivity(intent);
	}

	private class BootHelper extends Thread implements TileListener {

		private int tiles = 0;
		private int progress = 0;
		
		
		@Override
		public void run() {
			mbActive = true;
			try {

				// Controller initialisierung

				//TODO MapController initialisieren ohne MapView und Tile Fetcher übergeben
				// MapController
				FavoriteMenuController.getInstance();
				// HeadUpController.initializes(headUpView);
				OverlayController.getInstance();
				POIInfoController.getInstance();
				POIMenuController.getInstance();
				RouteController.getInstance();
				SearchMenuController.getInstance();

				progress += 200;
				updateProgress(progress);

				// Model initialisierung

				FavoritesManager.initialize(getApplicationContext());
				POIManager.initialize(getApplicationContext());
				RouteProcessing.getInstance();
				Looper.prepare();
				PositionManager.initialize(getApplicationContext());
				
				
				progress += 200;
				updateProgress(progress);

				// TileFetcher
				float lod = CurrentMapStyleModel.getInstance()
						.getCurrentMapStyle().getDefaultLevelOfDetail();
				Display display = getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);

				Location l = PositionManager.getInstance()
						.getLastKnownPosition();
				BoundingBox coorBox;

				if (l != null) {
					coorBox = new BoundingBox(new Coordinate(l.getLatitude(),
							l.getLongitude()), size, lod);
				} else {
					coorBox = new BoundingBox(defaultCoordinate, size, lod);
				}

				TileFetcher tileFetcher = new TileFetcher();
				int[] amountTop = TileUtility.getXYTileIndex(coorBox.getTopLeft(),(int) lod);
				int[] amountBottom = TileUtility.getXYTileIndex(coorBox.getBottomRight(),(int) lod);
				
				int amount = (amountBottom[0] - amountTop[0]) * (amountBottom[1] - amountTop[1]);
				
				tileFetcher.requestTiles((int) lod, amountTop[0], amountTop[1], amountBottom[0], amountBottom[1], this);

				updateProgress(progress);
				while (amount>tiles) {
					Log.d(TAG, "Tile Fetcher Schleife: " + amount + " > " + tiles);
					updateProgress(progress);
					sleep(50);
				}
				Log.d(TAG, "alles geladen!!");

			} catch (InterruptedException e) {
				// do nothing
			} finally {
				onContinue();
			}
		}

		@Override
		public void receiveTile(Bitmap tile, int x, int y, int levelOfDetail) {
			progress+=200;
			tiles++;
		}

	}
}