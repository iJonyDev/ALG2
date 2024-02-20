
package EPD3;

public class GilGamboaAdrian {
    static final int N = 10;
    static final int M = 4;

    public static void main(String[] args) {

        // SA
        SA a = new SA();
        int[][] solSA;
        int valor;
        solSA = a.AlgoritmoSA();
        System.out.println("El resultado de SA es:");
        for (int i = 0; i < M; i++) {
            valor = a.valorComponente(solSA, i);
            System.out.println("El componente " + i + " Con el valor " + valor);
        }
        System.out.println("El coste total es de: " + a.calculaCoste(solSA));

        System.out.println("------------------------------------------------------");
        // TS

        TS b = new TS();
        int[][] solTS;
        solTS = b.AlgoritmoTS();
        System.out.println("El resultado de TS es:");
        for (int i = 0; i < M; i++) {
            valor = b.valorComponente(solTS, i);
            System.out.println("El componente " + i + " Con el valor " + valor);
        }
        System.out.println("El coste total es de: " + b.calculaCoste(solTS));
    }

}
