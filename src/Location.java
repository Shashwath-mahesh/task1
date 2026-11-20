/** A bare point on the grid, with no business meaning. */
final class Location extends Place {
    Location(double x, double y) {
        super(-1, "Point", x, y);
    }

    Location(String name, double x, double y) {
        super(-1, name, x, y);
    }

    @Override protected String kind() { return "Location"; }
}