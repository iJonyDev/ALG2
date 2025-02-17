package EPD1;
/*
Modifique el ejercicio anterior para que, además de considerar el peso máximo de la
mochila, considere un beneficio asociado a cada elemento insertado. Esto es, defina un vector de beneficios
adicional, B = {b1, b2, ..., b100}, con bi = [1,100], tal que cada elemento pi tenga asociado un beneficio bi. El
objetivo ahora será maximizar el beneficio de los elementos insertados en la mochila, sin haber exceder el peso
máximo permitido. 
*/
import java.util.Arrays;
import java.util.Random;

public class SA_ej3_1 {

    public static final int NUM_VECINOS = 5;
    public static final int NUM_BITS = 5;
    public static final int T_FINAL = 5;
    public static final int T_INICIAL = 200;
    public static final int MAX_PESO = 180;
    public static final int[] PESOS = new int[NUM_BITS];
    public static final int[] BENEFICIOS = new int[NUM_BITS];

    public static void main(String[] args) {
        Random rand = new Random();
        for (int i = 0; i < NUM_BITS; i++) {
            PESOS[i] = rand.nextInt(100) + 1;
            BENEFICIOS[i] = rand.nextInt(100) + 1;
        }
        System.out.println("Pesos de los elementos: " + Arrays.toString(PESOS));
        System.out.println("Beneficios de los elementos: " + Arrays.toString(BENEFICIOS));
        int[] solucion = enfriamientoSimulado();
        System.out.println("\n\nEl valor máximo encontrado es x=" + binaryToDecimal(solucion) + ", siendo f(x)=" + calcularCoste(solucion));
    }

    private static int[] enfriamientoSimulado() {
        int[] solActual = {0,0,0,0,0};
        int[] solMejor = {0,0,0,0,0};
        int[][] vecinos;

        double costeActual = calcularCoste(solActual);
        double costeMejor = costeActual;
        double costeVecino;
        double delta;

        double temperatura = T_INICIAL;
        int exitos = -1;
        int i;

        while (temperatura >= T_FINAL && exitos != 0) {
            exitos = 0;
            System.out.println("\n*************************\nLa solucion actual para esta iteracion es: " + binaryToDecimal(solActual) + " \n");
            vecinos = generarVecinos(solActual);
            for (i = 0; i < NUM_VECINOS; i++) {
                costeVecino = calcularCoste(vecinos[i]);
                delta = costeVecino - costeActual;

                if (delta > 0 || probAceptacion(delta, temperatura)) {
                    exitos++;
                    solActual = Arrays.copyOf(vecinos[i], vecinos[i].length);
                    costeActual = costeVecino;

                    if (costeVecino > costeMejor) {
                        solMejor = Arrays.copyOf(vecinos[i], vecinos[i].length);
                        costeMejor = costeVecino;
                    }
                }
            }
            System.out.println("\nsolMejor tras la iteracion: " + binaryToDecimal(solMejor) + "\n");
            temperatura *= 0.9;
        }
        if (exitos == 0) {
            System.out.println("\nSe termina el proceso de búsqueda por no haber mejores vecinos");
        } else {
            System.out.println("\nSe termina el proceso de búsqueda por enfriarse el proceso");
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
        int pesoTotal = 0;
        int beneficioTotal = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 1) {
                pesoTotal += PESOS[i];
                beneficioTotal += BENEFICIOS[i];
            }
        }
        if (pesoTotal > MAX_PESO) {
            return 0; // Penalización por exceder el peso máximo
        }
        return beneficioTotal;
    }

    private static boolean probAceptacion(double delta, double temperatura) {
        return (Math.exp(delta / temperatura) > Math.random());
    }

    private static int binaryToDecimal(int[] array) {
        int n = 0;
        for (int i = 0; i < array.length; i++) {
            n += array[i] << i;
        }
        return n;
    }
}
