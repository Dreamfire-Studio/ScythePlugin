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
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

/**
 * A minimal, thread-aware event and task dispatcher for Bukkit plugins.
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li>Fire Bukkit {@link Event}s on the <strong>main server thread</strong>.</li>
 *   <li>Run user-supplied tasks on the main thread either immediately or after a delay.</li>
 * </ul>
 *
 * <h2>Threading</h2>
 * <p>
 * Event dispatch always occurs on the main thread. If the caller is already on the main thread,
 * dispatch is synchronous; otherwise the call is marshalled using {@link Scheduler#main(Runnable)}.
 * </p>
 *
 * <h2>Nullability & Contracts</h2>
 * <ul>
 *   <li>The constructor requires a non-null {@link Plugin}.</li>
 *   <li>{@link #fire(Event)} requires a non-null {@link Event} instance.</li>
 *   <li>{@link #runMain(Runnable)} and {@link #runLater(Runnable, long)} require a non-null task.</li>
 * </ul>
 *
 * <h2>Examples</h2>
 * <pre>{@code
 * EventBus bus = new EventBus(plugin);
 * bus.fire(new MyCustomEvent(...)); // safe from any thread
 * bus.runMain(() -> logger.info("Hello from main thread"));
 * bus.runLater(() -> doTick(), 20L); // ~1s later
 * }</pre>
 */
public final class EventBus {
    private final Scheduler scheduler;

    /**
     * Creates a new dispatcher bound to a specific plugin instance.
     *
     * @param plugin owning plugin (non-null)
     * @throws NullPointerException if {@code plugin} is null
     */
    public EventBus(final Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");
        this.scheduler = new Scheduler(plugin);
    }

    /**
     * Fires a Bukkit {@link Event}, ensuring dispatch on the main thread.
     * <p>
     * If called from the main thread, the event is fired immediately. Otherwise, it is posted
     * to the main thread for later execution within the same tick.
     * </p>
     *
     * @param event event to fire (non-null)
     * @throws NullPointerException if {@code event} is null
     */
    public void fire(final Event event) {
        Objects.requireNonNull(event, "event");
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getPluginManager().callEvent(event);
        } else {
            scheduler.main(() -> Bukkit.getPluginManager().callEvent(event));
        }
    }

    /**
     * Executes a task on the main thread as soon as possible.
     *
     * @param task runnable to execute (non-null)
     * @throws NullPointerException if {@code task} is null
     */
    public void runMain(final Runnable task) {
        scheduler.main(Objects.requireNonNull(task, "task"));
    }

    /**
     * Executes a task on the main thread after the specified delay.
     *
     * @param task       runnable to execute (non-null)
     * @param delayTicks delay in server ticks (20 ticks ≈ 1 second). Values &lt;= 0 are coerced to 1.
     * @throws NullPointerException if {@code task} is null
     */
    public void runLater(final Runnable task, final long delayTicks) {
        scheduler.mainLater(Objects.requireNonNull(task, "task"), delayTicks);
    }
}