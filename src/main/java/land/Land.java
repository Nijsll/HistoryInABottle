package land;

import enums.LandType;
import history.Record;
import nation.Civilization;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Land {
    private String name;
    private LandType type;
    private List<Civilization> civs;

    private int resources;
    private int baseResources;
    private int space;

    private Graph<Land, DefaultEdge> graph;

    public Land(Graph<Land, DefaultEdge> graph, String name, LandType type, int baseResources, int space) {
        this.name = name;
        this.type = type;
        this.civs = new ArrayList<>();
        this.baseResources = baseResources;
        this.resources = baseResources;
        this.space = space;
        this.graph = graph;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Land otherLand = (Land) obj; // Cast the object to a Land

        return this.name.equals(otherLand.getName());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }


    public String getName() {
        return name;
    }

    public LandType getType() {
        return type;
    }

    public int getResources() {
        return resources;
    }

    public void changeResources(int changeAmount) {
        this.resources += changeAmount;
    }

    public void normalizeResources() {
        if (this.resources == this.baseResources) {
            return;
        } else if (this.resources < this.baseResources) {
            this.resources++;
            return;
        }
        this.resources--;
    }

    public int getBaseResources() {
        return baseResources;
    }

    public int getSpace() {
        return space;
    }

    public ArrayList<Land> getDirectNeighbors() {
        ArrayList<Land> neighbors = new ArrayList<>();
        if (graph != null) {
            for (DefaultEdge edge : graph.edgesOf(this)) {
                Land source = graph.getEdgeSource(edge);
                Land target = graph.getEdgeTarget(edge);

                // Add the neighbor that is not the current Land object
                if (source.equals(this)) {
                    neighbors.add(target);
                } else {
                    neighbors.add(source);
                }
            }
        }
        return neighbors;
    }

    public Map<String, LandType> getNeighbourTypes() {
        // TODO
        return new HashMap<>();
    }

    public Map<Land, LandType> getNeighbourLandTypeMap() {
        // TODO
        return new HashMap<>();
    }

    public int getOccupiedResources() {
        // TODO
        return 0;
    }

    public List<Civilization> getCivs() {
        return this.civs;
    }

    public void addCiv(Civilization civ) {
        // TODO
    }

    public void remCiv(String civName) {
        // TODO
    }

    public void remCiv(Civilization civ) {
        remCiv(civ.getName());
    }

    public List<Record> passTime() {
        // TODO
        return new ArrayList<>();
    }

    public void cleanCivs() {
        // TODO remove destroyed civs
    }


}
