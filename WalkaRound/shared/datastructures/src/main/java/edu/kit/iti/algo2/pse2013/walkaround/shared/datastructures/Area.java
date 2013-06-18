package walkaround.datastructures;

import java.util.ArrayList;

public class Area {

	private ArrayList<Integer> areaCategories;

	private ArrayList<Coordinate> areaCoordinates;

	public Area() {
		areaCategories = new ArrayList<Integer>();
		areaCoordinates = new ArrayList<Coordinate>();
	}

	public ArrayList<Integer> getAreaCategories() {
		return areaCategories;
	}

	public ArrayList<Coordinate> getAreaCoordinates() {
		return areaCoordinates;
	}

}
