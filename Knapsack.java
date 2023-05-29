import java.util.Arrays;
import java.util.Random;

public class Knapsack {
    // Maximum temperature for the simulated annealing process
    private static final double MAX_TEMPERATURE = 10000;
    // Rate at which the temperature decreases
    private static final double COOLING_RATE = 0.70;
    private static int[] values = {68, 64, 47, 55, 72, 53, 81, 60, 72, 80, 62, 42, 48, 47, 68, 51, 48, 68, 83, 55, 48, 44, 49, 68, 63, 71, 82, 55, 60, 63, 56, 75, 42, 76, 42, 60, 75, 68, 67, 42, 71, 58, 66, 72, 67, 78, 49, 50, 51};
    private static int[] weights = {21, 11, 11, 10, 14, 12, 12, 14, 17, 13, 11, 13, 17, 14, 16, 10, 18, 10, 16, 17, 19, 12, 12, 16, 16, 13, 17, 12, 16, 13, 21, 11, 11, 10, 14, 12, 12, 14, 17, 13, 11, 13, 17, 14, 16, 10, 18, 10, 16};

    // The capacity of the knapsack
    private static int knapsackCapacity = 300;

    // Variables that keep track of the current solution and the best solution found so far
    private static boolean[] currentSolution;
    private static boolean[] bestSolution;
    private static int currentValue;
    private static int bestValue;

    public static void main(String[] args) {
        // Initialize the random number generator
        Random random = new Random();

        // Initialize the solution state variables with a random solution
        currentSolution = new boolean[values.length];
        for (int i = 0; i < currentSolution.length; i++) {
            currentSolution[i] = random.nextBoolean();
        }
        currentValue = calculateValue(currentSolution);

        // Initialize the best solution found so far with the initial solution
        bestSolution = Arrays.copyOf(currentSolution, currentSolution.length);
        bestValue = currentValue;

        // Start the simulated annealing process
        double temperature = MAX_TEMPERATURE;
        int iteration = 0;
        while (temperature > 15) { // repeat until the temperature is low enough
            iteration++;

            // Generate a neighbor solution by flipping the inclusion of a randomly chosen item
            boolean[] neighborSolution = Arrays.copyOf(currentSolution, currentSolution.length);
            int item = random.nextInt(neighborSolution.length);
            neighborSolution[item] = !neighborSolution[item];

            // Calculate the value of the neighbor solution
            int neighborValue = calculateValue(neighborSolution);

            // Decide if we should move to the neighbor solution
            double acceptanceProbability = calculateAcceptanceProbability(currentValue, neighborValue, temperature);
            double acceptance_random= random.nextDouble();
            if (acceptanceProbability > acceptance_random) {
                // If the acceptance probability is higher than the random number, move to the neighbor solution
                currentSolution = Arrays.copyOf(neighborSolution, neighborSolution.length);
                currentValue = neighborValue;
            }

            // If the current solution is better than the best solution found so far, update the best solution
            if (currentValue > bestValue) {
                bestSolution = Arrays.copyOf(currentSolution, currentSolution.length);
                bestValue = currentValue;
            }

            // Print the current state
            System.out.println("Iteration: " + iteration);
            System.out.println("Current solution: " + Arrays.toString(currentSolution));
            System.out.println("Current value: " + currentValue);
            System.out.println("Acceptance probability: " + acceptanceProbability);
            System.out.println("Random number: "+acceptance_random);
            System.out.println("Best solution: " + Arrays.toString(bestSolution));
            System.out.println("Best value: " + bestValue);
            System.out.println("Temperature: " + temperature);
            System.out.println("--------------------------");

            // Decrease the temperature
            temperature *= COOLING_RATE;
        }

        // Print the best solution found
        System.out.println("Final Best solution: " + Arrays.toString(bestSolution));
        System.out.println("Final Best value: " + bestValue);
    }

    // Helper method to calculate the fitness value of a solution
    private static int calculateValue(boolean[] solution) {
        int value = 0;
        int weight = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i]) {
                value += values[i];
                weight += weights[i];
            }
        }
        if (weight > knapsackCapacity) {
            return 0;
        } else {
            return value;
        }
    }

    // Helper method to calculate the acceptance probability of a neighbor solution
    private static double calculateAcceptanceProbability(int currentValue, int neighborValue, double temperature) {
        if (neighborValue > currentValue) {
            return 1;
        } else {
            return Math.exp((neighborValue - currentValue) / temperature);
        }
    }
}
