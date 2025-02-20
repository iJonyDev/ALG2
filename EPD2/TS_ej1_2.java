package EPD2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class TS_ej1_2 {
    public static final int NUM_VECINOS = 5;
    public static final int NUM_BITS = 5;
    public static final int TENENCIA_TABU = 2;
    public static final int MAX_ITERACIONES = 5;

    public static void main(String[] args) {
        int[] solucion = tabuSearch();
        System.out.println("\nEl mejor valor de x= " + binaryToDecimal(solucion) + ", con f(x)= " + calcularCoste(solucion));
    }
    
    private static int[] tabuSearch(){
        int[] solActual = new int[NUM_BITS];
        int[] solMejor = solActual;
        int[][] vecinos;
        Queue<int[]> listaTabu = new LinkedList<>();
        double costeActual = calcularCoste(solActual);
        double costeMejor = costeActual;
        int iteraciones = 0;

        while(iteraciones <= MAX_ITERACIONES){
            System.out.println("\n**************\nIteracion " + (iteraciones + 1));
            vecinos = generarVecinos(solActual);
            int[] mejorVecino = null;
            double mejorCosteVecino = -1;

            for (int i = 0; i < vecinos.length; i++) {
                double costeVecino = calcularCoste(vecinos[i]);
                System.out.println("\nVecino " + i + ", coste = " + costeVecino);
                if (!listaTabu.contains(vecinos[i]) && costeVecino > mejorCosteVecino) {
                    mejorVecino = vecinos[i];
                    mejorCosteVecino = costeVecino;
                }
            }
            if(mejorVecino != null){
                solActual = Arrays.copyOf(mejorVecino, mejorVecino.length);
                costeActual = mejorCosteVecino;
                if(costeActual > costeMejor){
                    solMejor = Arrays.copyOf(mejorVecino, mejorVecino.length);
                    costeMejor = costeActual;
                    System.out.println("\nSolucion mejor global actualizada en iteracion: " + (iteraciones + 1));
                }
                listaTabu.add(solActual);
                if(listaTabu.size() > TENENCIA_TABU){
                    listaTabu.poll();
                }
            }
            iteraciones++;
        }
        return solMejor;
    }

    private static int[] generarVecino(int[] solucion, int bit){
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        vecino[bit] = 1 - vecino[bit];
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion){
        int[][] vecinos = new int[NUM_VECINOS][NUM_BITS];
        for(int i = 0; i < NUM_VECINOS; i++){
            vecinos[i] = generarVecino(solucion, i);
        }
        return vecinos;
    }

    private static double calcularCoste(int[] array){
        int n = binaryToDecimal(array);
        return Math.pow(n,3) - 60*Math.pow(n,2) + 900*n + 100;
    }

    private static int binaryToDecimal(int[] array){
        int n = 0;
        for(int i = 0; i < array.length; i++){
            n += array[i] << i;
        }
        return n;
    }

}
