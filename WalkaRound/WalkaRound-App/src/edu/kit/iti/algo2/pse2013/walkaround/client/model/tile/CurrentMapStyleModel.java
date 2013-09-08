package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

/**
 * Diese Klasse hält den derzeitigen MapStyle vor
 * 
 * @author Ludwig Biermann
 * @version 1.5
 * 
 */
public class CurrentMapStyleModel {

	private final static MapStyle defaultMapStyle = MapStyle.MAPSTYLE_MAPQUEST;
	private static final String MAPNIK = "Mapnik";
	private static final String WANDERKARTE = "Wanderkarte";
	private static CurrentMapStyleModel currentMapModel;
	private MapStyle style;

	private CurrentMapStyleModel(MapStyle style) {
		this.style = style;
	}

	/**
	 * Gibt das CurrentMapStyleModel zurück. Falls dieses Objekt noch nicht
	 * initialisiert ist, wird Mapnik als Kartenstil verwendet.
	 * 
	 * @return CurrentMapStyleModel Objekt
	 */
	public static CurrentMapStyleModel getInstance() {
		if (currentMapModel == null) {
			return getInstance(defaultMapStyle);
		}
		return currentMapModel;
	}

	/**
	 * Gibt das CurrentMapStyleModel zurück. Falls diesen Objekt noch nicht
	 * initialisiert ist wird ein spezieller Kartenstil als Standart definiert.
	 * 
	 * @param style
	 *            der bei der initialisierung zu verwendener Kartenstil
	 * @return CurrentMapStyleModel Objekt
	 */
	public static CurrentMapStyleModel getInstance(MapStyle style) {
		if (currentMapModel == null) {
			currentMapModel = new CurrentMapStyleModel(style);
		}
		return currentMapModel;
	}

	/**
	 * Gibt den aktuellen Kartenstil zurück
	 * 
	 * @return MapStyle
	 */
	public MapStyle getCurrentMapStyle() {
		return this.style;
	}

	/**
	 * Sets the current Map Style
	 * 
	 * @param mapStyle the new map style
	 */
	public void setCurrentMapStyle(MapStyle mapStyle) {
		this.style = mapStyle;
	}

	/**
	 * Sets the current Map Style by a String
	 * 
	 * @param mapStyle the new map style
	 */
	public void setCurrentMapStyle(String mapStyle) {
		if(mapStyle.equals(MAPNIK)) {
			style = MapStyle.MAPSTYLE_MAPNIK;
		} else if(mapStyle.equals(WANDERKARTE)) {
			style = MapStyle.MAPSTYLE_WANDERKARTE;
		} else {
			style = defaultMapStyle;
		}
	}
}