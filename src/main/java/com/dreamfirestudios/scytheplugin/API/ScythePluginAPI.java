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
package com.dreamfirestudios.scytheplugin.API;

import com.dreamfirestudios.dreamconfig.DreamConfig;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.scytheplugin.Core.Services;
import com.dreamfirestudios.scytheplugin.Core.Try;
import com.dreamfirestudios.scytheplugin.Event.ScythePluginConfigReloadEvent;
import com.dreamfirestudios.scytheplugin.Event.ScythePluginConfigResetEvent;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.PulseConfig.ScythePluginConfig;
import com.dreamfirestudios.scytheplugin.PulseConfig.ScythePluginSerializableItems;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Public, static API surface for <em>ScythePlugin</em> features.
 * <p>Enables/disables the system, serializes items into config, and resets/reloads
 * configs with proper main-thread dispatch and plugin events.</p>
 */
public final class ScythePluginAPI {

    private ScythePluginAPI() { }

    /**
     * Set the system enabled flag and persist the config; success callback runs on main thread.
     *
     * @param onSuccess callback invoked with the updated config after save (non-null)
     * @param state     desired enabled state
     */
    public static void ScythePluginEnableSystem(final Consumer<ScythePluginConfig> onSuccess, final boolean state) {
        Objects.requireNonNull(onSuccess, "onSuccess");
        ScythePluginConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginConfig.class, config -> {
                    config.systemEnabled = state;
                    config.SaveDreamConfig(ScythePlugin.GetScythePlugin(), ignored -> Services.scheduler().main(() -> onSuccess.accept(config)));
                }
        );
    }

    /**
     * Toggle the system enabled flag and persist the config; success callback runs on main thread.
     *
     * @param onSuccess callback invoked with the updated config after save (non-null)
     */
    public static void ScythePluginEnableSystem(final Consumer<ScythePluginConfig> onSuccess) {
        Objects.requireNonNull(onSuccess, "onSuccess");
        ScythePluginConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginConfig.class, config -> {
            config.systemEnabled = !config.systemEnabled;
            config.SaveDreamConfig(ScythePlugin.GetScythePlugin(), ignored -> Services.scheduler().main(() -> onSuccess.accept(config)));
        });
    }

    /**
     * Serialize and store an {@link ItemStack} under an ID, then persist and callback on main thread.
     *
     * @param onSuccess callback invoked with the serializable-items config after save (non-null)
     * @param id        key to store the item under (non-null)
     * @param itemStack item to store (non-null)
     */
    public static void ScythePluginSerializeItem(final Consumer<ScythePluginSerializableItems> onSuccess, final String id, final ItemStack itemStack) {
        Objects.requireNonNull(onSuccess, "onSuccess");
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(itemStack, "itemStack");
        ScythePluginSerializableItems.ReturnStaticAsync(
                ScythePlugin.GetScythePlugin(),
                ScythePluginSerializableItems.class,
                cfg -> {
                    cfg.AddItemStack(id, itemStack);
                    cfg.SaveDreamConfig(ScythePlugin.GetScythePlugin(), ignored -> Services.scheduler().main(() -> onSuccess.accept(cfg)));
                }
        );
    }

    /**
     * Reset configs (fresh registration) and fire {@link ScythePluginConfigResetEvent}, no-op if disabled.
     *
     * @param settings message formatting/settings to pass through registration
     */
    public static void ScythePluginResetConfigs(final DreamMessageSettings settings) {
        Objects.requireNonNull(settings, "settings");
        ScythePluginConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginConfig.class, config -> {
            if (!config.systemEnabled) return;

            Services.scheduler().main(() -> {
                Try.runWithRetry("RegisterStatic(reset)", 3, Duration.ofMillis(50), () ->
                        DreamConfig.GetDreamConfig().RegisterStatic(ScythePlugin.GetScythePlugin(), true, settings));

                new ScythePluginConfigResetEvent().callEvent();
            });
        });
    }

    /**
     * Reload configs (non-destructive) and fire {@link ScythePluginConfigReloadEvent}, no-op if disabled.
     *
     * @param settings message formatting/settings to pass through registration
     */
    public static void ScythePluginReloadConfigs(final DreamMessageSettings settings) {
        Objects.requireNonNull(settings, "settings");
        ScythePluginConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginConfig.class, cfg -> {
            if (!cfg.systemEnabled) return;

            Services.scheduler().main(() -> {
                Try.runWithRetry("RegisterStatic(reload)", 3, Duration.ofMillis(50), () ->
                        DreamConfig.GetDreamConfig().RegisterStatic(ScythePlugin.GetScythePlugin(), false, settings));

                new ScythePluginConfigReloadEvent().callEvent();
            });
        });
    }
}