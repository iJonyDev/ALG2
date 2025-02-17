package EPD1;

import java.util.Arrays;

public class SA_p3_2 {
    public static final int NUM_COMPONENTES = 4;
    public static final int NUM_VALORES = 4;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;
    public static final int MAX_COSTE = 120;

    public static void main(String[] args) {
        int[][] valores = generarValoresAleatorios(NUM_COMPONENTES, NUM_VALORES);
        int[][] afinidades = generarAfinidadesAleatorias(NUM_COMPONENTES, NUM_VALORES);
        int[][] costes = calcularCostes(valores, afinidades);
        int[] solucion = enfriamientoSimulado(valores, afinidades, costes);
        System.out.println("\nLa mejor combinacion encontrada es: " + Arrays.toString(solucion)
                        + ", con afinidad total: " + calcularAfinidad(solucion, afinidades) 
                        + " y coste total: " + calcularCoste(solucion, costes));
    }

    private static int[] enfriamientoSimulado(int[][] valores, int[][] afinidades, int[][] costes){
        int[] solActual = generarSolucionInicial();
        while(calcularCoste(solActual, costes) > MAX_COSTE){
            solActual = generarSolucionInicial();
        }
        int[] solMejor = Arrays.copyOf(solActual, solActual.length);
        int[][] vecinos;

        double 
    }
}
