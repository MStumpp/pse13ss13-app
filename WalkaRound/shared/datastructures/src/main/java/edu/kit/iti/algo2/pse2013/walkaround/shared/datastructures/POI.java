package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.awt.*;


/**
 * This class represents a POI.
 * 
 * @author Matthias Stumpp
 * @version 1.0
 */
public class POI extends Location {

    /**
     * text info of POI.
     */
    private String textInfo;


    /**
     * url of image of POI.
     */
    private String url;


    /**
     * poi categories of POI.
     */
    private int[] poiCategories;


    /**
     * Creates an instance of POI.
     *
     * @param lat Latitude of POI.
     * @param lon Longitude of POI.
     * @param id ID of POI.
     * @param name Name of POI.
     * @param textInfo Text info of POI.
     * @param url URL of an image of POI.
     * @param poiCategories Categories of POI.
     */
    public POI(double lat, double lon, int id, String name, String textInfo, String url, int[] poiCategories) {
        this(lat, lon, id, name, textInfo, url, poiCategories, null);
    }


    /**
     * Creates an instance of POI.
     *
     * @param lat Latitude of POI.
     * @param lon Longitude of POI.
     * @param id ID of POI.
     * @param name Name of POI.
     * @param textInfo Text info of POI.
     * @param url URL of an image of POI.
     * @param poiCategories Categories of POI.
     * @param address Address of POI.
     */
    public POI(double lat, double lon, int id, String name, String textInfo, String url, int[] poiCategories, Address address) {
        super(lat, lon, id, name, address);
        this.textInfo = textInfo;
        this.url = url;
        this.poiCategories = poiCategories;
    }

    
    /**
     * Sets the textual information of this POI.
     * 
     * @param text text to set
     */
    public void setTextInfo(String text) {
    	this.textInfo = text;
    }
    
    
    /**
     * Returns text info of POI.
     *
     * @return String.
     */
    public String getTextInfo() {
	    return textInfo;
    }


    /**
     * Returns an url of the image for this POI.
     * 
     * @return String
     */
    public String getURL() {
    	return url;
    }
    
    
    /**
     * Returns an image of POI.
     *
     * @return BufferedImage.
     */
    // TODO: implement image loading
    public Image getImage() {
        return null;
    }


    /**
     * Returns all categories of POI.
     *
     * @return int[].
     */
    public int[] getPOICategories() {
        return poiCategories;
    }

}
