
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

public class TSP {

    // The input file is a SPACE delimited distance matrix
    // For this implementation, the triangle inequality does not need to be satisfied
    public static final String INPUT_FILE = "TSP20.txt";

    // Input data
    public static int[][] matrix;
    public static int numCities;

    // Debugging
    public static boolean printNewChildren = false;

    // Genetic Algorithm Paramaters
    public static final int POPULATION_SIZE = 80;
    public static final int NUM_EVOLUTION_ITERATIONS = 100;

    public static final double PORCENTAJE_R = 0.1;

    // When selecting two parents, we want the "fittest" parents to reproduce
    // This is done by randomly selecting X individuals in the population and 
    // selecting the top two from this sub-population. The size of the sub-population is tournament size
    // This must be less than POPULATION_SIZE
    public static double TOURNAMENT_SIZE_PCT = 0.1;
    public static int TOURNAMENT_SIZE = (int) (POPULATION_SIZE * TOURNAMENT_SIZE_PCT);
    // The probability a specific individual undergoes a single mutation
    public static double MUTATION_RATE = 0.5;
    // Probability of skipping crossover and using the best parent
    public static double CLONE_RATE = 0.01;
    // Elite percent is what we define as "high" fit individuals
    public static double ELITE_PERCENT = 0.1;
    // When selecting parents, the ELITE_PARENT_RATE is the probability that we select an elite parent
    public static double ELITE_PARENT_RATE = 0.1;
    // Forward progress epsilon (percent of first-attempt path cost)
    public static double EPSILON = 0.02;

    public static boolean cambiaConjuntoR = true;

    public static void main(String[] args) {

        int i, j, iteracion = 0;
        ComparadorDiferencias cmp = new ComparadorDiferencias();
        checkParameterErrors();
        TSP gm = new TSP();
        try {
            gm.readDistanceMatrix();
        } catch (IOException e) {
            System.err.println(e);
        }
        long startTime = System.nanoTime();

        Population conjuntoP = new Population(numCities);
        Population conjuntoR1 = new Population(numCities);
        Population conjuntoR2 = new Population(numCities);
        Population nuevosCandidatos = new Population(numCities);

        conjuntoP.initializePopulationRandomly(POPULATION_SIZE);

        conjuntoP.LocalSearch();

        Collections.sort(conjuntoP.individuals);

        conjuntoR1 = seleccionarR1(conjuntoP);

        conjuntoR2 = seleccionarR2(conjuntoP, conjuntoR1);

        while (cambiaConjuntoR) {
            iteracion++;

            //System.out.println("\n\nITERACION " + iteracion);
            nuevosCandidatos = combinacionR(conjuntoR1, conjuntoR2);
            nuevosCandidatos.LocalSearch();

            calcularDiversidadRespectoR1(conjuntoR1, nuevosCandidatos);

            cambiaConjuntoR = false;
            for (i = 0; i < nuevosCandidatos.individuals.size(); i++) {
                nuevosCandidatos.individuals.get(i).calculateCost();

                if (conjuntoR1.individuals.get(conjuntoR1.individuals.size() - 1).cost > nuevosCandidatos.individuals.get(i).cost) {
                    conjuntoR1.individuals.set((conjuntoR1.individuals.size() - 1), nuevosCandidatos.individuals.get(i));
                    Collections.sort(conjuntoR1.individuals);
                    calcularDiversidadRespectoR1(conjuntoR1, conjuntoR2);
                    Collections.sort(conjuntoR2.individuals, cmp);
                    cambiaConjuntoR = true;
                } else if (conjuntoR2.individuals.get(conjuntoR2.individuals.size() - 1).dispersion < nuevosCandidatos.individuals.get(i).dispersion) {
                    conjuntoR2.individuals.set((conjuntoR2.individuals.size() - 1), nuevosCandidatos.individuals.get(i));
                    Collections.sort(conjuntoR2.individuals, cmp);
                    cambiaConjuntoR = true;
                }
            }
        }

        System.out.println("La mejor solución es: ");
        System.out.println(conjuntoR1.individuals.get(0));
        System.out.println("Coste: " + conjuntoR1.individuals.get(0).getCost());

        long endTime = System.nanoTime();
        System.out.println("RUNNING TIME: " + (endTime - startTime));
    }

