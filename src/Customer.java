/** A delivery point that needs goods. */
final class Customer {
    private final int id;
    private final String name;
    private final location location;
    private final String address;
    private final int demand;

    Customer(int id, String name, location location, String address, int demand) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        if (demand <= 0) throw new IllegalArgumentException("Demand must be positive for " + name);
        this.id = id;
        this.name = name;
        this.location = location;
        this.address = address;
        this.demand = demand;
    }

    int id()            { return id; }
    String name()       { return name; }
    location location() { return location; }
    String address()    { return address; }
    int demand()        { return demand; }

    double distanceTo(location other) { return location.distanceTo(other); }
    double distanceTo(Customer other) { return location.distanceTo(other.location); }

    String describe() {
        return "Customer #" + id + " '" + name + "' at " + location + " | " + address
                + " | Demand: " + demand;
    }

    public String toString() { return name; }
}