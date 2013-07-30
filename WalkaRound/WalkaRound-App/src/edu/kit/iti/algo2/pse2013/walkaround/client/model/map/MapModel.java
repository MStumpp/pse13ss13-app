package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

// Java Library
import java.util.LinkedList;
import java.util.List;

// Android Library
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

// Walkaround Library
import edu.kit.iti.algo2.pse2013.walkaround.client.controller.map.MapController;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.data.POIManager;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.CurrentMapStyleModel;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.MapStyle;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileFetcher;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.tile.TileListener;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.CoordinateUtility;
import edu.kit.iti.algo2.pse2013.walkaround.client.model.util.TileUtility;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.DisplayCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;

/**
 * This class compute the Bitmaps of map and route.
 *
 * @author Ludwig Biermann
 *
 */
public class MapModel implements TileListener {

	/**
	 * Debug Tag
	 */
	private static String TAG_MAP_MODEL = MapModel.class.getSimpleName();

	/**
	 * the Instance of MapModel
	 */
	private static MapModel mapModel;

	public static int defaultBackground = Color.rgb(227, 227, 227);
	private static int defaultBackgroundEmpty = Color.argb(0, 0, 0, 0);
	// private static int defaultBackground = Color.rgb(252, 89, 171); //
	// violetter touch

	/**
	 * the require references
	 */
	private MapController mapController;
	private TileFetcher tileFetcher;

	/**
	 * current Level of Detail
	 */
	private float currentLevelOfDetail;

	/**
	 * the size of the current display
	 */
	private Point pxSize;

	/**
	 * the required Coordinates
	 */
	private Coordinate center;

	/**
	 * The Bitmap
	 */
	private Bitmap map;
	private Bitmap routeOverlayBitmap;
	Bitmap empty = Bitmap.createBitmap(new int[]{0x00000000}, 1, 1, Bitmap.Config.ARGB_8888);

	private int strokeWidth = 8;

	/**
	 * The Tile values
	 */
	private DisplayCoordinate mapOffset;
	private float currentTileWidth;

	private int xAmount;
	private int yAmount;

	private List<POI> poiList = new LinkedList<POI>();;

	private int[] xy = new int[2];

	/*
	 * -----------------Initialization-----------------
	 */

	/**
	 * initialize MapModel
	 *
	 * @param c
	 *            the Coordinate of the mid
	 * @param mapController
	 *            required reference of MapController
	 * @param size
	 *            display size
	 * @return instance of mapModel
	 */
	public static MapModel initialize(Coordinate c, MapController mapController, Point size) {
		if (mapModel == null) {
			mapModel = new MapModel(c, mapController, size);
		}
		return mapModel;
	}

	/**
	 * Gives back a Instance of MapView
	 *
	 * @return null if MapModel isn't initialize
	 */
	public static MapModel getInstance() {
		if (mapModel == null) {
			Log.d(TAG_MAP_MODEL, "Initialize MapView first");
			return null;
		}
		return mapModel;
	}

	/**
	 * Construct a new MapModel
	 *
	 * @param c
	 *            the Coordinate of the mid
	 * @param mapController
	 *            required reference of MapController
	 * @param pxSize
	 *            display size in pixels
	 */
	private MapModel(Coordinate c, MapController mapController, Point pxSize) {
		Log.d(TAG_MAP_MODEL, String.format("Center-Coordinate: %s", c.toString()));
		Log.d(TAG_MAP_MODEL, String.format("Display-Size: %d px * %d px", pxSize.x, pxSize.y));
		Log.d(TAG_MAP_MODEL, "Start initialization of MapModel...");

		this.mapController = mapController;
		this.center = c;
		this.pxSize = pxSize;
		Log.d(TAG_MAP_MODEL, "\t- Set given parameters as attributes ✓");

		this.tileFetcher = new TileFetcher();
		this.tileFetcher.setTileListener(this);
		Log.d(TAG_MAP_MODEL, "\t- Initialize TileFetcher ✓");

		this.currentLevelOfDetail = CurrentMapStyleModel.getInstance().getCurrentMapStyle().getDefaultLevelOfDetail();
		Log.d(TAG_MAP_MODEL, String.format("\t- Set level of detail to default (%f) ✓", currentLevelOfDetail));

		computeAmountOfTiles();
		Log.d(TAG_MAP_MODEL, String.format("\t- Computed Amount of Tiles (x: %d\ty: %d)✓", xAmount, yAmount));

		Log.d(TAG_MAP_MODEL, "\t- Initialize Bitmaps ✓");
		this.map = Bitmap.createBitmap(pxSize.x, pxSize.y, Bitmap.Config.ARGB_8888);
		this.map.prepareToDraw();

		this.empty = Bitmap.createBitmap(pxSize.x, pxSize.y, Bitmap.Config.ARGB_8888);
		this.routeOverlayBitmap = Bitmap.createBitmap(pxSize.x, pxSize.y, Bitmap.Config.ARGB_8888);
		this.routeOverlayBitmap.prepareToDraw();

		this.generateOverlayImages();

	}

