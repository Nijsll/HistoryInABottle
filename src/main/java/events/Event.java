package events;

import Exceptions.HandleEventException;
import enums.*;
import history.Record;
import nation.Civilization;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Event {
    private final String name;
    private final Civilization party1;

    private final boolean twoCivs;
    private final Civilization party2;
    private final Interaction interactionType;

    private final Map<EventOption, Object> options;
    private final Map<EventOption, Object> options2ndCiv;

    private String log;

//    CONSTRUCTION
    protected Event(String name, Civilization civ1, Map<EventOption, Object> options1) {
        this.name = name;
        this.log = "Event " + name + ": ";
        this.party1 = civ1;
        this.options = options1;
        this.twoCivs = false;
        this.interactionType = null;
        this.party2 = null;
        this.options2ndCiv = null;
    }
    protected Event(String name, Civilization civ1, Map<EventOption, Object> options1, Interaction interactionType, Civilization civ2, Map<EventOption, Object> options2) {
        this.name = name;
        this.log = "Event " + name + ": ";
        this.party1 = civ1;
        this.options = options1;
        this.twoCivs = true;
        this.interactionType = interactionType;
        this.party2 = civ2;
        this.options2ndCiv = options2;
    }

//    HANDLING

    public String handleEvent() throws HandleEventException {
        if (!this.twoCivs) {
           return handleEvent1(this.party1);
        } else {
            return handleEvent2(this.party1, this.party2);
        }
    }

    private String handleEvent1(Civilization civ) throws HandleEventException {
        if (this.twoCivs) throw new HandleEventException("Cannot handle event: Event is not an Event for 2 Civilizations, yet handleEvent1 was called");
        if (!validateEvent()) throw new HandleEventException("Cannot handle event: event not valid");
        for (EventOption option : this.options.keySet()) {
            if (!civ.isActive()) return civ.getName() + " was destroyed";
            handleOption(option, this.options.get(option), civ);
        }
        return this.log;
    }

    private String handleEvent2(Civilization civ1, Civilization civ2) throws HandleEventException {
        if (!this.twoCivs) throw new HandleEventException("Cannot handle event: Event is not an Event for 1 Civilization, yet handleEvent2 was called");
        if (!validateEvent()) throw new HandleEventException("Cannot handle event: event not valid");
        for (EventOption option : this.options.keySet()) {
            if (!civ1.isActive()) return civ1.getName() + " was destroyed";
            civ1 = handleOption(option, this.options.get(option), civ1);
        }
        for (EventOption option : this.options2ndCiv.keySet()) {
            if (!civ2.isActive()) return civ2.getName() + " was destroyed";
            civ2 = handleOption(option, this.options.get(option), civ2);
        }

        return this.log;
    }

    private Civilization handleOption(EventOption option, Object val, Civilization civ) {
        switch (option) {
            case Exile -> {
                civ.setNomadic(true);
                log += civ.getName() + " became nomadic; ";
            }
            case Settle -> {
                if (civ.getLocation().getAvailableResources(true) > 0) {
                    civ.setNomadic(false);
                    log += civ.getName() + " settled in " + civ.getLocation().getName() + "; ";
                }

            }
            case AddDeity -> {
                civ.getReligion().add((Deity) val);
                log += civ.getName() + " started worshipping " + ((Deity) val).name() + "; ";
            }
            case RemoveDeity -> {
                civ.getReligion().remove((Deity) val);
                log += civ.getName() + " stopped worshipping " + ((Deity) val).name() + "; ";
            }
            case Splinter -> {
                if (civ.getSize() > 2 && civ.getPopulation().getSize() > 2) {
                    Civilization[] newCivs = civ.splinter();
                    log += civ.getName() + " split into " + newCivs[0].getName() + " and " + newCivs[1].getName() + "; ";
                    return newCivs[0];
                }
            }
            case Stabilize -> {
                civ.setStable(true);
                log += civ.getName() + " was stabilized; ";
            }
            case Destabilize -> {
                civ.setStable(false);
                log += civ.getName() + " became unstable; ";
            }
            case SizeImpact -> {
                log += civ.getName() + "'s size changed from " + civ.getSize();
                int impact = (int) val;
                if (impact > 0) {
                    civ.grow(Math.abs(impact));
                } else {
                    civ.shrink(Math.abs(impact));
                }
                log += " to " + civ.getSize() + "; ";
            }
            case ChangeLeader -> {
                civ.setLeader((LeaderType) val);
                log += civ.getName() + " became ruled by a " + ((LeaderType) val).name() + "; ";
            }
            case GainAffinity -> {
                civ.getAffinity().add(civ.getLocation().getType());
                log += civ.getName() + " gained affinity with " + civ.getLocation().getType().name() + "; ";
            }
            case LoseAffinity -> {
                LandType current = civ.getLocation().getType();
                for (LandType landType : civ.getAffinity()) {
                    if (landType != current) {
                        log += civ.getName() + " lost affinity with " + landType.name() + "; ";
                        civ.getAffinity().remove(landType);
                        return civ;
                    }
                }
                log += civ.getName() + " did not lose any affinity; ";
            }
            case ChangeSpecialty -> {
                civ.setSpeciality((Specialty) val);
                log += civ.getName() + "'s specialty was changed to " + ((Specialty) val).name() + "; ";
            }
            case LoseSpecialty -> {
                civ.setSpeciality(Specialty.None);
                log += civ.getName() + " lost their specialty; ";
            }
            case ChangePopRace -> {
                String race = (String) val;
                int amount = (int) this.options.get(EventOption.ChangePopAmount);
                if (amount > 0) {
                    civ.getPopulation().growRaceBy(race, amount);
                    log += civ.getName() + "'s " + race + " population grew by" + amount + "; ";
                } else {
                    int abs = Math.abs(amount);
                    civ.getPopulation().shrinkRaceBy(race, abs);
                    log += civ.getName() + "'s " + race + " population shrunk by" + abs + "; ";
                }
            }
            case InitiativeImpact -> {
                civ.setInitiative(civ.getInitiative() + (int) val);
                log += civ.getName() + "'s initiative was changed by " + ((int) val) + "; ";
            }
            case LandResourcesImpact -> {
                civ.getLocation().changeResources((int) val);
                log += civ.getLocation().getName() + "'s resource amount was changed by " + ((int) val) + "; ";
            }
            case Destroy -> {
                double chance = (double) val;
                if (ThreadLocalRandom.current().nextDouble(1.0) < chance) {
                    civ.destroy();
                    log += civ.getName() + " was destroyed (" + (chance*100) + "%); ";
                }
            }

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
