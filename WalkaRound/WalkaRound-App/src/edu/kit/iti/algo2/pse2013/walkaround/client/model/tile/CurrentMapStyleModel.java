package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

/**
 * Diese Klasse hält den derzeitigen MapStyle vor
 *
 * @author Ludwig Biermann
 *
 */
public class CurrentMapStyleModel {

	private final static MapStyle defaultMapStyle = MapStyle.MAPSTYLE_MAPNIK;
	private static CurrentMapStyleModel currentMapModel;
	private MapStyle style;

	private CurrentMapStyleModel(MapStyle style) {
		this.style = style;
	}

	/**
	 * Gibt das CurrentMapStyleModel zurück. Falls dieses Objekt noch nicht initialisiert ist, wird Mapnik als 
	 * Kartenstil verwendet.
	 *
	 * @return CurrentMapStyleModel Objekt
	 */
	public static CurrentMapStyleModel getInstance() {
		if(currentMapModel == null){
			return getInstance(defaultMapStyle);
		}
		return currentMapModel;
	}

	/**
	 * Gibt das CurrentMapStyleModel zurück. Falls diesen Objekt noch nicht initialisiert ist wird ein spezieller Kartenstil als Standart definiert.
	 *
	 * @param style der bei der initialisierung zu verwendener Kartenstil
	 * @return CurrentMapStyleModel Objekt
	 */
	public static CurrentMapStyleModel getInstance(MapStyle style) {
		if(currentMapModel == null){
			currentMapModel = new CurrentMapStyleModel(style);
		}
		return currentMapModel;
	}

	/**
	 * Gibt den aktuellen Kartenstil zurück
	 * @return MapStyle
	 */
	public MapStyle getCurrentMapStyle() {
		return this.style;
	}

	/**
	 * Setzt einen neuen Kartenstil.
	 * @param style der neue KArtenstil.
	 */
	public void setCurrentMapStyle(MapStyle style) {
		this.style = style;
	}
}