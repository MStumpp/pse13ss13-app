package edu.kit.iti.algo2.pse2013.walkaround.shared.pbf;

import java.util.ArrayList;

import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Address;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Coordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.CrossingInformation;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.Location;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.LocationProtos.SaveLocationData;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.LocationProtos.SaveLocationData.SaveCoordinate;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.LocationProtos.SaveLocationData.SaveLocation;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.LocationProtos.SaveLocationData.SaveLocation.SaveAddress;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.LocationProtos.SaveLocationData.SavePOI;

public class ProtobufConverter {
	public static Address getAddress(SaveAddress saveAddress) {
		if (saveAddress == null) {
			return null;
		}
		return new Address(
			saveAddress.getStreet(),
			saveAddress.getHouseNumber(),
			saveAddress.getCity(),
			saveAddress.getPostalCode());
	}
	public static SaveAddress.Builder getAddressBuilder(Address a) {
		if (a == null) {
			return null;
		}
		return SaveAddress.newBuilder()
				.setCity(a.getCity())
				.setHouseNumber(a.getHouseNumber())
				.setPostalCode(a.getPostalCode())
				.setStreet(a.getStreet());
	}
	public static Coordinate getCoordinate(SaveCoordinate saveCoord) {
		float[] crossroads = new float[saveCoord.getCrossroadAngleCount()];
		for (int i = 0; i < saveCoord.getCrossroadAngleCount(); i++) {
			crossroads[i] = saveCoord.getCrossroadAngle(i);
		}
		CrossingInformation crossInfo = null;
		if (crossroads.length > 0) {
			crossInfo = new CrossingInformation(crossroads);
		}
		return new Coordinate(saveCoord.getLatitude(), saveCoord.getLongitude(), crossInfo);
	}
	public static SaveCoordinate.Builder getCoordinateBuilder(Coordinate c) {
		SaveCoordinate.Builder saveCoord = SaveCoordinate.newBuilder()
				.setLatitude(c.getLatitude())
				.setLongitude(c.getLongitude());
		if (c.getCrossingInformation() != null && c.getCrossingInformation().getCrossingAngles() != null) {
			for (float angle : c.getCrossingInformation().getCrossingAngles()) {
				saveCoord.addCrossroadAngle(angle);
			}
		}
		return saveCoord;
	}

	public static Location getLocation(SaveLocation saveLoc) {
		return new Location(
			saveLoc.getParent().getLatitude(),
			saveLoc.getParent().getLongitude(),
			saveLoc.getName(),
			getAddress(saveLoc.getAddress()));
	}
	public static SaveLocation.Builder getLocationBuilder(Location loc) {
		SaveLocation.Builder builder = SaveLocation.newBuilder()
				.setParent(getCoordinateBuilder(loc))
				.setID(loc.getId())
				.setName(loc.getName());
		if (loc.getAddress() != null) {
			builder.setAddress(getAddressBuilder(loc.getAddress()));
		}
		return builder;
	}

	public static LocationDataIO getLocationData(SaveLocationData saveLocationData) {
		LocationDataIO locationData = new LocationDataIO();
		for (SavePOI savePOI : saveLocationData.getPOIList()) {
			locationData.addPOI(getPOI(savePOI));
		}
		return locationData;
	}
	public static SaveLocationData.Builder getLocationDataBuilder(LocationDataIO l) {
		SaveLocationData.Builder builder = SaveLocationData.newBuilder();
		for (POI p : l.getPOIs()) {
			builder.addPOI(getPOIBuilder(p));
		}
		return builder;
	}
	public static POI getPOI(SavePOI savePOI) {
		int[] cats = new int[savePOI.getPOICategoryList().size()];
		for (int i = 0; i < cats.length; i++) {
			cats[i] = savePOI.getPOICategory(i);
		}
		return new POI(getLocation(savePOI.getParent()), savePOI.getTextInfo(), savePOI.getImageURL(), cats);
	}
	public static SavePOI.Builder getPOIBuilder(POI p) {
		int[] cats = p.getPOICategories();
		ArrayList<Integer> poiList = new ArrayList<Integer>();
		for (int i = 0; i < cats.length; i++) {
			poiList.add(cats[i]);
		}
		return SavePOI.newBuilder()
				.setParent(getLocationBuilder(p))
				.setTextInfo(p.getTextInfo())
				.setImageURL(p.getURL())
				.addAllPOICategory(poiList);
	}
}
