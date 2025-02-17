
package EPD3;

import java.util.*;

public class TS {
    static final int N = 10;
    static final int M = 4;
    public int[][] costes = new int[M][N];
    static final int NVECINOS = 4;
    static final int TENENCIA = 3;
    static final int NIteraciones = 50;
    public Queue<int[][]> listaTabu = new LinkedList();

    public int[][] AlgoritmoTS() {
        int[][] mejor = new int[M][N];
        int[][] actual = new int[M][N];
        int[][] vecino = new int[M][N];
        inicializaCostes();
        inicializaV(vecino);
        inicializaV(actual);
        inicializaV(mejor);
        for (int x = 0; x < NIteraciones; x++) {
            vecino = mejorVecino(actual);
            if (calculaCoste(vecino) > calculaCoste(mejor)) {
                for (int i = 0; i < M; i++) {
                    for (int j = 0; j < N; j++) {
                        mejor[i][j] = vecino[i][j];
                    }
                }
            }
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    actual[i][j] = vecino[i][j];
                }
            }
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
                if (v[i][j] == 1)
                    coste = costes[i][j];
            }
        }
        return coste;
    }

    public int[][] generaVecino(int[][] v) {
        int cambiar_componente1 = (int) (Math.random() * N);
        int cambiar_componente2 = (int) (Math.random() * N);
        int[][] vecinoAux = new int[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                vecinoAux[i][j] = v[i][j];
            }
        }
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
            if (calculaCoste(vecinoAux) > calculaCoste(vecinoMejor) && calculaCoste(vecinoAux) < 280
                    && !esTabu(vecinoAux)) { /*
                                              * Para coger al vecino comprobamos
                                              * que no se encuentre en la lista tabu
                                              * y que no supere el coste máximo especificado en el enunciado
                                              */
                vecinoMejor = Arrays.copyOf(vecinoAux, vecinoAux.length);
            }
        }
        if (listaTabu.size() > TENENCIA) // Borramos de la lista una vez que ya este llena la lista tabu según su
                                         // tenencia
            listaTabu.remove();
        listaTabu.add(vecinoMejor);
        return vecinoMejor;
    }

    public boolean esTabu(int[][] v) {
        return listaTabu.contains(v);
    }

    public int valorComponente(int[][] v, int componente) {
        int i = 0;
        boolean encontrado = false;
        while (i < M && !encontrado) {
            if (v[i][componente] == 1)
                encontrado = true;
            i++;
        }
        return i;
    }

}
