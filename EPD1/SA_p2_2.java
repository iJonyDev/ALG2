package EPD1;

import java.util.Arrays;
import java.util.Random;

public class SA_p2_2 {
    public static final int NUM_PROCESOS = 7;
    public static final int NUM_PROCESADORES = 3;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;

    private static final int[][] TIEMPOS = {
        {2, 3, 1},
        {4, 2, 3},
        {3, 1, 2},
        {5, 4, 3},
        {2, 3, 4},
        {3, 2, 1},
        {4, 3, 2}
    };

    public static void main(String[] args) {
        int[] solucion = enfriamientoSimulado();
        System.out.println("\nMejor solucion:" + Arrays.toString(solucion) + ", con tiempo total: " + calcularCoste(solucion));
    }

    private static int[] enfriamientoSimulado(){
        int[] solActual = generarSolucionInicial();
        int[] solMejor = Arrays.copyOf(solActual, solActual.length);
        int[][] vecinos;

        double costeActual = calcularCoste(solActual);
        double costeMejor = costeActual;
        double costeVecino;
        double delta;
        double temperatura = T_INICIAL;
        int exitos = -1;

        while(temperatura >= T_FINAL && exitos != 0){
            exitos = 0;
            System.out.println("\n******************\nSolucion actual para esta iteracion: " + Arrays.toString(solActual));
            vecinos = generarVecinos(solActual);
            for(int i = 0; i < vecinos.length ; i++){
                costeVecino = calcularCoste(vecinos[i]);
                delta = costeVecino - costeActual;
                if(delta < 0 || probAceptacion(delta,temperatura)){
                    exitos++;
                    solActual = Arrays.copyOf(vecinos[i],vecinos[i].length);
                    costeActual = costeVecino;
                    if(costeVecino < costeMejor){
                        solMejor = Arrays.copyOf(vecinos[i], vecinos[i].length);
                        costeMejor = costeVecino;
                    }
                }
            }
            System.out.println("\nsolMejor para esta iteracion: " + Arrays.toString(solMejor));
            temperatura *= 0.9;
        }
        if(exitos == 0){
            System.out.println("\nEl proceso termina por no haber mejores vecinos.");
        }else{
            System.out.println("\nEl proceso termina por enfriarse el proceso.");
        }
        return solMejor;
    }

    private static int[] generarVecino(int[] solucion, int proceso){
        Random rand = new Random();
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        vecino[proceso] = rand.nextInt(NUM_PROCESADORES);
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion){
        int[][] vecinos = new int[NUM_PROCESOS][solucion.length];
        for(int i = 0; i < NUM_PROCESOS; i++){
            vecinos[i] = generarVecino(solucion, i);
        }
        return vecinos;
    }

    private static double calcularCoste(int[] solucion){
        int[] tiemposProcesadores = new int[NUM_PROCESADORES];
        for(int i = 0; i < solucion.length ; i++){
            tiemposProcesadores[solucion[i]] += TIEMPOS[i][solucion[i]];
        }
        return Arrays.stream(tiemposProcesadores).max().getAsInt();
    }

    private static boolean probAceptacion(double delta, double temperatura){
        return (Math.exp(-delta/temperatura) > Math.random());
    }

    private static int[] generarSolucionInicial(){
        Random rand = new Random();
        int[] solucion = new int[NUM_PROCESOS];
        for(int i = 0; i < NUM_PROCESOS; i++){
            solucion[i] = rand.nextInt(NUM_PROCESADORES);
        }
        return solucion;
    }
}
