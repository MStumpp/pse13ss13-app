package edu.kit.iti.algo2.pse2013.walkaround.shared.pbf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
	public static Coordinate readCoordinate(File source) throws FileNotFoundException, IOException {
		BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(source));
		return coordinateFromBuilder(SaveCoordinate.parseFrom(inStream));
	}
	public static Location readLocation(File source) throws FileNotFoundException, IOException {
		BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(source));
		return locationFromBuilder(SaveLocation.parseFrom(inStream));
	}
	public static LocationDataIO readLocationData(File source) throws FileNotFoundException, IOException {
		BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(source));
		return locationDataFromBuilder(SaveLocationData.parseFrom(inStream));
	}
	public static POI readPOI(File source) throws FileNotFoundException, IOException {
		BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(source));
		return poiFromBuilder(SavePOI.parseFrom(inStream));
	}
	public static void write(Coordinate c, File target) throws FileNotFoundException, IOException {
		write(coordinateToBuilder(c), target);
	}
	public static void write(Location l, File target) throws FileNotFoundException, IOException {
		write(locationToBuilder(l), target);
	}
	public static void write(LocationDataIO l, File target) throws FileNotFoundException, IOException {
		write(locationDataToBuilder(l), target);
	}
	public static void write(POI p, File target) throws FileNotFoundException, IOException {
		write(poiToBuilder(p), target);
	}

	private static void write(Message.Builder builder, File target) throws FileNotFoundException, IOException {
		BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(target));
		builder.build().writeTo(outStream);
		outStream.flush();
		outStream.close();
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
			crossroads[i] = saveCoord.getCrossroadAngleList().get(i);
		}
		CrossingInformation crossInfo = new CrossingInformation(crossroads);
		return new Coordinate(saveCoord.getLatitude(), saveCoord.getLongtitude(), crossInfo);
	}
	private static SaveCoordinate.Builder coordinateToBuilder(Coordinate coord) {
		SaveCoordinate.Builder saveCoord = SaveCoordinate.newBuilder()
				.setLatitude(coord.getLatitude())
				.setLongtitude(coord.getLongtitude());
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
			saveLoc.getParent().getLongtitude(),
			saveLoc.getID(),
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
		return new POI(locationFromBuilder(savePOI.getParent()), savePOI.getTextInfo(), savePOI.getImageURL(), savePOI.getPOICategoryList());
	}
	private static SavePOI.Builder poiToBuilder(POI p) {
		return SavePOI.newBuilder()
				.setParent(locationToBuilder(p))
				.setTextInfo(p.getTextInfo())
				.setImageURL(p.getURL())
				.addAllPOICategory(p.getPOICategories());
	}

	/**
	public SavePOI.Builder getSavePOI() {
    	return SavePOI.newBuilder().setLatitude(getLatitude()).setLongtitude(getLongtitude())
    			.setName(getName()).setID(getId()).setTextInfo(getTextInfo()).setImageURL(getURL()).addAllPOICategory(poiCategories);
    }
    public static POI getInstance(SavePOI poi) {
    	return new POI(poi.getLatitude(), poi.getLongtitude(), poi.getID(), poi.getName(), poi.getTextInfo(), poi.getImageURL(), poi.getPOICategoryList());
    }
	 */
}