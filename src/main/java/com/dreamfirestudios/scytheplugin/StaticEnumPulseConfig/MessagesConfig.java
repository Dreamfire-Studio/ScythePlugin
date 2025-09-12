package com.dreamfirestudios.scytheplugin.StaticEnumPulseConfig;

import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.Enum.SP_Permissions;
import com.dreamfirestudios.dreamconfig.Abstract.StaticEnumPulseConfig;
import com.dreamfirestudios.dreamconfig.Interface.ConfigVersion;
import com.dreamfirestudios.dreamcore.DreamChat.DreamChat;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageFormatter;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.dreamcore.DreamLuckPerms.DreamLuckPerms;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Static config binding {@link com.dreamfirestudios.scytheplugin.Enum.SP_Messages} keys to String templates.
 *
 * <p>Provides utilities to send messages to players, the console, or
 * groups of players filtered by world, permissions, or OP status.</p>
 */
@PulseAutoRegister
@ConfigVersion(1)
public class MessagesConfig extends StaticEnumPulseConfig<MessagesConfig, com.dreamfirestudios.scytheplugin.Enum.SP_Messages, String> {

    /**
     * Asynchronously loads the MessagesConfig.
     *
     * @param onSuccess callback that receives the loaded config (never null)
     */
    public static void GetMessagesConfig(Consumer<MessagesConfig> onSuccess){
        Objects.requireNonNull(onSuccess, "onSuccess");
        PermissionsConfig.ReturnStaticAsync(ScythePlugin.GetInstance(), MessagesConfig.class, onSuccess);
    }

    /** @return the plugin main class */
    @Override public JavaPlugin mainClass() { return ScythePlugin.GetInstance(); }

    /** @return the enum key type ({@link com.dreamfirestudios.scytheplugin.Enum.SP_Messages}) */
    @Override protected Class<com.dreamfirestudios.scytheplugin.Enum.SP_Messages> getKeyClass() { return com.dreamfirestudios.scytheplugin.Enum.SP_Messages.class; }

    /** @return the value type ({@link String}) */
    @Override protected Class<String> getValueClass() { return String.class; }

    /**
     * Provides the default value for a given message key.
     *
     * @param key the enum key (never null)
     * @return the default message template string
     */
    @Override
    protected String getDefaultValueFor(final com.dreamfirestudios.scytheplugin.Enum.SP_Messages key) {
        Objects.requireNonNull(key, "key");
        return key.GetMessage();
    }

    /** @return false, config does not use subfolders */
    @Override public boolean useSubFolder() { return false; }

    /**
     * Sends a message to all online players, excluding some.
     *
     * @param msg message key (never null)
     * @param settings formatting settings (never null)
     * @param exclude players to skip (never null)
     */
    public void SendToAllPlayers(final com.dreamfirestudios.scytheplugin.Enum.SP_Messages msg, final DreamMessageSettings settings, List<UUID> exclude){
        Objects.requireNonNull(msg, "msg");
        Objects.requireNonNull(settings, "settings");
        Objects.requireNonNull(exclude, "exclude");
        for (var player : Bukkit.getOnlinePlayers()) {
            if (exclude.contains(player.getUniqueId())) continue;
            SendToSinglePlayer(msg, player, settings);
        }
    }

    // ... Similar Javadoc for each method (world filters, permission filters, OP filter) ...

    /**
     * Sends a message to a single player.
     *
     * @param msg message key (never null)
     * @param player target player (never null)
     * @param settings formatting settings (never null)
     */
    public void SendToSinglePlayer(final com.dreamfirestudios.scytheplugin.Enum.SP_Messages msg, final Player player, final DreamMessageSettings settings){
        Objects.requireNonNull(msg, "msg");
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(settings, "settings");
        var message = saveableHashmap.getHashMap().getOrDefault(msg, getDefaultValueFor(msg));
        Bukkit.getScheduler().runTask(ScythePlugin.GetInstance(), () ->
                DreamChat.SendMessageToPlayer(player, message, settings)
        );
    }

    /**
     * Sends a message into a {@link ConversationContext}.
     */
    public void SendMessageToContext(final com.dreamfirestudios.scytheplugin.Enum.SP_Messages msg, final Player player, final ConversationContext ctx, final DreamMessageSettings settings) {
        Objects.requireNonNull(msg, "msg");
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(ctx, "ctx");
        Objects.requireNonNull(settings, "settings");
        var message = saveableHashmap.getHashMap().getOrDefault(msg, getDefaultValueFor(msg));
        var formattedMessage = DreamMessageFormatter.format(message, player, settings);
        var plainMessage = PlainTextComponentSerializer.plainText().serialize(formattedMessage);
        Bukkit.getScheduler().runTask(ScythePlugin.GetInstance(), () ->
                ctx.getForWhom().sendRawMessage(plainMessage)
        );
    }

    /**
     * Sends a message to the server console.
     */
    public void SendToConsole(final com.dreamfirestudios.scytheplugin.Enum.SP_Messages msg, final DreamMessageSettings settings){
        Objects.requireNonNull(msg, "msg");
        Objects.requireNonNull(settings, "settings");
        var message = saveableHashmap.getHashMap().getOrDefault(msg, getDefaultValueFor(msg));
        Bukkit.getScheduler().runTask(ScythePlugin.GetInstance(), () ->
                DreamChat.SendMessageToConsole(message, settings)
        );
    }
}