package EPD2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class TS_ej2_2 {

    public static final int NUM_VECINOS = 10;
    public static final int NUM_BITS = 100;
    public static final int TENENCIA_TABU = 4;
    public static final int MAX_ITERACIONES = 10;
    public static final int CAPACIDAD_MOCHILA = 180;

    public static final int[] P = new int[NUM_BITS];
    public static final int[] B = new int[NUM_BITS];

    static{
        Random rand = new Random();
        for(int i = 0; i < NUM_BITS; i++){
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
        for (int i = 0; i < NUM_VECINOS; i++) {
            vecinos[i] = generarVecino(solucion, i);
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
