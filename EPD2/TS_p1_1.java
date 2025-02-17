package EPD2;
/*
Se desea fabricar un material aislante compuesto por siete materiales distintos. 
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class TS_p1_1 {

    public static final int NUM_VECINOS = 10;
    public static final int NUM_MATERIALES = 7;
    public static final int TENENCIA_TABU = 5;
    public static final int MAX_ITERACIONES = 10;

    private static final int[] MATERIALES = {1, 2, 3, 4, 5, 6, 7};

    public static void main(String[] args) {
        int[] solucion = tabuSearch();
        System.out.println("\n\nLa calidad aislante máxima encontrada es " + calcularCoste(solucion) + " con la solución: " + Arrays.toString(solucion));
    }

    private static int[] tabuSearch() {
        int[] solActual = Arrays.copyOf(MATERIALES, MATERIALES.length);
        int[] solMejor = Arrays.copyOf(MATERIALES, MATERIALES.length);
        int[][] vecinos;
        Queue<int[]> listaTabu = new LinkedList<>();

        double costeActual = calcularCoste(solActual);
        double costeMejor = costeActual;
        double costeVecino;
        int iteraciones = 0;

        while (iteraciones < MAX_ITERACIONES) {
            System.out.println("\n\n************** Iteracion " + (iteraciones + 1));
            vecinos = generarVecinos(solActual);
            int[] mejorVecino = null;
            double mejorCosteVecino = -1;

            for (int i = 0; i < NUM_VECINOS; i++) {
                System.out.println("\nVecino " + i + " generado: calidad aislante=" + calcularCoste(vecinos[i]));
                if (!listaTabu.contains(vecinos[i])) {
                    costeVecino = calcularCoste(vecinos[i]);
                    if (costeVecino > mejorCosteVecino) {
                        mejorVecino = vecinos[i];
                        mejorCosteVecino = costeVecino;
                    }
                }
            }

            if (mejorVecino != null) {
                solActual = Arrays.copyOf(mejorVecino, mejorVecino.length);
                costeActual = mejorCosteVecino;

                if (costeActual > costeMejor) {
                    solMejor = Arrays.copyOf(solActual, solActual.length);
                    costeMejor = costeActual;
                    System.out.println("\nSolucion mejor global actualizada en iteracion = " + (iteraciones + 1));
                }

                listaTabu.add(solActual);
                if (listaTabu.size() > TENENCIA_TABU) {
                    listaTabu.poll();
                }
            }

            iteraciones++;
        }

        return solMejor;
    }

    private static int[] generarVecino(int[] solucion, int i, int j) {
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        int temp = vecino[i];
        vecino[i] = vecino[j];
        vecino[j] = temp;
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion) {
        int[][] vecinos = new int[NUM_VECINOS][NUM_MATERIALES];
        Random rand = new Random();
        for (int i = 0; i < NUM_VECINOS; i++) {
            int pos1 = rand.nextInt(NUM_MATERIALES);
            int pos2 = rand.nextInt(NUM_MATERIALES);
            while (pos1 == pos2) {
                pos2 = rand.nextInt(NUM_MATERIALES);
            }
            vecinos[i] = generarVecino(solucion, pos1, pos2);
        }
        return vecinos;
    }

    private static double calcularCoste(int[] array) {
        return array[0] + array[1] + array[2];
    }
}