package org.concurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ExchangerTest {
    public static final Logger LOGGER = Logger.getLogger("CONCURRENCY");

    public static void main(String[] args) {
        Exchanger<List<String>> listExchanger = new Exchanger<>();
        List<String> prodBuffer = Collections.synchronizedList(new ArrayList<String>(5));
        List<String> consumerBuffer = Collections.synchronizedList(new ArrayList<String>(5));
        Thread producer = new Thread(new Producer(prodBuffer, listExchanger), "producer");
        Thread consumer = new Thread(new Consumer(consumerBuffer, listExchanger), "consumer");
        producer.start();
        consumer.start();
        LOGGER.info("End main.");
    }
}

class Producer implements Runnable {

    private List<String> buffer;
    private Exchanger<List<String>> exchanger;

    public Producer(List<String> buffer, Exchanger<List<String>> exchanger) {
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        AtomicInteger cycle = new AtomicInteger(1);

        for (int i = 0; i < 10; i++) {
            ExchangerTest.LOGGER.info("Producer cycle: " + cycle.getAndIncrement());
            for (int j = 0; j < 5; j++) {
                buffer.add("Event " + (j + 1));
            }
            ExchangerTest.LOGGER.info(() -> "Buffer filled " + buffer.toString());
            try {
                List<String> exchanged = exchanger.exchange(buffer);
                ExchangerTest.LOGGER.info(() -> "Producer: exchanged buffer :" + exchanged.toString());
            } catch (InterruptedException e) {
                ExchangerTest.LOGGER.severe("Interrupted!");
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Consumer implements Runnable {

    private List<String> buffer;
    private Exchanger<List<String>> exchanger;

    public Consumer(List<String> buffer, Exchanger<List<String>> exchanger) {
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    private static ThreadLocal<Integer> cycle = ThreadLocal.withInitial(() -> 1);

    @Override
    public void run() {


        AtomicInteger atomicInteger = new AtomicInteger(1);

        for (int i = 0; i < 10; i++) {
            ExchangerTest.LOGGER.info("Consumer cycle : " + atomicInteger.getAndIncrement());
            try {
                buffer = exchanger.exchange(buffer);
                ExchangerTest.LOGGER.info(() -> "Consumer: exchanged buffer " + buffer.toString());
            } catch (InterruptedException e) {
                ExchangerTest.LOGGER.severe("Interrupted!");
                Thread.currentThread().interrupt();
            }
            buffer.clear();
            cycle.set(cycle.get() + 1);
        }
    }
}
