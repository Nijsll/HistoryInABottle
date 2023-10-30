package events;

import Exceptions.ConstructEventException;
import enums.EventOption;
import enums.Interaction;
import nation.Civilization;

import java.util.Map;

public class EventBase {
    private String eventName;
    private int weight;

    private Map<EventOption, Object> options;

    private boolean twoCivs;
    private Interaction interactionType;
    private Map<EventOption, Object> options2ndCiv;

    public EventBase(String name, int weight) {
        this.eventName = name;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setTwoCivs(Interaction interaction) {
        this.twoCivs = true;
        this.interactionType = interaction;
    }

    public void setOption(EventOption opt, Object val) throws ConstructEventException {
        if (!validateOption(opt, val)) throw new ConstructEventException("Invalid value for option: " + opt.toString() + ": " + (val.getClass().getName()) + ".");
        this.options.put(opt, val);
    }

    public void setOptions2ndCiv(EventOption opt, Object val) throws ConstructEventException {
        if (!this.twoCivs) return;
        if (!validateOption(opt, val)) throw new ConstructEventException("Invalid value for option: " + opt.toString() + ": " + (val.getClass().getName()) + ".");
        this.options2ndCiv.put(opt, val);
    }

    private boolean validateOption(EventOption opt, Object val) {
        // TODO
        return true;
    }

    private boolean validateEvent() {
        // TODO
        return true;
    }

//    BUILDING

    public boolean isTwoCivs() { return this.twoCivs;}

    public Interaction getInteractionType() {
        return this.interactionType;
    }

    public Event build(Civilization civ) throws ConstructEventException {
        if (!validateEvent()) throw new ConstructEventException("Event could not be validated.");
        if (twoCivs) {
            throw new ConstructEventException("Attempted to build a TwoCiv Event while only giving one Civilization.");
        } else {
            return new Event(this.eventName, civ, this.options);
        }
    }

    public Event build(Civilization civ1, Civilization civ2) throws ConstructEventException {
        if (!validateEvent()) throw new ConstructEventException("Event could not be validated.");
        if (!twoCivs) {
            throw new ConstructEventException("Attempted to build a OneCiv Event while giving two Civilizations.");
        } else {
            return new Event(this.eventName, civ1, this.options, this.interactionType, civ2, this.options2ndCiv);
        }
    }
}
