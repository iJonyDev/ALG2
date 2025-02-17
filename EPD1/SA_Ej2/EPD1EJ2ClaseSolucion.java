/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPD1.SA_Ej2;

public class EPD1EJ2ClaseSolucion {

    public static final int N = 5; //Numero de elementos en el vector, son 5 para hacer una prueba
    public static final int NUM_VECINOS = 5;
    public static final int MAX_PESO = 180;
    public static final double T_INICIAL = 200;
    public static final double T_FINAL = 5;

    public static final int[] PESOS = {70, 20, 10, 80, 90}; // Hay al menos tres soluciones posibles

    public static void main(String[] args) {
        Solucion solucion = enfriamientoSimulado();
        System.out.println("Vector solución: " + solucion.toString());
    }

    private static Solucion enfriamientoSimulado() {

        Solucion solActual = new Solucion(PESOS);
        //Solucion solActual = new Solucion();
        Solucion solMejor = solActual.clone();
       
        Solucion[] vecinos = new Solucion[NUM_VECINOS]; // Matriz bidimensional para almacenar los vecinos (cada vecino es una Solucion)

        double costeActual = solActual.getCoste();
        double costeMejor = costeActual;
        double costeVecino; 
        double delta; // Diferencia de costes entre la solActual y el vecino

        double temperatura = T_INICIAL;
        int i;
        int exitos = -1;

        System.out.println("El peso máximo permitido es: " + MAX_PESO);

        while (temperatura > T_FINAL && exitos != 0) {     // Paramos cuando se enfría el proceso o cuando no hay vecinos mejores
            exitos = 0;

            System.out.println("\n*************************\nLa solucion actual para esta iteracion es: " + solActual.toString() + " \n");
            generarVecinos(solActual, vecinos); // Generamos los vecinos de la solucion actual

            for (i = 0; i < NUM_VECINOS; i++) {
                costeVecino = vecinos[i].getCoste(); // Calculamos el peso
                delta = costeVecino - costeActual;
                if (costeVecino <= MAX_PESO) { // Comprobamos que el peso no sea mayor que MAX_PESO
                    if (delta > 0){ // delta > 0 implica que el coste del vecino es mayor (queremos maximizar el coste)
                                    // o sea, significa que este vecino es el mejor de la solucion actual
                        
                        exitos++; // Indicamos que ha habido alguna solucion aceptada  
                        System.out.print("El vecino " + (vecinos[i]).toString() + " es mejor solucion que la actual");
                        
                        solActual = vecinos[i].clone();  // Actualizamos la solucion actual por el vecino mejor
                        costeActual = costeVecino; // Actualizamos el coste
                        
                        if (costeVecino > costeMejor) {  // Comprobamos si, además, es la mejor solución de todas las evaluadas
                            solMejor = vecinos[i].clone(); // Si lo es, actualizamos la sol mejor
                            costeMejor = costeVecino;
                            System.out.println(" y además actualizamos la solMejor: " + (vecinos[i]).toString());
                    }
                        else {
                            System.out.println(" pero no mejora a solMejor"); // Si no, no actualizamos la sol mejor
                        }
                    }
                    
                    else if (probAceptacion(delta, temperatura)) {  // No es mejor, pero se acepta
                        exitos++; // Indicamos que ha habido alguna solucion aceptada  
                        System.out.println("El vecino " + (vecinos[i]).toString() + " es peor solucion que la actual, pero se acepta");
                    
                        solActual = vecinos[i].clone();   // Actualizamos la solucion actual por un vecino peor, pero que ha pasado la probabilidad de aceptacion
                        costeActual = costeVecino;       // Actualizamos el coste
               
                    }  
                } else { // Sobrepasa el peso maximo
                   System.out.println("El vecino sobrepasa el máximo peso permitido, pesa " + costeVecino);
                }
            }
            temperatura = temperatura * 0.90; // Enfriamos
    
        }

        if (exitos == 0) {
            System.out.println("\nSe termina el proceso de búsqueda por no haber mejores vecinos");
        } else {
            System.out.println("\nSe termina el proceso de búsqueda por enfriarse el proceso");
        }
        return solMejor;
    }


    private static void generarVecinos(Solucion solActual, Solucion[] vecinos) {
        // Metodo auxiliar para generar los vecinos. 
        // Genera NUM_VECINOS como resultado de permutar aleatoriamente uno de los bits de la solucion
        int i;
        int bitObjetivo;

        for (i = 0; i < NUM_VECINOS; i++) {
            vecinos[i] = solActual.clone();    // Genero nuevo vecino, igual al de la solActual
            bitObjetivo = (int) (Math.random() * N);   // Selecciono un bit aleatorio para permutar para cada vecino       
            permuta(vecinos[i], bitObjetivo); // Permuta
            System.out.println("\n Vecino "+ i + " generado: " + vecinos[i]);
            
        }
    }
    
       private static void permuta(Solucion vecino, int bitObjetivo) {
        // Metodo para permutar un bit de la solución actual y convertirlo en un vecino nuevo
        if (vecino.getSol()[bitObjetivo] == 1) {
            vecino.setBitSol(bitObjetivo, 0);
        } else {
            vecino.setBitSol(bitObjetivo, 1);
        }
    }

     // La misma que en el ejercicio anterior
    private static boolean probAceptacion(double delta, double temperatura) {
        // Metodo para calcular la probabilidad de aceptacion
        // Como delta < 0 no hace falta poner el signo menos que hay en la fórmula de la EB
        return (Math.exp((delta) / temperatura) > Math.random());
    }
    
}
