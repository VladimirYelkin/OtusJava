package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);
    private Integer activeThread = 1;
    private final Integer numbersThreads ;

    public static void main(String[] args) {
        Demo demo = new Demo(2);
        demo.run();
    }

    public Demo(int numbersThreads) {
        this.numbersThreads = numbersThreads;
    }

    public void run() {
        IntStream.range(1, numbersThreads + 1)
                .mapToObj(i -> new Thread(() -> action(i)))
                .forEach(Thread::start);
    }

    private synchronized void action(Integer threadId) {

        int i = 1;
        int delta = 1;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (!(threadId.equals(activeThread))) {
                    logger.debug("threadId = {} active thread {}",threadId, activeThread);
                    this.wait();
                }

                if (i == 1) {
                    delta = 1;
                } else if (i == 10) {
                    delta = -1;
                }
                logger.info("threadId={} value={}", threadId, i);
                i = i + delta;

                sleep(300);

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
