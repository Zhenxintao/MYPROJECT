package com.bmts.heating.cache.library.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description
 * @Author fei.chang
 * @Date 2020/7/23 18:46
 * @Version 1.0
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int MAX_CACHE_SIZE;

    public LRUCache(int cacheSize) {
        super((int) Math.ceil(cacheSize / 0.75) + 1, 0.75f, true);
        MAX_CACHE_SIZE = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > MAX_CACHE_SIZE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : entrySet()) {
            sb.append(String.format("%s:%s ", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }
}