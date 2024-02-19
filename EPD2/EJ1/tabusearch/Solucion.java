package EPD2.EJ1.tabusearch;

public class Solucion implements Cloneable {

    private int[] sol;
    private int movimiento;
    private double coste;

    public Solucion() {
        this.sol = new int[] { 0, 0, 0, 0, 0 };
        this.movimiento = -1;
        this.coste = calcularCoste();
    }

    public Solucion(int[] sol, int movimiento) {
        this.sol = sol;
        this.movimiento = movimiento;
        this.coste = calcularCoste();
    }

    public int[] getSol() {
        return sol;
    }

    public void setSol(int[] sol) {
        this.sol = sol;
        this.coste = calcularCoste(); // Necesario actualizar el coste tras variar sol
    }

    public int getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(int movimiento) {
        this.movimiento = movimiento;

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
        // Calcula f(n) = n^3 - 60n^2 + 900n + 100
        int n = binaryToDecimal(this.sol);
        return Math.pow(n, 3) - 60 * Math.pow(n, 2) + 900 * n + 100;
    }

    private static int binaryToDecimal(int[] sol) {
        // Metodo para convertir el vector binario en un entero
        int n = 0;
        for (int i = 0; i < sol.length; i++) {
            if (sol[i] != 0) {
                n += Math.pow(2, i);
            }
        }
        return n;
    }

    @Override
    public String toString() {
        return "sol=" + binaryToDecimal(sol) + ", movimiento=" + movimiento + ", coste=" + coste;
    }

    @Override
    public Solucion clone() {
        Solucion nueva = new Solucion();
        int i;

        // Necesario hacer deep copy del vector para evitar referencias duplicadas al
        // mismo vector
        for (i = 0; i < this.sol.length; i++) {
            nueva.sol[i] = this.sol[i];
        }

        nueva.movimiento = this.movimiento;
        nueva.coste = this.coste;

        return nueva;
    }
}