	/*
	 * -----------------Getter-----------------
	 */

	/**
	 * Returns the middle of the map
	 * @return the center geo-coordinate
	 */
	public Coordinate getCenter() {
		return center;
	}

	/**
	 * Returns the upperLeft Coordinate
	 * @return the top left geo-oordinate
	 */
	public Coordinate getTopLeft() {
		return new Coordinate(center,
			CoordinateUtility.convertPixelsToDegrees(pxSize.y / 2f, currentLevelOfDetail, CoordinateUtility.DIRECTION_LATITUDE),
			- CoordinateUtility.convertPixelsToDegrees(pxSize.x / 2f, currentLevelOfDetail, CoordinateUtility.DIRECTION_LONGITUDE)
		);
	}

	/**
	 * Compute the bottom right corner as geographical coordinate
	 * @return the bottom right geo-coordinate
	 */
	public Coordinate getBottomRight() {
		return new Coordinate(center,
			- CoordinateUtility.convertPixelsToDegrees(pxSize.y / 2f, currentLevelOfDetail, CoordinateUtility.DIRECTION_LATITUDE),
			CoordinateUtility.convertPixelsToDegrees(pxSize.x / 2f, currentLevelOfDetail, CoordinateUtility.DIRECTION_LONGITUDE)
		);
	}

	/**
	 * compute the amount tiles needed to fill the display
	 */
	private void computeAmountOfTiles() {
		currentTileWidth = CoordinateUtility.computeCurrentTileWidthInPixels(this.currentLevelOfDetail);

		xAmount = (int) Math.ceil(pxSize.x / currentTileWidth) + 1;
		yAmount = (int) Math.ceil(pxSize.y / currentTileWidth) + 1;
	}

	/**
	 * Compute and gives the Tile Offset back
	 *
	 * @return Tile Offset
	 */
	private DisplayCoordinate computeTileOffset() {

		double lonDiff = ((center.getLongitude() + 180) % (360 / Math.pow(2,
				currentLevelOfDetail)));

		double latDiff = ((center.getLatitude() + 90) % (180 / Math.pow(2,
				currentLevelOfDetail)));

		Log.d("latDiff", "" + latDiff);

		float xDiff = CoordinateUtility.convertDegreesToPixels(lonDiff,
				currentLevelOfDetail, CoordinateUtility.DIRECTION_HORIZONTAL);

		float yDiff = CoordinateUtility.convertDegreesToPixels(latDiff,
				currentLevelOfDetail, CoordinateUtility.DIRECTION_VERTICAL);

		// TODO beim nach oben schieben muss yDiff auf dem Display kleiner
		// werden!
		// ich denke hier ist möglicherweise auch ein RundungsFehler!
		yDiff = yDiff - currentTileWidth;
		// yDiff = 256-yDiff;

		Log.d(TAG_MAP_MODEL, String.format("TileOffset: x: %.8fdp y: %.8fdp\n"
				+ "TileOffset: lon: %.8f lat: %.8f\n" + "Center: %s\n"
				+ "LevelOfDetail: %.8f", xDiff, yDiff, lonDiff, latDiff,
				center, currentLevelOfDetail));

		return new DisplayCoordinate(xDiff, -yDiff);
	}

	/**
	 * Gives back the current Level Of Detail
	 *
	 * @return current Level ofDetail
	 */
	public float getCurrentLevelOfDetail() {
		return this.currentLevelOfDetail;
	}

