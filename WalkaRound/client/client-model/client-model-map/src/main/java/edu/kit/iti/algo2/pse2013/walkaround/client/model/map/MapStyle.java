package edu.kit.iti.algo2.pse2013.walkaround.client.model.map;

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
	public static final MapStyle MAPSTYLE_MAPNIK = new MapStyle(0,18,"Mapnik", "http://tile.osm.org/%3$s/%1$s/%2$s.png");

	/**
	 *  Wanderkarten Kartenstil
	 */
	public static final MapStyle MAPSTYLE_WANDERKARTE = new MapStyle(0,18,"Wanderkarte", "http://www.wanderreitkarte.de/topo/%3$s/%1$s/%2$s.png");

	/**
	 *  Mapquest KArtenstil.
	 */
	public static final MapStyle MAPSTYLE_MAPQUEST = new MapStyle(0,18,"MapQuest", "http://otile1.mqcdn.com/tiles/1.0.0/osm/%3$s/%1$s/%2$s.png");


	private int minLevelOfDetail;
	private int maxLevelOfDetail;
	private String name;
	private String tileURL;

	/**
	 * Konstruiert einen neuen KartenStil.
	 *
	 * @param minLevelOfDetail  Minimales Level of Detail
	 * @param maxLevelOfDetail  Maximales Level of Detail
	 * @param name Name des Kartenstils
	 * @param tileURL Url der Tiles
	 */
	private MapStyle(int minLevelOfDetail, int maxLevelOfDetail, String name, String tileURL){
		if (!tileURL.contains("%1$s") || !tileURL.contains("%2$s") || !tileURL.contains("%3$s")) {
			throw new IllegalArgumentException("A MapStyle-URL has to have a zoomlevel-part and an x- and y-part!");
		}
		if (minLevelOfDetail < 0) {
			throw new IllegalArgumentException("The minimum zoomlevel of a mapstyle has to be greater than zero!");
		}
		this.minLevelOfDetail = minLevelOfDetail;
		this.maxLevelOfDetail = maxLevelOfDetail;
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
}
