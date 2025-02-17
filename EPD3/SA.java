package EPD3;

import java.util.Arrays;

public class SA {

    static final int N = 10;
    static final int M = 4;
    public int[][] costes = new int[M][N];
    static final int NVECINOS = 4;
    public int temp = 100;
    public int coste = 0;

    public int[][] AlgoritmoSA() {
        int[][] mejor = new int[M][N];
        int[][] actual = new int[M][N];
        int[][] vecino = new int[M][N];
        inicializaCostes();
        inicializaV(vecino);
        inicializaV(actual);
        inicializaV(mejor);
        while (temp > 10) {
            vecino = mejorVecino(actual);// Devuelve el mejor vecino de 4 vecinos generados
            // if (calculaCoste(vecino) <= 70) {
            if (calculaCoste(vecino) > calculaCoste(actual)
                    // Si el vecino es mejor que el actual lo cogemos como actual sino aplicamos la
                    // función de probabilidad
                    || Math.exp((calculaCoste(mejor) - calculaCoste(vecino)) / temp) > Math.random()) {
                actual = Arrays.copyOf(vecino, vecino.length);
            }
            if (calculaCoste(mejor) < calculaCoste(vecino)) {// Si el vecino es mejor que el mejor pues se convierte en
                                                             // el nuevo mejor
                mejor = Arrays.copyOf(vecino, vecino.length);
            }
            temp *= 0.9;
            // }
        }

        return mejor;
    }

    public void inicializaCostes() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                costes[i][j] = (int) (Math.random() * 100);
            }
        }
    }

    public void inicializaV(int[][] v) {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                v[i][j] = 0;
            }
        }
    }

    public int calculaCoste(int[][] v) {
        int coste = 0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (v[i][j] == 1) {
                    coste += costes[i][j];
                }
            }
        }
        return coste;
    }

    /*
     * Generamos un vecino de la siguiente forma: primero cogemos dos componentes
     * aleatorios y
     * después a esos dos comoponentes le cambiamos sus posibles valores de forma
     * aleatoria
     */
    public int[][] generaVecino(int[][] v) {
        int cambiar_componente1 = (int) (Math.random() * N);
        int cambiar_componente2 = (int) (Math.random() * N);
        int[][] vecinoAux = Arrays.copyOf(v, v.length);

        for (int i = 0; i < M; i++) {
            vecinoAux[i][cambiar_componente1] = 0;
            vecinoAux[i][cambiar_componente2] = 0;
        }
        int cambiar_tipo1 = (int) (Math.random() * M);
        int cambiar_tipo2 = (int) (Math.random() * M);
        vecinoAux[cambiar_tipo1][cambiar_componente1] = 1;
        vecinoAux[cambiar_tipo2][cambiar_componente2] = 1;

        return vecinoAux;
    }

    public int[][] mejorVecino(int[][] v) {
        int[][] vecinoMejor = new int[M][N];
        int[][] vecinoAux;
        inicializaV(vecinoMejor);
        for (int k = 0; k < NVECINOS; k++) {
            vecinoAux = generaVecino(v);
            // Para coger al vecino comprobamos que no supere el coste máximo especificado
            // en el enunciado
            if ((calculaCoste(vecinoAux) > calculaCoste(vecinoMejor)) && (calculaCoste(vecinoAux) + coste <= 280)) {
                vecinoMejor = Arrays.copyOf(vecinoAux, vecinoAux.length);
            }
        }
        coste += calculaCoste(vecinoMejor);
        return vecinoMejor;
    }

    public int valorComponente(int[][] v, int componente) {
        int i = 0;
        boolean encontrado = false;
        while (i < M && !encontrado) {
            if (v[i][componente] == 1) {
                encontrado = true;
            }
            i++;
        }
        return i;
    }
}
