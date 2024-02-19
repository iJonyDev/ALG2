package EPD2.EJ2;

public class Solucion implements Cloneable {

    private int[] sol;
    private final int[] beneficios;
    private final int[] pesos;
    private int movimiento;
    private double coste;
    private double peso;

    public Solucion(int[] beneficios, int[] pesos) {
        this.sol = new int[EPD2EJ2.N];
        this.beneficios = new int[EPD2EJ2.N];
        this.pesos = new int[EPD2EJ2.N];

        for (int i = 0; i < beneficios.length; i++) {
            this.sol[i] = 0;
            this.beneficios[i] = beneficios[i];
            this.pesos[i] = pesos[i];
        }
        this.movimiento = -1;
        this.coste = calcularCoste();
        this.peso = calcularPeso();
    }

    public Solucion() {
        this.sol = new int[EPD2EJ2.N];
        this.beneficios = new int[EPD2EJ2.N];
        this.pesos = new int[EPD2EJ2.N];
        this.movimiento = -1;
        this.coste = -1;
        this.peso = -1;
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

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setBitSol(int index, int value) {
        this.sol[index] = value;
        this.coste = calcularCoste(); // Necesario actualizar el coste tras variar sol
        this.peso = calcularPeso();
    }

    private double calcularCoste() {
        double aux = 0.0;
        for (int i = 0; i < beneficios.length; i++) {
            aux += sol[i] * beneficios[i];
        }

        return aux;
    }

    private double calcularPeso() {
        double aux = 0.0;
        for (int i = 0; i < pesos.length; i++) {
            aux += sol[i] * pesos[i];
        }

        return aux;
    }

    @Override
    public String toString() {
        return "peso=" + peso + ", movimiento=" + movimiento + ", beneficio=" + coste;
    }

    @Override
    public Solucion clone() {
        Solucion nueva = new Solucion();
        int i;

        // Necesario hacer deep copy del vector para evitar referencias duplicadas al
        // mismo vector
        for (i = 0; i < this.sol.length; i++) {
            nueva.sol[i] = this.sol[i];
            nueva.beneficios[i] = this.beneficios[i];
            nueva.pesos[i] = this.pesos[i];
        }

        nueva.movimiento = this.movimiento;
        nueva.coste = this.coste;
        nueva.peso = this.peso;

        return nueva;
    }
}
