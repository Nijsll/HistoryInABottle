package history;

public class Record {
    private final String eventName;
    private int eventYear;
    private final String description;

    public Record() {
        this.eventName = "";
        this.eventYear = 0;
        this.description = "";
    }

    public Record(String name, String description) {
        this.eventName = name;
        this.description = description;
    }

    public void setYear(int year) {
        this.eventYear = year;
    }

    public boolean isValid() {
        return (this.eventName != null && !this.eventName.equals("Error") && !this.eventName.equals("Null"));
    }

    @Override
    public String toString() {
        return "Record :)";
    }
}
