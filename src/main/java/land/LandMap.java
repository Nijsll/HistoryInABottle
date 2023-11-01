package land;

import enums.Deity;
import enums.LandType;
import enums.LeaderType;
import history.Record;
import nation.Civilization;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.ArrayList;
import java.util.List;

public class LandMap {
    private final Graph<Land, DefaultEdge> graph;



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

    public List<Land> getAllOccupiedLands() {
        List<Land> res = new ArrayList<>();
        for (Land l : graph.vertexSet()) {
            if (l.getCivs().size() > 0) res.add(l);
        }
        return res;
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

//    PASS TIME

    public List<Record> passTime() {
        List<Record> records = new ArrayList<>();
        for (Land land : graph.vertexSet()) {
            records.addAll(land.migration());
        }
        for (Land land : graph.vertexSet()) {
            records.addAll(land.passTime());
        }
        endTurn();
        return records;
    }

    private void endTurn() {
        this.graph.vertexSet().forEach(Land::endTurn);
    }

//    CONSTRUCTION

    public LandMap() {
        System.out.print("Initializing Map...");
        this.graph = new DefaultUndirectedGraph<>(DefaultEdge.class);

//        Create Lands
        Land reaches = new Land(graph, "Frozen Reaches", LandType.Tundra, 2, 20);
        Land wintercairn = new Land(graph, "Wintercairn Forest", LandType.Tundra, 5, 10);
        Land heightsWest = new Land(graph, "Norden Heights (west)", LandType.Mountains, 8, 12);
        Land heightsEast = new Land(graph, "Norden Heights (east)", LandType.Mountains, 8, 12);;
        Land novaCoast = new Land(graph, "Nova Magus Coast", LandType.Coast, 12, 8);
        Land marshes = new Land(graph, "Still Marshes", LandType.Swamp, 5, 10);
        Land titanleaf = new Land(graph, "Titanleaf Woodlands", LandType.Forest, 15, 15);
        Land vieronne = new Land(graph, "Vieronne Valley", LandType.Hills, 15, 10);
        Land redwood = new Land(graph, "Redwood Forest", LandType.Forest, 12, 12);
        Land gaeanCoast = new Land(graph, "Gaean Coast", LandType.Coast, 8, 10);
        Land gaeanMountains = new Land(graph, "Gaean Mountains", LandType.Mountains, 10, 10);
        Land marble = new Land(graph, "Marble Lake", LandType.Hills, 10, 8);
        Land zelhadrimWest = new Land(graph, "Zelhadrim Desert (west)", LandType.Desert, 5, 20);
        Land zelhadrimEast = new Land(graph, "Zelhadrim Desert (east)", LandType.Desert, 5, 20);
        Land madan = new Land(graph, "Madanian Coast", LandType.Coast, 4, 5);
        Land clawed = new Land(graph, "Clawed Coast", LandType.Coast, 10, 15);

        graph.addVertex(reaches);
        graph.addVertex(wintercairn);
        graph.addVertex(heightsWest);
        graph.addVertex(heightsEast);
        graph.addVertex(novaCoast);
        graph.addVertex(marshes);
        graph.addVertex(titanleaf);
        graph.addVertex(vieronne);
        graph.addVertex(redwood);
        graph.addVertex(gaeanCoast);
        graph.addVertex(gaeanMountains);
        graph.addVertex(marble);
        graph.addVertex(zelhadrimWest);
        graph.addVertex(zelhadrimEast);
        graph.addVertex(madan);
        graph.addVertex(clawed);

//        Add Edges
        graph.addEdge(reaches, wintercairn);
        graph.addEdge(reaches, heightsWest);
        graph.addEdge(reaches, heightsEast);
        graph.addEdge(wintercairn, novaCoast);
        graph.addEdge(wintercairn, heightsWest);
        graph.addEdge(heightsWest, heightsEast);
        graph.addEdge(heightsWest, novaCoast);
        graph.addEdge(heightsWest, titanleaf);
        graph.addEdge(heightsEast, vieronne);
        graph.addEdge(heightsEast, redwood);
        graph.addEdge(novaCoast, marshes);
        graph.addEdge(novaCoast, titanleaf);
        graph.addEdge(marshes, titanleaf);
        graph.addEdge(marshes, vieronne);
        graph.addEdge(titanleaf, vieronne);
        graph.addEdge(vieronne, redwood);
        graph.addEdge(vieronne, gaeanCoast);
        graph.addEdge(redwood, gaeanCoast);
        graph.addEdge(redwood, gaeanMountains);
        graph.addEdge(gaeanCoast, gaeanMountains);
        graph.addEdge(gaeanCoast, zelhadrimEast);
        graph.addEdge(gaeanMountains, zelhadrimEast);
        graph.addEdge(zelhadrimEast, zelhadrimWest);
        graph.addEdge(zelhadrimEast, marble);
        graph.addEdge(zelhadrimEast, madan);
        graph.addEdge(zelhadrimWest, madan);
        graph.addEdge(zelhadrimWest, clawed);

//        CREATE CIVILIZATIONS
        Civilization underbourne = new Civilization(heightsWest, "Underbourne Dwarves", LeaderType.Monarch, Deity.Moradin,"Dwarf", false);
        Civilization naporia = new Civilization(gaeanMountains, "Naporian Empire", LeaderType.Emperor, Deity.Corellon, "Elf", false);
        Civilization shaar = new Civilization(titanleaf, "Shaar Kingdom", LeaderType.Monarch, Deity.Anvandra, "Halfling", false);
        Civilization aelandria = new Civilization(novaCoast, "Aelandria", LeaderType.Saint, Deity.Cytheria, "Human", false);
        Civilization kajar = new Civilization(clawed, "El Kajar", LeaderType.Emperor, Deity.Pelor, "Human", false);
        Civilization sorval = new Civilization(marble, "Sorval", LeaderType.Saint, Deity.Erathis, "Human", false);
        sorval.getPopulation().growRaceBy("Dwarf", 100);
        sorval.getPopulation().growRaceBy("Elf", 100);
        sorval.getPopulation().setSize(1);

        Civilization gobbo = new Civilization(marshes, "Bloodhawk Clan", LeaderType.General, Deity.Maglubiyet, "Goblin", true);
        Civilization fist = new Civilization(heightsEast, "Fist of Gruumsh", LeaderType.General, Deity.Gruumsh, "Orc", true);
        Civilization wyrm = new Civilization(zelhadrimWest, "Great Wyrm Cult", LeaderType.Saint, Deity.Torog, "Human", true);

        System.out.print(" Done\n");
    }
}
