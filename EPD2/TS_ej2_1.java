package EPD2;
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class TS_ej2_1 {

    public static final int NUM_VECINOS = 10;
    public static final int NUM_BITS = 100;
    public static final int TENENCIA_TABU = 5;
    public static final int MAX_ITERACIONES = 10;
    public static final int CAPACIDAD_MOCHILA = 180;

    private static final int[] P = new int[NUM_BITS];
    private static final int[] B = new int[NUM_BITS];

    static {
        Random rand = new Random();
        for (int i = 0; i < NUM_BITS; i++) {
            P[i] = rand.nextInt(100) + 1;
            B[i] = rand.nextInt(10) + 1;
        }
    }

    public static void main(String[] args) {
        int[] solucion = tabuSearch();
        System.out.println("\n\nEl beneficio máximo encontrado es " + calcularCoste(solucion) + " con la solución: " + Arrays.toString(solucion));
    }

    private static int[] tabuSearch() {
        int[] solActual = new int[NUM_BITS];
        int[] solMejor = new int[NUM_BITS];
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
                System.out.println("\nVecino " + i + " generado: beneficio=" + calcularCoste(vecinos[i]));
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
        int pesoTotal = 0;
        int beneficioTotal = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 1) {
                pesoTotal += P[i];
                beneficioTotal += B[i];
            }
        }
        if (pesoTotal > CAPACIDAD_MOCHILA) {
            return -1; // Penalización por exceder la capacidad
        }
        return beneficioTotal;
    }
}