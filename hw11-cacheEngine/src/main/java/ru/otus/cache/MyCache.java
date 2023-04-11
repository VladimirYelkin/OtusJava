package ru.otus.cache;


import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V>  {
    //Надо реализовать эти методы
    private final Map<K, V> cacheMap = new WeakHashMap<>();
    @Override
    public void put(K key, V value) {
        cacheMap.put(key,value);
    }

    @Override
    public void remove(K key) {
        cacheMap.remove(key);
    }

    @Override
    public V get(K key) {
        return cacheMap.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {

    }

    @Override
    public void removeListener(HwListener<K, V> listener) {

    }
}
