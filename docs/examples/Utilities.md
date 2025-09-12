# Utilities — Core

## Overview

The **Utilities module** provides reusable, lightweight helpers that solve common plugin problems. These utilities are thread-safe where required, designed for simplicity, and intended to reduce boilerplate across all plugins.

---

## Included Utilities

### 1. Scheduler

* **Purpose**: Abstracts Bukkit’s scheduler, making it easier to ensure safe main-thread or async task execution.
* **Features**:

    * `main(Runnable task)` — run immediately on main thread.
    * `mainLater(Runnable task, long ticks)` — run later on main thread.
    * `async(Runnable task)` — run in a separate thread pool.
* **Best Practices**: Always marshal Bukkit API interactions (entities, worlds, etc.) through `Scheduler.main()`.

---

### 2. PermissionStrings

* **Purpose**: Safely construct permission nodes with consistent format.
* **Features**:

    * Format template: `pluginName.level.action` (e.g., `GlitchSMP.Admin.ReloadConfigs`).
    * Works with {@link \_\_project\_name\_\_PermissionLevel} enums.
* **Benefits**: Avoids hardcoded permission strings across plugins.

---

### 3. PlayerResolver

* **Purpose**: Converts raw inputs (e.g., names, UUIDs) into {@link Player} objects safely.
* **Features**:

    * Supports exact/partial name resolution.
    * Provides `Optional<Player>` instead of nulls.
    * Thread-safe: resolves on main thread only.
* **Use Case**: Useful for commands where players are referenced by name.

---

### 4. ExpiringCache\<K,V>

* **Purpose**: Temporary key/value store with per-entry expiry.
* **Features**:

    * Entries expire after configured `Duration`.
    * Lock-free for reads/writes.
    * `getOrCompute(K, Supplier<V>)` — auto-loads values.
* **Use Cases**:

    * Caching expensive lookups (LuckPerms groups, Mojang API, etc.).
    * Temporary session data.

---

### 5. RateLimiter<K>

* **Purpose**: Prevents excessive action frequency.
* **Features**:

    * Per-key cooldown enforcement.
    * `tryAcquire(K key, Duration cooldown)` — returns `true` if action allowed.
    * `remaining(K key)` — returns remaining cooldown time.
* **Use Case**: Prevents spam (chat commands, ability triggers, etc.).

---

### 6. Try / Retry

* **Purpose**: Safe execution wrapper for retrying flaky operations.
* **Features**:

    * `Try.run(Runnable, retries)` — retry logic with configurable attempts.
    * `Try.get(Supplier<T>, retries)` — retries until a value is obtained.
    * Exception-safe and logs failures.
* **Use Case**: External API calls or disk operations that may fail intermittently.

---

### 7. Service Locator

* **Purpose**: Central registry for shared services in a plugin.
* **Features**:

    * Register/retrieve singleton services (e.g., Config managers, API bridges).
    * Thread-safe.
    * Reduces circular dependencies.
* **Use Case**: Common service discovery point across plugin layers.

---

### 8. Version / Platform Checks

* **Purpose**: Unified utility to detect runtime platform or version.
* **Features**:

    * Detects Minecraft version (`1.21.x`, etc.).
    * Detects server platform (Paper, Spigot, Folia).
    * Provides capability flags (e.g., supports Adventure API, supports async chunks).
* **Use Case**: Conditional feature enabling.

---

## Usage Patterns

* All utilities are **final** and designed for direct use — no inheritance.
* Utilities with plugin dependencies (e.g., `Scheduler`) are lightweight wrappers bound per-plugin.
* All null inputs are guarded via `Objects.requireNonNull`.

---

## Example

```java
// Example: limiting how often a player can use /fly
private final RateLimiter<UUID> flyLimiter = new RateLimiter<>();

public void handleFlyCommand(Player player) {
    if (!flyLimiter.tryAcquire(player.getUniqueId(), Duration.ofSeconds(30))) {
        Duration left = flyLimiter.remaining(player.getUniqueId());
        player.sendMessage("Please wait " + left.getSeconds() + "s before flying again.");
        return;
    }
    player.setAllowFlight(true);
}
```

---

## Benefits

* Reduces boilerplate in plugin implementations.
* Improves safety with null-checks, thread-safety, and retry logic.
* Provides consistent conventions across all plugins built on the core.

---

## Testing

* All utilities are designed for unit testing.
* ExpiringCache and RateLimiter can use injected `Clock` instances for deterministic tests.
* Try/Retry can simulate failure sequences to verify retry logic.