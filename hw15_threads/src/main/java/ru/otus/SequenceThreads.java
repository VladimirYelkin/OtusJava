package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class SequenceThreads {
    private static final Logger logger = LoggerFactory.getLogger(SequenceThreads.class);
    private Integer activeThread = 1;
    private final Integer numbersThreads;

    public static void main(String[] args) throws InterruptedException {
        SequenceThreads demo = new SequenceThreads(2);
        demo.run();
    }

    public SequenceThreads(int numbersThreads) {
        this.numbersThreads = numbersThreads;
    }

    public void run() throws InterruptedException {
        var threads = IntStream.range(1, numbersThreads + 1)
                .mapToObj(idThread -> new Thread(() -> sequence(idThread)))
                .toList();
        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }
    }

    private synchronized void sequence(Integer threadId) {

        int counter = 1;
        int delta = 1;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (!(threadId.equals(activeThread))) {
                    logger.debug("threadId = {} active thread {}", threadId, activeThread);
                    this.wait();
                }

                if (counter == 1) {
                    delta = 1;
                } else if (counter == 10) {
                    delta = -1;
                }

                logger.info("threadId={} value={}", threadId, counter);
                counter += delta;
                sleep(1000);

                activeThread = (++activeThread > numbersThreads) ? 1 : activeThread;
                logger.debug(" active thread set {}", activeThread);

                notifyAll();
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
