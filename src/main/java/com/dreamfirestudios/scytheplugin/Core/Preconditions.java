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

import java.util.Objects;
import java.util.function.Supplier;

/**
 * /// <summary>
 * Minimal guard/condition helper.
 * /// </summary>
 * /// <remarks>
 * Intentionally tiny: prefer explicit, readable checks at call sites.
 * /// </remarks>
 * /// <example>
 * <code>
 * if (!Preconditions.check(() -&gt; player.isOnline())) {
 *     return;
 * }
 * </code>
 * /// </example>
 */
public final class Preconditions {
    private Preconditions() { }

    /**
     * /// <summary>Evaluate a boolean supplier.</summary>
     * /// <param name="cond">Condition supplier.</param>
     * /// <returns>Supplier result.</returns>
     */
    public static boolean check(final Supplier<Boolean> cond) {
        return Objects.requireNonNull(cond, "cond").get();
    }
}