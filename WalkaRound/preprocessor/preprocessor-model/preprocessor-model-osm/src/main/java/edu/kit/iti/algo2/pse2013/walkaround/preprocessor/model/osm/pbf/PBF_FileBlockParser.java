package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.pbf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import crosby.binary.BinaryParser;
import crosby.binary.Osmformat.DenseNodes;
import crosby.binary.Osmformat.HeaderBlock;
import crosby.binary.Osmformat.Node;
import crosby.binary.Osmformat.Relation;
import crosby.binary.Osmformat.Way;
import crosby.binary.file.BlockReaderAdapter;
import crosby.binary.file.FileBlockPosition;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMNode;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMWay;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;

public class PBF_FileBlockParser extends BinaryParser implements BlockReaderAdapter {
	private HashMap<Long, OSMNode> nodes = new HashMap<>();
	private int numEdges = 0;

	private GraphDataIO graphData;
	private LocationDataIO locationData;

	public PBF_FileBlockParser(GraphDataIO graphData, LocationDataIO locationData) {
		this.graphData = graphData;
		this.locationData = locationData;
	}

	@Override
	public void complete() {
		Logger.getLogger(this.getClass().getName()).info("Finished parsing osm-file");
		System.out.println(nodes.size());
	}
	public boolean skipBlock(FileBlockPosition blockPos) {
		return false;
	}

	@Override
	protected void parse(HeaderBlock block) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void parseDense(DenseNodes dNodes) {
		List<Long> ids = dNodes.getIdList();
		long id = 0;
		long lat = 0;
		long lon = 0;
		Iterator<Integer> keysVals = dNodes.getKeysValsList().iterator();
		for (int i = 0; i < ids.size(); i++) {
			OSMNode node = new OSMNode(id += ids.get(i), (lat += dNodes.getLat(i)) * granularity * .000000001, (lon += dNodes.getLon(i)) * granularity * .000000001);

			int j;
			while (keysVals.hasNext() && (j = keysVals.next()) != 0 && keysVals.hasNext()) {
				node.addTag(getStringById(j), getStringById(keysVals.next()));
			}
			nodes.put(node.getID(), node);
		}
	}

	@Override
	protected void parseNodes(List<Node> inNodes) {
		for (Node inNode : inNodes) {
			OSMNode node = new OSMNode(inNode.getId(), inNode.getLat(), inNode.getLon());
			List<Integer> keys = inNode.getKeysList();
			List<Integer> vals = inNode.getValsList();
			for (int i = 0; i < Math.min(keys.size(), vals.size()); i++) {
				node.addTag(getStringById(keys.get(i)), getStringById(vals.get(i)));
			}
			this.nodes.put(node.getID(), node);
		}
	}

	@Override
	protected void parseRelations(List<Relation> inRelations) {
		//TODO: Implement
	}

	@Override
	protected void parseWays(List<Way> inWays) {
		for (Way w : inWays) {
			boolean isValidWay = true;
			OSMWay way = new OSMWay(w.getId());
			List<Long> refs = w.getRefsList();
			if (refs != null) {
				long ref = 0;
				for (int i = 0; i < refs.size() && isValidWay; i++) {
					ref += refs.get(i);
					if (nodes.containsKey(ref)) {
						way.addNode(nodes.get(ref));
					} else {
						isValidWay = false;
					}
				}
			}
			for (int i = 0; i < Math.min(w.getKeysCount(), w.getValsCount()); i++) {
				way.addTag(getStringById(w.getKeys(i)), getStringById(w.getVals(i)));
			}
			if (isValidWay) {
				graphData.addEdges(way.getEdges());
			}
		}
	}
}