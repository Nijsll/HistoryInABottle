package land;

import enums.LandType;
import history.Record;
import nation.Civilization;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.ArrayList;
import java.util.List;

public class LandMap {
    private Graph<Land, DefaultEdge> graph;

    public LandMap() {
        this.graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
    }

    public Land getLand(String name) {
        // TODO
        return null;
    }

    public List<Civilization> getAllCivs() {
        List<Civilization> civs = new ArrayList<>();
        // TODO
        return civs;
    }

    public Civilization getCiv(String name) {
        // TODO
        return null;
    }

    public List<Record> passTime() {
        // TODO
        return new ArrayList<>();
    }
}
