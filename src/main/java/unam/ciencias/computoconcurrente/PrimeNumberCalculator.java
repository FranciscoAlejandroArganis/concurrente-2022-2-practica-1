package unam.ciencias.computoconcurrente;

/**
 * Clase que implementa una prueba de primalidad concurrente
 */

public class PrimeNumberCalculator implements Runnable {

    // Variables compartidas

    private static volatile boolean prime;
    private static int number;

    // Variables locales

    private int threads;
    private int from;
    private int to;

    /**
     * Construye un nuevo objeto PrimeNumberCalculator que opera con 1 hilo
     */

    public PrimeNumberCalculator () {
        threads = 1;
    }

    /**
     * Construye un nuevo objeto PrimeNumberCalculator que opera con la cantidad especificada de hilos
     * @param threads es la cantidad de hilos que se usan para probar la primalidad de un entero
     * El nuevo objeto opera con 1 hilo si threads es menor a 1
     */

    public PrimeNumberCalculator (int threads) {
        this.threads = threads > 1 ? threads : 1;
    }
    
    /**
     * Construye un nuevo objeto PrimeNumberCalculator para inicializar un hilo
     * @param from es el incio del rango de posibles divisores que el hilo va a probar
     * @param to es el final del rango de posibles divisor que el hilo va a probar
     */

    private PrimeNumberCalculator (int from, int to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Inicia la ejecución del hilo
     * Prueba los posibles divisores en el rango asignado a este hilo
     * Se detiene cuando agota el rango o cuando algún hilo encuentra un divisor
     */

    @ Override
    public void run () {
        while (prime && from <= to) {
            if (number % from == 0) {
                prime = false;
                return;
            }
            from ++;
        }
    }

    /**
     * Prueba si el número especificado es primo de forma concurrente
     * @param n es el número al que se aplica la pruba de primalidad
     * @return true si y solo si n es un número primo
     * @throws InterruptedException
     */

    public boolean isPrime (int n) throws InterruptedException {
        
        number = n >= 0 ? n : - n;
        if (number <= 1) return false;
        if (number <= 3) return true;
        prime = true;
        
        // Crear los hilos e iniciar su ejecución
        Thread [] threadArray = new Thread [threads];
        int root = (int) Math.sqrt (number);
        int length = 1 + (root - 2) / threads;
        int index = 0;
        int from = 2;
        int to = length + 1;
        while (to <= root) {
            threadArray [index] = new Thread (new PrimeNumberCalculator (from, to));
            threadArray [index].start ();
            from = to + 1;
            to += length;
            index ++;
        }
        if (from <= root) {
            threadArray [index] = new Thread (new PrimeNumberCalculator (from, root));
            threadArray [index].start ();
            index ++;
        }

        // Esperar a que todos los hilos terminen
        while (index > 0) {
            index --;
            threadArray [index].join ();
        }
        
        return prime;
    }

}
