package land;

import enums.LandType;
import history.Record;
import nation.Civilization;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class Land {
    private String name;
    private LandType type;
    private List<Civilization> civs;

    private int resources;
    private int baseResources;
    private int space;

    private Graph<Land, DefaultEdge> graph;

//    CONSTRUCTION

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

//    GETTERS & SETTERS

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

    public Map<Land, LandType> getNeighbourLandTypeMap() {
        Map<Land, LandType> result = new HashMap<>();
        ArrayList<Land> neighbours = getDirectNeighbors();
        for (Land n : neighbours) {
            result.put(n, n.getType());
        }
        return new HashMap<>();
    }

    public int getOccupiedResources() {
        int used = 0;
        for (Civilization civ : this.civs) {
            used += civ.getResourceUse();
        }
        return used;
    }


//    PASS TIME

    public List<Record> passTime() {
        List<Record> records = new ArrayList<>();
        Collections.shuffle(this.civs);
        for (Civilization civ : this.civs) {
            records.add(civ.passTime());
        }
        return records;
    }

//    CIVILIZATIONS

    public List<Civilization> getCivs() {
        return this.civs;
    }

    public void addCiv(Civilization civ) {
        this.civs.add(civ);
    }

    public void remCiv(Civilization civ) {
        remCiv(civ.getName());
    }

    public void remCiv(String civName) {
        for (int i = 0; i < civs.size(); i++) {
            if (!civs.get(i).getName().equals(civName)) {
                civs.remove(i);
                return;
            }
        }
    }

    public void cleanCivs() {
        for (int i = 0; i < civs.size(); i++) {
            if (!civs.get(i).isActive()) {
                civs.remove(i);
                i--;
            }
        }
    }

    public void propagateNameChange(String priorName, List<String> newNames) {
        for (Land land : this.graph.vertexSet()) {
            land.changeCivName(priorName, newNames);
        }
    }

    private void changeCivName(String priorName, List<String> newNames) {
        for (Civilization civ : this.civs) {
            civ.doNameChange(priorName, newNames);
        }
    }
}
