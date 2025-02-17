package EPD2.TS_EJ1;

import java.util.ArrayList;

public class EPD2EJ1 {

    public static final int NUM_VECINOS = 5;    // El enunciado pide 5 vecinos
    public static final int NUM_BITS = 5;       // La solución se codifica con 5 bits
    public static final int TENENCIA = 2;       // La tenencia no puede ser mayor que NUM_BITS
    public static final int ITERACIONES = 5;    // Iteraciones para la condicion de parada

//    public static int cont = 0;

    public static void main(String[] args) {
        Solucion sol = tabuSearch();
        System.out.println("\n\nEl valor máximo encontrado es " + sol + ", por tanto f(sol)=" + sol.getCoste());
    }

    public static Solucion tabuSearch() {
        //Solucion solActual = new Solucion(); // Puesto así para mostrar que hay dos constructores
        Solucion solActual = new Solucion(new int[]{0, 0, 0, 0, 0}, -1);
        Solucion solMejor = solActual.clone(); 
        Solucion vecinoMejor;
        Solucion[] vecinos = new Solucion[NUM_VECINOS]; // Array de Solucion para almacenar los vecinos
        ArrayList<Solucion> listaTabu = new ArrayList<>();
        int condParada = ITERACIONES;

        while (condParada > 0) {  
            System.out.println("\n\n************** Iteracion " + (ITERACIONES-condParada+1));
            //Genera un array con todos los vecinos posibles.
            generarVecinos(solActual, vecinos);
            //Selecciona el mejor de todos los vecinos y lo evalúa.
            vecinoMejor = seleccionarMejorVecino(vecinos, solMejor, listaTabu);
            // Comprobamos si el mejor vecino es la mejor solución global
            if (vecinoMejor.getCoste() > solMejor.getCoste()) {
                solMejor = vecinoMejor.clone();     // Actualizamos la mejor solución en caso afirmativo
                System.out.println("\nSolucion mejor global actualizada en condParada=" + condParada);
            }
            // Actualizamos la solución actual con la del mejor vecino, para comenzar ahí en la siguiente iteración
            solActual = vecinoMejor.clone();
            // Actualizamos la lista tabu con el mejor vecino seleccionado
            actualizarListaTabu(listaTabu, vecinoMejor);
            // Actualizamos la condicion de parada
            condParada--;
        }
        return solMejor;
    }

    private static void actualizarListaTabu(ArrayList<Solucion> listaTabu, Solucion elemTabu) {
        listaTabu.add(elemTabu);
        if (listaTabu.size() > TENENCIA) {
            listaTabu.remove(0);
        }
    }

    private static void generarVecinos(Solucion solActual, Solucion[] vecinos) {
        // Metodo para generar los vecinos de la manera que se pide en el enunciado
        int i;
        for (i = 0; i < NUM_VECINOS; i++) {
            vecinos[i] = solActual.clone();// Genero nuevo vecino, igual al de la solActual
            vecinos[i].setMovimiento(i);
            permuta(vecinos[i], i); // Permuto un bit diferente para cada vecino
            System.out.println("\nVecino " + i + " generado: " + vecinos[i]);
        }
        System.out.println("");
    }

    private static void permuta(Solucion vecino, int i) {
        // Metodo para permutar un bit de la solución actual y convertirlo en un vecino nuevo
        if (vecino.getSol()[i] == 1) {
            vecino.setBitSol(i, 0);
        } else {
            vecino.setBitSol(i, 1);
        }
    }

    public static boolean isTabu(ArrayList<Solucion> listaTabu, int movimiento) {
        boolean tabu = false;
        int i;
        for (i = 0; i < listaTabu.size(); i++) {
            if (listaTabu.get(i).getMovimiento() == movimiento) {
                tabu = true;
            }
        }
        return tabu;
    }

    private static boolean criterioAspiracion(Solucion solMejor, Solucion vecino, Solucion vecinoMejor) {
        // Si está en tabú pero es la mejor de todas, se acepta
        boolean aceptar = false;
        if (vecino.getCoste() > solMejor.getCoste() && vecino.getCoste() > vecinoMejor.getCoste()) { // Controlamos que no se haya encontrado el mejor entre los vecinos ya evaluados
            aceptar = true;
        }
        return aceptar;
    }

    private static Solucion seleccionarMejorVecino(Solucion[] vecinos, Solucion solMejor, ArrayList<Solucion> listaTabu) {
        // Puesto muy verboso para entender qué pasa con cada vecino
        Solucion vecinoMejor = vecinos[0].clone();
        
        int i;

        for (i = 1; i < NUM_VECINOS; i++) {
            System.out.println("\nProcesamos vecino " + i);
            if (!isTabu(listaTabu, vecinos[i].getMovimiento())) {
                if (vecinos[i].getCoste() > vecinoMejor.getCoste()) {
                    vecinoMejor = vecinos[i].clone();
                    System.out.println("Es el mejor vecino por el momento: " + vecinoMejor.toString());
                } else {
                    System.out.println("No está en la tabú pero no actualiza vecino");
                }
            } else if (criterioAspiracion(solMejor, vecinos[i], vecinoMejor) == true) {
                vecinoMejor = vecinos[i].clone();
                System.out.println("Es un movimiento tabú, pero cumple el criterio de aspiración");
            } else {
                System.out.println("El vecino es tabú y no cumple criterio de aspiración");
            }
        }
        System.out.println("\nEl vecino mejor válido seleccionado es: " + vecinoMejor.getCoste());
        return vecinoMejor;
    }

}
