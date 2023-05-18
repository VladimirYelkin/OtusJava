package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SequenceReentrantLock {
    private static final Logger logger = LoggerFactory.getLogger(SequenceReentrantLock.class);

    private final Lock lock = new ReentrantLock(true);

    public static void main(String[] args) throws InterruptedException {
        new SequenceReentrantLock().run();
    }

    private void run() throws InterruptedException {
        var t1 = new Thread(this::sequence);
        t1.setName("1");
        t1.start();

        var t2 = new Thread(this::sequence);
        t2.setName("2");
        t2.start();

        t1.join();
        t2.join();
    }


    private void sequence() {
        int counter = 1;
        int delta = 1;
        while (!Thread.currentThread().isInterrupted()) {
            lock.lock();
            try {
                if (counter == 1) {
                    delta = 1;
                } else if (counter == 10) {
                    delta = -1;
                }
                logger.info("threadId={} value={}", Thread.currentThread().getName(), counter);
                counter += delta;
                sleep();
            } finally {
                lock.unlock();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
