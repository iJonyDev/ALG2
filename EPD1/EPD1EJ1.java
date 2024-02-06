package EPD1;

import java.util.Arrays;

public class EPD1EJ1 {

    public static final int NUM_VECINOS = 5;
    public static final int NUM_BITS = 5;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;

    public static void main(String[] args) {
        int[] solucion = enfriamientoSimulado();
        System.out.println("\n\nEl valor máximo encontrado es x=" + binaryToDecimal(solucion) + ", siendo f(x)=" + calcularCoste(solucion));
    }

    private static int[] enfriamientoSimulado() {
        int[] solActual = {0, 0, 0, 0, 0};  // Inicialización de la solucion actual
        int[] solMejor = {0, 0, 0, 0, 0};   // solMejor igual a la solución actual, inicialmente
        int[][] vecinos = new int[NUM_VECINOS][NUM_BITS]; // Matriz bidimensional para almacenar los vecinos

        double costeActual = calcularCoste(solActual);
        double costeMejor = costeActual;
        double costeVecino;
        double delta;                    // Diferencia de costes entre la solActual y el vecino

        double temperatura = T_INICIAL;        // Inicialización temperatura 
        int exitos = -1;                 // Condición de parada si no mejor solución en una iteración
        int i;

        while (temperatura >= T_FINAL && exitos != 0) { // Mientras no se acaben las iteraciones y haya vecinos mejores
            exitos = 0;
            System.out.println("\n*************************\nLa solucion actual para esta iteracion es: " + binaryToDecimal(solActual) + " \n");
            generarVecinos(solActual, vecinos);     // Generamos los vecinos de la solucion actual

            for (i = 0; i < NUM_VECINOS; i++) {           // Para cada vecino, comprobamos si es mejor o se acepta aun siendo peor
                costeVecino = calcularCoste(vecinos[i]);
                delta = costeVecino - costeActual;

                if (delta > 0) { // Es mejor solucion
                    exitos++;   // Indicamos que ha habido alguna solucion aceptada
                    System.out.print("El vecino " + binaryToDecimal(vecinos[i]) + " es mejor solucion que la actual");
                    // Actualizamos la solucion actual por el vecino mejor
                    solActual = Arrays.copyOf(vecinos[i], vecinos[i].length);
                    costeActual = costeVecino;
                    if (costeVecino > costeMejor) {  // Comprobamos si es, ademas, la mejor de todas las soluciones encontradas en todas las ieraciones
                        solMejor = Arrays.copyOf(vecinos[i], vecinos[i].length);
                        costeMejor = costeVecino;
                        System.out.println(" y además actualizamos la solMejor: " + binaryToDecimal(vecinos[i]));
                    } else {
                        System.out.println(" pero no mejora a solMejor");
                    }
                } else if (probAceptacion(delta, temperatura)) { // No es mejor solucion, pero se acepta
                    exitos++; // Indicamos que ha habido alguna solucion aceptada
                    System.out.println("El vecino " + binaryToDecimal(vecinos[i]) + " es peor solucion que la actual, pero se acepta");
                    // Actualizamos la solucion actual por un vecino peor, pero que ha pasado la probabilidad de aceptacion
                    solActual = Arrays.copyOf(vecinos[i], vecinos[i].length);
                    costeActual = costeVecino;
                } else {
                    System.out.println("El vecino " + binaryToDecimal(vecinos[i]) + " ni es mejor ni pasa la probAceptacion");
                }
            }
            System.out.println("\nsolMejor tras la iteracion: " + binaryToDecimal(solMejor) + "\n");
            temperatura -= 0.1 * temperatura;  // Enfriamiento por descenso geométrico, según enunciado

            if (exitos == 0) {
                System.out.println("\nSe termina el proceso de búsqueda por no haber mejores vecinos");
            } else {
                System.out.println("\nSe termina el proceso de búsqueda por enfriarse el proceso");
            }
        }
        return solMejor;
    }

    private static double calcularCoste(int[] array) {
        // Metodo auxiliar que calcula f(n) = n^3 - 60n^2 + 900n + 100
        int n = binaryToDecimal(array);
        return Math.pow(n, 3) - 60 * Math.pow(n, 2) + 900 * n + 100;
    }

    private static void generarVecinos(int[] solActual, int[][] vecinos) {
        // Metodo auxiliar para generar los vecinos, tal y como pide en el enunciado
        int i;

        for (i = 0; i < NUM_VECINOS; i++) {
            vecinos[i] = Arrays.copyOf(solActual, solActual.length);  // Genero nuevo vecino, igual al de la solActual
            permuta(vecinos[i], i);                                 // Permuto un bit diferente para cada vecino
            System.out.println("\nVecino " + i + " generado: " + binaryToDecimal(vecinos[i]));
        }
        System.out.println("");
    }

    private static void permuta(int[] sVecino, int n) {
        // Metodo para permutar un bit
        if (sVecino[n] == 1) {
            sVecino[n] = 0;
        } else {
            sVecino[n] = 1;
        }
    }

    private static boolean probAceptacion(double delta, double temperatura) {
        // Metodo para calcular la probabilidad de aceptacion
        // Como delta < 0 no hace falta poner el signo menos que hay en la fórmula de la EB
        return (Math.exp((delta) / temperatura) > Math.random());
    }

    private static int binaryToDecimal(int[] array) {
        // Metodo para convertir el vector binario en un entero 
        int n = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                n += Math.pow(2, i);
            }
        }
        return n;
    }

}


/*  VERSION COMPACTA DEL CUERPO DEL CÓDIGO

    for (i = 0; i < NUM_VECINOS; i++) {    // Para cada vecino, comprobamos si es mejor o se acepta aun siendo peor
        costeVecino = coste(vecinos[i]);
        delta = costeVecino - costeActual;

        if (delta > 0 || probAceptacion(delta, temperatura)) { // Es mejor solucion o se acepta
            exitos++;   // Indicamos que ha habido alguna solucion aceptada
            // Actualizamos la solucion actual por el vecino mejor
            solActual = Arrays.copyOf(vecinos[i], vecinos[i].length);
            costeActual = costeVecino;

            if (costeVecino > costeMejor) {  // Comprobamos si es, ademas, la mejor de todas las soluciones encontradas en todas las ieraciones
                // Actualizamos la mejor solucion por dicho vecino
                solMejor = Arrays.copyOf(vecinos[i], vecinos[i].length);
                costeMejor = costeVecino;
            } 
    }
*/