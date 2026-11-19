/** Stateless 2-opt: one instance can safely serve many threads. */
final class TwoOpt {
    void improve(Route r) {
        for (boolean changed = true; changed; ) {
            changed = false;
            for (int i = 0; i < r.size() - 1; i++)
                for (int j = i + 1; j < r.size(); j++) {
                    location a = r.before(i), d = r.at(j + 1);
                    location b = r.stopAt(i).location(), c = r.stopAt(j).location();
                    if (a.distanceTo(c) + b.distanceTo(d) < a.distanceTo(b) + c.distanceTo(d) - 1e-9) {
                        r.reverse(i, j);
                        changed = true;
                    }
                }
        }
    }
}