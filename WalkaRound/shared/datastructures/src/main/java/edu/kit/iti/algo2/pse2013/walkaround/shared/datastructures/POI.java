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
public class POI extends Location implements Geometrizable, Categorizable {

	/**
	 * text info of POI.
	 */
	private String textInfo;

	/**
	 * url of image of POI.
	 */
	private URL url;

	private int[] categories;

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
	 */
	public POI(double lat, double lon, String name, String textInfo, URL url, int[] poiCategories) {
		this(lat, lon, name, textInfo, url, poiCategories == null?new int[0]:poiCategories, null);
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
		this(loc.getLatitude(), loc.getLongitude(), loc.getName(), textInfo, url, poiCategories == null?new int[0]:poiCategories);
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
		setCategories(poiCategories == null?new int[0]:poiCategories);
	}
	
	/**
	 * Creates an instance of POI.
	 * @param poi the poi of poi. ^^
	 */
	public POI(POI poi) throws MalformedURLException {
		this(
			poi.getLatitude(),
			poi.getLongitude(), 
			poi.getName() == null ? null : new String(poi.getName()), 
			poi.getTextInfo() == null ? null : new String(poi.getTextInfo()), 
			poi.getURL() == null ? null : new URL(poi.getURL().toExternalForm()),
			poi.getCategories() == null ? null : poi.getCategories(),
			poi.getAddress() == null ? null : poi.getAddress()
		);
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

	@Override
	public String toString() {
		String result = "POI:\n\t"
				+ "\n\tLocation: " + super.toString()
				+ "\n\tTextInfo: " + getTextInfo()
				+ "\n\tURL: " + (getURL() != null ? getURL().toExternalForm() : "")
				+ "\n\tPOI-Categories: ";
		if (getCategories() != null) {
			for (int i : getCategories()) {
				result += " " + i;
			}
		} else {
			result += "none";
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
		result = prime * result + Arrays.hashCode(getCategories());
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
		if (!Arrays.equals(getCategories(), other.getCategories())) {
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

	
	/* OLD Version:
	@Override
	public POI clone() {
    	URL newURL = null;
		try {
			if (getURL() != null) {
				newURL = new URL(getURL().toExternalForm());
			}
		} catch (MalformedURLException e) { }
		String textInfo = getTextInfo() == null ? null : new String(getTextInfo());
		POI clonedPOI = new POI(super.clone(), textInfo, newURL, Arrays.copyOf(getCategories(), getCategories().length));
		clonedPOI.setCrossingInformation(this.getCrossingInformation());
		return clonedPOI;
	}
	*/
	
    @Override
	public int numberDimensions() {
        return 2;
    }

    @Override
	public double valueForDimension(int dim) {
        if (dim < 0 || dim > numberDimensions()-1)
            throw new IllegalArgumentException("dim out of range");

        if (dim == 0)
            return getLatitude();
		return getLongitude();
    }

    @Override
	public int numberNodes() {
        return 1;
    }

    @Override
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

	@Override
	public int[] getCategories() {
		return categories;
	}

	@Override
	public void setCategories(int[] categories) {
		this.categories = categories;
	}

}