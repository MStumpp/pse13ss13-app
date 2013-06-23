package edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures;

import java.util.ArrayList;

public class Area {

	private int[] areaCategories;

	private ArrayList<Coordinate> areaCoordinates;

	public Area(int[] areaCategories, ArrayList<Coordinate> areaCoordinates) {
		this.areaCategories = areaCategories;
		this.areaCoordinates = new ArrayList<Coordinate>(areaCoordinates);
	}

	public int[] getAreaCategories() {
		return areaCategories;
	}

	public ArrayList<Coordinate> getAreaCoordinates() {
		return areaCoordinates;
	}

}
