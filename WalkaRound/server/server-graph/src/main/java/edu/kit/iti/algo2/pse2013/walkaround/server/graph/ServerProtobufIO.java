package edu.kit.iti.algo2.pse2013.walkaround.server.graph;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.google.protobuf.Message;

import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.GraphProtos.SaveGraphData;
import edu.kit.iti.algo2.pse2013.walkaround.shared.pbf.GraphProtos.SaveGraphData.SaveEdge;

public class ServerProtobufIO {
	public static void write(GraphDataIO graphData, FileOutputStream graphOutput) throws IOException {
		write(graphDataToBuilder(graphData), graphOutput);
	}

	private static void write(Message.Builder builder, OutputStream graphOutput) throws IOException {
		builder.build().writeTo(graphOutput);
	}

	private static SaveGraphData.Builder graphDataToBuilder(GraphDataIO graphData) {
		ArrayList<SaveEdge> edges = new ArrayList<SaveEdge>();
		for (Edge e : graphData.getEdges()) {
			edges.add(edgeToBuilder(e));
		}
		return SaveGraphData.newBuilder().addAllEdge(edges);
	}

	private static SaveEdge edgeToBuilder(Edge e) {
		return null;
	}
}