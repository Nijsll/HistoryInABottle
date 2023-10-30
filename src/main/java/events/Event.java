package events;

import Exceptions.HandleEventException;
import enums.*;
import history.Record;
import nation.Civilization;

import java.util.Map;

public class Event {
    private final String name;
    private final Civilization party1;

    private final boolean twoCivs;
    private final Civilization party2;
    private final Interaction interactionType;

    private final Map<EventOption, Object> options;
    private final Map<EventOption, Object> options2ndCiv;

//    CONSTRUCTION
    protected Event(String name, Civilization civ1, Map<EventOption, Object> options1) {
        this.name = name;
        this.party1 = civ1;
        this.options = options1;
        this.twoCivs = false;
        this.interactionType = null;
        this.party2 = null;
        this.options2ndCiv = null;
    }
    protected Event(String name, Civilization civ1, Map<EventOption, Object> options1, Interaction interactionType, Civilization civ2, Map<EventOption, Object> options2) {
        this.name = name;
        this.party1 = civ1;
        this.options = options1;
        this.twoCivs = true;
        this.interactionType = interactionType;
        this.party2 = civ2;
        this.options2ndCiv = options2;
    }

//    HANDLING

    public String handleEvent() throws HandleEventException {
        if (this.twoCivs) {
           return handleEvent1(this.party1);
        } else {
            return handleEvent2(this.party1, this.party2);
        }
    }

    private String handleEvent1(Civilization civ) throws HandleEventException {
        if (!this.twoCivs) throw new HandleEventException("Cannot handle event: Event is not an Event for 1 Civilization, yet handleEvent1 was called");
        if (!validateEvent()) throw new HandleEventException("Cannot handle event: event not valid");
        // TODO String description construction
        for (EventOption option : this.options.keySet()) {
            if (!civ.isActive()) return civ.getName() + " was destroyed";
            handleOption(option, this.options.get(option), civ);
        }
        return "1 Party Event: " + this.name;
    }

    private String handleEvent2(Civilization civ1, Civilization civ2) throws HandleEventException {
        if (!this.twoCivs) throw new HandleEventException("Cannot handle event: Event is not an Event for 2 Civilizations, yet handleEvent2 was called");
        if (!validateEvent()) throw new HandleEventException("Cannot handle event: event not valid");
        // TODO
        for (EventOption option : this.options.keySet()) {
            if (!civ1.isActive()) return civ1.getName() + " was destroyed";
            civ1 = handleOption(option, this.options.get(option), civ1);
        }
        for (EventOption option : this.options2ndCiv.keySet()) {
            if (!civ2.isActive()) return civ2.getName() + " was destroyed";
            civ2 = handleOption(option, this.options.get(option), civ2);
        }

        return "2 Party Event: " + this.name;
    }

    private Civilization handleOption(EventOption option, Object val, Civilization civ) {
        switch (option) {
            case Exile -> civ.setNomadic(true);
            case Settle -> civ.setNomadic(false);
            case AddDeity -> civ.getReligion().add((Deity) val);
            case RemoveDeity -> civ.getReligion().remove((Deity) val);
            case Splinter -> {
                return civ.split();
            }
            case Stabilize -> civ.setStable(true);
            case Destabilize -> civ.setStable(false);
            case SizeImpact -> {
                int impact = (int) val;
                if (impact > 0) {
                    civ.grow(Math.abs(impact));
                } else {
                    civ.shrink(Math.abs(impact));
                }
            }
            case ChangeLeader -> civ.setLeader((LeaderType) val);
            case GainAffinity -> civ.getAffinity().add(civ.getLocation().getType());
            case LoseAffinity -> civ.getAffinity().remove((LandType) val);
            case ChangeSpecialty -> civ.setSpeciality((Specialty) val);
            case LoseSpecialty -> civ.setSpeciality(Specialty.None);
            case ChangePopRace -> {
                String race = (String) val;
                int amount = (int) this.options.get(EventOption.ChangePopAmount);
                if (amount > 0) {
                    civ.getPopulation().growRaceBy(race, amount);
                } else {
                    civ.getPopulation().shrinkRaceBy(race, Math.abs(amount));
                }
            }
            case InitiativeImpact -> civ.setInitiative(civ.getInitiative()+ (int) val);
            case LandResourcesImpact -> civ.getLocation().changeResources((int) val);

        }
        return civ;
    }

//    OTHER

    public String getName() {
        return name;
    }

    public boolean isTwoCivs() {
        return twoCivs;
    }

    public static Record getErrorRecord(String msg) {
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
