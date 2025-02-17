package EPD1;
/*Queremos hallar el máximo de f(x)=x3-60x2+900x+100 entre x=0 y x=31. 
Para resolver el problema usando SA, se propone seguir la siguiente estrategia. 
En primer lugar, discretizamos el rango de valores de x con vectores binarios de 5 componentes entre 00000 y 11111. 
Estos 32 vectores constituyen S las soluciones factibles del problema.
Le damos un valor inicial a T intuitivamente, por ejemplo, T0 =100 o 500 y en cada iteración del algoritmo lo
reduciremos en 10%, es decir utilizando la estrategia de descenso geométrico: Tk = 0.9 Tk-1. 

Cada iteración consiste en lo siguiente:
1. El número de vecinos queda fijado a 5, siendo éstos variaciones de un bit de la solución. Por ejemplo, si
partimos de 00011, los 5 posibles vecinos resultantes serían: 10011, 01011, 00111, 00001, 00010.
2. Para aplicar el criterio de aceptación, escogemos un vecino, buscamos su coste asociado en la Tabla 1 que
se proporciona y calculamos la diferencia con la solución actual. Si está más cerca del óptimo se acepta, si
no, se aplica Pa (δ, T) = e-δ/T. Siendo T la temperatura actual y δ la diferencia de costes entre la solución
candidata y la actual.
3. Concluya la búsqueda cuando el proceso se enfríe o cuando no se acepte ninguna solución de su vecindad.

Considere T_INICIAL = 100 y T_FINAL = 1.
*/

import java.util.Arrays;

public class SA_ej1_1 {

    public static final int NUM_VECINOS = 5;
    public static final int NUM_BITS = 5;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;

    public static void main(String[] args) {
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

        while (temperatura >= T_FINAL && exitos != 0) {
            exitos = 0;
            System.out.println("\n*************************\nLa solucion actual para esta iteracion es: " + binaryToDecimal(solActual) + " \n");
            vecinos = generarVecinos(solActual);
            for (int i = 0; i < NUM_VECINOS; i++) {
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
        int n = binaryToDecimal(array);
        return Math.pow(n, 3) - 60 * Math.pow(n, 2) + 900 * n + 100;
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