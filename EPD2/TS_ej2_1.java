
/*
Queremos resolver el problema de la mochila utilizando TS. 
Para ello, suponga que la capacidad de la mochila es q = 180 y que puede insertar hasta 100 elementos, con pesos aleatorios
comprendidos entre [1, 100], esto es, considere el siguiente vector P = {p1, p2, ..., p100}, con pi = [1,100]. 
Además, se dispone de un beneficio asociado a cada elemento, B = {b1, b2, ..., b100}, con bi = [1,10]. 
Se permite que dos elementos pesen lo mismo y que tengan el mismo beneficio.
Resuelva el problema de tal modo que se maximice el beneficio y que no se incumpla la condición del peso
máximo permitido.
Suponga que:
1. La solución inicial es un vector de todo a 0 (ningún elemento incluido en la mochila).
2. La tenencia tabú es 5.
3. Se evalúan 10 vecinos en cada iteración.
4. Un vecino será la permutación de un bit escogido aleatoriamente.
5. El número de iteraciones es 10.
6. El nivel de aspiración se cumple si la solución es mejor que la mejor encontrada.
 */
package EPD2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class TS_ej2_1 {

    public static final int NUM_VECINOS = 10, NUM_BITS = 100, TENENCIA_TABU = 5, MAX_ITERACIONES = 10, CAPACIDAD_MOCHILA = 180;
    private static final int[] P = new int[NUM_BITS], B = new int[NUM_BITS];

    static {
        Random rand = new Random();
        for (int i = 0; i < NUM_BITS; i++) {
            P[i] = rand.nextInt(100) + 1;
            B[i] = rand.nextInt(10) + 1;
        }
    }

    public static void main(String[] args) {
        int[] solucion = tabuSearch();
        System.out.println("\nMejor solucion encontrada: " + Arrays.toString(solucion) + ", con beneficio max = " + calcularCoste(solucion));
    }

    private static int[] tabuSearch() {
        int[] solActual = new int[NUM_BITS], solMejor = solActual;
        Queue<int[]> listaTabu = new LinkedList<>();
        double costeActual = calcularCoste(solActual), costeMejor = costeActual;
        int iteraciones = 0;

        while (iteraciones++ < MAX_ITERACIONES) {
            System.out.println("\n************* \nIteracion " + (iteraciones + 1));
            int[][] vecinos = generarVecinos(solActual);
            int[] mejorVecino = null;
            double mejorCosteVecino = -1;

            for (int i = 0; i < vecinos.length; i++) {
                double costeVecino = calcularCoste(vecinos[i]);
                System.out.println("\nVecino " + i + ", beneficio = " + costeVecino);
                if (!listaTabu.contains(vecinos[i]) && costeVecino > mejorCosteVecino) {
                    mejorVecino = vecinos[i];
                    mejorCosteVecino = costeVecino;
                }
            }

            if (mejorVecino != null) {
                solActual = Arrays.copyOf(mejorVecino, mejorVecino.length);
                costeActual = mejorCosteVecino;
                if (costeActual > costeMejor) {
                    solMejor = Arrays.copyOf(solActual, solActual.length);
                    costeMejor = costeActual;
                    System.out.println("\nSolucion mejor global actualizada en iteracion: " + iteraciones);
                }
            }

            listaTabu.add(solActual);
            if (listaTabu.size() > TENENCIA_TABU) listaTabu.poll();
        }
        return solMejor;
    }

    private static int[] generarVecino(int[] solucion, int bit) {
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        vecino[bit] = 1 - vecino[bit];
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion) {
        int[][] vecinos = new int[NUM_VECINOS][NUM_BITS];
        Random rand = new Random();
        for (int i = 0; i < NUM_VECINOS; i++) {
            int bit = rand.nextInt(NUM_BITS);
            vecinos[i] = generarVecino(solucion, bit);
        }
        return vecinos;
    }

    private static double calcularCoste(int[] array) {
        int pesoTotal = 0, beneficioTotal = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 1) {
                pesoTotal += P[i];
                beneficioTotal += B[i];
            }
        }
        return (pesoTotal > CAPACIDAD_MOCHILA) ? -1 : beneficioTotal;
    }
}