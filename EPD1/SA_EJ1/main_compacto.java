/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPD1.SA_EJ1;

public class main_compacto {
    
    public static final int NUM_VECINOS = 5;
    public static final int NUM_BITS = 5;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;
    
    public static void main(String[] args) {
        Solucion solucion = enfriamientoSimulado();
        System.out.println("\n\nEl valor máximo encontrado es x=" + solucion + ", siendo f(x)=" + solucion.getCoste());
    }
    
    private static Solucion enfriamientoSimulado() {
        
        Solucion solActual = new Solucion();   
        Solucion solMejor = solActual.clone(); 
        
        Solucion[] vecinos = new Solucion[NUM_VECINOS]; // Matriz bidimensional para almacenar los vecinos

        double costeActual = solActual.getCoste();
        double costeMejor = costeActual; 
        double costeVecino;
        double delta; // Diferencia de costes entre la solActual y el vecino

        double temperatura = T_INICIAL;    
        int exitos = -1;  // Condición de parada si no mejor solución en una iteración
        int i;

        while (temperatura >= T_FINAL && exitos != 0) { // Mientras no se acaben las iteraciones y haya vecinos mejores
            exitos = 0;
            generarVecinos(solActual, vecinos); // Generamos los vecinos de la solucion actual

            for (i = 0; i < NUM_VECINOS; i++) {  // Para cada vecino, comprobamos si es mejor o se acepta aun siendo peor
                costeVecino = (vecinos[i]).getCoste();
                delta = costeVecino - costeActual;

                if (delta > 0 || probAceptacion(delta, temperatura)) {  // Si es mejor O cumple la probAceptacion
                    exitos++; 
                    solActual = (vecinos[i]).clone(); 
                    costeActual = costeVecino; 
                    
                    if (costeVecino > costeMejor) { // Comprobamos si ademas es la mejor de las soluciones
                        solMejor = (vecinos[i]).clone(); 
                        costeMejor = costeVecino;
                    } 
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
