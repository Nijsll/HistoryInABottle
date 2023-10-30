package events;

import Exceptions.ConstructEventException;
import enums.EventOption;
import nation.Civilization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class EventHandler {
    private static EventHandler instance = null;
    private static final Map<String, List<EventBase>> events = new HashMap<>();

    public static EventBase getEvent(Civilization civ) {
        if (instance == null) {
            synchronized (EventHandler.class) {
                if (instance == null) {
                    instance = new EventHandler();
                }
            }
        }

        List<EventBase> possible = new ArrayList<>();
        // TODO get correct events
        possible.addAll(events.get("All"));
        if (civ.isNomadic()) {
            possible.addAll(events.get("Nomadic"));
        } else {
            possible.addAll(events.get("Settled"));
        }

        // Get weight
        int totalWeight = 0;
        for (EventBase base : possible) {
            totalWeight += base.getWeight();
        }
        int chosen = ThreadLocalRandom.current().nextInt(0, totalWeight);
        for (EventBase base : possible) {
            chosen -= base.getWeight();
            if (chosen <= 0) {
                return base;
            }
        }
        System.out.println("Incorrect EventBase was returned to " + civ.getName());
        return new EventBase("Incorrect", 0);
    }

    private EventHandler() {
        // TODO make events
        // Categories
        events.put("All", new ArrayList<>());
        events.put("Nomadic", new ArrayList<>());
        events.put("Settled", new ArrayList<>());


        try {
//        EVENTS ALL
            String cat = "All";
            List<EventBase> eventBases = new ArrayList<>();
            EventBase base = new EventBase("grow", 10);
            base.setOption(EventOption.SizeImpact, 1);
            eventBases.add(base);

            base = new EventBase("shrink", 5);
            base.setOption(EventOption.SizeImpact, -1);
            eventBases.add(base);

            this.events.put(cat, eventBases);

//        NOMADIC
            cat = "Nomadic";
            eventBases = new ArrayList<>();
            base = new EventBase("settle", 5);
            base.setOption(EventOption.Settle, true);
            eventBases.add(base);

            this.events.put(cat, eventBases);

//        SETTLED
            cat = "Settled";
            eventBases = new ArrayList<>();
            base = new EventBase("gainAffinity", 5);
            base.setOption(EventOption.GainAffinity, true);
            eventBases.add(base);

            base = new EventBase("exile", 1);
            base.setOption(EventOption.Exile, true);
            eventBases.add(base);

            this.events.put(cat, eventBases);
        } catch (ConstructEventException e) {
            throw new RuntimeException(e);
        }


    }

}

