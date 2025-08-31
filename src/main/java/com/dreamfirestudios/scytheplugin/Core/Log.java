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

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * /// <summary>
 * Lightweight logger facade that respects a runtime debug flag.
 * /// </summary>
 * /// <remarks>
 * Routes to the plugin's {@link Logger}. Use {@link #debug(String, String)} for
 * conditional diagnostic messages.
 * /// </remarks>
 * /// <example>
 * <code>
 * Log log = Log.of(plugin, true);
 * log.info("Starting feature X");
 * log.debug("FeatureX", "tick=123 state=INIT");
 * </code>
 * /// </example>
 */
public final class Log {
    private final JavaPlugin plugin;
    private final boolean debugEnabled;

    private Log(final JavaPlugin plugin, final boolean debugEnabled) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.debugEnabled = debugEnabled;
    }

    /**
     * /// <summary>Create a new logger facade.</summary>
     * /// <param name="plugin">Owning plugin.</param>
     * /// <param name="debugEnabled">Whether debug messages should be emitted.</param>
     * /// <returns>Configured {@link Log} instance.</returns>
     */
    public static Log of(final JavaPlugin plugin, final boolean debugEnabled) {
        return new Log(plugin, debugEnabled);
    }

    /**
     * /// <summary>Log an informational message.</summary>
     * /// <param name="msg">Message text.</param>
     */
    public void info(final String msg) {
        plugin.getLogger().info(String.valueOf(msg));
    }

    /**
     * /// <summary>Log a warning message.</summary>
     * /// <param name="msg">Message text.</param>
     */
    public void warn(final String msg) {
        plugin.getLogger().warning(String.valueOf(msg));
    }

    /**
     * /// <summary>Log an error message.</summary>
     * /// <param name="msg">Message text.</param>
     */
    public void error(final String msg) {
        plugin.getLogger().severe(String.valueOf(msg));
    }

    /**
     * /// <summary>
     * Emit a debug line if debug is enabled.
     * /// </summary>
     * /// <param name="featureTag">Short tag (e.g., feature or subsystem name).</param>
     * /// <param name="msg">Debug text.</param>
     * /// <remarks>
     * Uses INFO level with a bracketed tag: <c>[Feature] message</c>.
     * /// </remarks>
     */
    public void debug(final String featureTag, final String msg) {
        if (debugEnabled) {
            plugin.getLogger().info("[" + String.valueOf(featureTag) + "] " + String.valueOf(msg));
        }
    }
}