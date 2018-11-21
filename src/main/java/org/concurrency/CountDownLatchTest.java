package org.concurrency;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CountDownLatchTest {
    public static final Logger LOGGER = Logger.getLogger("CONCURRENCY");

    public static void main(String[] args) {
        Conference conference = new Conference(10);
        Thread conferenceThread = new Thread(conference);
        conferenceThread.start();

        for (int i = 0; i < 10; i++) {
            Participant p = new Participant(String.valueOf(i + 1));
            p.setConference(conference);
            new Thread(p).start();
        }
    }
}

class Conference implements Runnable {

    private final CountDownLatch controller;

    public Conference(Integer minNumberOfParticipants) {
        this.controller = new CountDownLatch(minNumberOfParticipants);
    }

    public void arrive(String name) {
        CountDownLatchTest.LOGGER.info(() -> name + " has arrived.");
        controller.countDown();
    }

    @Override
    public void run() {
        CountDownLatchTest.LOGGER.info("Starting conference ...");
        CountDownLatchTest.LOGGER.info(() -> "Waiting for " + controller.getCount() + " participants : ");
        try {
            controller.await();
            CountDownLatchTest.LOGGER.info("Let's start conference!");
        } catch (InterruptedException e) {
            CountDownLatchTest.LOGGER.severe("Interrupted!");
            Thread.currentThread().interrupt();
        }
    }
}

class Participant implements Runnable {

    private Conference conference;
    private final String name;

    public Participant(String name) {
        this.name = name;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    @Override
    public void run() {
        //hesitate if go to conference
        long randomDuration = new Random().nextInt(10);
        try {
            TimeUnit.SECONDS.sleep(randomDuration);
        } catch (InterruptedException e) {
            CountDownLatchTest.LOGGER.severe("Interrupted!");
            Thread.currentThread().interrupt();
        }
        conference.arrive(this.name);
    }
}