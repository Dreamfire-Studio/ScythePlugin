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

package com.dreamfirestudios.scytheplugin.PulseConfig;

import com.dreamfirestudios.dreamconfig.Abstract.StaticPulseConfig;
import com.dreamfirestudios.dreamconfig.Interface.ConfigVersion;
import com.dreamfirestudios.dreamconfig.Interface.StorageComment;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.scytheplugin.Event.ScythePluginSystemToggleEvent;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Primary configuration for {@code ScythePlugin}.
 */
@PulseAutoRegister
@ConfigVersion(1)
public class ScythePluginConfig extends StaticPulseConfig<ScythePluginConfig> {

    @Override public JavaPlugin mainClass() { return ScythePlugin.GetScythePlugin(); }

    @StorageComment("WARNING: SYSTEM WON'T RUN IF FALSE!")
    public boolean systemEnabled = true;

    @StorageComment("Display debugs in the console logs for changes in this config!")
    public boolean debugConfig = false;

    @Override public boolean useSubFolder() { return false; }

    /**
     * Update {@link #systemEnabled} and emit a {@link ScythePluginSystemToggleEvent}.
     * Event is dispatched before persisting so listeners see the transition.
     *
     * @param onSuccess callback invoked post-save (non-null)
     * @param newState  new enabled state
     */
    public void ToggleSystemEnabled(final Consumer<ScythePluginConfig> onSuccess, final boolean newState) {
        Objects.requireNonNull(onSuccess, "onSuccess");
        final var event = new ScythePluginSystemToggleEvent(systemEnabled, newState);
        event.callEvent(); // dispatch on main thread if enabled (see Abstract event)
        systemEnabled = newState;
        SaveDreamConfig(ScythePlugin.GetScythePlugin(), onSuccess);
    }
}