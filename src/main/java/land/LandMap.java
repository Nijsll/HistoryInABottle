package land;

import history.Record;
import nation.Civilization;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.ArrayList;
import java.util.List;

public class LandMap {
    private final Graph<Land, DefaultEdge> graph;

    public LandMap() {
        this.graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
    }

    public Land getLand(String name) {
        for (Land land : graph.vertexSet()) {
            if (land.getName().equals(name)) {
                return land;
            }
        }
        System.out.println("No land with name " + name + " found");
        return null;
    }

    public List<Civilization> getAllCivs() {
        List<Civilization> civs = new ArrayList<>();
        for (Land land : graph.vertexSet()) {
            civs.addAll(land.getCivs());
        }
        return civs;
    }

    public Civilization getCiv(String name) {
        for (Land land : graph.vertexSet()) {
            for (Civilization civ : land.getCivs()) {
                if (civ.getName().equals(name)) {
                    return civ;
                }
            };
        }
        System.out.println("No civ with name " + name + " found");
        return null;
    }

    public List<Record> passTime() {
        List<Record> records = new ArrayList<>();
        for (Land land : graph.vertexSet()) {
            records.addAll(land.passTime());
        }
        return records;
    }
}
