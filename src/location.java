/** A point on the 2D grid. Immutable. */
record location(double x, double y) {
    double distanceTo(location o) { return Math.hypot(x - o.x, y - o.y); }
    public String toString() { return String.format("(%.2f, %.2f)", x, y); }
}