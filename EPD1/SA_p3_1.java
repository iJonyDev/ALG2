package EPD1;
/*
Un modelo de coche se configura a partir de N componentes distintos. Cada uno de
esos componentes puede tomar M posibles valores, siendo vij el valor que toma el componente i (i=1, ..., N)
para la posibilidad j (j=1, ..., M) La afinidad de los consumidores para cada posible valor vij es aij y el coste cij.
Se entiende que el coste es el valor por la afinidad, esto es, cij = vij * aij. Se desea encontrar una combinación
de componentes que alcance la máxima afinidad global con los gustos de los consumidores y cuyo coste no
supere un umbral MAX_COSTE.
Para poder comprobar que la metaheurística diseñada funciona, suponga que N = M = 4. Los valores aij y vij
estarán comprendidos entre [1, 10] y, por tanto, cij entre [1, 100]. Por último, MAX_COSTE < 120. 
*/
import java.util.Arrays;
import java.util.Random;

public class SA_p3_1 {
    public static final int NUM_COMPONENTES = 4; // Número de componentes
    public static final int NUM_VALORES = 4; // Número de valores posibles para cada componente
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;
    public static final int MAX_COSTE = 120;


    public static void main(String[] args) {
        int[][] valores = generarValoresAleatorios(NUM_COMPONENTES, NUM_VALORES);
        int[][] afinidades = generarAfinidadesAleatorias(NUM_COMPONENTES, NUM_VALORES);
        int[][] costes = calcularCostes(valores, afinidades);
        int[] solucion = enfriamientoSimulado(valores, afinidades, costes);
        System.out.println("\n\nLa mejor combinación encontrada es: " + Arrays.toString(solucion) 
                            + ", con afinidad total: " + calcularAfinidad(solucion, afinidades) 
                            + " y coste total: " + calcularCoste(solucion, costes));
    }

    private static int[] enfriamientoSimulado(int[][] valores, int[][] afinidades, int[][] costes) {
        int[] solActual = generarSolucionInicial();
        while (calcularCoste(solActual, costes) > MAX_COSTE) {
            solActual = generarSolucionInicial();
        }
        int[] solMejor = Arrays.copyOf(solActual, solActual.length);
        int[][] vecinos;

        double afinidadActual = calcularAfinidad(solActual, afinidades);
        double afinidadMejor = afinidadActual;
        double afinidadVecino;
        double delta;

        double temperatura = T_INICIAL;
        int exitos = -1;
        int i;

        while (temperatura >= T_FINAL && exitos != 0) {
            exitos = 0;
            System.out.println("\n*************************\nLa solucion actual para esta iteracion es: " + Arrays.toString(solActual) + " \n");
            vecinos = generarVecinos(solActual);
            for (i = 0; i < vecinos.length; i++) {
                if (calcularCoste(vecinos[i], costes) <= MAX_COSTE) {
                    afinidadVecino = calcularAfinidad(vecinos[i], afinidades);
                    delta = afinidadVecino - afinidadActual;

                    if (delta > 0 || probAceptacion(delta, temperatura)) {
                        exitos++;
                        solActual = Arrays.copyOf(vecinos[i], vecinos[i].length);
                        afinidadActual = afinidadVecino;

                        if (afinidadVecino > afinidadMejor) {
                            solMejor = Arrays.copyOf(vecinos[i], vecinos[i].length);
                            afinidadMejor = afinidadVecino;
                        }
                    }
                }
            }
            System.out.println("\nsolMejor tras la iteracion: " + Arrays.toString(solMejor) + "\n");
            temperatura *= 0.9;
        }
        if (exitos == 0) {
            System.out.println("\nSe termina el proceso de búsqueda por no haber mejores vecinos");
        } else {
            System.out.println("\nSe termina el proceso de búsqueda por enfriarse el proceso");
        }
        return solMejor;
    }

    private static int[] generarSolucionInicial() {
        Random rand = new Random();
        int[] solucion = new int[NUM_COMPONENTES];
        for (int i = 0; i < NUM_COMPONENTES; i++) {
            solucion[i] = rand.nextInt(NUM_VALORES);
        }
        return solucion;
    }

    private static int[] generarVecino(int[] solucion, int componente) {
        Random rand = new Random();
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        vecino[componente] = rand.nextInt(NUM_VALORES);
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion) {
        int[][] vecinos = new int[NUM_COMPONENTES][solucion.length];
        for (int i = 0; i < NUM_COMPONENTES; i++) {
            vecinos[i] = generarVecino(solucion, i);
        }
        return vecinos;
    }

    private static double calcularAfinidad(int[] solucion, int[][] afinidades) {
        double afinidadTotal = 0;
        for (int i = 0; i < solucion.length; i++) {
            afinidadTotal += afinidades[i][solucion[i]];
        }
        return afinidadTotal;
    }

    private static double calcularCoste(int[] solucion, int[][] costes) {
        double costeTotal = 0;
        for (int i = 0; i < solucion.length; i++) {
            costeTotal += costes[i][solucion[i]];
        }
        return costeTotal;
    }

    private static boolean probAceptacion(double delta, double temperatura) {
        return (Math.exp(delta / temperatura) > Math.random());
    }

    private static int[][] generarValoresAleatorios(int numComponentes, int numValores) {
        Random rand = new Random();
        int[][] valores = new int[numComponentes][numValores];
        for (int i = 0; i < numComponentes; i++) {
            for (int j = 0; j < numValores; j++) {
                valores[i][j] = rand.nextInt(10) + 1;
            }
        }
        return valores;
    }

    private static int[][] generarAfinidadesAleatorias(int numComponentes, int numValores) {
        Random rand = new Random();
        int[][] afinidades = new int[numComponentes][numValores];
        for (int i = 0; i < numComponentes; i++) {
            for (int j = 0; j < numValores; j++) {
                afinidades[i][j] = rand.nextInt(10) + 1;
            }
        }
        return afinidades;
    }

    private static int[][] calcularCostes(int[][] valores, int[][] afinidades) {
        int[][] costes = new int[valores.length][valores[0].length];
        for (int i = 0; i < valores.length; i++) {
            for (int j = 0; j < valores[i].length; j++) {
                costes[i][j] = valores[i][j] * afinidades[i][j];
            }
        }
        return costes;
    }
}