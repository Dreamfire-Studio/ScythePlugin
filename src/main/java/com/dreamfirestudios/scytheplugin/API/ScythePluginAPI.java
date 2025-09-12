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
package com.dreamfirestudios.scytheplugin.API;

import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.Events.ConfigReloadEvent;
import com.dreamfirestudios.scytheplugin.Events.ConfigResetEvent;
import com.dreamfirestudios.scytheplugin.StaticPulseConfig.SerializableItemsConfig;
import com.dreamfirestudios.dreamconfig.DreamConfig;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Public-facing API for ScythePlugin.
 *
 * <p>Provides helpers to reload/reset configs and serialize items into the
 * {@link SerializableItemsConfig} store.</p>
 */
public final class ScythePluginAPI {

    private ScythePluginAPI() {

    }

    /**
     * Reloads or resets all plugin configs.
     *
     * <p>Fires either {@link ConfigReloadEvent} or {@link ConfigResetEvent} first.
     * If the event is cancelled, no reload/reset occurs.</p>
     *
     * @param settings     message formatting settings (not null)
     * @param resetConfigs whether to reset configs instead of reloading them
     */
    public static void ReloadConfigs(final DreamMessageSettings settings, final boolean resetConfigs) {
        Objects.requireNonNull(settings, "settings");
        var event = resetConfigs ? new ConfigResetEvent() : new ConfigReloadEvent();
        event.callEvent();
        if (event.isCancelled()) {
            return;
        }
        DreamConfig.GetDreamConfig().RegisterStatic(ScythePlugin.GetInstance(), resetConfigs, settings);
    }

    /**
     * Serializes an {@link ItemStack} into the {@link SerializableItemsConfig}
     * under the given ID.
     *
     * @param itemName the unique identifier for the item (not null or empty)
     * @param itemStack the item to serialize (not null)
     * @param response callback once the config is updated (not null)
     */
    public static void SerialiseItem(final String itemName, final ItemStack itemStack, final Consumer<SerializableItemsConfig> response) {
        Objects.requireNonNull(itemName, "itemName");
        Objects.requireNonNull(itemStack, "itemStack");
        Objects.requireNonNull(response, "response");
        SerializableItemsConfig.AddItemStack(itemName, itemStack, response);
    }
}