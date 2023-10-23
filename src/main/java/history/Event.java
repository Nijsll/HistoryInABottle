package history;

import Exceptions.HandleEventException;
import enums.EventOption;
import enums.Interaction;
import nation.Civilization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Event {
    private final String name;
    private int year;
    private Civilization party1;

    private boolean twoCivs;
    private Civilization party2;
    private Interaction interactionType;

    private Map<EventOption, Object> options;
    private boolean built;

//    CONSTRUCTION

    public Event(String name) {
        this.name = name;
        this.twoCivs = false;
        this.options = new HashMap<>();
        this.built = false;
    }

    public void setTwoCivs(Interaction interaction) {
        this.twoCivs = true;
        this.interactionType = interaction;
    }

    public void setOption(EventOption opt, Object val) {
        if (this.built) return;
        // TODO add validation
        this.options.put(opt, val);
    }


//    INITIALIZATION

    public Event build() {
        try {
            Event e = (Event) this.clone();
            e.built = true;
            return e;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setParty1(Civilization party1) {
        this.party1 = party1;
    }

    public Interaction getInteractionType() {
        return interactionType;
    }

    public void setParty2(Civilization party2) {
        this.party2 = party2;
    }



//    HANDLING

    public String handleEvent1(Civilization civ) throws HandleEventException {
        if (!this.built) throw new HandleEventException("Cannot handle event: Event has not yet been built");
        if (!this.twoCivs) throw new HandleEventException("Cannot handle event: Event is not an Event for 1 Civilization, yet handleEvent1 was called");
        // TODO
        return "1 Party Event";
    }

    public String handleEvent2(Civilization civ1, Civilization civ2) throws HandleEventException {
        if (!this.built) throw new HandleEventException("Cannot handle event: Event has not yet been built");
        if (!this.twoCivs) throw new HandleEventException("Cannot handle event: Event is not an Event for 2 Civilizations, yet handleEvent2 was called");
        // TODO
        return "2 Party Event";
    }

//    OTHER

    public String getName() {
        return name;
    }

    public boolean isTwoCivs() {
        return twoCivs;
    }

    public Record getErrorRecord(String msg) {
        Record r = new Record("Error");
        r.addEvent(msg);
        return r;
    }

    private boolean validateEvent() {
        boolean valid = true;
        // TODO

        return valid;
    }







}
