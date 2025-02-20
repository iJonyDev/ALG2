/*
 * Se dispone de un conjunto de n procesos y un ordenador con m procesadores (de
características no necesariamente iguales). Se conoce el tiempo que requiere el procesador j-ésimo para
realizar el proceso i-ésimo, tij. Se desea encontrar un reparto de procesos entre los m procesadores tal que el
tiempo de finalización sea lo más corto posible. Tome tantas decisiones como estime conveniente e intente
comparar distintas soluciones con distintas configuraciones iniciales. 
 */
package EPD3.TipoExamen;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class SA_TS_p2_1 {
    public static final int NUM_PROCESOS = 4;
    public static final int NUM_PROCESADORES = 4;
    public static final int NUM_VECINOS = 10;
    public static final int TENENCIA_TABU = 4;
    public static final int MAX_ITERACIONES = 10;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;

    public static void main(String[] args) {
        int[][] tiempos = generarMatrizAleatoria(NUM_PROCESOS, NUM_PROCESADORES);

        int[] solucionTabu = tabuSearch(tiempos);
        System.out.println("\n\nLa mejor asignación encontrada por Tabu Search es: " + Arrays.toString(solucionTabu) 
                            + ", con tiempo total: " + calcularTiempoTotal(solucionTabu, tiempos));

        int[] solucionSA = enfriamientoSimulado(tiempos);
        System.out.println("\n\nLa mejor asignación encontrada por Enfriamiento Simulado es: " + Arrays.toString(solucionSA) 
                            + ", con tiempo total: " + calcularTiempoTotal(solucionSA, tiempos));
    }

    private static int[] tabuSearch(int[][] tiempos) {
        int[] solActual = generarSolucionInicial();
        int[] solMejor = solActual;
        double tiempoActual = calcularTiempoTotal(solActual, tiempos), tiempoMejor = tiempoActual;
        Queue<int[]> listaTabu = new LinkedList<>();
        int iteraciones = 0;

        while (iteraciones++ < MAX_ITERACIONES) {
            int[][] vecinos = generarVecinos(solActual);
            int[] mejorVecino = null;
            double mejorTiempoVecino = Double.MAX_VALUE;

            for (int i = 0; i < vecinos.length; i++) {
                double tiempoVecino = calcularTiempoTotal(vecinos[i], tiempos);
                if (!listaTabu.contains(vecinos[i]) && tiempoVecino < mejorTiempoVecino) {
                    mejorTiempoVecino = tiempoVecino;
                    mejorVecino = vecinos[i];
                }
            }

            if (mejorVecino != null) {
                solActual = Arrays.copyOf(mejorVecino, mejorVecino.length);
                tiempoActual = mejorTiempoVecino;
                if (tiempoActual < tiempoMejor) {
                    solMejor = Arrays.copyOf(solActual, solActual.length);
                    tiempoMejor = tiempoActual;
                }
                listaTabu.add(solActual);
                if (listaTabu.size() > TENENCIA_TABU) listaTabu.poll();
            }
        }
        return solMejor;
    }

    private static int[] enfriamientoSimulado(int[][] tiempos){
        int[] solActual = generarSolucionInicial();
        int[] solMejor = solActual;
        double tiempoActual = calcularTiempoTotal(solActual, tiempos), tiempoMejor = tiempoActual, temperatura = T_INICIAL, delta;
        int exitos = -1;

        while (temperatura >= T_FINAL && exitos != 0 ) {
            exitos = 0;
            int[][] vecinos = generarVecinos(solActual);
            for (int i = 0; i < vecinos.length; i++) {
                double tiempoVecino = calcularTiempoTotal(vecinos[i], tiempos);
                delta = tiempoVecino - tiempoActual;
                if (delta < 0 || probAceptacion(delta, temperatura)) {
                    exitos++;
                    solActual = Arrays.copyOf(vecinos[i], vecinos[i].length);
                    tiempoActual = tiempoVecino;
                    if (tiempoActual < tiempoMejor) {
                        solMejor = Arrays.copyOf(vecinos[i], vecinos[i].length);
                        tiempoMejor = tiempoActual;
                    }
                }
            }
            temperatura *= 0.9;
        }
        return solMejor;
    }

    private static int[] generarSolucionInicial() {
        Random rand = new Random();
        int[] solucion = new int[NUM_PROCESOS];
        for (int i = 0; i < NUM_PROCESOS; i++) 
            solucion[i] = rand.nextInt(NUM_PROCESADORES);
        return solucion;
    }

    private static int[] generarVecino(int[] solucion, int proceso) {
        Random rand = new Random();
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        vecino[proceso] = rand.nextInt(NUM_PROCESADORES);
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion) {
        int[][] vecinos = new int[NUM_VECINOS][solucion.length];
        for (int i = 0; i < NUM_VECINOS; i++) 
            vecinos[i] = generarVecino(solucion, i % NUM_PROCESOS);
        return vecinos;
    }

    private static int[][] generarMatrizAleatoria(int numProcesos, int numProcesadores) {
        Random rand = new Random();
        int[][] matriz = new int[numProcesos][numProcesadores];
        for (int i = 0; i < numProcesos; i++) 
            for (int j = 0; j < numProcesadores; j++) 
                matriz[i][j] = rand.nextInt(10) + 1;
        return matriz;
    }

    private static double calcularTiempoTotal(int[] solucion, int[][] tiempos) {
        double[] tiemposProcesadores = new double[NUM_PROCESADORES];
        for (int i = 0; i < solucion.length; i++) 
            tiemposProcesadores[solucion[i]] += tiempos[i][solucion[i]];
        return Arrays.stream(tiemposProcesadores).max().getAsDouble();
    }

    private static boolean probAceptacion(double delta, double temperatura){
        return (Math.exp(-delta/temperatura) > Math.random());
    }
}
