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

    public static void init() {
        System.out.print("Initializing Event Handler...");
        if (instance == null) {
            synchronized (EventHandler.class) {
                if (instance == null) {
                    instance = new EventHandler();
                }
            }
        }
        System.out.print(" Done\n");
    }

    public static EventBase getEvent(Civilization civ) {
        if (instance == null) {
            synchronized (EventHandler.class) {
                if (instance == null) {
                    instance = new EventHandler();
                }
            }
        }

        // TODO get correct events
        List<EventBase> possible = new ArrayList<>(events.get("All"));
        if (civ.isNomadic()) {
            possible.addAll(events.get("Nomadic"));
        } else {
            possible.addAll(events.get("Settled"));
        }

        if (civ.isStable()) {
            possible.addAll(events.get("Stable"));
        } else {
            possible.addAll(events.get("Unstable"));
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
        events.put("Stable", new ArrayList<>());
        events.put("Unstable", new ArrayList<>());


        try {
//        EVENTS ALL
            String cat = "All";
            EventBase base = new EventBase("grow", 8);
            base.setOption(EventOption.SizeImpact, 1);
            events.get(cat).add(base);

            base = new EventBase("shrink", 5);
            base.setOption(EventOption.SizeImpact, -1);
            events.get(cat).add(base);

//            base = new EventBase("die", 15);
//            base.setOption(EventOption.Destroy, 0.9);
//            events.get(cat).add(base);


//        NOMADIC
            cat = "Nomadic";
            base = new EventBase("settle", 25);
            base.setOption(EventOption.Settle, true);
            events.get(cat).add(base);


//        SETTLED
            cat = "Settled";
            base = new EventBase("gainAffinity", 5);
            base.setOption(EventOption.GainAffinity, true);
            events.get(cat).add(base);

            base = new EventBase("exile", 1);
            base.setOption(EventOption.Exile, true);
            events.get(cat).add(base);

            base = new EventBase("splinter", 1);
            base.setOption(EventOption.Splinter, true);
            events.get(cat).add(base);

//        STABLE
            cat = "Stable";
            base = new EventBase("prosper", 5);
            base.setOption(EventOption.SizeImpact, 2);
            events.get(cat).add(base);

//        UNSTABLE
            cat = "Unstable";
            base = new EventBase("collapse", 10);
            base.setOption(EventOption.Destroy, 1.0);
            events.get(cat).add(base);

            base = new EventBase("stablilize", 10);
            base.setOption(EventOption.Stabilize, true);


        } catch (ConstructEventException e) {
            throw new RuntimeException(e);
        }


    }

}

