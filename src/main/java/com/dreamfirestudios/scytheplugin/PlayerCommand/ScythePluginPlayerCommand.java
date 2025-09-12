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

package com.dreamfirestudios.scytheplugin.PlayerCommand;

import com.dreamfirestudios.dreamcommand.Annotations.PCMethod;
import com.dreamfirestudios.dreamcommand.Annotations.PCTab;
import com.dreamfirestudios.dreamcommand.Enums.TabType;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.scytheplugin.API.ScythePluginAPI;
import com.dreamfirestudios.scytheplugin.Core.PlayerCommand.ScythePluginPlayerCommandHelper;
import com.dreamfirestudios.scytheplugin.Core.Services;
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginMessages;
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginPermissions;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.PulseConfig.ScythePluginMessagesConfig;
import com.dreamfirestudios.scytheplugin.SmartInvs.ScythePluginCoreMenu;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Player command entrypoint for {@code ScythePlugin}.
 *
 * Responsibilities:
 *  - Expose admin GUI and operational commands to players.
 *  - Use PlayerCommandHelper for consistent enable/permission checks.
 *  - Marshal UI/feedback to the main thread via Services.scheduler().
 */
@PulseAutoRegister
public final class ScythePluginPlayerCommand {

    public static final String COMMAND_NAME = "scytheplugin";
    public static final String[] COMMAND_ALIASES = {};
    public static final boolean COMMAND_DEBUG = false;

    public ScythePluginPlayerCommand() { }

    /** Open admin GUI (permission-only). */
    @PCMethod({})
    public void ScythePluginMethod(final Player player) {
        Objects.requireNonNull(player, "player");
        ScythePluginPlayerCommandHelper.checkPermission(
                ScythePluginPermissions.AdminConsole, player, true, DreamMessageSettings.all(),
                () -> Services.scheduler().main(() -> new ScythePluginCoreMenu(player))
        );
    }

    /** Enable/disable system (permission-only). */
    @PCMethod({"enable"})
    public void ScythePluginEnableMethod(final Player player, final boolean state) {
        Objects.requireNonNull(player, "player");
        ScythePluginPlayerCommandHelper.checkPermission(
                ScythePluginPermissions.EnableSystem, player, true, DreamMessageSettings.all(),
                () -> ScythePluginAPI.ScythePluginEnableSystem(cfg -> {
                    ScythePluginMessagesConfig.ReturnStaticAsync(
                            ScythePlugin.GetScythePlugin(),
                            ScythePluginMessagesConfig.class,
                            messageConfig -> Services.scheduler().main(() ->
                                    messageConfig.SendMessageToPlayer(
                                            state ? ScythePluginMessages.ConsoleEnabledSystem
                                                    : ScythePluginMessages.ConsoleDisableSystem,
                                            player, DreamMessageSettings.all()))
                    );
                }, state)
        );
    }

    /** Serialize main-hand item (permission-only). */
    @PCMethod({"serialize"})
    @PCTab(pos = 1, type = TabType.PureData, data = "ITEM ID")
    public void ScythePluginSerializeItemMethod(final Player player, final String itemName) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(itemName, "itemName");

        ScythePluginPlayerCommandHelper.checkPermission(
                ScythePluginPermissions.SerializeItem, player, true, DreamMessageSettings.all(),
                () -> ScythePluginAPI.ScythePluginSerializeItem(x -> {
                    ScythePluginMessagesConfig.ReturnStaticAsync(
                            ScythePlugin.GetScythePlugin(),
                            ScythePluginMessagesConfig.class,
                            messageConfig -> Services.scheduler().main(() ->
                                    messageConfig.SendMessageToPlayer(
                                            ScythePluginMessages.PlayerSerializedItem,
                                            player, DreamMessageSettings.all(), itemName))
                    );
                }, itemName, player.getInventory().getItemInMainHand())
        );
    }

    /** Reset configs (requires system enabled + permission). */
    @PCMethod({"configs", "reset"})
    public void ScythePluginConfigsResetMethod(final Player player) {
        Objects.requireNonNull(player, "player");
        ScythePluginPlayerCommandHelper.checkSystemEnabledAndPermission(
                ScythePluginPermissions.ResetConfigs, player, true, DreamMessageSettings.all(),
                () -> {
                    ScythePluginAPI.ScythePluginResetConfigs(DreamMessageSettings.all());
                    ScythePluginMessagesConfig.ReturnStaticAsync(
                            ScythePlugin.GetScythePlugin(),
                            ScythePluginMessagesConfig.class,
                            messageConfig -> Services.scheduler().main(() ->
                                    messageConfig.SendMessageToPlayer(
                                            ScythePluginMessages.PlayerResetConfig,
                                            player, DreamMessageSettings.all()))
                    );
                });
    }

    /** Reload configs (requires system enabled + permission). */
    @PCMethod({"configs", "reload"})
    public void ScythePluginReloadMethod(final Player player) {
        Objects.requireNonNull(player, "player");
        ScythePluginPlayerCommandHelper.checkSystemEnabledAndPermission(
                ScythePluginPermissions.ReloadConfigs, player, true, DreamMessageSettings.all(),
                () -> {
                    ScythePluginAPI.ScythePluginReloadConfigs(DreamMessageSettings.all());
                    ScythePluginMessagesConfig.ReturnStaticAsync(
                            ScythePlugin.GetScythePlugin(),
                            ScythePluginMessagesConfig.class,
                            messageConfig -> Services.scheduler().main(() ->
                                    messageConfig.SendMessageToPlayer(
                                            ScythePluginMessages.PlayerReloadedConfig,
                                            player, DreamMessageSettings.all()))
                    );
                });
    }
}