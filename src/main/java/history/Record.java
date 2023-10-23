package history;

import java.util.ArrayList;
import java.util.List;

public class Record {
    private final String eventName;
    private int eventYear;
    private final List<String> happenings;

    public Record() {
        this.eventName = "";
        this.eventYear = 0;
        this.happenings = new ArrayList<>();
    }

    public Record(String name) {
        this.eventName = name;
        this.happenings = new ArrayList<>();
    }

    public void setYear(int year) {
        this.eventYear = year;
    }

    public boolean isValid() {
        return (this.eventName != null && !this.eventName.equals("Error") && !this.eventName.equals("Null"));
    }

    public void addEvent(String description) {
        this.happenings.add(description);
    }

    @Override
    public String toString() {
        return "Record :)";
    }
}
