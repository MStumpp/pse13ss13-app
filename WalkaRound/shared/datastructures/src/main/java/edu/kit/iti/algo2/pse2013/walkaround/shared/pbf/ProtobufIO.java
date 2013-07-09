package edu.kit.iti.algo2.pse2013.walkaround.shared.pbf;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.google.protobuf.Message;

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

public class ProtobufIO {
	public static Coordinate readCoordinate(InputStream fileStream) throws IOException {
		return coordinateFromBuilder(SaveCoordinate.parseFrom(fileStream));
	}
	public static Location readLocation(InputStream fileStream) throws IOException {
		return locationFromBuilder(SaveLocation.parseFrom(fileStream));
	}
	public static LocationDataIO readLocationData(InputStream fileStream) throws IOException {
		return locationDataFromBuilder(SaveLocationData.parseFrom(fileStream));
	}
	public static POI readPOI(InputStream fileStream) throws IOException {
		return poiFromBuilder(SavePOI.parseFrom(fileStream));
	}
	public static void write(Coordinate c, OutputStream fileStream) throws IOException {
		write(coordinateToBuilder(c), fileStream);
	}
	public static void write(Location l, OutputStream fileStream) throws IOException {
		write(locationToBuilder(l), fileStream);
	}
	public static void write(LocationDataIO l, OutputStream fileStream) throws IOException {
		write(locationDataToBuilder(l), fileStream);
	}
	public static void write(POI p, OutputStream fileStream) throws IOException {
		write(poiToBuilder(p), fileStream);
	}

	private static void write(Message.Builder builder, OutputStream fileStream) throws IOException {
		builder.build().writeTo(fileStream);
		fileStream.flush();
		fileStream.close();
	}

	private static Address addressFromBuilder(SaveAddress saveAddress) {
		if (saveAddress == null) {
			return null;
		}
		return new Address(
			saveAddress.getStreet(),
			saveAddress.getHouseNumber(),
			saveAddress.getCity(),
			saveAddress.getPostalCode());
	}
	private static SaveAddress.Builder addressToBuilder(Address a) {
		if (a == null) {
			return null;
		}
		return SaveAddress.newBuilder()
				.setCity(a.getCity())
				.setHouseNumber(a.getHouseNumber())
				.setPostalCode(a.getPostalCode())
				.setStreet(a.getStreet());
	}
	private static Coordinate coordinateFromBuilder(SaveCoordinate saveCoord) {
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
	private static SaveCoordinate.Builder coordinateToBuilder(Coordinate coord) {
		SaveCoordinate.Builder saveCoord = SaveCoordinate.newBuilder()
				.setLatitude(coord.getLatitude())
				.setLongitude(coord.getLongitude());
		if (coord.getCrossingInformation() != null && coord.getCrossingInformation().getCrossingAngles() != null) {
			for (float angle : coord.getCrossingInformation().getCrossingAngles()) {
				saveCoord.addCrossroadAngle(angle);
			}
		}
		return saveCoord;
	}
	private static Location locationFromBuilder(SaveLocation saveLoc) {
		return new Location(
			saveLoc.getParent().getLatitude(),
			saveLoc.getParent().getLongitude(),
			saveLoc.getName(),
			addressFromBuilder(saveLoc.getAddress()));
	}
	private static SaveLocation.Builder locationToBuilder(Location loc) {
		SaveLocation.Builder builder = SaveLocation.newBuilder()
				.setParent(coordinateToBuilder(loc))
				.setID(loc.getId())
				.setName(loc.getName());
		if (loc.getAddress() != null) {
			builder.setAddress(addressToBuilder(loc.getAddress()));
		}
		return builder;
	}
	private static LocationDataIO locationDataFromBuilder(SaveLocationData saveLocationData) {
		LocationDataIO locationData = new LocationDataIO();
		for (SavePOI savePOI : saveLocationData.getPOIList()) {
			locationData.addPOI(poiFromBuilder(savePOI));
		}
		return locationData;
	}
	private static SaveLocationData.Builder locationDataToBuilder(LocationDataIO l) {
		SaveLocationData.Builder builder = SaveLocationData.newBuilder();
		for (POI p : l.getPOIs()) {
			builder.addPOI(poiToBuilder(p));
		}
		return builder;
	}
	private static POI poiFromBuilder(SavePOI savePOI) {
		int[] cats = new int[savePOI.getPOICategoryList().size()];
		for (int i = 0; i < cats.length; i++) {
			cats[i] = savePOI.getPOICategory(i);
		}
		return new POI(locationFromBuilder(savePOI.getParent()), savePOI.getTextInfo(), savePOI.getImageURL(), cats);
	}
	private static SavePOI.Builder poiToBuilder(POI p) {
		int[] cats = p.getPOICategories();
		ArrayList<Integer> poiList = new ArrayList<Integer>();
		for (int i = 0; i < cats.length; i++) {
			poiList.add(cats[i]);
		}
		return SavePOI.newBuilder()
				.setParent(locationToBuilder(p))
				.setTextInfo(p.getTextInfo())
				.setImageURL(p.getURL())
				.addAllPOICategory(poiList);
	}
}