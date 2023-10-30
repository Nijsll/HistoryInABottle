package nation;

import Exceptions.ConstructEventException;
import Exceptions.HandleEventException;
import enums.*;
import events.Event;
import events.EventBase;
import events.EventHandler;
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
        this.speciality = Specialty.None;

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
        removeFromOthers();
    }

    public Civilization split() {
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
        changeName(new ArrayList<>(Arrays.asList(civ1.getName(), civ2.getName())));
        destroy();
        return civ1;
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
        this.size = Math.max(0, this.size-amount);
    }

    private void changeName(List<String> newNames) {
        this.location.propagateNameChange(this.name, newNames);
    }

    private void removeFromOthers() {
        changeName(new ArrayList<>());
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

    public void doNameChange(String priorName, List<String> newNames) {
        if (this.allies.contains(priorName)) {
            this.allies.remove(priorName);
            this.allies.addAll(newNames);
        }
        if (this.enemies.contains(priorName)) {
            this.enemies.remove(priorName);
            this.enemies.addAll(newNames);
        }
    }

//    EVENTS

    public Record passTime() {
        Record rec = new Record(this.name + " - " + (new Date()));
        if (!this.active) {
            rec.addEvent(this.name + " was destroyed.");
            return rec;
        }

//        EVENT
        if (rollInitiative()) {
            EventBase eventBase = EventHandler.getEvent(this);
            Event event;
            try {
                if (!eventBase.isTwoCivs()) {
                    event = eventBase.build(this);
                    rec.addEvent(event.handleEvent());
                } else {
                    // Get Correct neighbour
                    List<Civilization> neighbours = getNeighbours(eventBase.getInteractionType());
                    if (neighbours.size() == 0) {
                        System.out.println("Civ " + this.name + " failed to get second civ for event");
                    } else {
                        Civilization chosenCiv = neighbours.get(ThreadLocalRandom.current().nextInt(0, neighbours.size())+1);
                        event = eventBase.build(this, chosenCiv);
                        rec.addEvent(event.handleEvent());
                    }

                }
            } catch (ConstructEventException | HandleEventException e) {
                return Event.getErrorRecord(e.getMessage());
            }

        }

        if (!this.active) {
            rec.addEvent(this.name + " was destroyed.");
            return rec;
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
            rec.addEvent("Population grew.");
        } else if (this.size < this.population.getSize()) {
            this.population.shrinkBy(1);
            rec.addEvent("Population shrunk.");
            // Maybe make civ non-stable in this case
        }

//        Destruction
        if (this.size == 0) {
            this.destroy();
            rec.addEvent(this.name + " was destroyed since their size dropped to 0.");
        } else if (this.population.getSize() == 0) {
            this.destroy();
            rec.addEvent(this.name + " was destroyed since their population dropped to 0.");
        }

        return rec;
    }

    public boolean rollInitiative() {
        double n = Math.random();
        if (n < (1.0/this.initiative)) {
            // EVENT HAPPENS
            this.initiative = Math.min(this.initiative + 4, 10);
            return true;
        } else {
            // EVENT DOES NOT HAPPEN
            this.initiative--;
            return false;
        }

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

    public int getResourceUse() {
        int usage = this.size;
        LandType type = this.location.getType();
        for (LandType land : this.affinity) {
            if (land.equals(type)) {
                usage--;
            }
        }
        return Math.max(1, usage);
    }

//    GETTERS & SETTERS

    public boolean isNomadic() {
        return nomadic;
    }

    public Land getLocation() {
        return location;
    }

    public int getSize() {
        return size;
    }

    public Population getPopulation() {
        return population;
    }

    public boolean isStable() {
        return stable;
    }

    public Set<String> getCustoms() {
        return customs;
    }

    public List<LandType> getAffinity() {
        return affinity;
    }

    public Set<String> getAllies() {
        return allies;
    }

    public Set<String> getEnemies() {
        return enemies;
    }

    public LeaderType getLeader() {
        return leader;
    }

    public Set<Deity> getReligion() {
        return religion;
    }

    public Specialty getSpeciality() {
        return speciality;
    }

    public List<String> getAncestors() {
        return ancestors;
    }

    public boolean isAggressive() {
        return aggressive;
    }

    public boolean isActive() {
        return active;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNomadic(boolean nomadic) {
        this.nomadic = nomadic;
    }

    public void setSize(int size) {
        this.size = Math.max(0, size);
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }

    public void setLeader(LeaderType leader) {
        this.leader = leader;
    }

    public void setSpeciality(Specialty speciality) {
        this.speciality = speciality;
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }
}
