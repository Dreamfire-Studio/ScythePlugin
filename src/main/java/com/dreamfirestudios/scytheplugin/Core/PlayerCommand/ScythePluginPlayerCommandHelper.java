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
package com.dreamfirestudios.scytheplugin.Core.PlayerCommand;

import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginPermissions;
import com.dreamfirestudios.scytheplugin.PulseConfig.ScythePluginConfig;
import com.dreamfirestudios.scytheplugin.PulseConfig.ScythePluginPermissionsConfigs;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Small utility for PlayerCommand workflows:
 * <ul>
 *   <li>Check if the system is enabled</li>
 *   <li>Check if a player has a permission</li>
 *   <li>Check both: system enabled AND player has permission</li>
 * </ul>
 *
 * All checks use the plugin’s async config accessors and invoke the provided callback
 * only when the check passes. If a check fails, the callback is not invoked.
 */
public final class ScythePluginPlayerCommandHelper {

    private ScythePluginPlayerCommandHelper() { }

    /**
     * Resolves the current config and invokes {@code onEnabled} only when {@code systemEnabled == true}.
     *
     * @param onEnabled callback to run if enabled (non-null)
     */
    public static void checkSystemEnabled(final Runnable onEnabled) {
        Objects.requireNonNull(onEnabled, "onEnabled");
        ScythePluginConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginConfig.class, cfg -> {
                    if (cfg.systemEnabled) onEnabled.run();
                }
        );
    }

    /**
     * Checks whether the player has the given permission at any known level.
     * If {@code sendError} is true, the permission layer will send the enum’s error message.
     *
     * @param perm       permission key (non-null)
     * @param player     player (non-null)
     * @param sendError  whether to send the error message on failure
     * @param settings   chat settings (non-null)
     * @param onAllowed  callback when the player is authorized (non-null)
     */
    public static void checkPermission(final ScythePluginPermissions perm, final Player player, final boolean sendError, final DreamMessageSettings settings, final Runnable onAllowed) {
        Objects.requireNonNull(perm, "perm");
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(settings, "settings");
        Objects.requireNonNull(onAllowed, "onAllowed");
        ScythePluginPermissionsConfigs.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginPermissionsConfigs.class, cfg -> {
                    if (cfg.DoesPlayerHavePermission(perm, player, sendError, settings)) {
                        onAllowed.run();
                    }
                }
        );
    }

    /**
     * Runs {@code onOk} only if the system is enabled AND the player has the given permission.
     * Order of evaluation matches legacy behavior: first enabled, then permission.
     *
     * @param perm       permission key (non-null)
     * @param player     player (non-null)
     * @param sendError  whether to send the error message on permission failure
     * @param settings   chat settings (non-null)
     * @param onOk       callback when both checks pass (non-null)
     */
    public static void checkSystemEnabledAndPermission(final ScythePluginPermissions perm, final Player player, final boolean sendError, final DreamMessageSettings settings, final Runnable onOk) {
        Objects.requireNonNull(perm, "perm");
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(settings, "settings");
        Objects.requireNonNull(onOk, "onOk");
        ScythePluginConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginConfig.class, cfg -> {
                    if (!cfg.systemEnabled) return;
                    ScythePluginPermissionsConfigs.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginPermissionsConfigs.class, permsCfg -> {
                                if (permsCfg.DoesPlayerHavePermission(perm, player, sendError, settings)) {
                                    onOk.run();
                                }
                            }
                    );
                }
        );
    }
}