	/**
	 *
	 * @param c
	 * @return
	 */
	public Location getNearbyLocation(Coordinate c) {
		// TODO
		return null;
	}

	/**
	 * update the POI of the Display
	 */
	public void updatePOIofDisplay() {
		if (!POIManager.getInstance().isEmpty()) {

			poiList = POIManager.getInstance().getPOIsWithinRectangle(
					getTopLeft(), getBottomRight(), currentLevelOfDetail);

			Log.d(TAG_MAP_MODEL, "POI Anzahl" + poiList.size());

			LinkedList<DisplayPOI> poi = new LinkedList<DisplayPOI>();

			for (POI value : poiList) {
				poi.add(new DisplayPOI(CoordinateUtility
						.convertDegreesToPixels(value.getLongitude()
								- getTopLeft().getLongitude(),
								currentLevelOfDetail,
								CoordinateUtility.DIRECTION_X),
						CoordinateUtility.convertDegreesToPixels(
								getTopLeft().getLatitude()
										- value.getLatitude(),
								currentLevelOfDetail,
								CoordinateUtility.DIRECTION_Y), value.getId()));
			}

			Log.d(TAG_MAP_MODEL, "POI Display Anzahl" + poi.size());
			if (poi.size() > 0) {
				Log.d(TAG_MAP_MODEL, "POI Display " + poi.get(0).getX());
			}
			mapController.onPOIChange(poi);
		}
	}

	/*
	 * -----------------Setter-----------------
	 */

	/**
	 * set a new MapStyle
	 */
	public void setNewStyle() {
		this.generateOverlayImages();
	}

	/**
	 * Set a new current Level of Detail
	 *
	 */
	public void setCurrentLevelOfDetail(float levelOfDetail) {
		this.currentLevelOfDetail = levelOfDetail;
	}

	/*
	 * -----------------MapModel Calls-----------------
	 */

	/**
	 * shifts the map by a delta
	 *
	 * @param delta
	 *            the shifting delta
	 */
	public void shift(DisplayCoordinate delta) {

		Log.d(TAG_MAP_MODEL, "Center Before: " + this.center.toString());
		Log.d(TAG_MAP_MODEL, "Delta " + delta);

		this.center = new Coordinate(this.center,
				-CoordinateUtility.convertPixelsToDegrees(delta.getY(),
						this.currentLevelOfDetail,
						CoordinateUtility.DIRECTION_Y),
				CoordinateUtility.convertPixelsToDegrees(delta.getX(),
						this.currentLevelOfDetail,
						CoordinateUtility.DIRECTION_X));

		Log.d(TAG_MAP_MODEL, "Center After: " + this.center.toString());

		this.generateOverlayImages();
	}

	/**
	 * Zooms by a Delta to a DisplayCoordinate
	 *
	 * @param delta
	 *            the zoom delta
	 * @param dc
	 *            the target DisplayCoordinate
	 */
	public boolean zoom(float delta, DisplayCoordinate dc) {
		return this.zoom(delta, CoordinateUtility.convertDisplayCoordinateToCoordinate(dc, getTopLeft(), currentLevelOfDetail));
	}

	/**
	 * Zooms by a delta to the middle of the Display
	 *
	 * @param delta
	 *            the zoom delta
	 */
	public boolean zoom(float delta) {
		return this.zoom(delta, getCenter());
	}

	/**
	 * Zooms by a Delta to a Coordinate
	 *
	 * @param delta
	 *            the zoom delta
	 * @param c
	 *            the target Coordinate
	 */
	public boolean zoom(float delta, Coordinate c) {

		Log.d(TAG_MAP_MODEL, "ZOOM by " + delta + " to " + c.toString());

		final MapStyle mapStyle = CurrentMapStyleModel.getInstance().getCurrentMapStyle();
		final float nextLevelOfDetail = this.currentLevelOfDetail + delta;

		if (nextLevelOfDetail > mapStyle.getMaxLevelOfDetail() || nextLevelOfDetail < mapStyle.getMinLevelOfDetail()) {
			return false;
		}

		this.currentLevelOfDetail = nextLevelOfDetail;

		final double deltaX = c.getLongitude() - this.center.getLongitude();
		double deltaY = this.center.getLatitude() - c.getLatitude();

		double y = center.getLatitude() + deltaY;
		double x = center.getLongitude() - deltaX;
		this.center = new Coordinate(y, x);

		this.computeAmountOfTiles();
		this.generateOverlayImages();

		return true;
	}

