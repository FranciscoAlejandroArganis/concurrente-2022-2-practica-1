package unam.ciencias.computoconcurrente;

/**
 * Clase que implementa un algoritmo concurrente para el promedio de una matriz
 */

public class MatrixUtils implements Runnable {
    
    // Variables compartidas

    private static int dim;
    private static int [] [] matrix;
    private static volatile Double [] averages;

    // Variables locales

    private int threads;
    private int from;
    private int to;
    private int index;

    /**
     * Construye un nuevo objeto MatrixUtils que opera con 1 hilo
     */

    public MatrixUtils () {
        threads = 1;
    }

    /**
     * Construye un nuevo objeto MatrixUtils que opera con la cantidad especificada de hilos
     * @param threads es la cantidad de hilos que se usan para calcular el promedio de una matriz
     * El nuevo objeto opera con 1 hilo si threads es menor a 1
     */

    public MatrixUtils (int threads) {
        this.threads = threads > 1 ? threads : 1;
    }

    /**
     * Construye un nuevo objeto MAtrixUtils para inicializar un hilo
     * @param from es el incio del rango de entradas de la matriz que el hilo va a promediar
     * @param to es el final del rango de entradas de la matriz que el hilo va a promediar
     * @param index es el identificador del hilo
     */

    private MatrixUtils (int from, int to, int index) {
        this.from = from;
        this.to = to;
        this.index = index;
    }

    /**
     * Inicia la ejecución del hilo
     * Promedia las entradas de la matriz en el rango asignado a este hilo
     * Se detiene cuando agota el rango
     */

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

    /**
     * Calcula el promedio de la matriz especificada de forma concurrente
     * @param matrix es la matriz con la que se calcula el promedio
     * @return el promedio de las entradas de la matriz
     * @throws InterruptedException
     */

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
