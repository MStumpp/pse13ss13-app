package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.pbf;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import crosby.binary.BinaryParser;
import crosby.binary.Osmformat;
import crosby.binary.Osmformat.DenseNodes;
import crosby.binary.Osmformat.HeaderBlock;
import crosby.binary.Osmformat.Node;
import crosby.binary.Osmformat.PrimitiveGroup;
import crosby.binary.Osmformat.Relation;
import crosby.binary.Osmformat.Way;
import crosby.binary.file.BlockReaderAdapter;
import crosby.binary.file.FileBlockPosition;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMNode;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.OSMWay;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategory;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.mapdata.category.OSMCategoryFactory;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.POI;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.GraphDataIO;

public class PBF_FileBlockParser extends BinaryParser implements BlockReaderAdapter {
	/**
	 * In this state all nodes that are part of a way are added to {@link this#interestingNodes}.
	 */
	private static final short STATE_FIND_NODES_FOR_WAYS = 0;
	/**
	 * In this state all nodes in {@link this#interestingNodes} are filled with content (lat, lon and the tags).
	 * Then the ways are added to the object {@link this#graphData}.
	 */
	private static final short STATE_PARSE_WAYS = 1;
	/**
	 * Now the {@link this#interestingNodes} are cleared.
	 * All punctiform POIs are being added to {@link this#locationData}.
	 * All nodes, that are part of extensive POIs are being added to {@link this#interestingNodes}.
	 */
	private static final short STATE_FIND_NODES_FOR_POIS = 2;
	/**
	 * The nodes in {@link this#interestingNodes} are being filled with content (lat, lon and the tags).
	 * Then the POI-ways, these nodes are part of, are being added to {@link this#locationData}.
	 */
	private static final short STATE_PARSE_POIS = 3;
	/**
	 * This state marks, that no further run is necessary
	 */
	private static final short STATE_FINISH = 4;

	private Map<Long, OSMNode> interestingNodes = new TreeMap<Long, OSMNode>();

	private static final Logger logger = Logger.getLogger(PBF_FileBlockParser.class.getSimpleName());

	private GraphDataIO graphData;
	private LocationDataIO locationData;

	private short state = STATE_FIND_NODES_FOR_WAYS;
	private static int readBlocks = 0;

	public PBF_FileBlockParser(GraphDataIO graphData, LocationDataIO locationData) {
		this.graphData = graphData;
		this.locationData = locationData;
	}

	@Override
	public void complete() {
		System.out.println();
		readBlocks = 0;
		if (state == STATE_FIND_NODES_FOR_WAYS) {
			logger.info(String.format("Finished first run\n\t%d interesting nodes for ways found.", interestingNodes.size()));
		} else if (state == STATE_PARSE_WAYS) {
			logger.info(String.format("Finished second run:\n\t%d Edges found.", graphData.getEdges().size()));
			interestingNodes.clear();
		} else if (state == STATE_FIND_NODES_FOR_POIS) {
			logger.info(String.format("Finished third run:\n\t%d interesting nodes for POIs found.\n\t%d POIs found.", interestingNodes.size(), locationData.getPOIs().size()));
		} else if (state == STATE_PARSE_POIS) {
			logger.info(String.format("Finished fourth run:\n\tAdded extensive POIs. Now %d POIs in total.", locationData.getPOIs().size()));
		}
		state++;
	}
	public boolean skipBlock(FileBlockPosition blockPos) {
		return false;
	}

	@Override
	protected void parse(HeaderBlock block) {
		logger.info("Started parsing of file:\n\tEach character represents roughly 8000 osm-elements.\n\t\t- D for DenseNodes\n\t\t- N for Nodes\n\t\t- W for Ways\n\t\t- R for Relations");
	}

	public void parse(Osmformat.PrimitiveBlock block) {
		super.parse(block);
		for (PrimitiveGroup group : block.getPrimitivegroupList()) {
			readBlocks++;
			if (group.getDense() != null && group.getDense().getIdCount() >= 1) {
				System.out.print('D');
			} else if (group.getNodesList() != null && group.getNodesList().size() >= 1) {
				System.out.print('N');
			} else if (group.getWaysList() != null && group.getWaysList().size() >= 1) {
				System.out.print('W');
			} else if (group.getRelationsList() != null && group.getRelationsList().size() >= 1) {
				System.out.print('R');
			} else {
				readBlocks--;
			}
			if (readBlocks % 50 == 0) {
				System.out.println();
			}
		}
	}

