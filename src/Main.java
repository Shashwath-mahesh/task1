import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Problem problem = Problem.load(Path.of("cvrp_1000_customers (1).csv"), 100, 65);
        System.out.println(problem.depot.describe());
        System.out.println(problem.fleet.get(0).describe());
        System.out.println(problem.customers.get(0).describe());
        System.out.println();

        long start = System.nanoTime();
        ClarkeWrightConstructor constructor = new ClarkeWrightConstructor(problem);
        List<Route> routes = constructor.construct();
        TwoOpt twoOpt = new TwoOpt();
        routes.parallelStream().forEach(twoOpt::improve);
        problem.validate(routes);

        double total = 0;
        for (Route r : routes) {
            double d = r.distance();
            total += d;
            Vehicle v = r.vehicle();
            System.out.printf("%n%s (%s) | Customers: %d | Load: %d/%d | Distance: %.2f%n",
                    v.registration(), v.driver(), r.size(), r.load(), v.capacity(), d);
            System.out.println("   Route: " + r.path());
            System.out.print(r.report());
        }
        System.out.printf("%nTotal distance: %.2f%n", total);
        System.out.println("Vehicles used: " + routes.size());
        System.out.println("Customers delivered: " + problem.customers.size());
        System.out.println("Validation: PASSED");
        System.out.printf("Execution time: %.2f ms%n", (System.nanoTime() - start) / 1_000_000.0);
    }
}