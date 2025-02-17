package EPD2.TS_EJ2;

import java.util.ArrayList;

public class EPD2EJ2 {

    final static int N = 100; // tamaño de la solución
    final static int NUM_VECINOS = 10; // número de vecinos
    final static int PESOMAX = 180; // máximo peso permitido
    final static int TENENCIA = 3; // tenencia tabú
    final static int ITERACIONES = 10; // número de iteraciones

    public static void main(String[] args) {
        Solucion sol = tabuSearch();
        System.out.println("\n\nEl valor máximo encontrado es " + sol);
    }

    public static Solucion tabuSearch() {

        int beneficios[] = new int[N];  // vector de beneficios
        int pesos[] = new int[N];  // vector de pesos
        // Inicializo de manera aleatoria vectores de peso y beneficio
        for (int i = 0; i < N; i++) {
            beneficios[i] = (int) (Math.random() * 10) + 1;
            pesos[i] = (int) (Math.random() * 100) + 1;
        }

        Solucion solActual = new Solucion(beneficios, pesos);
        Solucion solMejor = solActual.clone();
        Solucion vecinoMejor;
        Solucion[] vecinos = new Solucion[NUM_VECINOS]; // Matriz bidimensional para almacenar los vecinos
        ArrayList<Solucion> listaTabu = new ArrayList<>();

        int iteraciones = ITERACIONES;
        boolean existeMejor = true;
        while (iteraciones > 0 && existeMejor) {
            System.out.println("\n\n************** Iteracion " + (ITERACIONES - iteraciones + 1));
            //Genera un array con todos los vecinos posibles.
            generarVecinos(solActual, vecinos);
            //Selecciona el mejor de todos los vecinos y lo evalúa.
            vecinoMejor = seleccionarMejorVecino(vecinos, solMejor, listaTabu, beneficios, pesos);
            //Si el vecinoMejor tiene un movimiento de -1 es porque no se encontro ninguno válido en el método anterior: parada
            if (vecinoMejor.getMovimiento() == -1) {
                existeMejor = false;
            }
            // Comprobamos si el mejor vecino es la mejor solución global
            if (vecinoMejor.getCoste() > solMejor.getCoste()) {
                solMejor = vecinoMejor.clone();     // Actualizamos la mejor solución en caso afirmativo
                System.out.println("\nSolucion mejor global actualizada en condParada=" + iteraciones);
            }
            // Actualizamos la solución actual con la del mejor vecino, para comenzar ahí en la siguiente iteración
            solActual = vecinoMejor.clone();
            // Actualizamos la lista tabu con el mejor vecino seleccionado
            actualizarListaTabu(listaTabu, vecinoMejor);
            // Actualizamos la condicion de parada
            iteraciones--;
        }
        if (existeMejor == false) {
            System.out.println("Fin de la búsqueda por no haber vecinos válidos");
        } else {
            System.out.println("Fin de la búsqueda por acabar las iteraciones");
        }

        return solMejor;
    }

    private static void generarVecinos(Solucion solActual, Solucion[] vecinos) {
        // Metodo para generar los vecinos: selecciono un bit al azar y lo permuto
        int i;
        int bitObjetivo;
        for (i = 0; i < NUM_VECINOS; i++) {
            vecinos[i] = solActual.clone();     // Genero nuevo vecino, igual al de la solActual
            bitObjetivo = (int) (Math.random() * N);
            vecinos[i].setMovimiento(bitObjetivo);        // Almaceno el movimiento realizado, para pasarlo luego a la lista tabú
            
            permuta(vecinos[i], bitObjetivo);   // Permuto un bit diferente para cada vecino
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

    private static Solucion seleccionarMejorVecino(Solucion[] vecinos, Solucion solMejor, ArrayList<Solucion> listaTabu, int[] beneficios, int[] pesos) {
        // Puesto muy verboso para entender qué pasa con cada vecino
        Solucion vecinoMejor = new Solucion(beneficios, pesos); 
        int i;
        for (i = 0; i < NUM_VECINOS; i++) {
            System.out.println("\nProcesamos vecino " + i);
            if (vecinos[i].getPeso() < PESOMAX) { // Se añade una restricción de peso máximo
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
            } else {
                System.out.println("Vecino no válido por excederse del peso");
            }
        }
        System.out.println("\nEl vecino mejor válido seleccionado es: " + vecinoMejor.getCoste());

        return vecinoMejor;
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
        if (vecino.getCoste() > solMejor.getCoste() && vecino.getCoste() > vecinoMejor.getCoste() && vecino.getCoste() <= PESOMAX) { // Controlamos que no se haya encontrado el mejor entre los vecinos ya evaluados y que no se exceda del peso permitido
            aceptar = true;
        }
        return aceptar;
    }

    private static void actualizarListaTabu(ArrayList<Solucion> listaTabu, Solucion elemTabu) {
        listaTabu.add(elemTabu);
        if (listaTabu.size() > TENENCIA) {
            listaTabu.remove(0);
        }
    }
}
