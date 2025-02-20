package EPD1;

import java.util.Arrays;

public class SA_p1_2 {
    // Numero de combinaciones posibles de intercambio de 7 elementos tomados de 2 en 2. 
    public static final int NUM_VECINOS = 21;
    public static final int NUM_MATERIALES = 7;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;
    private static final int[] MATERIALES = {1, 2, 3, 4, 5, 6, 7};
    public static void main(String[] args) {
        int[] solucion = enfriamientoSimulado();
        System.out.println("\n\nMejor orden encontrado es: " + Arrays.toString(solucion) + ",con calida aislante: " + calcularCoste(solucion));
    }

    private static int[] enfriamientoSimulado(){
        int[] solActual = MATERIALES, solMejor = solActual;
        double costeActual = calcularCoste(solActual), costeMejor = costeActual, costeVecino, delta, temperatura = T_INICIAL;
        int exitos = -1;
        while(temperatura >= T_FINAL && exitos != 0){
            exitos = 0;
            System.out.println("\n*******************\nSolucion actual para esta iteracion" + Arrays.toString(solActual) + "\n");
            int[][] vecinos = generarVecinos(solActual);
            for(int i = 0; i < NUM_VECINOS ; i++){
                costeVecino = calcularCoste(vecinos[i]);
                delta = costeVecino - costeActual;
                if(delta > 0 || probAceptacion(delta, temperatura)){
                    exitos++;
                    solActual = Arrays.copyOf(vecinos[i], vecinos[i].length);
                    costeActual = costeVecino;
                    if(costeVecino > costeMejor){
                        solMejor = Arrays.copyOf(vecinos[i], vecinos[i].length);
                        costeMejor = costeVecino;
                    }
                }
            }
            System.out.println("\nsolMejor tras la iteracion: " + Arrays.toString(solMejor) + "\n");
            temperatura *= 0.9;
        }
        if(exitos == 0){
            System.out.println("\nEl proceso termina por no haber mejores vecinos.");
        }else{
            System.out.println("\nEl proceso termina por enfriarse el proceso.");
        }
        return solMejor;
    }

    private static int[] generarVecino(int[] solucion, int i, int j){
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        int temp = vecino[i];
        vecino[i] = vecino[j];
        vecino[j] = temp;
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion){
        int[][] vecinos = new int[NUM_VECINOS][NUM_MATERIALES];
        int index = 0;
        for(int i = 0; i < NUM_MATERIALES; i++){
            for(int j = i + 1; j < NUM_MATERIALES; j++){
                vecinos[index++] = generarVecino(solucion, i, j);
            }
        }
        return vecinos;
    }

    private static double calcularCoste(int[] array){
        return array[0] + array[1] + array[2];
    }

    private static boolean probAceptacion(double delta, double temperatura){
        return (Math.exp(delta/temperatura) > Math.random());
    }
}
