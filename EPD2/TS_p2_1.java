package EPD2;
/*
Se dispone de un conjunto de n procesos y un ordenador con m procesadores (de
características no necesariamente iguales). Se conoce el tiempo que requiere el procesador j-ésimo para
realizar el proceso i-ésimo, tij. Se desea encontrar un reparto de procesos entre los m procesadores tal que el
tiempo de finalización sea lo más corto posible. Tome tantas decisiones como estime conveniente e intente
comparar distintas soluciones con distintas configuraciones iniciales. 
*/
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class TS_p2_1 {

    public static final int NUM_VECINOS = 10;
    public static final int NUM_PROCESOS = 7;
    public static final int NUM_PROCESADORES = 3;
    public static final int TENENCIA_TABU = 5;
    public static final int MAX_ITERACIONES = 10;


    private static final int[][] TIEMPOS = new int[NUM_PROCESOS][NUM_PROCESADORES];

    static {
        Random rand = new Random();
        for (int i = 0; i < NUM_PROCESOS; i++) {
            for (int j = 0; j < NUM_PROCESADORES; j++) {
                TIEMPOS[i][j] = rand.nextInt(10) + 1;
            }
        }
    }

    public static void main(String[] args) {
        int[] solucion = tabuSearch();
        System.out.println("\n\nEl tiempo de finalización mínimo encontrado es " + calcularCoste(solucion) + " con la solución: " + Arrays.toString(solucion));
    }

    private static int[] tabuSearch() {
        int[] solActual = generarSolucionInicial(), solMejor = solActual;
        Queue<int[]> listaTabu = new LinkedList<>();
        double costeActual = calcularCoste(solActual), costeMejor = costeActual;
        int iteraciones = 0;
    
        while (iteraciones++ < MAX_ITERACIONES) {
            System.out.println("\n\n************** Iteracion " + (iteraciones + 1));
            int[][] vecinos = generarVecinos(solActual);
            int[] mejorVecino = null;
            double mejorCosteVecino = Double.MAX_VALUE;
    
            for (int i = 0; i < vecinos.length; i++) {
                double costeVecino = calcularCoste(vecinos[i]);
                System.out.println("\nVecino "+ i +", tiempo de finalización=" + costeVecino);
                if (!listaTabu.contains(vecinos[i]) && costeVecino < mejorCosteVecino) {
                    mejorVecino = vecinos[i];
                    mejorCosteVecino = costeVecino;
                }
            }
    
            if (mejorVecino != null) {
                solActual = Arrays.copyOf(mejorVecino, mejorVecino.length);
                costeActual = mejorCosteVecino;
                if (costeActual < costeMejor) {
                    solMejor = Arrays.copyOf(solActual, solActual.length);
                    costeMejor = costeActual;
                    System.out.println("\nSolucion mejor global actualizada en iteracion = " + iteraciones);
                }
                listaTabu.add(solActual);
                if (listaTabu.size() > TENENCIA_TABU) listaTabu.poll();
            }
        }
        return solMejor;
    }

    private static int[] generarSolucionInicial() {
        Random rand = new Random();
        int[] solucion = new int[NUM_PROCESOS];
        for (int i = 0; i < NUM_PROCESOS; i++) {
            solucion[i] = rand.nextInt(NUM_PROCESADORES);
        }
        return solucion;
    }

    private static int[] generarVecino(int[] solucion, int pos, int nuevoProcesador) {
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        vecino[pos] = nuevoProcesador;
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion) {
        int[][] vecinos = new int[NUM_VECINOS][NUM_PROCESOS];
        Random rand = new Random();
        for (int i = 0; i < NUM_VECINOS; i++) {
            int pos = rand.nextInt(NUM_PROCESOS);
            int nuevoProcesador = rand.nextInt(NUM_PROCESADORES);
            vecinos[i] = generarVecino(solucion, pos, nuevoProcesador);
        }
        return vecinos;
    }

    private static double calcularCoste(int[] array) {
        int[] tiemposProcesadores = new int[NUM_PROCESADORES];
        for (int i = 0; i < array.length; i++) {
            tiemposProcesadores[array[i]] += TIEMPOS[i][array[i]];
        }
        //Retornar el valor máximo como un entero que representa el tiempo de finalización
        return Arrays.stream(tiemposProcesadores).max().getAsInt();
        
    }
}