package edu.kit.iti.algo2.pse2013.walkaround.shared.server;

import java.util.List;

import android.graphics.Bitmap;

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
    private List<Integer> poiCategories;


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
    public POI(double lat, double lon, int id, String name, String textInfo, String url, List<Integer> poiCategories) {
        this(lat, lon, id, name, textInfo, url, poiCategories, null);
    }

    /**
     * Creates an instance of POI. This is for example useful when parsing from a PBF-File.
     *
     * @param loc A prototype of the POI, which will be created. It contains all attributes, that are inherited by the superclass Location
     * @param textInfo Textinfo of POI
     * @param imageURL an URL to an image of the POI
     * @param poiCategories the POI-Categories, this POI belongs to
     */
    public POI(Location loc, String textInfo, String imageURL, List<Integer> poiCategories) {
    	this(loc.getLatitude(), loc.getLongtitude(), loc.getId(), loc.getName(), textInfo, imageURL, poiCategories);
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
    public POI(double lat, double lon, int id, String name, String textInfo, String url, List<Integer> poiCategories, Address address) {
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
     * @return Bitmap image of the POI (Android requires {@link Bitmap} instead of {@link BufferedImage})
     */
    public Bitmap getImage() {
        return null; // TODO: implement image loading
    }


    /**
     * Returns all categories of POI.
     *
     * @return A list of category-IDs
     */
    public List<Integer> getPOICategories() {
        return poiCategories;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((poiCategories == null) ? 0 : poiCategories.hashCode());
		result = prime * result
				+ ((textInfo == null) ? 0 : textInfo.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof POI)) {
			return false;
		}
		POI other = (POI) obj;
		if (poiCategories == null) {
			if (other.poiCategories != null) {
				return false;
			}
		} else if (!poiCategories.equals(other.poiCategories)) {
			return false;
		}
		if (textInfo == null) {
			if (other.textInfo != null) {
				return false;
			}
		} else if (!textInfo.equals(other.textInfo)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}

}