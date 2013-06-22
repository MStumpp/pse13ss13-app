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
	public static MapStyle MAPSTYLE_MAPNIK = new MapStyle(0,0,"Mapnik", "");
	
	/**
	 *  Wanderkarten Kartenstil
	 */
	public static MapStyle MAPSTYLE_WANDERKARTE = new MapStyle(0,0,"Wanderkartek", "");
	
	/**
	 *  Mapquest KArtenstil.
	 */
	public static MapStyle MAPSTYLE_MAPQUEST = new MapStyle(0,0,"MapQuest", "");
	
	
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
