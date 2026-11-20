/** Common parent of Location, Customer and Depot: shared id, name, coordinates and address. */
abstract class Place {
    private final int id;
    private final String name;
    private final double x;
    private final double y;
    private final String address;

    /** Full form. */
    protected Place(int id, String name, double x, double y, String address) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.address = address;
    }

    /** Coordinates only: address is derived from the grid position. */
    protected Place(int id, String name, double x, double y) {
        this(id, name, x, y, "Grid " + String.format("(%.2f, %.2f)", x, y));
    }

    /** Copy the position from another place. */
    protected Place(int id, String name, Place at, String address) {
        this(id, name, at.x, at.y, address);
    }

    public int id()         { return id; }
    public String name()    { return name; }
    public double x()       { return x; }
    public double y()       { return y; }
    public String address() { return address; }

    /** Distance to raw coordinates. */
    public double distanceTo(double otherX, double otherY) {
        return Math.hypot(x - otherX, y - otherY);
    }

    /** Distance to another place (same name, different parameters -> overloading). */
    public double distanceTo(Place other) {
        return distanceTo(other.x, other.y);
    }

    public String coords() { return String.format("(%.2f, %.2f)", x, y); }

    /** What kind of place this is, e.g. "Customer". Each subclass must say. */
    protected abstract String kind();

    /** Extra text a subclass wants appended to describe(). */
    protected String extra() { return ""; }

    public String describe() {
        return kind() + " #" + id + " '" + name + "' at " + coords() + " | " + address + extra();
    }

    @Override public String toString() { return name; }
}