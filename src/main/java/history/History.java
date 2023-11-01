package history;

import land.Land;
import land.LandMap;
import nation.Civilization;
import util.Global;
import util.IO;

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
            if (Global.quit) break;
        }
        finish();
    }

    private void doDecade() {
        List<Record> records = map.passTime();
        records.removeIf(r -> !r.isValid());
        records.forEach(r -> r.setYear(year));
        System.out.println("\nIn the year's of " + year);
        if (records.size() > 0) {
            this.records.put(year, records);
            for (Record rec : this.records.get(year)) {
                System.out.println(rec);
                if (Global.speed <= 0) IO.getEnter();
            }
        } else {
            System.out.println("Nothing happened.");
        }

        printLands();
        printCivs();

    }

    private void printLands() {
        System.out.println("\nLands:");
        System.out.println(Land.getHeaderString());
        for (Land land : this.map.getAllOccupiedLands()) {
            System.out.println(land);
        }

    }

    private void printCivs() {
        System.out.println("\nCivilizations:");
        System.out.println(Civilization.getHeaderString());
        for (Civilization civ : this.map.getAllCivs()) {
            System.out.println(civ);
        }
        if (Global.speed <= 1) IO.getEnter();
    }

    private void finish() {
        List<Integer> decades = this.records.keySet().stream().toList();
//        Collections.sort(decades);
        if (Global.verbose) {
            for (int year : decades) {
                System.out.println("In the " + year + "'s:");
                for (Record rec : this.records.get(year)) {
                    System.out.println(rec);
                }
            }
        }
        System.out.println("\nEnd\n");
    }

}
