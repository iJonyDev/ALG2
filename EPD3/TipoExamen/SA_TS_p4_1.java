/*
 * Una villa de La Isla de las tentaciones puede recibir hasta M mujeres con pareja. 
 * Cada una de essa mujeres va a ser tentada por T solteros en busca del amor
 * , siendo Aij, la afinidad que exite entre mujer i (i=1,...,M) con un tentador j(j=1,...,T). 
 * 
 * En un momento determinado, se desean realizar M citas simultaneas 
 * de tal manera que la afinidad del conjunto de las citas sea la maxiam posible. 
 * 
 * Para resolver el problema suponga que: M=5 y T=10, 
 * Los valores de Aij estaran comprendidos entre [0,10] y los puede inicializar manualmente.
 */
package EPD3.TipoExamen;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class SA_TS_p4_1 {

    public static final int NUM_MUJERES = 5;
    public static final int NUM_TENTADORES = 10;
    public static final int NUM_VECINOS = 10;
    public static final int TENENCIA_TABU = 4;
    public static final int MAX_ITERACIONES = 100;
    public static final int UMBRAL_COSTE = 50; // Máximo global

    // Matriz de afinidades (valores de afinidad)
    public static final int[][] MATRIZ_AFINIDADES = {
        {7, 8, 5, 6, 9, 4, 3, 2, 1, 0},
        {6, 7, 8, 5, 4, 3, 2, 1, 0, 9},
        {5, 6, 7, 8, 9, 4, 3, 2, 1, 0},
        {4, 5, 6, 7, 8, 9, 3, 2, 1, 0},
        {3, 4, 5, 6, 7, 8, 9, 2, 1, 0}
    };

    public static void main(String[] args) {
        int[] solucionTabu = tabuSearch();
        System.out.println("\n\nLa mejor combinación encontrada por Tabu Search es: " + Arrays.toString(solucionTabu)
                + ", con afinidad total: " + calcularCoste(solucionTabu));

        int[] solucionSA = enfriamientoSimulado();
        System.out.println("\n\nLa mejor combinación encontrada por Enfriamiento Simulado es: " + Arrays.toString(solucionSA)
                + ", con afinidad total: " + calcularCoste(solucionSA));
    }

    private static int[] tabuSearch() {
        int[] solActual = generarSolucionInicial();
        int[] solMejor = solActual;
        double costeActual = calcularCoste(solActual), costeMejor = costeActual;
        Queue<int[]> listaTabu = new LinkedList<>();
        int iteraciones = 0;

        while (iteraciones++ < MAX_ITERACIONES) {
            int[][] vecinos = generarVecinos(solActual);
            int[] mejorVecino = null;
            double mejorCalidadVecino = -1;

            for (int i = 0; i < vecinos.length; i++) {
                double calidadVecino = calcularCoste(vecinos[i]);
                if (!listaTabu.contains(vecinos[i]) && calidadVecino > mejorCalidadVecino) {
                    mejorCalidadVecino = calidadVecino;
                    mejorVecino = vecinos[i];
                }
            }

            if (mejorVecino != null) {
                solActual = Arrays.copyOf(mejorVecino, mejorVecino.length);
                costeActual = mejorCalidadVecino;
                if (costeActual > costeMejor) {
                    solMejor = Arrays.copyOf(solActual, solActual.length);
                    costeMejor = costeActual;
                }
                listaTabu.add(solActual);
                if (listaTabu.size() > TENENCIA_TABU) listaTabu.poll();
            }
        }
        return solMejor;
    }

    private static int[] enfriamientoSimulado() {
        int[] solActual = generarSolucionInicial();
        int[] solMejor = solActual;
        double costeActual = calcularCoste(solActual), costeMejor = costeActual, temperatura = 100, delta;
        int exitos = -1;

        while (temperatura >= 1 && exitos != 0) {
            exitos = 0;
            int[][] vecinos = generarVecinos(solActual);
            for (int i = 0; i < vecinos.length; i++) {
                double calidadVecino = calcularCoste(vecinos[i]);
                delta = calidadVecino - costeActual;
                if (delta > 0 || probAceptacion(delta, temperatura)) {
                    exitos++;
                    solActual = Arrays.copyOf(vecinos[i], vecinos[i].length);
                    costeActual = calidadVecino;
                    if (costeActual > costeMejor) {
                        solMejor = Arrays.copyOf(vecinos[i], vecinos[i].length);
                        costeMejor = calidadVecino;
                    }
                }
            }
            temperatura *= 0.9;
        }
        return solMejor;
    }

    private static int[] generarSolucionInicial() {
        int[] solucion = {0, 1, 2, 3, 4};
        Random rand = new Random();
        for (int i = 0; i < NUM_MUJERES; i++) {
            int j = rand.nextInt(NUM_TENTADORES);
            solucion[i] = j;
        }
        return solucion;
    }

    private static int[] generarVecino(int[] solucion, int i, int j) {
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        vecino[i] = j;
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion) {
        int[][] vecinos = new int[NUM_VECINOS][solucion.length];
        Random rand = new Random();
        for (int i = 0; i < NUM_VECINOS; i++) {
            int a = rand.nextInt(NUM_MUJERES);
            int b = rand.nextInt(NUM_TENTADORES);
            vecinos[i] = generarVecino(solucion, a, b);
        }
        return vecinos;
    }

    private static double calcularCoste(int[] solucion) {
        double coste = 0;
        for (int i = 0; i < NUM_MUJERES; i++) {
            coste += MATRIZ_AFINIDADES[i][solucion[i]];
        }
        return coste;
    }

    private static boolean probAceptacion(double delta, double temperatura) {
        return (Math.exp(delta / temperatura) > Math.random());
    }
}