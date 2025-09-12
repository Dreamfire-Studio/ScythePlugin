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
package com.dreamfirestudios.scytheplugin.Listeners;

import com.dreamfirestudios.scytheplugin.Events.ConfigReloadEvent;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

/**
 * Listener that reacts to {@link ConfigReloadEvent}.
 * <p>
 * When configs are reloaded, this listener closes all currently open
 * inventories for online players to ensure menus refresh properly.
 * </p>
 */
@PulseAutoRegister
public class ConfigReload implements Listener {

    /**
     * Handles the {@link ConfigReloadEvent}.
     *
     * @param event The fired reload event (never {@code null}).
     */
    @EventHandler
    public void onConfigReloaded(ConfigReloadEvent event) {
        Objects.requireNonNull(event, "event");
        for (var player : Bukkit.getOnlinePlayers()) {
            player.closeInventory();
        }
    }
}