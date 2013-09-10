package edu.kit.iti.algo2.pse2013.walkaround.client.controller.starter;

import android.app.Activity;
import android.graphics.Point;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.map.BoundingBox;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.sensorinformation.PositionManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.CurrentMapStyleModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.PreferenceUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;

	/**
	 * A Helper Class which boots Walkaround
	 *
	 * @author Ludwig Biermann
	 * @version 2.3
	 *
	 */
	public class MapStarter extends SingleStarter {
		private static final String TAG = MapStarter.class.getSimpleName();
		private Activity mainActivity;

		public MapStarter(Activity mainActivity) {
			this.mainActivity = mainActivity;
		}

		@Override
		public void run() {
			try {
				Thread findPosition = new Thread(new Runnable() {
					@Override
					public void run() {
						Looper.prepare();
						PositionManager.initialize(mainActivity.getApplicationContext());
						makeStep(0);
					}
				});

				findPosition.start();

				CurrentMapStyleModel.getInstance();
				String mapStyle = PreferenceUtility.getInstance().getMapStyle();
				CurrentMapStyleModel.getInstance().setCurrentMapStyle(mapStyle);
				makeStep(1);

//				float lod = CurrentMapStyleModel.getInstance().getCurrentMapStyle().getDefaultLevelOfDetail();
				Display display = mainActivity.getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);

				BoundingBox.initialize(size);
				BoundingBox coorBox = BoundingBox.getInstance();
				makeStep(2);

				findPosition.join();

				Location l = PositionManager.getInstance().getLastKnownPosition();
				if (l != null) {
					coorBox.setCenter(new Coordinate(l.getLatitude(), l.getLongitude()));
				}
				makeStep(3);

//				int[] amountTop = TileUtility.getXYTileIndex(
//						coorBox.getTopLeft(), (int) lod);
//				amountTop[0]--;
//				amountTop[1]--;
//
//				int[] amountBottom = TileUtility.getXYTileIndex(
//						coorBox.getBottomRight(), (int) lod);
//
//				amountBottom[0]++;
//				amountBottom[1]++;
//
//				int amount = (amountBottom[0] - amountTop[0])
//						* (amountBottom[1] - amountTop[1]);
//
//				stepSize = 400 / amount;

				TileFetcher.init(mainActivity);
				makeStep(4);

				//TileFetcher.getInstance().requestTiles((int) lod, amountTop[0], amountTop[1], amountBottom[0], amountBottom[1], this);

				Log.d(TAG, "alles geladen!!");
			} catch (InterruptedException e) {
				Log.e(TAG, "InterruptedException while booting!");
			}
			finish();
		}

//		@Override
//		public void receiveTile(Bitmap tile, int x, int y, int levelOfDetail) {
//			progress += stepSize;
//			tiles++;
//		}

		@Override
		public void finish() {
			listener.onStarterFinished();
		}

		@Override
		public int[] getSteps() {
			return new int[]{
				200, // findPosition
				50, // mapStyleModel
				100, // boundingBox
				50, // setCenter
				50, // TileFetcher
			};
		}
	}