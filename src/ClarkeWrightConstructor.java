import java.util.*;

/**
 * Clarke-Wright savings construction.
 * Start with one trip per customer (depot -> customer -> depot). Joining two trips i and j
 * saves  d(depot,i) + d(depot,j) - d(i,j)  of driving. Apply the biggest savings first,
 * as long as the joined trip still fits in a truck.
 */
final class ClarkeWrightConstructor {
    private final Problem p;
    ClarkeWrightConstructor(Problem p) { this.p = p; }

    /** A saving from driving customer a and customer b one after the other. */
    private record Saving(Customer a, Customer b, double value) {}

    /** A growing trip (not yet tied to a vehicle). */
    private static final class Chain {
        final Deque<Customer> stops = new ArrayDeque<>();
        int load;
        Chain(Customer c) { stops.add(c); load = c.demand(); }
        Customer first() { return stops.peekFirst(); }
        Customer last()  { return stops.peekLast(); }
    }

    List<Route> construct() {
        int limit = p.maxCapacity();
        List<Customer> cs = p.customers;

        // 1. one chain per customer
        Map<Customer, Chain> chainOf = new HashMap<>();
        for (Customer c : cs) chainOf.put(c, new Chain(c));

        // 2. every pair's saving, biggest first
        List<Saving> savings = new ArrayList<>();
        for (int i = 0; i < cs.size(); i++)
            for (int j = i + 1; j < cs.size(); j++) {
                Customer a = cs.get(i), b = cs.get(j);
                double s = p.depot.distanceTo(a) + p.depot.distanceTo(b) - a.distanceTo(b);
                if (s >= 0) savings.add(new Saving(a, b, s));
            }
        savings.sort((x, y) -> Double.compare(y.value(), x.value()));

        // 3. merge chains when the two customers are chain ends and the load fits
        for (Saving s : savings) {
            Chain ca = chainOf.get(s.a()), cb = chainOf.get(s.b());
            if (ca == cb || ca.load + cb.load > limit) continue;

            boolean aEnd = s.a() == ca.last(),  aStart = s.a() == ca.first();
            boolean bEnd = s.b() == cb.last(),  bStart = s.b() == cb.first();
            if (!(aEnd || aStart) || !(bEnd || bStart)) continue;   // an interior stop cannot be joined

            if (!aEnd) reverse(ca);        // make a the tail of ca
            if (!bStart) reverse(cb);      // make b the head of cb
            for (Customer c : cb.stops) { ca.stops.addLast(c); chainOf.put(c, ca); }
            ca.load += cb.load;
        }

        // 4. turn each distinct chain into a Route on a real Vehicle (biggest loads first)
        List<Chain> chains = new ArrayList<>(new LinkedHashSet<>(chainOf.values()));
        chains.sort((x, y) -> Integer.compare(y.load, x.load));
        if (chains.size() > p.fleet.size())
            throw new RuntimeException("Need " + chains.size() + " vehicles but fleet has " + p.fleet.size());

        List<Vehicle> trucks = new ArrayList<>(p.fleet);
        trucks.sort((x, y) -> Integer.compare(y.capacity(), x.capacity()));

        List<Route> routes = new ArrayList<>();
        for (int k = 0; k < chains.size(); k++) {
            Route r = new Route(p.depot, trucks.get(k));
            for (Customer c : chains.get(k).stops) r.insert(r.size(), c);
            routes.add(r);
        }
        return List.copyOf(routes);
    }

    private static void reverse(Chain ch) {
        List<Customer> tmp = new ArrayList<>(ch.stops);
        Collections.reverse(tmp);
        ch.stops.clear();
        ch.stops.addAll(tmp);
    }
}