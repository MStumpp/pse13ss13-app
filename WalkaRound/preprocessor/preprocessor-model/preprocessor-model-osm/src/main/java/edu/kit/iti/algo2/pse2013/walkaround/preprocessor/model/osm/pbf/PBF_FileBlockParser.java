package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.pbf;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import crosby.binary.BinaryParser;
import crosby.binary.Osmformat.DenseNodes;
import crosby.binary.Osmformat.HeaderBlock;
import crosby.binary.Osmformat.Node;
import crosby.binary.Osmformat.Relation;
import crosby.binary.Osmformat.Way;
import crosby.binary.file.BlockReaderAdapter;
import crosby.binary.file.FileBlockPosition;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMNode;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMWay;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategoryFactory;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;

public class PBF_FileBlockParser extends BinaryParser implements BlockReaderAdapter {
	private static final short STATE_FIND_NEEDED_NODES = 0;
	private static final short STATE_PARSE_WAYS_N_POIS = 1;
	private static final short STATE_FINISH = 2;

	private Map<Long, OSMNode> nodes = new TreeMap<Long, OSMNode>();

	private GraphDataIO graphData;
	private LocationDataIO locationData;
	private short state = STATE_FIND_NEEDED_NODES;

	public PBF_FileBlockParser(GraphDataIO graphData, LocationDataIO locationData) {
		this.graphData = graphData;
		this.locationData = locationData;
	}

	@Override
	public void complete() {
		state++;
		Logger.getLogger(this.getClass().getSimpleName()).info("Finished parsing osm-file");
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
		if (state == STATE_PARSE_WAYS_N_POIS) {
//			if (dNodes.getIdList().size() > 0) {
//				System.out.println(String.format("ParseDenseNodes (%d)", nodes.size()));
//			}
			List<Long> ids = dNodes.getIdList();
			long id = 0;
			long lat = 0;
			long lon = 0;
			Iterator<Integer> keysVals = dNodes.getKeysValsList().iterator();
			for (int i = 0; i < ids.size(); i++) {
				id += ids.get(i);
				lat += dNodes.getLat(i);
				lon += dNodes.getLon(i);

				OSMNode currentNode = null;

				if (nodes.containsKey(id)) {
					currentNode = nodes.get(id);
				} else {
					currentNode = new OSMNode(id, lat, lon);
				}
				currentNode.setLatitude(lat * granularity * .000000001);
				currentNode.setLongitude(lon * granularity * .000000001);

				int j;
				while (keysVals.hasNext() && (j = keysVals.next()) != 0 && keysVals.hasNext()) {
					currentNode.addTag(getStringById(j), getStringById(keysVals.next()));
				}
				if (currentNode.getName() != null && currentNode.getPOICategories().length > 0) {
					locationData.addPOI(currentNode.convertToPOI());
				}
			}
		}
	}

	@Override
	protected void parseNodes(List<Node> inNodes) {
		if (state == STATE_PARSE_WAYS_N_POIS) {
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
	}

	@Override
	protected void parseRelations(List<Relation> inRelations) {
		//TODO: Implement
	}

	@Override
	protected void parseWays(List<Way> inWays) {
//		if (inWays.size() > 0) {
//			System.out.println(String.format("parseWays(%d NeededNodes)", nodes.size()));
//		}
		for (Way w : inWays) {
			boolean isValidWay = true;
			OSMWay way = new OSMWay(w.getId());
			List<Long> nodeRefs = w.getRefsList();

			if (nodeRefs != null) {
				long ref = 0;
				for (int i = 0; i < nodeRefs.size() && isValidWay; i++) {
					ref += nodeRefs.get(i);
					if (state != STATE_FIND_NEEDED_NODES) {
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
				if (isValidWay && OSMCategoryFactory.createFootwayCategory().accepts(way)) {
					if (state == STATE_FIND_NEEDED_NODES) {
						long curID = 0;
						for (Long idDiff : w.getRefsList()) {
							nodes.put(curID += idDiff, new OSMNode(curID));
						}
					} else {
						graphData.addEdges(way.getEdges());
					}
				}
			}
		}
	}
	public boolean needsFurtherRun() {
		return state != STATE_FINISH;
	}
}