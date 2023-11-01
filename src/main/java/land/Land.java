package land;

import enums.LandType;
import history.Record;
import nation.Civilization;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import util.Util;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Land {
    private String name;
    private LandType type;
    private List<Civilization> civs;

    private int resources;
    private int baseResources;
    private int space;

    private Set<String> ruins;

    private Graph<Land, DefaultEdge> graph;

//    CONSTRUCTION

    public Land(Graph<Land, DefaultEdge> graph, String name, LandType type, int baseResources, int space) {
        this.name = name;
        this.type = type;
        this.civs = new CopyOnWriteArrayList<>();
        this.baseResources = baseResources;
        this.resources = baseResources;
        this.space = space;
        this.graph = graph;
        this.ruins = new HashSet<>();
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
        return result;
    }

//    RESOURCES

    public int getOccupiedResources(boolean countNomads) {
        int used = 0;
        for (Civilization civ : this.civs) {
            if (countNomads || !civ.isNomadic()) {
                used += civ.getResourceUse();
            }
        }
        return used;
    }

    public int getAvailableResources(boolean countNomads) {
        int occ = getOccupiedResources(countNomads);
        return this.resources - occ;
    }

    public boolean isAbundant() {
        return getAvailableResources(true) > (Math.floorDiv(this.resources, 2));
    }

//    PASS TIME

    public List<Record> passTime() {
        List<Record> records = new ArrayList<>();
        Collections.shuffle(this.civs);
        for (Civilization civ : this.civs) {
            records.add(civ.passTime());
        }
        cleanCivs();

        if (getAvailableResources(true) < 0) {
            this.resources--;
        }

        return records;
    }

    public List<Record> migration() {
        List<Record> recs = new ArrayList<>();
        for (Civilization civ : this.civs) {
            recs.add(civ.tryMigrate());
        }
        return recs;
    }

    public void endTurn() {
        this.civs.forEach(Civilization::newTurn);
    }

//    CIVILIZATIONS

    public List<Civilization> getCivs() {
        return this.civs;
    }

    public void addCiv(Civilization civ) {
        civ.setLocation(this);
        this.civs.add(civ);

    }

    public void remCiv(Civilization civ) {
        remCiv(civ.getName());
    }

    public void remCiv(String civName) {
        for (int i = 0; i < civs.size(); i++) {
            if (civs.get(i).getName().equals(civName)) {
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

    public void addToRuins(String civName) {
        this.ruins.add(civName);
    }

    private void changeCivName(String priorName, List<String> newNames) {
        for (Civilization civ : this.civs) {
            civ.doNameChange(priorName, newNames);
        }
    }

//    TO STRING
    @Override
    public String toString() {
        return String.format("%-25s | %-5s | %-5s | %-5s | %-5s", this.name, this.civs.size(), this.resources, this.getAvailableResources(true), Util.getBoolString(isAbundant()));
    }

    public static String getHeaderString() {
        return String.format("%-25s | %-5s | %-5s | %-5s | %-5s", "Name", "#Civs", "Res", "Ava", "Ab");
    }
}
