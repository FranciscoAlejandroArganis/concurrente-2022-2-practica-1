package unam.ciencias.computoconcurrente;

public class MatrixUtils implements Runnable {
    
    private static int dim;
    private static int [] [] matrix;
    private static volatile Double [] averages;

    private int threads;
    private int from;
    private int to;
    private int index;

    public MatrixUtils () {
        threads = 1;
    }

    public MatrixUtils (int threads) {
        this.threads = threads > 1 ? threads : 1;
    }

    private MatrixUtils (int from, int to, int index) {
        this.from = from;
        this.to = to;
        this.index = index;
    }

    @ Override
    public void run () {
        int i, j;
        double sum = 0;
        int total = to - from + 1;
        while (from <= to) {
            i = from / dim;
            j = from - dim * i;
            sum += matrix [i] [j];
            from ++;
        }
        averages [index] = sum / total;
    }

    public double findAverage (int [] [] matrix) throws InterruptedException {
        if (matrix.length == 0) return 0;
        dim = matrix [0].length;
        if (dim == 0) return 0;
        MatrixUtils.matrix = matrix;
        averages = new Double [threads];

        // Crear los hilos e iniciar su ejecución
        Thread [] threadArray = new Thread [threads];
        int size = matrix.length * dim;
        int length = 1 + (size - 1) / threads;
        int index = 0;
        int from = 0;
        int to = length - 1;
        while (to < size) {
            threadArray [index] = new Thread (new MatrixUtils (from, to, index));
            threadArray [index].start ();
            from = to + 1;
            to += length;
            index ++;
        }
        if (from < size) {
            threadArray [index] = new Thread (new MatrixUtils (from, size - 1, index));
            threadArray [index].start ();
            index ++;
        }

        // Esperar a que todos los hilos terminen
        while (index > 0) {
            index --;
            threadArray [index].join ();
        }

        // Calcular el promedio de los promedios parciales de cada hilo
        double sum = 0;
        index = 0;
        while (index < threads && averages [index] != null) {
            sum += averages [index];
            index ++;
        }

        return sum / index;
    }

}
