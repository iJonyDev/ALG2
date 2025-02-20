package EPD1;

import java.util.Arrays;
import java.util.Random;

public class SA_p3_3 {
    public static final int NUM_COMPONENTES = 4;
    public static final int NUM_VALORES = 4;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;
    public static final int MAX_COSTE = 120;

    private static final int[][] valores = new int[NUM_COMPONENTES][NUM_VALORES];
    private static final int[][] afinidades = new int[NUM_COMPONENTES][NUM_VALORES];

    static {
        Random rand = new Random();
        for (int i = 0; i < NUM_COMPONENTES; i++) {
            for (int j = 0; j < NUM_VALORES; j++) {
                valores[i][j] = rand.nextInt(10) + 1;
                afinidades[i][j] = rand.nextInt(10) + 1;
            }
        }
    }

    public static void main(String[] args) {
        int[][] costes = calcularCostes(valores, afinidades);
        int[] solucion = enfriamientoSimulado(afinidades, costes);
        System.out.println("\nLa mejor combinacion encontrada es: " + Arrays.toString(solucion)
                        + ", con afinidad total: " + calcularAfinidad(solucion, afinidades) 
                        + " y coste total: " + calcularCoste(solucion, costes));
    }

    private static int[] enfriamientoSimulado(int[][] afinidades, int[][] costes){
        int[] solActual = generarSolucionInicial(costes);
        int[] solMejor = solActual;
        int[][] vecinos;

        double afinidadActual = calcularAfinidad(solActual, afinidades);
        double afinidadMejor = afinidadActual;
        double afinidadVecino;
        double delta;
        double temperatura = T_INICIAL;
        int exitos = -1;

        while (temperatura >= T_FINAL && exitos != 0 ) {
            exitos = 0;
            System.out.println("\n************************\nSolucion actual para esta iteracion es: " + Arrays.toString(solActual));
            vecinos = generarVecinos(solActual);
            for(int i=0; i < vecinos.length ; i++){
                if(calcularCoste(vecinos[i], costes) <= MAX_COSTE){
                    afinidadVecino = calcularAfinidad(vecinos[i], afinidades);
                    delta = afinidadVecino - afinidadActual;
                    if(delta > 0 || probAceptacion(delta, temperatura)){
                        exitos++;
                        solActual = Arrays.copyOf(vecinos[i], vecinos[i].length);
                        afinidadActual = afinidadVecino;
                        if(afinidadActual > afinidadMejor){
                            solMejor = Arrays.copyOf(vecinos[i], vecinos[i].length);
                            afinidadMejor = afinidadVecino;
                        }
                    }
                }
            }
            System.out.println("\nSolucion mejor para esta iteracion: " + Arrays.toString(solMejor));
            temperatura *= 0.9;
        }
        if(exitos == 0){
            System.out.println("\nTermina el proceso por no haber mejoras vecinos.");
        }else{
            System.out.println("\nTermina el proceso por enfriarse el proceso.");
        }
        return solMejor;
    }

    private static int[] generarSolucionInicial(int[][] costes){
        Random rand = new Random();
        int[] solucion;
        do {
            solucion = new int[NUM_COMPONENTES];
            for(int i = 0; i < NUM_COMPONENTES; i++){
                solucion[i] = rand.nextInt(NUM_VALORES);
            }
        } while(calcularCoste(solucion, costes) > MAX_COSTE);
        return solucion;
    }

    private static int[] generarVecino(int[] solucion, int componente){
        Random rand = new Random();
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        vecino[componente] = rand.nextInt(NUM_VALORES);
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion){
        int[][] vecinos = new int[NUM_COMPONENTES][solucion.length];
        for(int i=0; i< NUM_COMPONENTES; i++){
            vecinos[i] = generarVecino(solucion, i);
        }
        return vecinos;
    }

    private static double calcularAfinidad(int[] solucion, int[][] afinidades){ 
        double afinidadTotal = 0;
        for(int i=0; i < solucion.length ; i++){
            afinidadTotal += afinidades[i][solucion[i]];
        }
        return afinidadTotal;
    }

    private static double calcularCoste(int[] solucion, int[][] costes){ 
        double costeTotal = 0;
        for(int i=0; i < solucion.length ; i++){
            costeTotal += costes[i][solucion[i]];
        }
        return costeTotal;
    }

    private static int[][] calcularCostes(int[][] valores, int[][] afinidades){
        int[][] costes = new int[valores.length][valores[0].length];
        for(int i=0; i < valores.length; i++){
            for(int j=0; j < valores[i].length; j++){
                costes[i][j] = valores[i][j]*afinidades[i][j];
            }
        }
        return costes;
    }

    private static boolean probAceptacion(double delta, double temperatura){
        return (Math.exp(delta/temperatura) > Math.random());
    }
}