	@Override
	protected void parseDense(DenseNodes dNodes) {
		if (state == STATE_PARSE_WAYS || state == STATE_PARSE_POIS || state == STATE_FIND_NODES_FOR_POIS) {
			List<Long> ids = dNodes.getIdList();
			long id = 0;
			long lat = 0;
			long lon = 0;
			Iterator<Integer> keysVals = dNodes.getKeysValsList().iterator();
			for (int i = 0; i < ids.size(); i++) {
				id += ids.get(i);
				lat += dNodes.getLat(i);
				lon += dNodes.getLon(i);

				OSMNode currentNode;
				if (interestingNodes.containsKey(id)) {
					currentNode = interestingNodes.get(id);
				} else {
					currentNode = new OSMNode(id);
				}
				currentNode.setLatitude(lat * granularity * .000000001);
				currentNode.setLongitude(lon * granularity * .000000001);

				int j;
				while (keysVals.hasNext() && (j = keysVals.next()) != 0 && keysVals.hasNext()) {
					currentNode.addTag(getStringById(j), getStringById(keysVals.next()));
				}
				if (state == STATE_FIND_NODES_FOR_POIS) {
					POI poi = currentNode.getPOI();
					if (poi != null) {
						locationData.addPOI(poi);
					}
				}
			}
		}
	}

	@Override
	protected void parseNodes(List<Node> inNodes) {
		if (state == STATE_PARSE_WAYS || state == STATE_PARSE_POIS || state == STATE_FIND_NODES_FOR_POIS) {
			for (Node inNode : inNodes) {
				OSMNode currentNode;
				if (interestingNodes.containsKey(inNode.getId())) {
					currentNode = interestingNodes.get(inNode.getId());
				} else {
					currentNode = new OSMNode(inNode.getId());
				}
				currentNode.setLatitude(inNode.getLat() * granularity * .000000001);
				currentNode.setLongitude(inNode.getLon() * granularity * .000000001);

				List<Integer> keys = inNode.getKeysList();
				List<Integer> vals = inNode.getValsList();
				for (int i = 0; i < Math.min(keys.size(), vals.size()); i++) {
					currentNode.addTag(getStringById(keys.get(i)), getStringById(vals.get(i)));
				}
				if (state == STATE_FIND_NODES_FOR_POIS) {
					POI poi = currentNode.getPOI();
					if (poi != null) {
						locationData.addPOI(poi);
					}
				}
			}
		}
	}

	@Override
	protected void parseRelations(List<Relation> inRelations) {
		//TODO: Implement
	}

	@Override
	protected void parseWays(List<Way> inWays) {
		for (Way w : inWays) {
			OSMWay way = new OSMWay(w.getId());
			List<Long> nodeRefs = w.getRefsList();

			if (nodeRefs != null) {
				boolean isValidWay = true;
				long ref = 0;
				for (int i = 0; i < nodeRefs.size() && isValidWay; i++) {
					ref += nodeRefs.get(i);
					if (state == STATE_PARSE_WAYS || state == STATE_PARSE_POIS) {
						if (interestingNodes.containsKey(ref)) {
							way.addNode(interestingNodes.get(ref));
						} else {
							isValidWay = false;
						}
					}
				}
				if (isValidWay) {
					for (int i = 0; i < Math.min(w.getKeysCount(), w.getValsCount()); i++) {
						way.addTag(getStringById(w.getKeys(i)), getStringById(w.getVals(i)));
					}
					OSMCategory footCat = OSMCategoryFactory.createFootwayCategory();
					OSMCategory allAreaCat = OSMCategoryFactory.createAllAreaCategory();
					OSMCategory allPOICat = OSMCategoryFactory.createAllPOICategory();
					if (footCat.accepts(way) || allAreaCat.accepts(way)) {
						if (state == STATE_FIND_NODES_FOR_WAYS) {
							long curID = 0;
							for (Long idDiff : w.getRefsList()) {
								interestingNodes.put(curID += idDiff, new OSMNode(curID));
							}
						} else if (state == STATE_PARSE_WAYS){
							if (footCat.accepts(way)) {
								graphData.addEdges(way.getEdges());
							}
							if (allAreaCat.accepts(way)) {
								locationData.addArea(way.getArea());
							}
						}
					}
					if (allPOICat.accepts(way)) {
						if (state == STATE_PARSE_POIS) {
							POI poi = way.getPOI();
							if (poi != null) {
								locationData.addPOI(poi);
							}
						} else if (state == STATE_FIND_NODES_FOR_POIS) {
							long curID = 0;
							for (Long idDiff : w.getRefsList()) {
								interestingNodes.put(curID += idDiff, new OSMNode(curID));
							}
						}
					}
				}
			}
		}
	}
	public boolean needsFurtherRun() {
		return state < STATE_FINISH;
	}
}