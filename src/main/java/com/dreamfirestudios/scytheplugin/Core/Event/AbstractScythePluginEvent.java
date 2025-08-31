/*
 * MIT License
 *
 * Copyright (c) 2025 Dreamfire Studio
 */
package com.dreamfirestudios.scytheplugin.Core.Event;

import com.dreamfirestudios.scytheplugin.Core.EventBus;
import com.dreamfirestudios.scytheplugin.Core.Log;
import com.dreamfirestudios.scytheplugin.PulseConfig.ScythePluginConfig;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * <summary>Abstract base class for all {@code ScythePlugin} events.</summary>
 * <remarks>
 * <ul>
 *   <li>Provides Bukkit {@link HandlerList} boilerplate.</li>
 *   <li>Centralizes dispatch through {@link EventBus} (main-thread safe).</li>
 *   <li>Skips firing when the system is disabled; optional debug logging.</li>
 * </ul>
 * </remarks>
 */
public abstract class AbstractScythePluginEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * <summary>Constructs a plugin event, auto-detecting async state.</summary>
     */
    protected AbstractScythePluginEvent() {
        super(!Bukkit.isPrimaryThread());
    }

    @Override
    public HandlerList getHandlers() { return HANDLERS; }

    public static HandlerList getHandlerList() { return HANDLERS; }

    /**
     * <summary>Fire this event if the system is enabled; always on the main thread.</summary>
     */
    public void fireEvent() {
        ScythePluginConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginConfig.class, cfg -> {
            final Log log = Log.of(ScythePlugin.GetScythePlugin(), cfg.debugConfig);
            if (!cfg.systemEnabled) {
                if (cfg.debugConfig) log.debug("Events", "Skipped (disabled): " + getClass().getSimpleName());
                return;
            }
            if (cfg.debugConfig) log.debug("Events", "Firing: " + getClass().getSimpleName());
            new EventBus(ScythePlugin.GetScythePlugin()).fire(this);
        });
    }
}