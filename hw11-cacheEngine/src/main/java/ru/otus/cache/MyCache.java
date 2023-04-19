package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final Map<K, V> cacheMap = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    {   //logger.debug
        listeners.add(new WeakReference<>((key, value, action) -> logger.debug("{}: logger  key:{}, value:{}", action, key, value)));
    }

    //Надо реализовать эти методы
    @Override
    public void put(K key, V value) {
        event(key, value, "put");
        cacheMap.put(key, value);
    }

    @Override
    public void remove(K key) {
        event(key, cacheMap.get(key), "remove");
        cacheMap.remove(key);
    }

    @Override
    public V get(K key) {
        event(key, cacheMap.get(key), "get");
        return cacheMap.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.removeIf(hwListenerWeakReference ->
                Optional.ofNullable(hwListenerWeakReference.get()).isEmpty() || listener.equals(hwListenerWeakReference.get())
        );
    }

    private void event(K key, V value, String action) {

        listeners.forEach(listener -> {
            try {
                Optional.ofNullable(listener.get()).ifPresent(kvHwListener -> kvHwListener.notify(key, value, action));
            } catch (Exception e) {
                logger.error("call listener exception", e);
            }
        });
    }
}