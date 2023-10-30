package history;

import land.LandMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class History {
    private int year;
    private LandMap map;
    private Map<Integer, List<Record>> records;

    private final int TIMESEGMENTLENGHT = 10;

    public History() {
        this.year = 0;
        this.map = new LandMap();
        this.records = new HashMap<>();
    }

    public void passTime(int years) {
        int decades = Math.floorDiv(years, TIMESEGMENTLENGHT);
        for (int y = 0; y < decades; y++) {
            doDecade();
            year += TIMESEGMENTLENGHT;
        }
        finish();
    }

    private void doDecade() {
        List<Record> records = map.passTime();
        records.removeIf(r -> !r.isValid());
        records.forEach(r -> r.setYear(year));
        if (records.size() > 0) this.records.put(year, records);
    }

    private void finish() {
        List<Integer> decades = this.records.keySet().stream().toList();
        Collections.sort(decades);
        for (int year : decades) {
            System.out.println("In the " + year + "'s:");
            for (Record rec : this.records.get(year)) {
                System.out.println(rec);
            }
        }
        System.out.println("End\n");
    }

}
