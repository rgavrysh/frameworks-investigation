package org.spring;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {
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
        System.out.println(name + " has arrived.");
        controller.countDown();
    }

    @Override
    public void run() {
        System.out.println("Starting conference ...");
        System.out.printf("Waiting for %d participants : \n", controller.getCount());
        try {
            controller.await();
            System.out.println("Let's start conference!");
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        conference.arrive(this.name);
    }
}