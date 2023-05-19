package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SequenceExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SequenceExecutor.class);
    private static final String FIRST_THREAD_NAME = "0";
    private static final String SECOND_THREAD_NAME = "1";

    private String activeThread = FIRST_THREAD_NAME;
    private final AtomicInteger counter = new AtomicInteger(0);


    public static void main(String[] args) {
        SequenceExecutor sequenceExecutor = new SequenceExecutor();
        sequenceExecutor.run();
    }

    public void run() {
        var executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> sequence(FIRST_THREAD_NAME, SECOND_THREAD_NAME));
        executor.submit(() -> sequence(SECOND_THREAD_NAME, FIRST_THREAD_NAME));

        executor.shutdown();
    }


    private void sequence(String threadId, String nextThreadId) {
        int delta = 1;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (this) {
                    while (!threadId.equals(activeThread)) {
                        this.wait();
                    }

                    if (FIRST_THREAD_NAME.equals(threadId)) {
                        if (counter.get() == 1) {
                            delta = 1;
                        } else if (counter.get() == 10) {
                            delta = -1;
                        }
                        counter.addAndGet(delta);
                    }

                    logger.info("threadId={} value={}", threadId, counter.get());
                    activeThread = nextThreadId;
                    sleep(300);
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
