package EPD2;
/*
Queremos hallar el máximo de f(x)=x3-60x2+900x+100 entre x=0 y x=31. 
Para resolver el problema usando TS, se propone seguir la siguiente estrategia. 
En primer lugar, discretizamos el rango de valores de x con vectores binarios de 5 componentes entre 00000 y 11111. 
Estos 32 vectores constituyen S las soluciones factibles del problema. 
En la Tabla Auxiliar puede ver cuáles son los valores asociados a cada solución posible.
Suponga una tenencia tabú de 3 unidades de tiempo. Defina una condición de parada que considere adecuada. 
*/
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class TS_ej1_1 {

    public static final int NUM_VECINOS = 5;
    public static final int NUM_BITS = 5;
    public static final int TENENCIA_TABU = 2;
    public static final int MAX_ITERACIONES = 5;

    public static void main(String[] args) {
        int[] solucion = tabuSearch();
        System.out.println("\n\nEl valor máximo encontrado es x=" + binaryToDecimal(solucion) + ", siendo f(x)=" + calcularCoste(solucion));
    }

    private static int[] tabuSearch() {
        int[] solActual = {0, 0, 0, 0, 0};
        int[] solMejor = {0, 0, 0, 0, 0};
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
                System.out.println("\nVecino " + i + " generado: sol=" + binaryToDecimal(vecinos[i]) + ", movimiento=" + i + ", coste=" + calcularCoste(vecinos[i]));
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
        for (int i = 0; i < NUM_VECINOS; i++) {
            vecinos[i] = generarVecino(solucion, i);
        }
        return vecinos;
    }

    private static double calcularCoste(int[] array) {
        int n = binaryToDecimal(array);
        return Math.pow(n, 3) - 60 * Math.pow(n, 2) + 900 * n + 100;
    }

    private static int binaryToDecimal(int[] array) {
        int n = 0;
        for (int i = 0; i < array.length; i++) {
            n += array[i] << i;
        }
        return n;
    }
}