    public static Population combinacionR(Population conjuntoR1, Population conjuntoR2) {
        Population nuevosCandidatos = new Population(numCities);
        int i, j;

        /* Combina todas las posibles parejas del conjunto R */
        for (i = 0; i < conjuntoR1.individuals.size(); i++) {
            for (j = 0; j < conjuntoR1.individuals.size(); j++) {
                nuevosCandidatos.individuals.add(combinarParejasCandidatos(conjuntoR1.individuals.get(i), conjuntoR2.individuals.get(j)));
                if (i < j) {
                    nuevosCandidatos.individuals.add(combinarParejasCandidatos(conjuntoR1.individuals.get(i), conjuntoR1.individuals.get(j)));
                    nuevosCandidatos.individuals.add(combinarParejasCandidatos(conjuntoR2.individuals.get(i), conjuntoR2.individuals.get(j)));
                }
            }
        }
        return nuevosCandidatos;
    }

    public static Individual combinarParejasCandidatos(Individual p1, Individual p2) {
        Individual child = new Individual();
        // Generate a subtour from parent 1
        int index1 = (int) (Math.random() * numCities);
        int index2 = (int) (Math.random() * numCities);
        int start = Math.min(index1, index2);
        int end = Math.max(index1, index2);
        // Add the subtour from parent 1
        for (int i = start; i < end; i++) {
            child.addCityToTour(p1.getCity(i));
        }
        // Add the remaining cities from parent 2
        for (int j = 0; j < numCities; j++) {
            if (!child.tour.contains(p2.getCity(j))) {
                child.addCityToTour(p2.getCity(j));
            }
        }
        // Small chance of cloning the better parent
        if (Math.random() < TSP.CLONE_RATE) {
            if (p1.getCost() < p2.getCost()) {
                return p1;
            } else {
                return p2;
            }
        }
        return child;
    }

    public static Population seleccionarR1(Population conjuntoP) {
        Population conjuntoR1 = new Population(numCities);
        for (int i = 0; i < PORCENTAJE_R * POPULATION_SIZE; i++) {
            conjuntoR1.individuals.add(conjuntoP.individuals.get(i));
        }
        return conjuntoR1;
    }

    public static void calcularDiversidadRespectoR1(Population conjuntoR1, Population conjunto) {
        int i, j, k, diferencias;

        /* Calcular diversidad de todos respecto a R1 */
        for (i = 0; i < conjunto.individuals.size(); i++) {
            diferencias = 0;
            for (j = 0; j < conjuntoR1.individuals.size(); j++) {
                for (k = 0; k < conjunto.individuals.get(i).tour.size(); k++) {
                    if (conjunto.individuals.get(i).tour.get(k) != conjuntoR1.individuals.get(j).tour.get(k)) {
                        diferencias += 1;
                    }
                }
            }
            conjunto.individuals.get(i).dispersion = diferencias;
        }
    }

    public static Population seleccionarR2(Population conjuntoP, Population conjuntoR1) {
        Population conjuntoR2 = new Population(numCities);
        ComparadorDiferencias cmp = new ComparadorDiferencias();

        calcularDiversidadRespectoR1(conjuntoR1, conjuntoP);

        /*Se ordena el Conjunto P según su diversidad respecto a R1 */
        Collections.sort(conjuntoP.individuals, cmp);

        /* Añadimos a R2 los más diversos respecto a R1 */
        for (int i = 0; i < PORCENTAJE_R * POPULATION_SIZE; i++) {
            conjuntoR2.individuals.add(conjuntoP.individuals.get(i));
        }
        return conjuntoR2;
    }

    public TSP() {

    }

    public void initializePopulation() {

    }

    public void readDistanceMatrix() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));

        StringBuilder build = new StringBuilder();
        // Find out how many cities there are in the file
        numCities = 0;
        while (!build.append(br.readLine()).toString().equalsIgnoreCase("null")) {
            numCities++;
            build.setLength(0); // Clears the buffer
        }
        matrix = new int[numCities][numCities];
        // Reset reader to the start of the file
        br = new BufferedReader(new FileReader(INPUT_FILE));
        // Populate the distance matrix
        int currentCity = 0;
        build = new StringBuilder();
        while (!build.append(br.readLine()).toString().equalsIgnoreCase("null")) {
            String[] tokens = build.toString().split(" ");
            for (int i = 0; i < numCities; i++) {
                matrix[currentCity][i] = Integer.parseInt(tokens[i]);
            }
            currentCity++;
            build.setLength(0); // Clears the buffer
        }
    }

    public void printMatrix() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void checkParameterErrors() {
        boolean error = false;
        if (POPULATION_SIZE < TOURNAMENT_SIZE) {
            System.err.println("ERROR: Tournament size must be less than population size.");
            error = true;
        }
        if (POPULATION_SIZE < 0) {
            System.err.println("ERROR: Population size must be greater than zero");
            error = true;
        }
        if (TOURNAMENT_SIZE < 0) {
            System.err.println("ERROR: Tournament size must be greater than zero");
            error = true;
        }
        if (error == true) {
            System.exit(1);
        }
    }
}
