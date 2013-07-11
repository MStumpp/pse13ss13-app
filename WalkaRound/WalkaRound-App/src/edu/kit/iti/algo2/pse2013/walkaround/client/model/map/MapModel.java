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
	private final static int DEFAULT_TILE_SIZE = 256;

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
	private Point size;

	/**
	 * the required Coordinates
	 */
	private Coordinate upperLeft;
	private Coordinate mid;

	/**
	 * The Bitmap
	 */
	private Bitmap map;
	private Bitmap routeOverlayBitmap;
	Bitmap empty;

	private int strokeWidth = 8;

	/**
	 * The Tile values
	 */
	private DisplayCoordinate mapOffset;
	private float currentTileWidth;

	private int xAmount;
	private int yAmount;

	private int xZoomBorder;
	private int yZoomBorder;

	List<POI> poiList;
	
	
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
	public static MapModel initialize(Coordinate c,
			MapController mapController, Point size) {
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
		if (mapModel != null) {
			Log.d(TAG_MAP_MODEL, "initialice MapView first");
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
	 * @param size
	 *            display size
	 */
	private MapModel(Coordinate c, MapController mapController, Point size) {
		Log.d(TAG_MAP_MODEL, "Display-Size: " + size.x + " * " + size.y);
		Log.d(TAG_MAP_MODEL, "Map Model will initialice");
		poiList = new LinkedList<POI>();
		this.size = size;
		empty = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		
		Log.d(TAG_MAP_MODEL, "Referenzen will initialice");
		this.mapController = mapController;
		this.tileFetcher = new TileFetcher();
		this.tileFetcher.setTileListener(this);

		Log.d(TAG_MAP_MODEL, "compute important Points");
		this.upperLeft = c;

		Log.d(TAG_MAP_MODEL, "get default Level of Detail");
		this.currentLevelOfDetail = CurrentMapStyleModel.getInstance()
				.getCurrentMapStyle().getDefaultLevelOfDetail();

		Log.d("TAG_MAP_MODEL", "upperleft: " + upperLeft);

		this.computeMid();
		Log.d("TAG_MAP_MODEL", "Mid: " + mid);

		this.upperLeft = new Coordinate(this.upperLeft, -Math.abs(this.mid
				.getLatitude() - this.upperLeft.getLatitude()),
				Math.abs(this.mid.getLongitude()
						- this.upperLeft.getLongitude()));

		Log.d("TAG_MAP_MODEL", "real upperLeft " + upperLeft);

		Log.d(TAG_MAP_MODEL, "compute Amount of Tiles");
		computeAmountsOfTiles();

		Log.d(TAG_MAP_MODEL, "Amount x " + xAmount + " y " + yAmount);

		Log.d(TAG_MAP_MODEL, "compute zoom levels ");
		final MapStyle mapStyle = CurrentMapStyleModel.getInstance()
				.getCurrentMapStyle();

		boolean run = true;

		for (int a = mapStyle.getMinLevelOfDetail(); a <= mapStyle
				.getMaxLevelOfDetail() && run; a++) {
			if (Math.pow(2d, (double) a) >= xAmount) {
				xZoomBorder = a;
				run = false;
			}
		}

		run = true;

		for (int a = mapStyle.getMinLevelOfDetail(); a <= mapStyle
				.getMaxLevelOfDetail() && run; a++) {
			if (Math.pow(2d, (double) a) >= yAmount) {
				yZoomBorder = a;
				run = false;
			}
		}

		Log.d(TAG_MAP_MODEL, "initialice map");

		this.map = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		this.map.prepareToDraw();

		this.routeOverlayBitmap = Bitmap.createBitmap(1, 1,
				Bitmap.Config.ARGB_8888);

		this.generateOverlayImages();

	}

	/*
	 * -----------------Compute-----------------
	 */

	/**
	 * compute the middle of the map
	 */
	private void computeMid() {
		this.mid = CoordinateUtility.convertDisplayCoordinateToCoordinate(
				new DisplayCoordinate(size.x / 2, size.y / 2), upperLeft,
				currentLevelOfDetail);
	}

	/**
	 * compute the middle of the map
	 */
	private Coordinate computeBottomRight() {
		return CoordinateUtility.convertDisplayCoordinateToCoordinate(
				new DisplayCoordinate(size.x / 2, size.y / 2), upperLeft,
				currentLevelOfDetail);
	}

	/**
	 * compute the amount tiles possible to show
	 */
	private void computeAmountsOfTiles() {

		currentTileWidth = CoordinateUtility
				.computeCurrentTileWidthInPixels(this.currentLevelOfDetail);

		xAmount = (int) Math.ceil(size.x / currentTileWidth);
		yAmount = (int) Math.ceil(size.y / currentTileWidth);

		xAmount++;
		yAmount++;
	}

	/**
	 * Compute and gives the Tile Offset back
	 * 
	 * @return Tile Offset
	 */
	private DisplayCoordinate computeTileOffset() {

		float lonDiff = (float) ((upperLeft.getLongitude() + 180) % (360 / Math
				.pow(2, currentLevelOfDetail)));

		float latDiff = (float) ((upperLeft.getLatitude() + 90) % (180 / Math
				.pow(2, currentLevelOfDetail)));

		float xDiff = CoordinateUtility.convertDegreesToPixels(lonDiff,
				currentLevelOfDetail, CoordinateUtility.DIRECTION_HORIZONTAL);

		float yDiff = CoordinateUtility.convertDegreesToPixels(latDiff,
				currentLevelOfDetail, CoordinateUtility.DIRECTION_VERTICAL);

		// TODO beim nach oben schieben muss yDiff auf dem Display kleiner
		// werden!
		// ich denke hier ist möglicherweise auch ein RundungsFehler!
		yDiff = yDiff
				- CoordinateUtility
						.computeCurrentTileWidthInPixels(currentLevelOfDetail);
		// yDiff = 256-yDiff;

		Log.d(TAG_MAP_MODEL, String.format("TileOffset: x: %.8fdp y: %.8fdp\n"
				+ "TileOffset: lon: %.8f lat: %.8f\n" + "UpperLeft: %s\n"
				+ "LevelOfDetail: %.8f", xDiff, yDiff, lonDiff, latDiff,
				upperLeft, currentLevelOfDetail));

		return new DisplayCoordinate(xDiff, yDiff);
	}

	/*
	 * -----------------Getter-----------------
	 */

	/**
	 * gives the upperLeft Coordinate back
	 * 
	 * @return the upper left Coordinate
	 */
	public Coordinate getUpperLeft() {
		return upperLeft;
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

			Coordinate bottomRight = CoordinateUtility
					.convertDisplayCoordinateToCoordinate(
							new DisplayCoordinate(size.x, size.y), upperLeft,
							currentLevelOfDetail);

			poiList = POIManager.getInstance().getPOIsWithinRectangle(
					upperLeft, bottomRight, currentLevelOfDetail);
			
			LinkedList<DisplayPOI> poi = new LinkedList<DisplayPOI>();
			
			for(POI value: poiList){
				poi.add(new DisplayPOI(
						CoordinateUtility.convertDegreesToPixels(value.getLongitude(), currentLevelOfDetail, CoordinateUtility.DIRECTION_X),
						CoordinateUtility.convertDegreesToPixels(value.getLatitude(), currentLevelOfDetail, CoordinateUtility.DIRECTION_Y),
						value.getId()));
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

		Log.d(TAG_MAP_MODEL, "UpperLeft " + this.upperLeft.toString());
		Log.d(TAG_MAP_MODEL, "Delta " + delta);

		this.upperLeft = new Coordinate(this.upperLeft,
				-CoordinateUtility.convertPixelsToDegrees(delta.getY(),
						this.currentLevelOfDetail,
						CoordinateUtility.DIRECTION_Y),
				CoordinateUtility.convertPixelsToDegrees(delta.getX(),
						this.currentLevelOfDetail,
						CoordinateUtility.DIRECTION_X));

		Log.d(TAG_MAP_MODEL, "upperLeft " + this.upperLeft.toString());

		this.generateOverlayImages();
	}

	/**
	 * Zooms by a Delta to a DisplayCoordinate
	 * 
	 * @param delta
	 *            the zoom delta
	 * @param c
	 *            the target DisplayCoordinate
	 */
	public boolean zoom(float delta, DisplayCoordinate c) {
		return this.zoom(delta, CoordinateUtility
				.convertDisplayCoordinateToCoordinate(c, upperLeft,
						currentLevelOfDetail));
	}

	/**
	 * Zooms by a delta to the middle of the Display
	 * 
	 * @param delta
	 *            the zoom delta
	 */
	public boolean zoom(float delta) {
		this.computeMid();
		return this.zoom(delta, mid);
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

		final MapStyle mapStyle = CurrentMapStyleModel.getInstance()
				.getCurrentMapStyle();
		final float nextLevelOfDetail = this.currentLevelOfDetail + delta;

		if (nextLevelOfDetail > mapStyle.getMaxLevelOfDetail()
				|| nextLevelOfDetail < mapStyle.getMinLevelOfDetail()) {
			return false;
		}

		this.currentLevelOfDetail = nextLevelOfDetail;

		final double deltaX = c.getLongitude() - this.upperLeft.getLongitude();
		double deltaY = this.upperLeft.getLatitude() - c.getLatitude();

		double y = upperLeft.getLatitude() + deltaY;
		double x = upperLeft.getLongitude() - deltaX;
		this.upperLeft = new Coordinate(y, x);

		this.computeAmountsOfTiles();
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

		if (this.currentLevelOfDetail < this.xZoomBorder
				&& this.currentLevelOfDetail < this.yZoomBorder) {
			Log.d(TAG_MAP_MODEL, "generiere Bitmap kleiner display ");
			final int size = ((int) Math.pow(2, this.currentLevelOfDetail))
					* DEFAULT_TILE_SIZE;
			this.createBitmap(size, size);
			return true;
		} else if (this.currentLevelOfDetail < this.xZoomBorder) {
			Log.d(TAG_MAP_MODEL, "generiere Bitmap kleiner x Achse ");
			final int sizeX = ((int) Math.pow(2, this.currentLevelOfDetail))
					* DEFAULT_TILE_SIZE;
			this.createBitmap(sizeX, size.y);
			return true;
		} else if (this.currentLevelOfDetail < this.yZoomBorder) {
			Log.d(TAG_MAP_MODEL, "generiere Bitmap kleiner y Achse ");
			final int sizeY = ((int) Math.pow(2, this.currentLevelOfDetail))
					* DEFAULT_TILE_SIZE;
			this.createBitmap(size.x, sizeY);
			return true;
		}
		Log.d(TAG_MAP_MODEL, "create Bitmap greater than Display ");
		this.createBitmap(size.x, size.y);
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
			this.map.recycle();
			System.gc();
			this.map = Bitmap.createBitmap(size.x, size.y,
					Bitmap.Config.ARGB_8888);
			this.map.prepareToDraw();
		}

		Log.d(TAG_MAP_MODEL, "create Route Bitmap");
		synchronized (this.routeOverlayBitmap) {
			mapController.onRouteOverlayImageChange(empty);
			this.routeOverlayBitmap.recycle();
			this.routeOverlayBitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			this.routeOverlayBitmap.prepareToDraw();
		}
		
		Log.d(TAG_MAP_MODEL, "call drawing");
		this.fetchTiles();
		this.drawDisplayCoordinates(this.mapController.getCurrentRouteLines());
		//TODO
		//this.updatePOIofDisplay();
	}

	/**
	 * asks the tile fetcher to send back the needed tiles
	 * 
	 * @return true if it is possible to get the tiles
	 */
	private boolean fetchTiles() {

		this.mapOffset = this.computeTileOffset();

		final boolean result = tileFetcher.requestTiles(
				Math.round(currentLevelOfDetail), upperLeft, xAmount, yAmount);

		Log.d(TAG_MAP_MODEL, "Tiles werden angefordert: " + result + " x "
				+ xAmount + " y " + yAmount);

		return result;
	}

	/*
	 * -----------------Drawn Methods-----------------
	 */

	/**
	 * Draws the Route Overlay between DisplayWaypoints
	 * 
	 * @param lines
	 *            a list of Points
	 */
	public void drawDisplayCoordinates(final List<DisplayCoordinate> lines) {
		if (!lines.isEmpty()) {
			Log.d("CANVAS_DRAW_LINE", "Länge" + lines.size());
			for (int a = 0; a < (lines.size() - 1); a++) {
				Log.d("CANVAS_DRAW_LINE", "Von" + a + " nach " + (a + 1));
				if (a + 1 < lines.size() && lines.get(a) != null
						&& lines.get(a + 1) != null) {
					this.drawRouteLine(lines.get(a).getX(),
							lines.get(a).getY(), lines.get(a + 1).getX(), lines
									.get(a + 1).getY());

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

		if (!routeOverlayBitmap.isRecycled()) {
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
	}

	@Override
	public void receiveTile(final Bitmap tile, final int x, final int y,
			final int levelOfDetail) {
		Log.d(TAG_MAP_MODEL, "Receive Tile!");

		if (!map.isRecycled()) {

			int[] xy = TileUtility.getXYTileIndex(upperLeft,
					Math.round(currentLevelOfDetail));

			int localX = x - xy[0];
			int localY = y - xy[1];
			Log.d(TAG_MAP_MODEL, "Normalise Tile:  x " + localX + " y "
					+ localY);

			synchronized (map) {
				if (!map.isRecycled()) {
					Canvas canvas = new Canvas(map);
					Log.d(TAG_MAP_MODEL, "ZEICHNE!");
					canvas.drawBitmap(tile, (localX * tile.getWidth())
							- mapOffset.getX(), (localY * tile.getHeight())
							- mapOffset.getY(), null);
				}
			}
			this.mapController.onMapOverlayImageChange(map);

		}

	}
}
