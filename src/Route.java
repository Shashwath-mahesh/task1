import java.util.*;

/** One vehicle's trip: depot -> customers -> depot. Mutable, used by one thread at a time. */
final class Route {
    private final Depot depot;
    private final Vehicle vehicle;
    private final List<Customer> stops = new ArrayList<>();
    private int load;

    Route(Depot depot, Vehicle vehicle) { this.depot = depot; this.vehicle = vehicle; }

    Vehicle vehicle()        { return vehicle; }
    int size()               { return stops.size(); }
    int load()               { return load; }
    Customer stopAt(int i)   { return stops.get(i); }
    List<Customer> stops()   { return Collections.unmodifiableList(stops); }

    /** Location before position pos (depot at start). */
    location before(int pos) { return pos == 0 ? depot.location() : stops.get(pos - 1).location(); }
    /** Location at position pos (depot at end). */
    location at(int pos)     { return pos == stops.size() ? depot.location() : stops.get(pos).location(); }

    boolean fits(Customer c) { return vehicle.canCarry(load + c.demand()); }

    void insert(int pos, Customer c) { stops.add(pos, c); load += c.demand(); }
    void reverse(int from, int to)   { Collections.reverse(stops.subList(from, to + 1)); }

    double distance() {
        double d = 0;
        location prev = depot.location();
        for (Customer c : stops) { d += prev.distanceTo(c.location()); prev = c.location(); }
        return d + prev.distanceTo(depot.location());
    }

    /** e.g. "Central Warehouse -> Customer 5 -> Customer 9 -> Central Warehouse" */
    String path() {
        StringBuilder sb = new StringBuilder(depot.name());
        for (Customer c : stops) sb.append(" -> ").append(c.name());
        return sb.append(" -> ").append(depot.name()).toString();
    }

    /** Detailed stop-by-stop report: who is visited, in what order, and how far each leg is. */
    String report() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("   Start : %s %s%n", depot.name(), depot.location()));
        location prev = depot.location();
        int runningLoad = 0;
        double travelled = 0;
        int n = 1;
        for (Customer c : stops) {
            double leg = prev.distanceTo(c.location());
            travelled += leg;
            runningLoad += c.demand();
            sb.append(String.format("   %2d. %-14s %-16s demand %2d | load %2d/%d | leg %6.2f | total %7.2f%n",
                    n++, c.name(), c.location(), c.demand(), runningLoad, vehicle.capacity(), leg, travelled));
            prev = c.location();
        }
        double back = prev.distanceTo(depot.location());
        travelled += back;
        sb.append(String.format("   Return: %s | leg %.2f | route total %.2f%n", depot.name(), back, travelled));
        return sb.toString();
    }
}