	/**
	 *
	 */
	public void onChangeOfActivePOICategories() {
		// TODO
	}

	/*
	 * -----------------Generate-----------------
	 */

	/**
	 * generate the Image with the right Sizes
	 */
	public boolean generateOverlayImages() {

		/*
		 * if (this.currentLevelOfDetail < this.xZoomBorder &&
		 * this.currentLevelOfDetail < this.yZoomBorder) { Log.d(TAG_MAP_MODEL,
		 * "generiere Bitmap kleiner display "); final int size = ((int)
		 * Math.pow(2, this.currentLevelOfDetail)) DEFAULT_TILE_SIZE;
		 * this.createBitmap(size, size); return true; } else if
		 * (this.currentLevelOfDetail < this.xZoomBorder) { Log.d(TAG_MAP_MODEL,
		 * "generiere Bitmap kleiner x Achse "); final int sizeX = ((int)
		 * Math.pow(2, this.currentLevelOfDetail)) DEFAULT_TILE_SIZE;
		 * this.createBitmap(sizeX, size.y); return true; } else if
		 * (this.currentLevelOfDetail < this.yZoomBorder) { Log.d(TAG_MAP_MODEL,
		 * "generiere Bitmap kleiner y Achse "); final int sizeY = ((int)
		 * Math.pow(2, this.currentLevelOfDetail)) DEFAULT_TILE_SIZE;
		 * this.createBitmap(size.x, sizeY); return true; }
		 */
		Log.d(TAG_MAP_MODEL, "create Bitmap greater than Display " + pxSize.x + " " + pxSize.y);
		this.createBitmap(pxSize.x, pxSize.y);
		return true;
	}

	/**
	 * recycle and creates a new map recycle and creates a new routeOverlay
	 *
	 * @param width
	 *            of the map and routeOverlay
	 * @param height
	 *            of the map and routeOverlay
	 */
	private void createBitmap(int width, int height) {

		Log.d(TAG_MAP_MODEL, "create Map Bitmap");

		synchronized (this.map) {
			mapController.onMapOverlayImageChange(empty);
			// this.map.recycle();
			// System.gc();
			this.map.eraseColor(defaultBackground);
			// this.map = Bitmap.createScaledBitmap(this.map, width, height,
			// false);
			// this.map = Bitmap.createBitmap(width, height,
			// Bitmap.Config.ARGB_8888);
			this.map.prepareToDraw();
		}

		Log.d(TAG_MAP_MODEL, "call drawing");
		this.fetchTiles();
		// if(this.fetchTiles()) {
		// this.drawDisplayCoordinates(this.mapController.getCurrentRouteLines());
		this.updatePOIofDisplay();
		// } else {
		// 	Log.e(TAG_MAP_MODEL, "Couldn't fetch Tiles");
		// }
	}

	/**
	 * asks the tile fetcher to send back the needed tiles
	 *
	 * @return true if it is possible to get the tiles
	 */
	private boolean fetchTiles() {

		mapOff = false;
		xy = TileUtility.getXYTileIndex(getTopLeft(),
				Math.round(currentLevelOfDetail));
		this.mapOffset = this.computeTileOffset();
		mapOff = true;

		Log.d("test", "x: " + this.mapOffset.getX() + " y: " + this.mapOffset.getY());
		Log.d("test", "x: " + TileUtility.getXYTileIndex(getTopLeft(), (int) this.currentLevelOfDetail)[1]);

		final boolean result = tileFetcher.requestTiles(
				Math.round(currentLevelOfDetail), getTopLeft(), xAmount, yAmount);

		Log.d(TAG_MAP_MODEL, "Tiles werden angefordert: " + result + " x " + xAmount + " y " + yAmount);

		return result;
	}

	boolean mapOff = true;

	/*
	 * -----------------Drawn Methods-----------------
	 */

