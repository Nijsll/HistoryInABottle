package history;

import land.LandMap;

import java.util.Map;
import java.util.List;

public class History {
    private int year = 0;
    private LandMap map;
    private Map<Integer, List<Record>> records;

    private final int TIMESEGMENTLENGHT = 10;

    public void passTime(int years) {
        int decades = Math.floorDiv(years, TIMESEGMENTLENGHT);
        // TODO
    }

    private void doDecade() {
        // TODO
    }

    private void finish() {
        // TODO outputs entire history
    }

}
