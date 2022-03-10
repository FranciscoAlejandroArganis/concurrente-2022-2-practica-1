package unam.ciencias.computoconcurrente;

public class PrimeNumberCalculator implements Runnable {

    private static volatile boolean prime;
    private static int number;

    private int threads;
    private int from;
    private int to;

    public PrimeNumberCalculator () {
        threads = 1;
    }

    public PrimeNumberCalculator (int threads) {
        this.threads = threads > 1 ? threads : 1;
    }
    
    private PrimeNumberCalculator (int from, int to) {
        this.from = from;
        this.to = to;
    }

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
