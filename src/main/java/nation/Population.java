package nation;

import java.util.HashMap;
import java.util.Map;

public class Population {
    private Map<String, Integer> people;

    public Population(String race) {
        this.people = new HashMap<>();
        this.people.put(race, 100);
    }

    public Population(String race, int amount) {
        this.people = new HashMap<>();
        this.people.put(race, amount);
    }

    public int getSize() {
        int total = 0;
        for (Integer n : people.values()) {
            total += n;
        }
        return Math.floorDiv(total, 100);
    }

    private Map<String, Double> getPercentages() {
        double size = (double) this.getSize();
        Map<String, Double> percentages = new HashMap<>();
        for (String race : this.people.keySet()) {
            percentages.put(race, ((double) this.people.get(race)) / size);
        }
        return percentages;
    }

    public void shrinkBy(int n) {
        int currentSize = this.getSize();
        if (currentSize <= n) {
            System.out.println("Shrink of " + n + " impossible on population if size " + currentSize);
            return;
        }
        Map<String, Double> percentages = this.getPercentages();
        for (String race : percentages.keySet()) {
            int newValue = this.people.get(race) - (int) Math.floor(percentages.get(race) * ((double) n * 100));
            if (newValue == 0) {
                this.people.remove(race);
            } else {
                this.people.put(race, newValue);
            }
        }

    }

    private void growBy(int n) {
        int currentSize = this.getSize();
        Map<String, Double> percentages = this.getPercentages();
        for (String race : percentages.keySet()) {
            int newValue = this.people.get(race) + (int) Math.floor(percentages.get(race) * ((double) n * 100));
            this.people.put(race, newValue);
        }
    }

    public void setSize(int size) {
        int currentSize = this.getSize();
        if (currentSize == size) return;
        int dif = Math.abs(currentSize - size);
        if (currentSize < size) {
            this.growBy(dif);
        } else {
            this.shrinkBy(dif);
        }
    }

    @Override
    public String toString() {
        if (this.people.size() == 0) {
            return "Total population size: 0";
        }

        String s = "Total population size: " + this.getSize() + " - Races: ";
        Map<String, Double> parts = this.getPercentages();
        for (String race : parts.keySet()) {
            s += race + ": " + ((int) (parts.get(race)*100)) + "%, ";
        }
        return s.substring(0, s.length()-2);
    }
}
