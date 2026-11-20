/** The warehouse where every route starts and ends. */
final class Depot extends Place {
    static final String DEFAULT_HOURS = "06:00-22:00";
    private final String openingHours;

    Depot(int id, String name, double x, double y, String address, String openingHours) {
        super(id, name, x, y, address);
        this.openingHours = openingHours;
    }

    Depot(int id, String name, double x, double y, String openingHours) {
        super(id, name, x, y);
        this.openingHours = openingHours;
    }

    Depot(int id, String name, double x, double y) {
        this(id, name, x, y, DEFAULT_HOURS);
    }

    String openingHours() { return openingHours; }

    @Override protected String kind()  { return "Depot"; }
    @Override protected String extra() { return " | Hours: " + openingHours; }
}