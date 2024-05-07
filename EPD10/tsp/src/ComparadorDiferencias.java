
import java.util.Comparator;

/* Comparador usado para ordenar por el atributo diferencias */
public class ComparadorDiferencias implements Comparator {

    @Override
    public int compare(Object a, Object b) {
        int f1 = ((Individual) a).dispersion;
        int f2 = ((Individual) b).dispersion;

        if (f1 < f2) {
            return 1;
        } else if (f1 > f2) {
            return -1;
        } else {
            return 0;
        }
    }
}
