/** A delivery truck with its own identity, driver and capacity. */
final class Vehicle {
    private final int id;
    private final String registration;
    private final String driver;
    private final int capacity;

    Vehicle(int id, String registration, String driver, int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        this.id = id;
        this.registration = registration;
        this.driver = driver;
        this.capacity = capacity;
    }

    int id()              { return id; }
    String registration() { return registration; }
    String driver()       { return driver; }
    int capacity()        { return capacity; }

    boolean canCarry(int totalLoad) { return totalLoad <= capacity; }

    String describe() {
        return "Vehicle #" + id + " [" + registration + "] Driver: " + driver + " | Capacity: " + capacity;
    }

    public String toString() { return registration; }
}