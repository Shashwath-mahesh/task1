import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/** The whole problem: one depot, many customers, a fleet. Immutable and validated. */
final class Problem {
    final Depot depot;
    final List<Customer> customers;
    final List<Vehicle> fleet;

    Problem(Depot depot, List<Customer> customers, List<Vehicle> fleet) {
        if (depot == null) throw new RuntimeException("Depot missing");
        if (fleet.isEmpty()) throw new RuntimeException("Fleet is empty");
        int maxCapacity = 0;
        long fleetCapacity = 0;
        for (Vehicle v : fleet) {
            maxCapacity = Math.max(maxCapacity, v.capacity());
            fleetCapacity += v.capacity();
        }
        long total = 0;
        for (Customer c : customers) {
            if (c.demand() > maxCapacity)
                throw new RuntimeException("Demand of " + c.name() + " exceeds every vehicle's capacity");
            total += c.demand();
        }
        if (total > fleetCapacity) throw new RuntimeException("Insufficient fleet capacity");
        this.depot = depot;
        this.customers = List.copyOf(customers);
        this.fleet = List.copyOf(fleet);
    }

    int maxCapacity() {
        int m = 0;
        for (Vehicle v : fleet) m = Math.max(m, v.capacity());
        return m;
    }

    /** CSV columns: id,x,y,demand[,name]. First row is the depot. Fleet is generated. */
    static Problem load(Path file, int vehicleCount, int capacity) throws IOException {
        Depot depot = null;
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(file)) {
            br.readLine(); // skip header
            for (String line; (line = br.readLine()) != null; ) {
                String[] v = line.split(",");
                int id = Integer.parseInt(v[0].trim());
                double x = Double.parseDouble(v[1].trim());
                double y = Double.parseDouble(v[2].trim());
                int demand = Integer.parseInt(v[3].trim());
                if (depot == null) {
                    if (id != 0) throw new RuntimeException("Depot missing");
                    String name = v.length > 4 && !v[4].isBlank() ? v[4].trim() : "Central Warehouse";
                    depot = new Depot(id, name, x, y);
                } else {
                    if (id != customers.size() + 1) throw new RuntimeException("Invalid customer ID");
                    String name = v.length > 4 && !v[4].isBlank() ? v[4].trim() : "Customer " + id;
                    customers.add(new Customer(id, name, x, y, demand));
                }
            }
        }
        if (depot == null) throw new RuntimeException("Depot missing");

        List<Vehicle> fleet = new ArrayList<>();
        for (int i = 1; i <= vehicleCount; i++)
            fleet.add(new Vehicle(i, String.format("TRK-%03d", i), "Driver " + i, capacity));
        return new Problem(depot, customers, fleet);
    }

    void validate(List<Route> routes) {
        if (routes.size() > fleet.size()) throw new RuntimeException("Too many vehicles");
        Map<Customer, Integer> visits = new HashMap<>();
        for (Route r : routes) {
            if (!r.vehicle().canCarry(r.load()))
                throw new RuntimeException("Capacity exceeded by " + r.vehicle());
            for (Customer c : r.stops()) visits.merge(c, 1, Integer::sum);
        }
        for (Customer c : customers)
            if (visits.getOrDefault(c, 0) != 1) throw new RuntimeException("Invalid visit: " + c.name());
    }
}