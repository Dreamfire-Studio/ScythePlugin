/*
 * MIT License
 *
 * Copyright (c) 2025 Dreamfire Studio
 */
package com.dreamfirestudios.scytheplugin.Core;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <summary>Simple lock-free TTL cache for hot values.</summary>
 * <remarks>Entries expire lazily on read.</remarks>
 */
public final class ExpiringCache<K, V> {

    private static final class Entry<V> {
        final V v; final long exp;
        Entry(V v, long exp) { this.v = v; this.exp = exp; }
    }

    private final long ttlNanos;
    private final ConcurrentMap<K, Entry<V>> map = new ConcurrentHashMap<>();

    /**
     * <summary>Create a cache with the given TTL.</summary>
     */
    public ExpiringCache(final Duration ttl) {
        Objects.requireNonNull(ttl, "ttl");
        this.ttlNanos = Math.max(1L, ttl.toNanos());
    }

    /**
     * <summary>Get an item if present and not expired.</summary>
     */
    public Optional<V> get(final K key) {
        Objects.requireNonNull(key, "key");
        final Entry<V> e = map.get(key);
        if (e == null) return Optional.empty();
        if (System.nanoTime() > e.exp) { map.remove(key, e); return Optional.empty(); }
        return Optional.ofNullable(e.v);
    }

    /**
     * <summary>Get or load value (loader takes the key), installing with TTL on miss.</summary>
     */
    public V get(final K key, final Function<K, V> loaderIfMiss) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(loaderIfMiss, "loaderIfMiss");
        final long now = System.nanoTime();
        final Entry<V> e = map.get(key);
        if (e != null && now <= e.exp) return e.v;
        final V v = loaderIfMiss.apply(key);
        map.put(key, new Entry<>(v, now + ttlNanos));
        return v;
    }

    /**
     * <summary>Get or compute value (loader doesn't need the key), installing with TTL on miss.</summary>
     * <example>
     * <code>
     * boolean allowed = cache.getOrCompute(cacheKey, () -> expensiveCheck());
     * </code>
     * </example>
     */
    public V getOrCompute(final K key, final Supplier<V> loaderIfMiss) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(loaderIfMiss, "loaderIfMiss");
        final long now = System.nanoTime();
        final Entry<V> e = map.get(key);
        if (e != null && now <= e.exp) return e.v;
        final V v = loaderIfMiss.get();
        map.put(key, new Entry<>(v, now + ttlNanos));
        return v;
    }

    /**
     * <summary>Put a value directly with the default TTL.</summary>
     */
    public void put(final K key, final V value) {
        Objects.requireNonNull(key, "key");
        final long exp = System.nanoTime() + ttlNanos;
        map.put(key, new Entry<>(value, exp));
    }

    /**
     * <summary>Invalidate a single key.</summary>
     */
    public void invalidate(final K key) { Objects.requireNonNull(key, "key"); map.remove(key); }

    /**
     * <summary>Invalidate all entries.</summary>
     */
    public void invalidateAll() { map.clear(); }
}