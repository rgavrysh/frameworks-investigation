package org.spring;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.*;

public class SemaphoreTest {

    public static void main(String[] args) throws InterruptedException {
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
        this.semaphore = new Semaphore(1);
    }

    public void printJob(Object document) {
        try {
            semaphore.acquire();
            Duration duration = Duration.ofSeconds(3);
            System.out.printf("%s: PrintQueue: Printing a document during %d seconds\n",
                    Thread.currentThread().getName(), duration.getSeconds());
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}

class Job implements Runnable {
    private PrintQueue printQueue;

    public Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    @Override
    public void run() {
        System.out.printf("%s: Going to print a document\n", Thread.currentThread().getName());
        printQueue.printJob(new Object());
        System.out.printf("%s: The document has been printed\n", Thread.currentThread().getName());
    }
}