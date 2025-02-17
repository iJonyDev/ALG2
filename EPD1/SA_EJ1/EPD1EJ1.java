/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPD1.SA_EJ1;

public class EPD1EJ1 {

    public static final int NUM_VECINOS = 5;
    public static final int NUM_BITS = 5;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;
    
    public static void main(String[] args) {
        Solucion solucion = enfriamientoSimulado();
        System.out.println("\n\nEl valor máximo encontrado es x=" + solucion + ", siendo f(x)=" + solucion.getCoste());
    }
    
    private static Solucion enfriamientoSimulado() {
        
        Solucion solActual = new Solucion();  // Inicialización de la solucion actual por defecto
        // Solucion solActual = new Solucion(new int[]{1,1,1,1,1});    // Inicialización de la solucion actual dando el valor inicial
        Solucion solMejor = solActual.clone(); // solMejor igual a la solución actual, inicialmente
        
        Solucion[] vecinos = new Solucion[NUM_VECINOS]; // Matriz bidimensional para almacenar los vecinos

        double costeActual = solActual.getCoste();
        double costeMejor = costeActual; // costeMejor igual al actual, inicialmente 
        double costeVecino;
        double delta;                    // Diferencia de costes entre la solActual y el vecino

        double temperatura = T_INICIAL;        // Inicialización temperatura 
        int exitos = -1;                 // Condición de parada si no mejor solución en una iteración
        int i;

        while (temperatura >= T_FINAL && exitos != 0) { // Mientras no se acaben las iteraciones y haya vecinos mejores
            exitos = 0;
            System.out.println("\n*************************\nLa solucion actual para esta iteracion es: " + solActual.toString() + " \n");
            generarVecinos(solActual, vecinos);     // Generamos los vecinos de la solucion actual

            for (i = 0; i < NUM_VECINOS; i++) {    // Para cada vecino, comprobamos si es mejor o se acepta aun siendo peor
                costeVecino = (vecinos[i]).getCoste();
                delta = costeVecino - costeActual;

                if (delta > 0) {  // delta > 0 implica que el coste del vecino es mayor (queremos maximizar el coste)
                                  // o sea, este vecino es el mejor de la solucion actual
                    exitos++;   // Indicamos que ha habido alguna solucion aceptada
                    System.out.print("El vecino " + (vecinos[i]).toString() + " es mejor solucion que la actual");
                    
                    solActual = (vecinos[i]).clone(); // Actualizamos la solucion actual por el vecino mejor
                    costeActual = costeVecino; // Actualizamos el coste
                    
                    if (costeVecino > costeMejor) {  // Comprobamos si es, ademas, la mejor de todas las soluciones encontradas en todas las ieraciones
                        solMejor = (vecinos[i]).clone(); //Si lo es, actualizamos la solMejor
                        costeMejor = costeVecino;
                        System.out.println(" y además actualizamos la solMejor: " + (vecinos[i]).toString());
                    } else {
                        System.out.println(" pero no mejora a solMejor");
                    }
                    
                } else if (probAceptacion(delta, temperatura)) { // No es mejor solucion, pero se acepta
                    
                    exitos++; // Indicamos que ha habido alguna solucion aceptada
                    System.out.println("El vecino " + (vecinos[i]).toString() + " es peor solucion que la actual, pero se acepta");
                    
                    solActual = (vecinos[i]).clone(); // Actualizamos la solucion actual por un vecino peor, pero que ha pasado la probabilidad de aceptacion
                    costeActual = costeVecino;
                   
                } else {
                    System.out.println("El vecino " + (vecinos[i]).toString() + " ni es mejor ni pasa la probAceptacion");
                }
            }
            System.out.println("\nsolMejor tras la iteracion: " + solMejor.toString() + "\n");
            temperatura -= 0.1 * temperatura;  // Enfriamiento por descenso geométrico, según enunciado
        }
        
        if (exitos == 0) {
                System.out.println("\nSe termina el proceso de búsqueda por no haber mejores vecinos");
            } else {
                System.out.println("\nSe termina el proceso de búsqueda por enfriarse el proceso");
            }
        return solMejor;
    }


    private static void generarVecinos(Solucion solActual, Solucion[] vecinos) {
        // Metodo auxiliar para generar los vecinos, tal y como pide en el enunciado
        int i;
        for (i = 0; i < NUM_VECINOS; i++) {
            vecinos[i] = solActual.clone();  // Genero nuevo vecino, igual al de la solActual
            permuta(vecinos[i], i);         // Permuto un bit diferente para cada vecino
            System.out.println("\nVecino " + i + " generado: " + (vecinos[i]).toString());
        }
        System.out.println("");
    }

    private static void permuta(Solucion sVecino, int n) {
        // Metodo para permutar un bit
        if (sVecino.getSol()[n] == 1) {
            sVecino.setBitSol(n, 0);
        } else {
            sVecino.setBitSol(n, 1);
        }
    }

    private static boolean probAceptacion(double delta, double temperatura) {
        // Metodo para calcular la probabilidad de aceptacion 
        // Como delta < 0 no hace falta poner el signo menos que hay en la fórmula de la EB
        return (Math.exp((delta) / temperatura) > Math.random());
    }


    
}
