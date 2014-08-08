package com.tw.go.plugins.artifactory.testutils;

import java.util.HashMap;

public class MapBuilder {
    public static <K, V> FluentMap<K, V> map(K key, V value) {
        return new FluentMap<K, V>().and(key, value);
    }

    public static class FluentMap<K, V> extends HashMap<K, V> {
        public FluentMap<K, V> and(K key, V value) {
            put(key, value);
            return this;
        }
    }
}
