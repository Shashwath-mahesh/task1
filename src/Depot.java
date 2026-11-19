/** The warehouse where every route starts and ends. */
final class Depot {
    private final int id;
    private final String name;
    private final location location;
    private final String address;
    private final String openingHours;

    Depot(int id, String name, location location, String address, String openingHours) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        this.id = id;
        this.name = name;
        this.location = location;
        this.address = address;
        this.openingHours = openingHours;
    }

    int id()              { return id; }
    String name()         { return name; }
    location location()   { return location; }
    String address()      { return address; }
    String openingHours() { return openingHours; }

    double distanceTo(location other) { return location.distanceTo(other); }

    String describe() {
        return "Depot #" + id + " '" + name + "' at " + location + " | " + address
                + " | Hours: " + openingHours;
    }

    public String toString() { return name; }
}