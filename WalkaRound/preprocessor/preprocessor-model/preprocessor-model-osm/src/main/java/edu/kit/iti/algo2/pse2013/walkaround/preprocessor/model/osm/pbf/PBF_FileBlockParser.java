package edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.pbf;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import crosby.binary.BinaryParser;
import crosby.binary.Osmformat.DenseNodes;
import crosby.binary.Osmformat.HeaderBlock;
import crosby.binary.Osmformat.Node;
import crosby.binary.Osmformat.Relation;
import crosby.binary.Osmformat.Way;
import crosby.binary.file.BlockReaderAdapter;
import crosby.binary.file.FileBlock;
import crosby.binary.file.FileBlockPosition;
import edu.kit.iti.algo2.pse2013.walkaround.preprocessor.model.osm.GraphDataIO;
import edu.kit.iti.algo2.pse2013.walkaround.server.graph.Vertex;
import edu.kit.iti.algo2.pse2013.walkaround.shared.datastructures.LocationDataIO;

public class PBF_FileBlockParser extends BinaryParser implements BlockReaderAdapter {
	private ArrayList<Vertex> vertices = new ArrayList<>();

	public PBF_FileBlockParser(GraphDataIO graphData, LocationDataIO locationData) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void complete() {
		Log.d(this.getClass().getName(), "Finished parsing osm-file");
	}

	@Override
	protected void parse(HeaderBlock arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void parseDense(DenseNodes nodes) {
		List<Long> nodeList = nodes.getIdList();
		long currID = 0;
		for (Long l : nodeList) {
			if (l != null) {
				vertices.add(new Vertex(currID += l));
			}
		}
	}

	@Override
	protected void parseNodes(List<Node> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void parseRelations(List<Relation> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void parseWays(List<Way> arg0) {
		// TODO Auto-generated method stub

	}

}
