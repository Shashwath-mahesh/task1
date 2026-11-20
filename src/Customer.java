/** A delivery point that needs goods. */
final class Customer extends Place {
    private final int demand;

    Customer(int id, String name, double x, double y, String address, int demand) {
        super(id, name, x, y, address);
        this.demand = checkDemand(name, demand);
    }

    Customer(int id, String name, double x, double y, int demand) {
        super(id, name, x, y);
        this.demand = checkDemand(name, demand);
    }

    Customer(int id, String name, Place at, String address, int demand) {
        super(id, name, at, address);
        this.demand = checkDemand(name, demand);
    }

    private static int checkDemand(String name, int demand) {
        if (demand <= 0) throw new IllegalArgumentException("Demand must be positive for " + name);
        return demand;
    }

    int demand() { return demand; }

    @Override protected String kind()  { return "Customer"; }
    @Override protected String extra() { return " | Demand: " + demand; }
}