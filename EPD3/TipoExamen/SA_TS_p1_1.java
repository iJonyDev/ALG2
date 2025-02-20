/*
 * Se desea fabricar un material aislante compuesto por siete materiales distintos. 
Se desea encontrar cuál es el orden en que deben mezclarse dichos materiales para asegurar que sea lo más aislante
posible. 
Suponga la siguiente situación:
Materiales: {1, 2, 3, 4, 5, 6, 7}
Se puede apreciar un vector en el que aparece el orden en el que se combinan los materiales y, además, una
matriz triangular para identificar posibles permutaciones de orden, es decir, posibles soluciones a las que ir.
Por ejemplo, si se tiene la siguiente matriz triangular:
[0, 1, 2, 3 , 4 , 5 , 6 ]
[0, 0, 8, 9 , 10, 11, 12]
[0, 0, 0, 15, 16, 17, 18]
[0, 0, 0, 0 , 22, 23, 24]
[0, 0, 0, 0 , 0 , 29, 30]
[0, 0, 0, 0 , 0 , 0 , 36]
Así, la posición (4,5) estaría haciendo referencia a que se intercambie el orden de los materiales 4 por el 5.
Para evaluar la calidad aislante de la solución, suponga que ésta se mide por la suma de los tres primeros
componentes, esto es, sobre la figura de arriba sería 4+2+7 = 13. Si realizamos la permutación 4 por 5,
entonces, la nueva solución candidata sería [5, 2, 7, 1, 4, 6, 3], siendo su coste 5+2+7 = 14 (>13) y por tanto se
aceptaría. De esta condición se deduce que el máximo se alcanzará cuando tengamos en las tres posiciones
superiores {5, 6, 7}, en cualquier orden posible o, en otras palabras, que el máximo global sería 5+6+7 = 18.
 */
package EPD3.TipoExamen;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class SA_TS_p1_1 {

    public static final int NUM_MATERIALES = 7;
    public static final int NUM_VECINOS = 10;
    public static final int TENENCIA_TABU = 4;
    public static final int MAX_ITERACIONES = 100;
    public static final int UMBRAL_COSTE = 18; // Máximo global

    // Matriz triangular de afinidades (valores de intercambio)
    public static final int[][] MATRIZ_AFINIDADES = {
        {0, 1, 2, 3, 4, 5, 6},
        {0, 0, 8, 9, 10, 11, 12},
        {0, 0, 0, 15, 16, 17, 18},
        {0, 0, 0, 0, 22, 23, 24},
        {0, 0, 0, 0, 0, 29, 30},
        {0, 0, 0, 0, 0, 0, 36},
        {0, 0, 0, 0, 0, 0, 0}
    };

    public static void main(String[] args) {
        int[] solucionTabu = tabuSearch();
        System.out.println("\n\nLa mejor combinación encontrada por Tabu Search es: " + Arrays.toString(solucionTabu)
                + ", con calidad aislante: " + calcularCoste(solucionTabu));

        int[] solucionSA = enfriamientoSimulado();
        System.out.println("\n\nLa mejor combinación encontrada por Enfriamiento Simulado es: " + Arrays.toString(solucionSA)
                + ", con calidad aislante: " + calcularCoste(solucionSA));
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
        int[] solucion = {1, 2, 3, 4, 5, 6, 7};
        Random rand = new Random();
        for (int i = 0; i < NUM_MATERIALES; i++) {
            int j = rand.nextInt(NUM_MATERIALES);
            int temp = solucion[i];
            solucion[i] = solucion[j];
            solucion[j] = temp;
        }
        return solucion;
    }

    private static int[] generarVecino(int[] solucion, int i, int j) {
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        int temp = vecino[i];
        vecino[i] = vecino[j];
        vecino[j] = temp;
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion) {
        int[][] vecinos = new int[NUM_VECINOS][solucion.length];
        Random rand = new Random();
        for (int i = 0; i < NUM_VECINOS; i++) {
            int a = rand.nextInt(NUM_MATERIALES);
            int b = rand.nextInt(NUM_MATERIALES);
            vecinos[i] = generarVecino(solucion, a, b);
        }
        return vecinos;
    }

    private static double calcularCoste(int[] solucion) {
        return solucion[0] + solucion[1] + solucion[2];
    }

    private static boolean probAceptacion(double delta, double temperatura) {
        return (Math.exp(delta / temperatura) > Math.random());
    }
}