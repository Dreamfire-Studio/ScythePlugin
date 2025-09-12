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

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

/**
 * <summary>
 * Task scheduling helper for Bukkit plugins.
 * </summary>
 * <remarks>
 * <ul>
 *   <li>Provides safe scheduling on the main thread or asynchronously.</li>
 *   <li>Supports delayed and repeating tasks.</li>
 *   <li>Ensures delays and periods are at least 1 tick.</li>
 * </ul>
 * </remarks>
 */
public final class Scheduler {
    private final Plugin plugin;

    /**
     * <summary>
     * Creates a new scheduler bound to the given plugin.
     * </summary>
     * <param name="plugin">The plugin responsible for scheduling tasks.</param>
     */
    public Scheduler(final Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
    }

    /**
     * <summary>
     * Runs a task immediately on the main thread.
     * </summary>
     * <param name="task">The task to run.</param>
     */
    public void main(final Runnable task) {
        Objects.requireNonNull(task, "task");
        if (Bukkit.isPrimaryThread()) task.run();
        else Bukkit.getScheduler().runTask(plugin, task);
    }

    /**
     * <summary>
     * Runs a task later on the main thread.
     * </summary>
     * <param name="task">The task to run.</param>
     * <param name="delayTicks">Delay in ticks before execution (minimum 1).</param>
     */
    public void mainLater(final Runnable task, final long delayTicks) {
        Objects.requireNonNull(task, "task");
        final long delay = Math.max(1L, delayTicks);
        Bukkit.getScheduler().runTaskLater(plugin, task, delay);
    }

    /**
     * <summary>
     * Runs a task asynchronously.
     * </summary>
     * <param name="task">The task to run.</param>
     */
    public void async(final Runnable task) {
        Objects.requireNonNull(task, "task");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    /**
     * <summary>
     * Runs a task asynchronously after a delay.
     * </summary>
     * <param name="task">The task to run.</param>
     * <param name="delayTicks">Delay in ticks before execution (minimum 1).</param>
     */
    public void asyncLater(final Runnable task, final long delayTicks) {
        Objects.requireNonNull(task, "task");
        final long delay = Math.max(1L, delayTicks);
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
    }

    /**
     * <summary>
     * Runs a task repeatedly on the main thread.
     * </summary>
     * <param name="task">The task to run.</param>
     * <param name="delayTicks">Initial delay in ticks before first run (minimum 1).</param>
     * <param name="periodTicks">Period in ticks between runs (minimum 1).</param>
     * <returns>The Bukkit task ID for cancellation.</returns>
     */
    public int repeatMain(final Runnable task, final long delayTicks, final long periodTicks) {
        Objects.requireNonNull(task, "task");
        final long delay = Math.max(1L, delayTicks);
        final long period = Math.max(1L, periodTicks);
        return Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period).getTaskId();
    }

    /**
     * <summary>
     * Runs a task repeatedly asynchronously.
     * </summary>
     * <param name="task">The task to run.</param>
     * <param name="delayTicks">Initial delay in ticks before first run (minimum 1).</param>
     * <param name="periodTicks">Period in ticks between runs (minimum 1).</param>
     * <returns>The Bukkit task ID for cancellation.</returns>
     */
    public int repeatAsync(final Runnable task, final long delayTicks, final long periodTicks) {
        Objects.requireNonNull(task, "task");
        final long delay = Math.max(1L, delayTicks);
        final long period = Math.max(1L, periodTicks);
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period).getTaskId();
    }
}