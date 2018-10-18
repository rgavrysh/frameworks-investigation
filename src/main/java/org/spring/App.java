package org.spring;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Spring Core!
 */
public class App {
    public static Logger logger = Logger.getLogger("MAIN");

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        logger.info("Start ...");

        int workingNumber = 5;
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> logger.info("future1 executing ..."), pool);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> logger.info("future2 executing ..."), pool);
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> logger.info("future3 executing ..."), pool);
        CompletableFuture<Void> future4 = CompletableFuture.runAsync(() -> logger.info("future4 executing ..."), pool);
        CompletableFuture<Object> allOf = CompletableFuture.anyOf(future1, future2, future3, future4);

        logger.info("is done : " + allOf.isDone());
//        allOf.join();
        //        logger.info("future get : " + future.getNow(0));
        logger.info("is done : ");


//        Thread thread1 = new Thread(() -> {
////            boolean isInterrupted = false;
//            while (!Thread.currentThread().isInterrupted()) {
//                try {
//                    System.out.println(Thread.currentThread().getName() + " executing...");
//                    Thread.currentThread().sleep(2000l);
//                    throw new NullPointerException("Uncaught NPE.");
//                } catch (InterruptedException e) {
//                    System.err.println(Thread.currentThread().getName() + " interrupted");
//                    Thread.currentThread().interrupt();
////                    isInterrupted = true;
//                }
//            }
//        });
//
//        thread1.setDaemon(true);
//        thread1.setName("printer");
//        thread1.setPriority(3);
//        thread1.setUncaughtExceptionHandler((thread, throwable) -> {
//            if (thread.getName().equalsIgnoreCase(thread1.getName())) {
//                if (throwable instanceof NullPointerException) {
//                    System.out.println("no worries. it is npe runtime exception thrown purposely.");
//                }
//            }
//        });
//        thread1.start();

        ArrayList<Integer> integers = new ArrayList<>(1000_000);
        while (true) {
//            logger.info(Thread.currentThread().getName() + " working ...");
            for (int i = 0; i < 1000_000; i++) {
                integers.add(i, i);
            }
        }
//            try {
//                Thread.sleep(3000l);
//                System.gc();
////                thread1.interrupt();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        AnnotationConfigApplicationContexontext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);

//        applicationContext.getBean(SimpleComponent.class).doStuff();
//        logger.info("Application Context created.");
    }
}
