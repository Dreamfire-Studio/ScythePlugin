/*
 * MIT License
 *
 * Copyright (c) 2025 Dreamfire Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
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
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * Small retry/backoff helpers for flaky or order-sensitive calls.
 */
public final class Try {
    private Try() {}

    /** Runnable version (fire-and-forget). Retries on ANY Throwable. */
    public static void runWithRetry(final String opName, final int maxAttempts, final Duration baseDelay, final Runnable body) {
        Objects.requireNonNull(opName, "opName");
        Objects.requireNonNull(baseDelay, "baseDelay");
        Objects.requireNonNull(body, "body");
        if (maxAttempts < 1) throw new IllegalArgumentException("maxAttempts must be >= 1");
        int attempt = 0;
        while (true) {
            attempt++;
            try {
                body.run();
                return; // success
            } catch (Throwable t) {
                if (attempt >= maxAttempts) {
                    // rethrow on final failure
                    if (t instanceof RuntimeException re) throw re;
                    throw new RuntimeException(opName + " failed after " + attempt + " attempts", t);
                }
                sleep(backoffWithJitter(baseDelay, attempt));
            }
        }
    }

    public static <T> T callWithRetry(final String opName, final int maxAttempts, final Duration baseDelay, final Supplier<T> body) {
        Objects.requireNonNull(body, "body");
        final Holder<T> out = new Holder<>();
        runWithRetry(opName, maxAttempts, baseDelay, () -> out.value = body.get());
        return out.value;
    }

    private static Duration backoffWithJitter(final Duration base, final int attempt) {
        long baseMs = Math.max(1, base.toMillis());
        long exp = baseMs << (attempt - 1);
        long jitter = (long) (exp * (ThreadLocalRandom.current().nextDouble(-0.2, 0.2)));
        long ms = Math.max(1, exp + jitter);
        long cap = 30_000L;
        return Duration.ofMillis(Math.min(ms, cap));
    }

    private static void sleep(final Duration d) {
        try {
            Thread.sleep(Math.max(1, d.toMillis()));
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while backing off", ie);
        }
    }

    private static final class Holder<T> { T value; }
}