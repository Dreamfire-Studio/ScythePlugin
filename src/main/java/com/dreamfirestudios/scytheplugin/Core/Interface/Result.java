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
package com.dreamfirestudios.scytheplugin.Core.Interface;

/**
 * /// <summary>
 * Minimal algebraic result type representing either a successful value ({@link Ok})
 * or an error message ({@link Err}).
 * /// </summary>
 * /// <remarks>
 * This sealed interface is intended for simple success/failure flows without exceptions.
 * It is binary and lightweight, and pairs well with command/validation pipelines.
 * /// </remarks>
 * /// <example>
 * <code>
 * Result&lt;Integer&gt; r = new Result.Ok&lt;&gt;(42);
 * if (r instanceof Result.Ok&lt;Integer&gt; ok) {
 *     Integer v = ok.value();
 * }
 * </code>
 * /// </example>
 *
 * @param <T> Payload type when operation succeeds.
 */
public sealed interface Result<T> permits Result.Ok, Result.Err {

    /**
     * /// <summary>Successful result wrapping a non-null value.</summary>
     *
     * @param value The computed value.
     * @param <T>   Value type.
     */
    record Ok<T>(T value) implements Result<T> { }

    /**
     * /// <summary>Failed result carrying a human-readable error message.</summary>
     *
     * @param message Error description (intended for logs/UI).
     * @param <T>     Expected value type (unused for failures).
     */
    record Err<T>(String message) implements Result<T> { }
}