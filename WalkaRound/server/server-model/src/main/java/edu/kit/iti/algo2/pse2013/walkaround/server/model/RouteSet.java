package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Edge;
import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

import java.util.*;

public class RouteSet {

    private Vertex source;

    private Set<Vertex> targets;

    private Map<Vertex, Double> weightedLengths;

    private Map<Vertex, List<Vertex>> routes;

    private Map<Vertex, Set<Edge>> routeEdges;

    RouteSet(Vertex source, Set<Vertex> ring) {
        this.source = source;
        this.targets = ring;
        weightedLengths = new TreeMap<Vertex, Double>();
        routes = new TreeMap<Vertex, List<Vertex>>();
        routeEdges = new TreeMap<Vertex, Set<Edge>>();
        for (Vertex target : ring) {
            weightedLengths.put(target, target.getCurrentWeightedLength());
            routes.put(target, getRouteVertexList(source, target));
            routeEdges.put(target, getRouteEdgeList(source, target));
        }
    }

    private List<Vertex> getRouteVertexList(Vertex source, Vertex target) {

        LinkedList<Vertex> route = new LinkedList<Vertex>();
        route.add(target);
        Vertex currentParent = target.getParent();
        while (currentParent != null && !currentParent.equals(source)) {
            route.addFirst(currentParent);
            currentParent = currentParent.getParent();
        }

        if (currentParent != null)
            route.addFirst(currentParent);

        return route;
    }

    private Set<Edge> getRouteEdgeList(Vertex source, Vertex target) {

        LinkedList<Edge> route = new LinkedList<Edge>();
        route.add(target.getOutgoingEdge(target.getParent()));
        Vertex currentParent = target.getParent();
        while (currentParent != null && !currentParent.equals(source)) {
            route.addFirst(currentParent.getOutgoingEdge(currentParent.getParent()));
            currentParent = currentParent.getParent();
        }

        return new HashSet<Edge>(route);
    }

    public Set<Vertex> getTargets() {
        return targets;
    }

    public List<Vertex> getRouteVertices(Vertex vertex) {
        return routes.get(vertex);
    }

    public Set<Edge> getRouteEdges(Vertex vertex) {
        return routeEdges.get(vertex);
    }

    public double getWeigthedLength(Vertex vertex) {
        return weightedLengths.get(vertex);
    }

}