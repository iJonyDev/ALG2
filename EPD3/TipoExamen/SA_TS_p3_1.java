/*
Un modelo de coche se configura a partir de N componentes distintos. Cada uno de
esos componentes puede tomar M posibles valores, siendo vij el valor que toma el componente i (i=1, ..., N)
para la posibilidad j (j=1, ..., M) La afinidad de los consumidores para cada posible valor vij es aij y el coste cij.
Se entiende que el coste es el valor por la afinidad, esto es, cij = vij * aij. Se desea encontrar una combinación
de componentes que alcance la máxima afinidad global con los gustos de los consumidores y cuyo coste no
supere un umbral MAX_COSTE.
Para poder comprobar que la metaheurística diseñada funciona, suponga que N = M = 4. Los valores aij y vij
estarán comprendidos entre [1, 10] y, por tanto, cij entre [1, 100]. Por último, UMBRAL_COSTE < 200. 
*/
package EPD3.TipoExamen;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class SA_TS_p3_1 {
    public static final int NUM_COMPONENTES = 4;
    public static final int NUM_VALORES = 4;
    public static final int NUM_VECINOS = 10;
    public static final int TENENCIA_TABU = 4;
    public static final int MAX_ITERACIONES = 10;
    public static final int UMBRAL_COSTE = 200;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;

    public static void main(String[] args) {
        int[][] valores = generarMatrizAleatoria(NUM_COMPONENTES, NUM_VALORES);
        int[][] afinidades = generarMatrizAleatoria(NUM_COMPONENTES, NUM_VALORES);
        int[][] costes = calcularCostes(valores, afinidades);

        int[] solucionTabu = tabuSearch(afinidades, costes);
        System.out.println("\n\nLa mejor combinación encontrada por Tabu Search es: " + Arrays.toString(solucionTabu) 
                            + ", con afinidad total: " + calcularTotal(solucionTabu, afinidades) 
                            + " y coste total: " + calcularTotal(solucionTabu, costes));

        int[] solucionSA = enfriamientoSimulado(afinidades, costes);
        System.out.println("\n\nLa mejor combinación encontrada por Enfriamiento Simulado es: " + Arrays.toString(solucionSA) 
                            + ", con afinidad total: " + calcularTotal(solucionSA, afinidades) 
                            + " y coste total: " + calcularTotal(solucionSA, costes));
    }

    private static int[] tabuSearch(int[][] afinidades, int[][] costes) {
        int[] solActual = generarSolucionInicial();
        while (calcularTotal(solActual, costes) > UMBRAL_COSTE) solActual = generarSolucionInicial();
        int[] solMejor = solActual;
        double afinidadActual = calcularTotal(solActual, afinidades), afinidadMejor = afinidadActual;
        Queue<int[]> listaTabu = new LinkedList<>();
        int iteraciones = 0;

        while (iteraciones++ < MAX_ITERACIONES) {
            int[][] vecinos = generarVecinos(solActual);
            int[] mejorVecino = null;
            double mejorAfinidadVecino = -1;

            for (int i = 0; i < vecinos.length; i++) {
                double costeVecino = calcularTotal(vecinos[i], costes);
                double afinidadVecino = calcularTotal(vecinos[i], afinidades);
                if (!listaTabu.contains(vecinos[i]) && afinidadVecino > mejorAfinidadVecino && costeVecino <= UMBRAL_COSTE ) {
                        mejorAfinidadVecino = afinidadVecino;
                        mejorVecino = vecinos[i];
                }
            }

            if (mejorVecino != null) {
                solActual = Arrays.copyOf(mejorVecino, mejorVecino.length);
                afinidadActual = mejorAfinidadVecino;
                if (afinidadActual > afinidadMejor) {
                    solMejor = Arrays.copyOf(solActual, solActual.length);
                    afinidadMejor = afinidadActual;
                }
                listaTabu.add(solActual);
                if (listaTabu.size() > TENENCIA_TABU) listaTabu.poll();
            }
        }
        return solMejor;
    }

    private static int[] enfriamientoSimulado(int[][] afinidades, int[][] costes){
        int[] solActual = generarSolucionInicial();
        while (calcularTotal(solActual, costes) > UMBRAL_COSTE) solActual = generarSolucionInicial();
        int[] solMejor = solActual;
        double afinidadActual = calcularTotal(solActual, afinidades), afinidadMejor = afinidadActual, temperatura = T_INICIAL, delta;
        int exitos = -1;

        while (temperatura >= T_FINAL && exitos != 0 ) {
            exitos = 0;
            int[][] vecinos = generarVecinos(solActual);
            for (int i = 0; i < vecinos.length; i++) {
                double costeVecino = calcularTotal(vecinos[i], costes);
                double afinidadVecino = calcularTotal(vecinos[i], afinidades);
                delta = afinidadVecino - afinidadActual;
                if (costeVecino <= UMBRAL_COSTE && (delta > 0 || probAceptacion(delta, temperatura))) {
                    exitos++;
                    solActual = Arrays.copyOf(vecinos[i], vecinos[i].length);
                    afinidadActual = afinidadVecino;
                    if (afinidadActual > afinidadMejor) {
                        solMejor = Arrays.copyOf(vecinos[i], vecinos[i].length);
                        afinidadMejor = afinidadVecino;
                    }
                }
            }
            temperatura *= 0.9;
        }
        return solMejor;
    }

    private static int[] generarSolucionInicial() {
        Random rand = new Random();
        int[] solucion = new int[NUM_COMPONENTES];
        for (int i = 0; i < NUM_COMPONENTES; i++) 
            solucion[i] = rand.nextInt(NUM_VALORES);
        return solucion;
    }

    private static int[] generarVecino(int[] solucion, int componente) {
        Random rand = new Random();
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        vecino[componente] = rand.nextInt(NUM_VALORES);
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion) {
        int[][] vecinos = new int[NUM_VECINOS][solucion.length];
        for (int i = 0; i < NUM_VECINOS; i++) 
            vecinos[i] = generarVecino(solucion, i % NUM_COMPONENTES);
        return vecinos;
    }

    private static int[][] generarMatrizAleatoria(int numComponentes, int numValores) {
        Random rand = new Random();
        int[][] matriz = new int[numComponentes][numValores];
        for (int i = 0; i < numComponentes; i++) 
            for (int j = 0; j < numValores; j++) 
                matriz[i][j] = rand.nextInt(10) + 1;
        return matriz;
    }

    private static double calcularTotal(int[] solucion, int[][] matriz) {
        double total = 0;
        for (int i = 0; i < solucion.length; i++) 
            total += matriz[i][solucion[i]];
        return total;
    }

    private static int[][] calcularCostes(int[][] valores, int[][] afinidades) {
        int[][] costes = new int[valores.length][valores[0].length];
        for (int i = 0; i < valores.length; i++) 
            for (int j = 0; j < valores[i].length; j++) 
                costes[i][j] = valores[i][j] * afinidades[i][j];
        return costes;
    }

    private static boolean probAceptacion(double delta, double temperatura){
        return (Math.exp(delta/temperatura) > Math.random());
    }
}