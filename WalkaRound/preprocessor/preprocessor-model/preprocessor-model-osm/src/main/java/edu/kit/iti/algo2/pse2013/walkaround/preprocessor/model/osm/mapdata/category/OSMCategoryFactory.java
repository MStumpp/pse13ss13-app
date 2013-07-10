package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category;

public class OSMCategoryFactory {
	public static final OSMCategory createFootwayCategory() {
		OSMTagCategory footway = new OSMTagCategory();
		footway.addTag("highway", "footway");
		OSMTagCategory sidewalkBoth = new OSMTagCategory();
		sidewalkBoth.addTag("sidewalk", "both");
		OSMTagCategory sidewalkLeft = new OSMTagCategory();
		sidewalkBoth.addTag("sidewalk", "left");
		OSMTagCategory sidewalkRight = new OSMTagCategory();
		sidewalkBoth.addTag("sidewalk", "right");
		return new OSMOrCategory(footway,
			new OSMOrCategory(
				sidewalkLeft,
				new OSMOrCategory(
					sidewalkBoth,
					sidewalkRight
				)
			)
		);
	}
	public static final getPOICategory(Category c) {

	}
	public static final OSMCategory createFastFoodCategory() {
		OSMTagCategory fastFood = new OSMTagCategory();
		fastFood.addTag("amenity", "fast_food");
		return fastFood;
	}
	public static final OSMCategory createShopCategory() {
		OSMTagCategory fastFood = new OSMTagCategory();
		fastFood.addTag("amenity", "fast_food");
		return fastFood;
	}
}
