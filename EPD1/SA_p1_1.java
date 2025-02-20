/*Se desea fabricar un material aislante compuesto por siete materiales distintos. Se desea
encontrar cuál es el orden en que deben mezclarse dichos materiales para asegurar que sea lo más aislante
posible. Suponga la siguiente situación:
Materiales: {1, 2, 3, 4, 5, 6, 7}
Se puede apreciar un vector en el que aparece el orden en el que se combinan los materiales y, además, una
matriz triangular para identificar posibles permutaciones de orden, es decir, posibles soluciones a las que ir.
Por ejemplo, si se tiene la siguiente matriz triangular:
[0, 1, 2, 3 , 4 , 5 , 6 ]
[0, 0, 8, 9 , 10, 11, 12]
[0, 0, 0, 15, 16, 17, 18]
[0, 0, 0, 0 , 22, 23, 24]
[0, 0, 0, 0 , 0 , 29, 30]
[0, 0, 0, 0 , 0 , 0 , 36]
Así, la posición (4,5) estaría haciendo referencia a que se intercambie el orden de los materiales 4 por el 5.
Para evaluar la calidad aislante de la solución, suponga que ésta se mide por la suma de los tres primeros
componentes, esto es, sobre la figura de arriba sería 4+2+7 = 13. Si realizamos la permutación 4 por 5,
entonces, la nueva solución candidata sería [5, 2, 7, 1, 4, 6, 3], siendo su coste 5+2+7 = 14 (>13) y por tanto se
aceptaría. De esta condición se deduce que el máximo se alcanzará cuando tengamos en las tres posiciones
superiores {5, 6, 7}, en cualquier orden posible o, en otras palabras, que el máximo global sería 5+6+7 = 18.
*/
package EPD1;

import java.util.Arrays;

public class SA_p1_1 {
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
