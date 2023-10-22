package history;

import enums.EventOption;
import nation.Civilization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandler {
    private static EventHandler instance = null;
    private static Map<String, List<Event>> events = new HashMap<>();

    public static List<Event> getEvents(Civilization civ) {
        if (instance == null) {
            synchronized (EventHandler.class) {
                if (instance == null) {
                    instance = new EventHandler();
                }
            }
        }


        List<Event> result = new ArrayList<>();
        result.addAll(events.get("All"));
        // TODO get correct events

        return result;
    }

    private Event createEvent(String eventName) {
        return new Event(eventName);
    }

    private EventHandler() {
        // TODO make events
        // Categories
        events.put("All", new ArrayList<>());


//        EVENTS ALL
        String cat = "All";
        Event e = createEvent("grow");
        e.setOption(EventOption.SizeImpact, 1);
        this.events.get(cat).add(e);

        e = createEvent("shrink");
        e.setOption(EventOption.SizeImpact, -1);
        this.events.get(cat).add(e);


    }

}

