package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put("1", 10);

        logger.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        cache.removeListener(listener);


        logger.info(" Эксперимент с List<WeakReference> ");
        cache.addListener(listener);
        cache.addListener((key, value, action) -> logger.info(" logger lambda  key:{}, value:{}, action: {}", key, value, action));
        cache.addListener(new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info(" logger anonymouse class  key:{}, value:{}, action: {}", key, value, action);
            }
        });
        cache.put("2", 22);

        listener = null;
        logger.info("listener equal: {}", listener);
        logger.info("getValue:{}", cache.get("2"));

        System.gc();
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("GC worked");

        logger.info("getValue:{}", cache.get("2"));
        cache.put("3", 33);
        logger.info("getValue:{}", cache.get("3"));
        cache.removeListener((key, value, action) -> logger.info(" logger lambda  key:{}, value:{}, action: {}", key, value, action));
        cache.put("3", 333);

        logger.info("getValue:{}", cache.get("3"));
        cache.addListener(new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                throw new IllegalArgumentException("illegal argument");
            }
        });

        logger.info("getValue:{}", cache.get("Error"));


    }
}