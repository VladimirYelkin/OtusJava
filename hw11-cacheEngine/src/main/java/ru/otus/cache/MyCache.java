package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final Map<K, V> cacheMap = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    //Надо реализовать эти методы
    @Override
    public void put(K key, V value) {
        logger.debug("put: key:{} value{}  size WeekHashMap{}", key, value, cacheMap.size());
        event(key, value, "put");
        cacheMap.put(key, value);
    }

    @Override
    public void remove(K key) {
        logger.debug("remove: key:{} size WeekHashMap{}", key, cacheMap.size());
        event(key, cacheMap.get(key), "remove");
        cacheMap.remove(key);
    }

    @Override
    public V get(K key) {
        logger.debug("get: key:{} size WeekHashMap{}", key, cacheMap.size());
        event(key, cacheMap.get(key), "get");
        return cacheMap.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        logger.debug("addListener: {} ", listener);
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        logger.debug("remove: {} ", listener);
        listeners.removeIf(hwListenerWeakReference ->
                Optional.ofNullable(hwListenerWeakReference.get()).isEmpty() || listener.equals(hwListenerWeakReference.get())
        );
    }

    private void event(K key, V value, String action) {
        logger.debug("event key:{} value:{} action:{} ", key, value, action);

        listeners.forEach(listener -> {
            try {
                Optional.ofNullable(listener.get()).ifPresent(kvHwListener -> kvHwListener.notify(key, value, action));
            } catch (Exception e) {
                logger.error("call listener exception", e);
            }
        });
    }
}