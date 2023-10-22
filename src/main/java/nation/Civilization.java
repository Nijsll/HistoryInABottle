package nation;

import enums.*;
import history.Event;
import history.EventHandler;
import history.Record;
import land.Land;

import java.util.ArrayList;
import java.util.List;

public class Civilization {
    private String name;
    private List<String> customs;
    private Land location;
    private int size;
    private boolean nomadic;
    private List<LandType> affinity;
    private List<String> allies;
    private List<String> enemies;
    private LeaderType leader;
    private List<Deity> religion;
    private Specialty speciality;
    private Population population;
    private boolean stable;

    private int initiative;

    public Civilization(Land loc, String name, LeaderType leader, Deity deity, String startRace) {
        this.name = name;
        this.customs = new ArrayList<>();
        this.location = loc;
        this.size = 1;
        this.nomadic = false;
        this.affinity = new ArrayList<>();
        this.affinity.add(loc.getType());
        this.allies = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.leader = leader;
        this.religion = new ArrayList<>();
        this.religion.add(deity);
        this.population = new Population(startRace);
        this.stable = true;

        this.initiative = 1;
    }

//    INFO SELF

    public String getName() {
        return this.name;
    }

//    CHANGE SELF

    public void destroy() {
        // TODO
    }

    public void split() {
        // TODO
    }

    public void migrate() {
        // TODO
    }

    public void grow() {
        // TODO
    }

    public void shrink() {
        // TODO
    }

//    INTERACTION

    private List<Civilization> getNeighbours() {
        // TODO
        return new ArrayList<>();
    }

    public void absorb(Civilization civ) {
        // TODO
    }

    public int rateCompatibility(Civilization civ) {
        // TODO
        return 0;
    }


//    EVENTS

    public Record passTime() {
        // TODO
        return new Record();
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

    private List<Event> getEvents() {
        return EventHandler.getEvents(this);
    }

    private Record getNullEvent() {
        return new Record("Null", "Nothing happened");
    }
}
