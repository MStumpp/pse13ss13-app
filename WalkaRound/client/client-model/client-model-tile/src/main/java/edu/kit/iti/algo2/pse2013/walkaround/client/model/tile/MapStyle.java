package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

/**
 * Diese Klasse repräsentiert einen Kartenstil von Open Street Map.
 *
 * @author Ludwig Biermann
 *
 */
public class MapStyle {

	/**
	 *  Mapnik Kartenstil.
	 */
	public static MapStyle MAPSTYLE_MAPNIK = new MapStyle(0,18,"Mapnik", "http://tile.osm.org/%3$s/%1$s/%2$s.png", 18);

	/**
	 *  Wanderkarten Kartenstil
	 */
	public static MapStyle MAPSTYLE_WANDERKARTE = new MapStyle(0,18,"Wanderkarte", "http://www.wanderreitkarte.de/topo/%3$s/%1$s/%2$s.png", 18);

	/**
	 *  Mapquest KArtenstil.
	 */
	public static MapStyle MAPSTYLE_MAPQUEST = new MapStyle(0,19,"MapQuest", "http://otile1.mqcdn.com/tiles/1.0.0/map/%3$s/%1$s/%2$s.jpg", 18);


	private int minLevelOfDetail;
	private int maxLevelOfDetail;
	private String name;
	private String tileURL;
	private int defaultLevelOfDetail;

	/**
	 * Konstruiert einen neuen KartenStil.
	 *
	 * @param minLevelOfDetail  Minimales Level of Detail
	 * @param maxLevelOfDetail  Maximales Level of Detail
	 * @param name Name des Kartenstils
	 * @param tileURL Url der Tiles
	 */
	private MapStyle(int minLevelOfDetail, int maxLevelOfDetail, String name, String tileURL, int defaultLevelOfDetail){
		this.minLevelOfDetail = minLevelOfDetail;
		this.maxLevelOfDetail = maxLevelOfDetail;
		this.defaultLevelOfDetail = defaultLevelOfDetail;
		this.name = name;
		this.tileURL = tileURL;
	}

	/**
	 * Gibt den minimal Level of Detail zurück.
	 *
	 * @return Minimales Level of Detail
	 */
	public int getMinLevelOfDetail() {
		return minLevelOfDetail;
	}

	/**
	 *  Gibt den maximalen Level of Detail zurück.
	 *
	 * @return  Maximales Level of Detail
	 */
	public int getMaxLevelOfDetail() {
		return maxLevelOfDetail;
	}

	/**
	 * Gibt den Namen des Kartenstils zurück.
	 *
	 * @return Name des Kartenstils
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gibt die Url der Tiles zurück.
	 *
	 * @return Url der Tiles
	 */
	public String getTileURL() {
		return tileURL;
	}

	/**
	 * Gibt den Standart Level of Detail zur�ck
	 *
	 * @return Standart Level of Detail
	 */
	public int getDefaultLevelOfDetail(){
		return this.defaultLevelOfDetail;
	}
}
