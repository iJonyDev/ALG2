/*
Un modelo de coche se configura a partir de N componentes distintos. Cada uno de
esos componentes puede tomar M posibles valores, siendo vij el valor que toma el componente i (i=1, ..., N)
para la posibilidad j (j=1, ..., M) La afinidad de los consumidores para cada posible valor vij es aij y el coste cij.
Se entiende que el coste es el valor por la afinidad, esto es, cij = vij * aij. Se desea encontrar una combinación
de componentes que alcance la máxima afinidad global con los gustos de los consumidores y cuyo coste no
supere un umbral MAX_COSTE.
Para poder comprobar que la metaheurística diseñada funciona, suponga que N = M = 4. Los valores aij y vij
estarán comprendidos entre [1, 10] y, por tanto, cij entre [1, 100]. Por último, MAX_COSTE < 120. 
*/
package EPD1;
import java.util.Arrays;
import java.util.Random;

public class SA_p3_1 {
    public static final int NUM_COMPONENTES = 4;
    public static final int NUM_VALORES = 4;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;
    public static final int MAX_COSTE = 120;

    private static final int[][] valores = new int[NUM_COMPONENTES][NUM_VALORES];
    private static final int[][] afinidades = new int[NUM_COMPONENTES][NUM_VALORES];

    static {
        Random rand = new Random();
        for (int i = 0; i < NUM_COMPONENTES; i++) {
            for (int j = 0; j < NUM_VALORES; j++) {
                valores[i][j] = rand.nextInt(10) + 1;
                afinidades[i][j] = rand.nextInt(10) + 1;
            }
        }
    }

    public static void main(String[] args) {
        int[][] costes = calcularCostes(valores, afinidades);
        int[] solucion = enfriamientoSimulado(afinidades, costes);
        System.out.println("\nLa mejor combinacion encontrada es: " + Arrays.toString(solucion)
                        + ", con afinidad total: " + calcularTotal(solucion, afinidades) 
                        + " y coste total: " + calcularTotal(solucion, costes));
    }

    private static int[] enfriamientoSimulado(int[][] afinidades, int[][] costes){
        int[] solActual = generarSolucionInicial();
        while (calcularTotal(solActual, costes) > MAX_COSTE) solActual = generarSolucionInicial();
        int[] solMejor = solActual;
        double afinidadActual = calcularTotal(solActual, afinidades), afinidadMejor = afinidadActual,temperatura = T_INICIAL,delta;
        int exitos = -1;

        while (temperatura >= T_FINAL && exitos != 0 ) {
            exitos = 0;
            System.out.println("\n************************\nSolucion actual para esta iteracion es: " + Arrays.toString(solActual));
            int[][] vecinos = generarVecinos(solActual);
            for (int i = 0; i < vecinos.length; i++) {
                double costeVecino = calcularTotal(vecinos[i], costes);
                double afinidadVecino = calcularTotal(vecinos[i], afinidades);
                delta = afinidadVecino - afinidadActual;
                if (costeVecino <= MAX_COSTE && (delta > 0 || probAceptacion(delta, temperatura))) {
                    exitos++;
                    solActual = Arrays.copyOf(vecinos[i], vecinos[i].length);
                    afinidadActual = afinidadVecino;
                    if (afinidadActual > afinidadMejor) {
                        solMejor = Arrays.copyOf(vecinos[i], vecinos[i].length);
                        afinidadMejor = afinidadVecino;
                    }
                }
            }
            System.out.println("\nSolucion mejor para esta iteracion: " + Arrays.toString(solMejor));
            temperatura *= 0.9;
        }
        if(exitos == 0){
            System.out.println("\nTermina el proceso por no haber mejoras vecinos.");
        }else{
            System.out.println("\nTermina el proceso por enfriarse el proceso.");
        }
        return solMejor;
    }

    private static int[] generarSolucionInicial(){
        Random rand = new Random();
        int[] solucion = new int[NUM_COMPONENTES];
            for(int i = 0; i < NUM_COMPONENTES; i++){
                solucion[i] = rand.nextInt(NUM_VALORES);
            }
        return solucion;
    }

    private static int[] generarVecino(int[] solucion, int componente){
        Random rand = new Random();
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        vecino[componente] = rand.nextInt(NUM_VALORES);
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion){
        int[][] vecinos = new int[NUM_COMPONENTES][solucion.length];
        for(int i=0; i< NUM_COMPONENTES; i++){
            vecinos[i] = generarVecino(solucion, i);
        }
        return vecinos;
    }

    private static double calcularTotal(int[] solucion, int[][] matriz) {
        double total = 0;
        for (int i = 0; i < solucion.length; i++) {
            total += matriz[i][solucion[i]];
        }
        return total;
    }

    private static int[][] calcularCostes(int[][] valores, int[][] afinidades){
        int[][] costes = new int[valores.length][valores[0].length];
        for(int i=0; i < valores.length; i++){
            for(int j=0; j < valores[i].length; j++){
                costes[i][j] = valores[i][j]*afinidades[i][j];
            }
        }
        return costes;
    }

    private static boolean probAceptacion(double delta, double temperatura){
        return (Math.exp(delta/temperatura) > Math.random());
    }
}