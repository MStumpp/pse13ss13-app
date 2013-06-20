package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

/**
 * Diese Klasse hält den derzeitigen MapStyle vor
 * 
 * @author Ludwig Biermann
 *
 */
public class CurrentMapStyleModel {

	private final MapStyle defaultMapStyle = MapStyle.MAPSTYLE_MAPNIK;
	private CurrentMapStyleModel currentMapModel;
	private MapStyle style;
	
	private CurrentMapStyleModel(MapStyle style) {
		this.style = style;
	}

	/**
	 * Gibt das CurrentMapStyleModel zurück. Falls dieses Objekt noch nicht initialisiert ist wird Mapnik als 
	 * Kartenstil verwendet.
	 * 
	 * @return CurrentMapStyleModel Objekt
	 */
	public CurrentMapStyleModel getInstance() {
		if(this.currentMapModel == null){
			return getInstance(this.defaultMapStyle);
		}
		return this.currentMapModel;
	}
	
	/**
	 * Gibt das CurrentMapStyleModel zurück. Falls diesen Objekt noch nicht initialisiert ist wird ein spezieller Kartenstil als Standart definiert.
	 * 
	 * @param style der bei der initialisierung zu verwendener Kartenstil
	 * @return CurrentMapStyleModel Objekt
	 */
	public CurrentMapStyleModel getInstance(MapStyle style) {
		if(this.currentMapModel == null){
			this.currentMapModel = new CurrentMapStyleModel(style);
		}
		return this.currentMapModel;
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
