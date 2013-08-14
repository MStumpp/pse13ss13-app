package edu.kit.iti.algo2.pse2013.walkaround.server.model;

import edu.kit.iti.algo2.pse2013.walkaround.shared.graph.Vertex;

import java.util.*;

public class RouteSet {

    private Vertex source;

    private Set<Vertex> targets;

    private Map<Vertex, Double> weightedLengths;

    private Map<Vertex, List<Vertex>> routes;

    RouteSet(Vertex source, Set<Vertex> ring) {
        this.source = source;
        this.targets = ring;
        weightedLengths = new TreeMap<Vertex, Double>();
        routes = new TreeMap<Vertex, List<Vertex>>();
        for (Vertex target : ring) {
            weightedLengths.put(target, target.getCurrentWeightedLength());
            routes.put(target, getRouteList(source, target));
        }
    }

    private List<Vertex> getRouteList(Vertex source, Vertex target) {

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

    public Set<Vertex> getTargets() {
        return targets;
    }

    public List<Vertex> getRoute(Vertex vertex) {
        return routes.get(vertex);
    }

    public double getWeithedLength(Vertex vertex) {
        return weightedLengths.get(vertex);
    }

}