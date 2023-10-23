package nation;

import Exceptions.HandleEventException;
import enums.*;
import history.Event;
import history.EventHandler;
import history.Record;
import land.Land;
import util.UtilFunctions;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Civilization {
    private String name;

    private boolean nomadic;
    private Land location;

    private int size;
    private Population population;
    private boolean stable;

    private Set<String> customs;
    private List<LandType> affinity;
    private Set<String> allies;
    private Set<String> enemies;
    private LeaderType leader;
    private Set<Deity> religion;
    private Specialty speciality;
    private List<String> ancestors;

    private boolean aggressive;
    private boolean active;
    private int initiative;

    public Civilization(Land loc, String name, LeaderType leader, Deity deity, String startRace, boolean aggressive) {
        this.name = name;
        this.customs = new HashSet<>();
        this.location = loc;
        this.size = 1;
        this.nomadic = false;
        this.affinity = new ArrayList<>();
        this.affinity.add(loc.getType());
        this.allies = new HashSet<>();
        this.enemies = new HashSet<>();
        this.leader = leader;
        this.religion = new HashSet<>();
        this.religion.add(deity);
        this.population = new Population(startRace);
        this.stable = true;
        this.aggressive = aggressive;
        this.ancestors = new ArrayList<>();
        this.ancestors.add(name);

        this.active = true;
        this.initiative = 1;
    }

    public Civilization(Civilization civ) {
        this.name = civ.name;

        this.nomadic = civ.nomadic;
        this.location = civ.location;

        this.size = civ.size;
        this.population = new Population(civ.population);
        this.stable = civ.stable;

        this.customs = new HashSet<>();
        this.customs.addAll(civ.customs);
        this.affinity = new ArrayList<>();
        this.affinity.addAll(civ.affinity);
        this.allies = new HashSet<>();
        this.allies.addAll(civ.allies);
        this.enemies = new HashSet<>();
        this.enemies.addAll(civ.enemies);
        this.leader = civ.leader;
        this.religion = new HashSet<>();
        this.religion.addAll(civ.religion);
        this.speciality = civ.speciality;
        this.ancestors = new ArrayList<>();
        this.ancestors.addAll(civ.ancestors);

        this.aggressive = civ.aggressive;
        this.active = civ.active;
        this.initiative = civ.initiative;
    }

//    INFO SELF

    public String getName() {
        return this.name;
    }

//    CHANGE SELF

    public void destroy() {
        this.active = false;
    }

    public void split() {
//        Create 2 "halves"
        Civilization civ1 = new Civilization(this);
        civ1.population.setSize(population.getSize()/2);
        civ1.size = civ1.size/2;
        Civilization civ2 = new Civilization(civ1);

//        Setup 1
        civ1.name = this.name + "-1";
        civ1.ancestors.add(civ1.name);
        this.location.addCiv(civ1);

//        Setup 2
        civ2.name = this.name + "-2";
        civ2.ancestors.add(civ2.name);
        this.location.addCiv(civ2);

//        End
        destroy();
    }

    public void migrate() {
        // Get Destination
        Land destination = getDestination();
        destination.addCiv(this);
        this.location.remCiv(this.name);
        this.location = destination;
    }

    public void grow(int amount) {
        this.size += amount;
    }

    public void shrink(int amount) {
        this.size -= amount;
    }

//    INTERACTION

    private List<Civilization> getNeighbours(Interaction interaction) {
        List<Civilization> allNeighbours = this.location.getCivs();
        List<Civilization> result = new ArrayList<>();
        for (Civilization civ : allNeighbours) {
            if (filterCiv(civ, interaction)) result.add(civ);
        }
        return result;
    }

    // Returns false if civ = this
    private boolean filterCiv(Civilization civ, Interaction interaction) {
        if (!civ.active) return false;
        String civName = civ.getName();
        if (civName.equals(this.name)) return false;
        switch (interaction) {
            case All -> {
                return true;
            }
            case Allies -> {
                return this.allies.contains(civName);
            }
            case Enemies -> {
                return this.enemies.contains(civName);
            }
            case Neutral -> {
                return (!this.enemies.contains(civName) && !this.allies.contains(civName));
            }
            case NoAllies -> {
                return !this.allies.contains(civName);
            }
            case NoEnemies -> {
                return !this.enemies.contains(civName);
            }
        }
        return false;
    }

    public void consume(Civilization civ) {
        this.size += civ.size;
        civ.destroy();
    }

    public void absorb(Civilization civ) {
        this.population.add(civ.population);
        this.religion.addAll(civ.religion);
        this.customs.addAll(civ.customs);
        this.ancestors.addAll(civ.ancestors);
        this.size += civ.size;
        civ.destroy();
    }

    public int rateCompatibility(Civilization civ) {
        int comp = 0;
        if (civ.ancestors.contains(this.ancestors.get(0))) comp += 5;
        if (this.speciality.equals(civ.speciality)) comp += 2;
        if (this.aggressive != civ.aggressive) comp -= 10;
        for (String custom : this.customs) {
            if (civ.customs.contains(custom)) comp++;
        }
        for (String ally : this.allies) {
            if (civ.allies.contains(ally)) {
                comp++;
            }
            if (civ.enemies.contains(ally)) {
                comp--;
            }
        }
        for (String enemy : this.enemies) {
            if (civ.allies.contains(enemy)) {
                comp--;
            }
            if (civ.enemies.contains(enemy)) {
                comp++;
            }
        }
        for (Deity deity : this.religion) {
            if (civ.religion.contains(deity)) comp++;
        }
        return comp;
    }


//    EVENTS

    public Record passTime() {
        Record r = new Record(this.name + " - " + (new Date()));
        if (!this.active) {
            r.addEvent(this.name + " was destroyed.");
            return r;
        }

//        EVENT
        if (rollInitiative()) {
            Event event = getEvent();
            try {
                if (!event.isTwoCivs()) {
                    r.addEvent(event.handleEvent1(this));
                } else {
                    // Get Correct neighbour
                    List<Civilization> neighbours = getNeighbours(event.getInteractionType());
                    if (neighbours.size() == 0) {
                        System.out.println("Civ " + this.name + " failed to get second civ for event");
                    } else {
                        Civilization chosenCiv = neighbours.get(ThreadLocalRandom.current().nextInt(0, neighbours.size())+1);
                        r.addEvent(event.handleEvent2(this, chosenCiv));
                    }

                }
            } catch (HandleEventException e) {
                return event.getErrorRecord(e.getMessage());
            }

        }

        if (!this.active) {
            r.addEvent(this.name + " was destroyed.");
            return r;
        }

//        Nomadic
        if (this.nomadic) {
            if (this.size > 1) {
                this.size = 1;
                this.population.setSize(1);
            }
            migrate();
        }

//        Population control
        if (this.size > this.population.getSize()) {
            this.population.growBy(1);
            r.addEvent("Population grew.");
        } else if (this.size < this.population.getSize()) {
            this.population.shrinkBy(1);
            r.addEvent("Population shrunk.");
            // Maybe make civ non-stable in this case
        }

//        Destruction
        if (this.size == 0) {
            this.destroy();
            r.addEvent(this.name + " was destroyed since their size dropped to 0.");
        } else if (this.population.getSize() == 0) {
            this.destroy();
            r.addEvent(this.name + " was destroyed since their population dropped to 0.");
        }

        return r;
    }

    public boolean rollInitiative() {
        double n = Math.random();
        if (n < (1.0/this.initiative)) {
            // EVENT HAPPENS
            this.initiative = 5;
            return true;
        } else {
            // EVENT DOES NOT HAPPEN
            this.initiative--;
            return false;
        }

    }

    /*
        Gets and builds random event from possible events
     */
    private Event getEvent() {
        List<Event> events = EventHandler.getEvents(this);
        return events.get(ThreadLocalRandom.current().nextInt(0, events.size()+1)).build();
    }

    private Record getNullEvent() {
        Record r = new Record("Null");
        r.addEvent("Nothing happened.");
        return r;
    }

//    HELPER FUNCTIONS
    private Land getDestination() {
        Map<Land, LandType> lands = this.location.getNeighbourLandTypeMap();
        Set<Land> possible = new HashSet<>();
        for (Land land : lands.keySet()) {
            if (this.affinity.contains(lands.get(land)) || (land.getSpace() > land.getOccupiedResources())) {
                possible.add(land);
            }
        }
        if (possible.size() == 0) {
            possible.addAll(lands.keySet());
        }
        return UtilFunctions.getRandomFromSet(possible);
    }


}