	/**
	 * Draws the Route Overlay between DisplayWaypoints
	 *
	 * @param lines
	 *            a list of Points
	 */
	private void drawDisplayCoordinates(final List<DisplayCoordinate> lines) {
		if (!lines.isEmpty()) {
			Log.d("CANVAS_DRAW_LINE", "Länge" + lines.size());
			for (int a = 0; a < (lines.size() - 1); a++) {
				Log.d("CANVAS_DRAW_LINE", "Von" + a + " nach " + (a + 1));
				if (a + 1 < lines.size() && lines.get(a) != null && lines.get(a + 1) != null) {
					this.drawRouteLine(lines.get(a).getX(), lines.get(a).getY(), lines.get(a + 1).getX(), lines.get(a + 1).getY());
				}
			}
		}
	}

	/**
	 * Draw a Line between two points.
	 *
	 * @param fromX
	 *            from x
	 * @param fromY
	 *            from y
	 * @param toX
	 *            to x
	 * @param toY
	 *            to y
	 */
	private void drawRouteLine(final float fromX, final float fromY,
			final float toX, final float toY) {

		synchronized (routeOverlayBitmap) {
			if (!routeOverlayBitmap.isRecycled()) {
				Canvas canvas = new Canvas(routeOverlayBitmap);

				Paint pinsel = new Paint();
				pinsel.setColor(Color.rgb(64, 64, 255));
				pinsel.setStrokeWidth(this.strokeWidth);

				// if (fromX > 0 || fromY > 0 || toX > 0 || toY > 0) {
				// if (fromX < size.x || fromY < size.y || toX < size.x
				// || toY < size.y) {
				Log.d(TAG_MAP_MODEL, "ZEICHNE!");
				canvas.drawLine(fromX, fromY + 22, toX, toY + 22, pinsel);
				// }
				// }

				mapController.onRouteOverlayImageChange(routeOverlayBitmap);
			}
		}
	}

	@Override
	public void receiveTile(final Bitmap tile, final int x, final int y,
			final int levelOfDetail) {
		Log.d(TAG_MAP_MODEL, "Receive Tile!");

		int tileX = x - xy[0];
		// TODO hmm
		int tileY = (y - xy[1]);

		Log.d(TAG_MAP_MODEL, "Normalise Tile:  x " + tileX + " y " + tileY);

		synchronized (map) {
			if (!map.isRecycled() && mapOff && tile != null) {
				Canvas canvas = new Canvas(map);
				Log.d(TAG_MAP_MODEL, "ZEICHNE! ");
				if (tileY == 0 && tileX == 0) {
					Log.d(TAG_MAP_MODEL,
							"ZEICHNE OFF! "
									+ ((tileX * currentTileWidth) - mapOffset
											.getX())
									+ " : "
									+ ((tileY * currentTileWidth) - mapOffset
											.getY()));
				}
				canvas.drawBitmap(
						Bitmap.createScaledBitmap(tile,
								Math.round(currentTileWidth),
								Math.round(currentTileWidth), false),
						(tileX * currentTileWidth) - mapOffset.getX(),
						(tileY * currentTileWidth) - mapOffset.getY(), null);
			}
		}
		this.mapController.onMapOverlayImageChange(map);

	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public POI getPOIInformationById(int id) {
		for (POI value : this.poiList) {
			if (value.getId() == id) {
				return value;
			}
		}
		return null;
	}

	public void notifyMoveWaypoint(List<DisplayCoordinate> lines) {

		Log.d(TAG_MAP_MODEL, "create Route Bitmap");
		synchronized (this.routeOverlayBitmap) {
			mapController.onRouteOverlayImageChange(empty);
			// this.routeOverlayBitmap.recycle();
			// System.gc();
			this.routeOverlayBitmap.eraseColor(defaultBackgroundEmpty);
			// this.routeOverlayBitmap =
			// Bitmap.createScaledBitmap(this.routeOverlayBitmap, width, height,
			// false);
			// this.routeOverlayBitmap = Bitmap.createBitmap(width, height,
			// Bitmap.Config.ARGB_8888);
			this.routeOverlayBitmap.prepareToDraw();
		}
		this.drawDisplayCoordinates(lines);
	}
}
