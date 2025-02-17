/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPD1.SA_Ej2;

public class Solucion implements Cloneable{
    
    private int[] sol; // Codificacion de la mochila
    private final int[] pesos; // Pesos de cada elemento en la mochila
    private double coste; // Peso total de la mochila

    public Solucion(int[] pesos) {
        this.sol = new int[EPD1EJ2ClaseSolucion.N];
      
        this.pesos = pesos;
        this.coste = calcularCoste();
    }

    public Solucion() { //Construccion de un elemento vacio
        this.sol = new int[EPD1EJ2ClaseSolucion.N];
        
        this.pesos = new int[EPD1EJ2ClaseSolucion.N];
        this.coste = -1;
    }
    
    public int[] getSol() {
        return sol;
    }

    public void setSol(int[] sol) {
        this.sol = sol;
        this.coste = calcularCoste();   // Necesario actualizar el coste tras variar sol
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }


    public void setBitSol(int index, int value) {
        this.sol[index] = value;
        this.coste = calcularCoste(); // Necesario actualizar el coste tras variar sol
    }

    private double calcularCoste() {
        double aux = 0.0;
        for (int i = 0; i < this.pesos.length; i++) {
            aux += sol[i] * this.pesos[i];
        }

        return aux;
    }

    public String toString() {
        // Imprimir el vector por pantalla
        String r = "[";
        int i;
        for (i = 0; i < this.sol.length; i++) {
            // Imprimir cada elemento
            r = r + this.sol[i];

            // Agregar una coma y un espacio si no es el último elemento
            if (i < this.sol.length - 1) {
                r = r + ", ";
            }
        }
        r = r + "]"; // Cerrar los corchetes
        r = r + " peso=" + this.coste; // Añadir el peso
        
        return r;
    }

    public Solucion clone() {
        Solucion nueva = new Solucion();
        int i;

        // Necesario hacer deep copy del vector para evitar referencias duplicadas al mismo vector
        for (i = 0; i < this.sol.length; i++) {
            nueva.sol[i] = this.sol[i];
            nueva.pesos[i] = this.pesos[i];
        }

        nueva.coste = this.coste;

        return nueva;
    }
    
}