package EPD1;

import java.util.Arrays;

public class SA_ej1_2{
    public static final int NUM_VECINOS = 5;
    public static final int NUM_BITS = 5;
    public static final int T_FINAL = 1;
    public static final int T_INICIAL = 100;

    public static void main(String[] args){
        int[] solucion = enfriamientoSimulado();
        System.out.println("\n\nEL valor maximo encontrado es x=" + binaryToDecimal(solucion) + ",siendo f(x)=" + calcularCoste(solucion) );
    }

    private static int[] enfriamientoSimulado(){
        int[] solActual = new int[NUM_BITS];
        int[] solMejor = new int[NUM_BITS];
        int[][] vecinos;

        double costeActual = calcularCoste(solActual);
        double costeMejor = costeActual;
        double costeVecino;
        double delta;

        double temperatura = T_INICIAL;
        int exitos = -1;

        while (temperatura > T_FINAL && exitos != 0){
            exitos = 0;
            System.out.println("\n***********************\nSolucion actual para esta iteracion es: " + binaryToDecimal(solActual) + " \n");
            vecinos = generarVecinos(solActual);
            for(int i = 0; i < NUM_VECINOS; i ++){
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
            System.out.println("\nsoMejor tras la iteracion: " + binaryToDecimal(solMejor) + "\n");
            temperatura *= 0.9;
        }
        if (exitos == 0){
            System.out.println("\nSe termina el proceso de busqueda por no haber mejores vecinos");
        }else{
            System.out.println("\nSe termina el proceso de busqueda por enfriarse el proceso ");
        }
        return solMejor;
    }

    private static int[] generarVecino(int[] solucion, int bit){
        int[] vecino = Arrays.copyOf(solucion, solucion.length);
        vecino[bit] = 1 - vecino[bit];
        return vecino;
    }

    private static int[][] generarVecinos(int[] solucion){
        int[][] vecinos = new int[NUM_VECINOS][NUM_BITS];
        for(int i = 0; i < NUM_VECINOS; i++){
            vecinos[i] = generarVecino(solucion, i);
        }
        return vecinos;
    }

    private static double calcularCoste(int[] array){
        int n = binaryToDecimal(array);
        return Math.pow(n,3) - 60*Math.pow(n,2) + 900*n + 100;
    }

    private static boolean probAceptacion(double delta, double temperatura){
        return (Math.exp(delta / temperatura) > Math.random());
    }

    private static int binaryToDecimal(int[] array){
        int n = 0;
        for(int i = 0; i < array.length ;i++){
            n += array[i] << i;
        }
        return n;
    }
}