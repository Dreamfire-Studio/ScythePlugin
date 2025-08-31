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

import com.dreamfirestudios.dreamconfig.Abstract.StaticEnumPulseConfig;
import com.dreamfirestudios.dreamconfig.Interface.ConfigVersion;
import com.dreamfirestudios.dreamcore.DreamChat.DreamChat;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageFormatter;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.scytheplugin.Core.Services;
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginMessages;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Localized/templated message configuration with main-thread-safe delivery.
 */
@PulseAutoRegister
@ConfigVersion(1)
public final class ScythePluginMessagesConfig
        extends StaticEnumPulseConfig<ScythePluginMessagesConfig, ScythePluginMessages, String> {

    @Override public JavaPlugin mainClass() { return ScythePlugin.GetScythePlugin(); }
    @Override protected Class<ScythePluginMessages> getKeyClass() { return ScythePluginMessages.class; }
    @Override protected Class<String> getValueClass() { return String.class; }
    @Override protected String getDefaultValueFor(final ScythePluginMessages key) { return key.GetTemplate(); }
    @Override public boolean useSubFolder() { return false; }

    private static Object[] withPlugin(final Object... args) {
        final String pluginName = ScythePlugin.class.getSimpleName();
        final int baseLen = (args == null ? 0 : args.length);
        final Object[] full = new Object[baseLen + 1];
        full[0] = pluginName;
        if (baseLen > 0) System.arraycopy(args, 0, full, 1, baseLen);
        return full;
    }

    private static String formatPlain(final String template, final DreamMessageSettings settings, final Object... fmtArgs) {
        Objects.requireNonNull(template, "template");
        Objects.requireNonNull(settings, "settings");
        final var comp = DreamMessageFormatter.format(String.format(template, fmtArgs), settings);
        return PlainTextComponentSerializer.plainText().serialize(comp);
    }

    private static String formatPlain(final String template, final Player p, final DreamMessageSettings settings, final Object... fmtArgs) {
        Objects.requireNonNull(template, "template");
        Objects.requireNonNull(settings, "settings");
        final var comp = DreamMessageFormatter.format(String.format(template, fmtArgs), p, settings);
        return PlainTextComponentSerializer.plainText().serialize(comp);
    }

    public void SendMessageToBroadcast(final ScythePluginMessages msg, final DreamMessageSettings settings, final Object... args){
        Objects.requireNonNull(msg, "msg");
        Objects.requireNonNull(settings, "settings");
        final var template = getDefaultValueFor(msg);
        if (template == null || template.isEmpty()) return;

        Services.scheduler().main(() ->
                DreamChat.BroadcastMessage(formatPlain(template, settings, withPlugin(args)), settings));
    }

    public void SendMessageToPlayerPermission(final ScythePluginMessages msg,
                                              final com.dreamfirestudios.scytheplugin.Enum.ScythePluginPermissions perm,
                                              final DreamMessageSettings settings, final Object... args) {
        Objects.requireNonNull(msg, "msg");
        Objects.requireNonNull(perm, "perm");
        Objects.requireNonNull(settings, "settings");

        Services.scheduler().main(() -> {
            for (final var player : Bukkit.getOnlinePlayers()) {
                ScythePluginPermissionsConfigs.ReturnStaticAsync(ScythePlugin.GetScythePlugin(),
                        ScythePluginPermissionsConfigs.class, cfg -> {
                            if (cfg.DoesPlayerHavePermission(perm, player, false, settings)) {
                                SendMessageToPlayer(msg, player, settings, args);
                            }
                        });
            }
        });
    }

    public void SendMessageToPlayer(final ScythePluginMessages msg, final Player player,
                                    final DreamMessageSettings settings, final Object... args){
        Objects.requireNonNull(msg, "msg");
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(settings, "settings");
        final var template = getDefaultValueFor(msg);
        if (template == null || template.isEmpty()) return;

        Services.scheduler().main(() ->
                DreamChat.SendMessageToPlayer(player,
                        formatPlain(template, player, settings, withPlugin(args)), settings));
    }

    public void SendMessageToContext(final ScythePluginMessages msg, final Player player,
                                     final ConversationContext ctx, final DreamMessageSettings settings, final Object... args) {
        Objects.requireNonNull(msg, "msg");
        Objects.requireNonNull(ctx, "ctx");
        Objects.requireNonNull(settings, "settings");
        final var template = getDefaultValueFor(msg);
        if (template == null || template.isEmpty()) return;

        Services.scheduler().main(() ->
                ctx.getForWhom().sendRawMessage(
                        formatPlain(template, player, settings, withPlugin(args))));
    }

    public void SendMessageToConsole(final ScythePluginMessages msg, final DreamMessageSettings settings, final Object... args){
        Objects.requireNonNull(msg, "msg");
        Objects.requireNonNull(settings, "settings");
        final var template = getDefaultValueFor(msg);
        if (template == null || template.isEmpty()) return;

        Services.scheduler().main(() ->
                DreamChat.SendMessageToConsole(
                        formatPlain(template, settings, withPlugin(args)), settings));
    }
}