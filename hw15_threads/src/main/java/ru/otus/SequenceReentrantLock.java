package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SequenceReentrantLock {
    private static final Logger logger = LoggerFactory.getLogger(SequenceReentrantLock.class);
    private static final String FIRST_THREAD_NAME = "1";
    private static final String SECOND_THREAD_NAME = "2";
    private final Lock lock = new ReentrantLock(true);
    private final AtomicBoolean isFirstThreadStart = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {
        new SequenceReentrantLock().run();
    }

    private void run() throws InterruptedException {
        var t2 = new Thread(this::sequence);
        t2.setName(SECOND_THREAD_NAME);

        var t1 = new Thread(this::sequence);
        t1.setName(FIRST_THREAD_NAME);

        t2.start();
        t1.start();

        t1.join();
        t2.join();
    }


    private void sequence() {
        int counter = 1;
        int delta = 1;
        logger.debug("Thread_name {}", Thread.currentThread().getName());

        while (!Thread.currentThread().isInterrupted()) {
            logger.debug(" before lock {} {}", ((ReentrantLock) lock).getHoldCount(), ((ReentrantLock) lock).isHeldByCurrentThread());
            String currentThreadName = Thread.currentThread().getName();
            lock.lock();
            try {
                if ((isFirstThreadStart.get()) || (FIRST_THREAD_NAME.equals(currentThreadName))) {
                    if (counter == 1) {
                        delta = 1;
                    } else if (counter == 10) {
                        delta = -1;
                    }
                    logger.info("threadId={} value={}", currentThreadName, counter);
                    counter += delta;
                    isFirstThreadStart.set(true);
                    sleep();
                    logger.debug("{} {}", ((ReentrantLock) lock).getHoldCount(), ((ReentrantLock) lock).isHeldByCurrentThread());
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
