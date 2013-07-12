package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * OptimizeRouteProcessor which takes a route and computes an optimized route
 * regarding the Waypoints contained in the given route. A route is represented
 * as RouteInfoTransfer.
 * 
 * @author Matthias Stumpp
 * @version 1.0
 */
public class OptimizeRouteProcessor {

	/**
	 * OptimizeRouteProcessor instance.
	 */
	private static OptimizeRouteProcessor instance;

	/**
	 * ShortestPathProcessor instance.
	 */
	private ShortestPathProcessor shortestPathProcessor;

	/**
	 * Creates an instance of OptimizeRouteProcessor.
	 * 
	 * @param shortestPathProcessor
	 *            ShortestPathProcessor used for route optimization.
	 */
	private OptimizeRouteProcessor(ShortestPathProcessor shortestPathProcessor) {
		this.shortestPathProcessor = shortestPathProcessor;
	}

	/**
	 * Instantiates and/or returns a singleton instance of
	 * OptimizeRouteProcessor.
	 * 
	 * @return OptimizeRouteProcessor.
	 * @throws InstantiationException
	 *             If not instantiated before.
	 */
	public static OptimizeRouteProcessor getInstance()
			throws InstantiationException {
		if (instance == null)
			throw new InstantiationException(
					"singleton must be initialized first");
		return instance;
	}

	/**
	 * Instantiates and returns a singleton instance of OptimizeRouteProcessor.
	 * 
	 * @param shortestPathProcessor
	 *            ShortestPathProcessor used for shortest path computation.
	 * @return OptimizeRouteProcessor.
	 */
	public static OptimizeRouteProcessor init(
			ShortestPathProcessor shortestPathProcessor) {
		if (shortestPathProcessor == null)
			throw new IllegalArgumentException(
					"ShortestPathProcessor must be provided");
		if (instance != null)
			throw new IllegalArgumentException(
					"OptimizeRouteProcessor already initialized");
		instance = new OptimizeRouteProcessor(shortestPathProcessor);
		return instance;
	}

	/**
	 * Computes an optimized route based on a given route.
	 * 
	 * @param vertices
	 *            The route to be optimized.
	 * @return RouteInfoTransfer.
	 * @throws ShortestPathComputeException
	 * @throws NoShortestPathExistsException
	 */
	public List<Vertex> computeOptimizedRoute(List<Vertex> vertices)
			throws NoShortestPathExistsException, ShortestPathComputeException {
		if (vertices == null)
			throw new IllegalArgumentException(
					"list of vertices must be provided");
		if (vertices.size() < 2)
			throw new IllegalArgumentException(
					"number of vertices mist be equal to or greater than 2");
		LinkedList<Vertex> resultRoute = new LinkedList<Vertex>();
		Vertex start = vertices.get(0);
		Vertex end = vertices.get(vertices.size() - 1);

		List<List<Vertex>> permutations = getPermutations(vertices);
		double lengthOfRoute = 0;

		for (int i = 0; i < permutations.size(); i++) {
			LinkedList<Vertex> currentRoute = new LinkedList<Vertex>();
			permutations.get(i).add(0, start);
			permutations.get(i).add(end);
			for (int j = 0; j < permutations.get(i).size() - 1; j++) {
				currentRoute.addAll(shortestPathProcessor.computeShortestPath(
						permutations.get(i).get(j),
						permutations.get(i).get(j + 1)));
			}

			int currentLength = 0;
			for (Iterator<Vertex> iter = currentRoute.iterator(); iter
					.hasNext();) {
				Vertex current = iter.next();
				currentLength += current.getCurrentLength();
			}

			if (lengthOfRoute < currentLength) {
				resultRoute = currentRoute;
			}
		}
		return resultRoute;
	}

	private List<List<Vertex>> getPermutations(List<Vertex> listToPermutate) {

		List<List<Vertex>> result = new ArrayList<List<Vertex>>();

		listToPermutate.remove(0);
		listToPermutate.remove(listToPermutate.size() - 1);

		if (listToPermutate.size() == 0) {
			return null;
		}

		computePermutations(new ArrayList<Vertex>(), listToPermutate, result);

		return result;

	}

	private void computePermutations(List<Vertex> beginningStrings,
			List<Vertex> listToPermutate, List<List<Vertex>> result) {

		if (listToPermutate.size() <= 1) {

			List<Vertex> list = new ArrayList<Vertex>();
			list.addAll(beginningStrings);
			list.addAll(listToPermutate);

			result.add(list);

		} else {
			for (int i = 0; i < listToPermutate.size(); i++) {
				List<Vertex> newStrings = new ArrayList<Vertex>();
				for (int j = 0; j < i; j++)
					newStrings.add(listToPermutate.get(j));
				for (int j = i + 1; j < listToPermutate.size(); j++)
					newStrings.add(listToPermutate.get(j));

				List<Vertex> newStrings2 = new ArrayList<Vertex>();
				newStrings2.addAll(beginningStrings);
				newStrings2.add(listToPermutate.get(i));

				computePermutations(newStrings2, newStrings, result);
			}
		}
	}

}
