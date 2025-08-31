/*
 * MIT License
 *
 * Copyright (c) 2025 Dreamfire Studio
 */
package com.dreamfirestudios.scytheplugin.Core;

import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <summary>
 * Simple service locator for plugin-scoped utilities (Scheduler, caches, etc).
 * </summary>
 * <remarks>
 * Call {@link #bootstrap(JavaPlugin)} during plugin enable to register core services.
 * </remarks>
 */
public final class Services {
    private static final Map<Class<?>, Object> REGISTRY = new ConcurrentHashMap<>();
    private static final Map<String, RateLimiter> RATE_LIMITERS = new ConcurrentHashMap<>();

    private Services() {}

    /**
     * <summary>Registers built-in services for this plugin instance.</summary>
     * <param name="plugin">Owning plugin (non-null).</param>
     */
    public static void bootstrap(final JavaPlugin plugin) {
        Objects.requireNonNull(plugin, "plugin");
        register(Scheduler.class, new Scheduler(plugin));
    }

    /**
     * <summary>Register a concrete service implementation.</summary>
     */
    public static <T> void register(final Class<T> type, final T impl) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(impl, "impl");
        REGISTRY.put(type, impl);
    }

    /**
     * <summary>Resolve a required service or throw if missing.</summary>
     */
    public static <T> T get(final Class<T> type) {
        Objects.requireNonNull(type, "type");
        final Object o = REGISTRY.get(type);
        if (o == null) throw new IllegalStateException("Service not registered: " + type.getName());
        return type.cast(o);
    }

    /**
     * <summary>Resolve an optional service.</summary>
     */
    public static <T> Optional<T> maybe(final Class<T> type) {
        Objects.requireNonNull(type, "type");
        final Object o = REGISTRY.get(type);
        return Optional.ofNullable(type.cast(o));
    }

    /**
     * <summary>Check registration status.</summary>
     */
    public static boolean isRegistered(final Class<?> type) {
        Objects.requireNonNull(type, "type");
        return REGISTRY.containsKey(type);
    }

    // --- Helpers (simple factories) ---

    /**
     * <summary>Create a new expiring cache with a given TTL.</summary>
     * <remarks>The name is informational (useful for logs/metrics).</remarks>
     */
    public static <K, V> ExpiringCache<K, V> expiringCache(final String name, final Duration ttl) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(ttl, "ttl");
        return new ExpiringCache<>(ttl);
    }

    /**
     * <summary>Get or create a shared (non-keyed) rate limiter by name.</summary>
     */
    public static RateLimiter rateLimiter(final String name, final int permits, final Duration window) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(window, "window");
        return RATE_LIMITERS.computeIfAbsent(name, n -> RateLimiter.of(permits, window));
    }

    /**
     * <summary>Resolve the scheduler service.</summary>
     */
    public static Scheduler scheduler() {
        return get(Scheduler.class);
    }
}