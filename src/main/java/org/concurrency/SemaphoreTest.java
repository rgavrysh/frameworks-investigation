package org.concurrency;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class SemaphoreTest {

    public static final Logger LOGGER = Logger.getLogger("CONCURRENCY");

    public static void main(String[] args) {
        PrintQueue printQueue = new PrintQueue();
        ArrayList<Thread> runnables = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            runnables.add(i, new Thread(new Job(printQueue), "Thread-" + i));
        }
//        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (Thread t : runnables) {
            t.start();
//            threadPool.execute(t);
        }


    }
}

class PrintQueue {
    private final Semaphore semaphore;

    PrintQueue() {
        this.semaphore = new Semaphore(3);
    }

    void printJob() {
        try {
            semaphore.acquire();
            Duration duration = Duration.ofSeconds(3);
            SemaphoreTest.LOGGER.info(() -> Thread.currentThread().getName() +
                    ": PrintQueue: Printing a document during " + duration.getSeconds() + " seconds.");
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            SemaphoreTest.LOGGER.severe("Interrupted!");
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }
}

class Job implements Runnable {
    private PrintQueue printQueue;

    Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    @Override
    public void run() {
        SemaphoreTest.LOGGER.info(() -> Thread.currentThread().getName() + ": Going to print a document.");
        printQueue.printJob();
        SemaphoreTest.LOGGER.info(() -> Thread.currentThread().getName() + ": The document has been printed");
    }
}