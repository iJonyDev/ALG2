/*
Se dispone de un conjunto de n procesos y un ordenador con m procesadores (de
características no necesariamente iguales). Se conoce el tiempo que requiere el procesador j-ésimo para
realizar el proceso i-ésimo, tij. Se desea encontrar un reparto de procesos entre los m procesadores tal que el
tiempo de finalización sea lo más corto posible. Tome tantas decisiones como estime conveniente e intente
comparar distintas soluciones con distintas configuraciones iniciales. 
*/
package EPD1;
import java.util.Arrays;
import java.util.Random;

public class SA_p2_1 {
    public static final int NUM_PROCESOS = 7;
    public static final int NUM_PROCESADORES = 3;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;

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
        int[] solucion = enfriamientoSimulado();
        System.out.println("\n\nLa mejor asignación encontrada es: " + Arrays.toString(solucion) + ", con tiempo total: " + calcularCoste(solucion));
    }

    private static int[] enfriamientoSimulado() {
        int[] solActual = generarSolucionInicial(), solMejor = solActual;
        double costeActual = calcularCoste(solActual), costeMejor = costeActual, costeVecino, delta, temperatura = T_INICIAL;
        int exitos = -1;

        while (temperatura >= T_FINAL && exitos != 0) {
            exitos = 0;
            System.out.println("\n*************************\nLa solucion actual para esta iteracion es: " + Arrays.toString(solActual) + " \n");
            int[][] vecinos = generarVecinos(solActual);
            for (int i = 0; i < vecinos.length; i++) {
                costeVecino = calcularCoste(vecinos[i]);
                delta = costeVecino - costeActual;
                if (delta < 0 || probAceptacion(delta, temperatura)) {
                    exitos++;
                    solActual = Arrays.copyOf(vecinos[i], vecinos[i].length);
                    costeActual = costeVecino;
                    if (costeVecino < costeMejor) {
                        solMejor = Arrays.copyOf(vecinos[i], vecinos[i].length);
                        costeMejor = costeVecino;
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
        int[] solucion = new int[NUM_PROCESOS];
        for (int i = 0; i < NUM_PROCESOS; i++) {
            solucion[i] = rand.nextInt(NUM_PROCESADORES);
        }
        return solucion;
    }

    private static int[] generarVecino(int[] solucion, int proceso) {
        Random rand = new Random();
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        vecino[proceso] = rand.nextInt(NUM_PROCESADORES);
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion) {
        int[][] vecinos = new int[NUM_PROCESOS][solucion.length];
        for (int i = 0; i < NUM_PROCESOS; i++) {
            vecinos[i] = generarVecino(solucion, i);
        }
        return vecinos;
    }

    private static double calcularCoste(int[] solucion) {
        int[] tiemposProcesadores = new int[NUM_PROCESADORES];
        for (int i = 0; i < solucion.length; i++) {
            tiemposProcesadores[solucion[i]] += TIEMPOS[i][solucion[i]];
        }
        return Arrays.stream(tiemposProcesadores).max().getAsInt();
    }

    private static boolean probAceptacion(double delta, double temperatura) {
        return (Math.exp(-delta / temperatura) > Math.random());
    }
}