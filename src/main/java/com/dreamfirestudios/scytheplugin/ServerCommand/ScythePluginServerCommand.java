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
package com.dreamfirestudios.scytheplugin.ServerCommand;

import com.dreamfirestudios.dreamcommand.Annotations.PCMethod;
import com.dreamfirestudios.dreamcommand.Annotations.PCOP;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.scytheplugin.API.ScythePluginAPI;
import com.dreamfirestudios.scytheplugin.Core.Services;
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginMessages;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.PulseConfig.ScythePluginConfig;
import com.dreamfirestudios.scytheplugin.PulseConfig.ScythePluginMessagesConfig;
import org.bukkit.command.CommandSender;

import java.util.Objects;

/**
 * Console/server command endpoints (no player context).
 */
@PulseAutoRegister
public final class ScythePluginServerCommand {

    public static final String COMMAND_NAME = "scytheplugin_server";
    public static final String[] COMMAND_ALIASES = {};
    public static final boolean COMMAND_DEBUG = false;

    public ScythePluginServerCommand() { }

    /** Report status to console. No-op when system disabled. */
    @PCMethod({"status"})
    @PCOP
    public void ScythePluginMethod(final CommandSender sender) {
        Objects.requireNonNull(sender, "sender");
        ScythePluginConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            ScythePluginMessagesConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginMessagesConfig.class, messageConfig ->
                    Services.scheduler().main(() ->
                            messageConfig.SendMessageToConsole(ScythePluginMessages.SystemIsntEnabled, DreamMessageSettings.all()))
            );
        });
    }

    /** Enable/disable system via console. */
    @PCMethod({"enable"})
    @PCOP
    public void ScythePluginEnableMethod(final CommandSender sender, final boolean state) {
        Objects.requireNonNull(sender, "sender");
        ScythePluginAPI.ScythePluginEnableSystem(x -> {
            ScythePluginMessagesConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginMessagesConfig.class, messagesConfig ->
                    Services.scheduler().main(() ->
                            messagesConfig.SendMessageToConsole(
                                    state ? ScythePluginMessages.ConsoleEnabledSystem : ScythePluginMessages.ConsoleDisableSystem,
                                    DreamMessageSettings.all()))
            );
        }, state);
    }

    /** Reset all plugin configs. No-op when system disabled. */
    @PCMethod({"configs", "reset"})
    @PCOP
    public void ScythePluginConfigsResetMethod(final CommandSender sender) {
        Objects.requireNonNull(sender, "sender");
        ScythePluginConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            ScythePluginAPI.ScythePluginResetConfigs(DreamMessageSettings.all());
            ScythePluginMessagesConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginMessagesConfig.class, messageConfig ->
                    Services.scheduler().main(() ->
                            messageConfig.SendMessageToConsole(ScythePluginMessages.PlayerResetConfig, DreamMessageSettings.all()))
            );
        });
    }

    /** Reload all plugin configs. No-op when system disabled. */
    @PCMethod({"configs", "reload"})
    @PCOP
    public void ScythePluginConfigsReloadMethod(final CommandSender sender) {
        Objects.requireNonNull(sender, "sender");
        ScythePluginConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginConfig.class, config -> {
            if (!config.systemEnabled) return;
            ScythePluginAPI.ScythePluginReloadConfigs(DreamMessageSettings.all());
            ScythePluginMessagesConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginMessagesConfig.class, messageConfig ->
                    Services.scheduler().main(() ->
                            messageConfig.SendMessageToConsole(ScythePluginMessages.PlayerReloadedConfig, DreamMessageSettings.all()))
            );
        });
    }
}