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
package com.dreamfirestudios.scytheplugin.Core.Events;

import com.dreamfirestudios.scytheplugin.ScythePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Base class for custom plugin events.
 * <p>
 * Provides:
 * <ul>
 *     <li>Thread-safety for event firing (schedules on main thread if needed).</li>
 *     <li>Cancellation support via {@link Cancellable}.</li>
 *     <li>Static handler list required for Bukkit event registration.</li>
 * </ul>
 */
public abstract class AbstractEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;

    /**
     * Creates a new event, marking it asynchronous if not running on the primary thread.
     */
    protected AbstractEvent() {
        super(!Bukkit.isPrimaryThread());
    }

    /**
     * Returns the handler list for this event type.
     *
     * @return the handler list (never null)
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Static accessor for Bukkit's event registration system.
     *
     * @return the global handler list (never null)
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Whether this event has been cancelled.
     *
     * @return true if cancelled, false otherwise
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event.
     *
     * @param cancel true to cancel, false to allow
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Calls this event through the Bukkit {@link org.bukkit.plugin.PluginManager}.
     * <p>
     * If not on the main thread, the call is scheduled asynchronously.
     *
     * @return true if the event was cancelled, false otherwise
     */
    public boolean callEvent() {
        Objects.requireNonNull(ScythePlugin.GetInstance(), "Plugin instance cannot be null");
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getPluginManager().callEvent(this);
        } else {
            Bukkit.getScheduler().runTask(ScythePlugin.GetInstance(), () ->
                    Bukkit.getPluginManager().callEvent(this));
        }
        return isCancelled();
    }
}