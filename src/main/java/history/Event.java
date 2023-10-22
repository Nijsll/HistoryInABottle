package history;

import enums.EventOption;
import nation.Civilization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Event {
    private final String name;
    private int year;
    private Civilization party1;
    private Civilization party2;
    private boolean twoCivs;
    private Map<EventOption, Object> options;

//    CONSTRUCTION

    public Event(String name) {
        this.name = name;
        this.twoCivs = false;
        this.options = new HashMap<>();
    }

    public void setTwoCivs() {
        this.twoCivs = true;
    }


//    INITIALIZATION

    public void setParty1(Civilization party1) {
        this.party1 = party1;
    }

    public void setParty2(Civilization party2) {
        this.party2 = party2;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setOption(EventOption opt, Object val) {
        // TODO add validation
        this.options.put(opt, val);
    }


//    HANDLING

    public Record handleEvent1(Civilization civ) {
        // TODO
        return new Record();
    }

    public Record handleEvent2(Civilization civ1, Civilization civ2) {
        // TODO
        return new Record();
    }

//    OTHER

    public String getName() {
        return name;
    }

    public boolean isTwoCivs() {
        return twoCivs;
    }

    private Record getErrorRecord(String msg) {
        return new Record("Error", msg);
    }

    private boolean validateEvent() {
        boolean valid = true;
        // TODO

        return valid;
    }







}
