/*
 * MIT License
 *
 * Copyright (c) 2025 Dreamfire Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.dreamfirestudios.scytheplugin.Core;

import java.time.Duration;
import java.util.Objects;

/**
 * Non-blocking, thread-safe token bucket rate limiter.
 *
 * <p>Supports two creation styles:</p>
 * <ul>
 *   <li>{@link #of(int, Duration)} — X permits per time window (e.g., 2 per second)</li>
 *   <li>{@link #perSecond(double, int)} — permits/second with a burst cap</li>
 * </ul>
 *
 * <p>Use {@link #tryAcquire()} or {@link #tryAcquire(int)} to attempt consumption.
 * Calls never block; they return {@code true} on success or {@code false} if the
 * current bucket does not have enough tokens.</p>
 *
 * <p>Implementation notes: uses a simple synchronized refill-and-consume with
 * double precision for fractional accrual. This is plenty fast for plugin usage,
 * avoids busy-waiting, and keeps correctness straightforward.</p>
 */
public final class RateLimiter {

    /** Tokens added per nanosecond. */
    private final double tokensPerNano;

    /** Maximum stored tokens (burst). */
    private final double burst;

    /** Currently stored tokens. Guarded by 'this'. */
    private double stored;

    /** Last refill time in nanos. Guarded by 'this'. */
    private long lastNanos;

    private RateLimiter(final double tokensPerNano, final double burst, final long nowNanos) {
        if (tokensPerNano <= 0.0) throw new IllegalArgumentException("tokensPerNano must be > 0");
        if (burst <= 0.0) throw new IllegalArgumentException("burst must be > 0");
        this.tokensPerNano = tokensPerNano;
        this.burst = burst;
        this.stored = burst;      // start full
        this.lastNanos = nowNanos; // initialize clock
    }

    /**
     * Create a limiter that allows {@code permits} per {@code window} (e.g., 2 per second).
     */
    public static RateLimiter of(final int permits, final Duration window) {
        Objects.requireNonNull(window, "window");
        if (permits <= 0) throw new IllegalArgumentException("permits must be > 0");
        final long nanos = window.toNanos();
        if (nanos <= 0L) throw new IllegalArgumentException("window must be > 0");
        final double perNano = permits / (double) nanos;
        return new RateLimiter(perNano, permits, System.nanoTime());
    }

    /**
     * Create a limiter that accrues {@code permitsPerSecond} with a maximum burst of {@code burst}.
     */
    public static RateLimiter perSecond(final double permitsPerSecond, final int burst) {
        if (permitsPerSecond <= 0.0) throw new IllegalArgumentException("permitsPerSecond must be > 0");
        if (burst <= 0) throw new IllegalArgumentException("burst must be > 0");
        final double perNano = permitsPerSecond / 1_000_000_000.0;
        return new RateLimiter(perNano, burst, System.nanoTime());
    }

    /** Try to acquire a single permit. Non-blocking. */
    public boolean tryAcquire() {
        return tryAcquire(1);
    }

    /**
     * Try to acquire {@code permits} permits. Non-blocking.
     *
     * @return {@code true} if acquired; {@code false} otherwise.
     */
    public synchronized boolean tryAcquire(final int permits) {
        if (permits <= 0) throw new IllegalArgumentException("permits must be > 0");
        refillLocked(System.nanoTime());
        if (stored >= permits) {
            stored -= permits;
            return true;
        }
        return false;
    }

    /** Current tokens (approximate, for diagnostics/metrics). */
    public synchronized double availablePermits() {
        refillLocked(System.nanoTime());
        return stored;
    }

    /** Next refill based on elapsed time since last tick. */
    private void refillLocked(final long now) {
        final long elapsed = now - lastNanos;
        if (elapsed <= 0) return;
        final double add = elapsed * tokensPerNano;
        if (add > 0.0) {
            stored = Math.min(burst, stored + add);
            lastNanos = now;
        }
    }

    @Override
    public synchronized String toString() {
        return "RateLimiter{tokensPerNano=" + tokensPerNano +
                ", burst=" + burst +
                ", stored=" + stored + '}';
    }
}