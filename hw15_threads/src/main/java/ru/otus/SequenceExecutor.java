package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SequenceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SequenceExecutor.class);
    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicBoolean isReadyNewIncrement = new AtomicBoolean(true);
    private final Object monitor = this;

    public static void main(String[] args) {
        SequenceExecutor sequenceExecutor = new SequenceExecutor();
        sequenceExecutor.run();
    }

    public void run() {
        var executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> generateSequenceAndShow(1, 10));
        executor.submit(this::showSequenceDouble);
    }

    private void generateSequenceAndShow(int beginSequence, int endSequence) {
        counter.set(beginSequence - 1);
        int delta = 1;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (monitor) {
                    while (!(isReadyNewIncrement.get())) {
                        this.wait();
                    }
                    if (counter.get() == beginSequence) {
                        delta = 1;
                    } else if (counter.get() == endSequence) {
                        delta = -1;
                    }
                    counter.addAndGet(delta);
                    logger.info("threadId={} value={}", Thread.currentThread().getId(), counter.get());
                    isReadyNewIncrement.set(false);
                    sleep(1000);
                    notifyAll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    private void showSequenceDouble() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (monitor) {
                    while (isReadyNewIncrement.get()) {
                        this.wait();
                    }
                    logger.info("threadId={} value={}", Thread.currentThread().getId(), counter.get());
                    isReadyNewIncrement.set(true);
                    sleep(1000);
                    notifyAll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
