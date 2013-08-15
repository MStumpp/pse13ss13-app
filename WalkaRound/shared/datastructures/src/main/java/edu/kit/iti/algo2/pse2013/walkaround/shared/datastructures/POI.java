package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * This class represents a POI.
 *
 * @author Matthias Stumpp
 * @version 1.0
 */
public class POI extends Location implements Geometrizable {

	/**
	 * text info of POI.
	 */
	private String textInfo;

	/**
	 * url of image of POI.
	 */
	private URL url;

	/**
	 * poi categories of POI.
	 */
	private int[] poiCategories;

	/**
	 * Creates an instance of POI.
	 *
	 * @param lat
	 *            Latitude of POI.
	 * @param lon
	 *            Longitude of POI.
	 * @param name
	 *            Name of POI.
	 * @param textInfo
	 *            Text info of POI.
	 * @param url
	 *            URL of an image of POI.
	 * @param categories
	 *            Categories of POI.
	 */
	public POI(double lat, double lon, String name, String textInfo, URL url, int[] categories) {
		this(lat, lon, name, textInfo, url, categories, null);
	}

	/**
	 * Creates an instance of POI. This is for example useful when parsing from
	 * a PBF-File.
	 *
	 * @param loc
	 *            A prototype of the POI, which will be created. It contains all
	 *            attributes, that are inherited by the superclass Location
	 * @param textInfo
	 *            Textinfo of POI
	 * @param url
	 *            an URL to an image of the POI
	 * @param poiCategories
	 *            the POI-Categories, this POI belongs to
	 */
	public POI(Location loc, String textInfo, URL url, int[] poiCategories) {
		this(loc.getLatitude(), loc.getLongitude(), loc.getName(), textInfo, url, poiCategories);
	}

	/**
	 * Creates an instance of POI.
	 *
	 * @param lat
	 *            Latitude of POI.
	 * @param lon
	 *            Longitude of POI.
	 * @param name
	 *            Name of POI.
	 * @param textInfo
	 *            Text info of POI.
	 * @param url
	 *            URL of an image of POI.
	 * @param poiCategories
	 *            Categories of POI.
	 * @param address
	 *            Address of POI.
	 */
	public POI(double lat, double lon, String name, String textInfo, URL url, int[] poiCategories, Address address) {
		super(lat, lon, name, address);
		this.textInfo = textInfo;
		this.url = url;
		this.poiCategories = poiCategories;
	}

	/**
	 * Sets the textual information of this POI.
	 *
	 * @param text
	 *            text to set
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
	 * Sets the url of an image of this POI.
	 *
	 * @param url String
	 */
	public void setURL(URL url) {
		this.url = url;
	}

	/**
	 * Returns an url of the image for this POI.
	 *
	 * @return URL
	 */
	public URL getURL() {
		return url;
	}

	/**
	 * Returns all categories of POI.
	 *
	 * @return A list of category-IDs
	 */
	public int[] getPOICategories() {
		return poiCategories;
	}

	public String toString() {
		String result = "POI:\n\t"
				+ "\n\tLocation: " + super.toString()
				+ "\n\tTextInfo: " + getTextInfo()
				+ "\n\tURL: " + getURL().toExternalForm()
				+ "\n\tPOI-Categories: ";
		for (int i : getPOICategories()) {
			result += " " + i;
		}
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(poiCategories);
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
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof POI)) {
			return false;
		}
		POI other = (POI) obj;
		if (!Arrays.equals(poiCategories, other.poiCategories)) {
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

	@Override
	public POI clone() {
    	URL newURL;
		try {
			newURL = new URL(getURL().toExternalForm());
		} catch (MalformedURLException e) {
			newURL = null;
		}
		return new POI(super.clone(), new String(getTextInfo()), newURL, Arrays.copyOf(getPOICategories(), getPOICategories().length));
	}


    // Geometrizable Interface

    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable#valueForDimension()
     */
    public int numberDimensions() {
        return 2;
    }


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable#valueForDimension(int)
     */
    public double valueForDimension(int dim) {

        if (dim < 0 || dim > numberDimensions()-1)
            throw new IllegalArgumentException("dim out of range");

        if (dim == 0)
            return getLatitude();
        else
            return getLongitude();
    }


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable#numberNodes()
     */
    public int numberNodes() {
        return 1;
    }


    /* (non-Javadoc)
     * @see edu.kit.iti.algo2.pse2013.walkaround.shared.geometry.Geometrizable#getNode(int nodeNumber)
     */
    public Geometrizable getNode(int nodeNumber) {

        if (nodeNumber < 0 || (nodeNumber > numberNodes()-1))
            throw new IllegalArgumentException("node number out of range");

        return this;
    }


    @Override
    public int compareTo(Object o) {
        if (this.getId() > ((POI)o).getId()) {
            return 1;
        } else if (this.getId() < ((POI)o).getId()) {
            return -1;
        }
        return 0;
    }

}