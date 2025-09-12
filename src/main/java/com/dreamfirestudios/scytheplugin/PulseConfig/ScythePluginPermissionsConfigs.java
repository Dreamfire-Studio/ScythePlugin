/*
 * MIT License
 *
 * Copyright (c) 2025 Dreamfire Studio
 */
package com.dreamfirestudios.scytheplugin.PulseConfig;

import com.dreamfirestudios.dreamconfig.Abstract.StaticEnumPulseConfig;
import com.dreamfirestudios.dreamconfig.Interface.ConfigVersion;
import com.dreamfirestudios.dreamcore.DreamChat.DreamChat;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.dreamcore.DreamLuckPerms.DreamLuckPerms;
import com.dreamfirestudios.scytheplugin.Core.ExpiringCache;
import com.dreamfirestudios.scytheplugin.Core.Services;
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginPermissionLevel;
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginPermissions;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.Util.PermissionStrings;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.Objects;

/**
 * <summary>Permission string resolution and checks with lightweight caching + deny cooldown.</summary>
 */
@PulseAutoRegister
@ConfigVersion(1)
public final class ScythePluginPermissionsConfigs extends StaticEnumPulseConfig<ScythePluginPermissionsConfigs, ScythePluginPermissions, String> {

    /** Small cache of (player+perm-combo) -> allowed for 5s to reduce LP lookups. */
    private final ExpiringCache<String, Boolean> permCache =
            Services.expiringCache("__scythe_perm_cache", Duration.ofSeconds(5));

    /** Per (player,perm) deny cooldown ≈ 2 messages/second. */
    private final ExpiringCache<String, Boolean> denyCooldown =
            Services.expiringCache("__scythe_perm_deny_cooldown", Duration.ofMillis(500));

    @Override public JavaPlugin mainClass() { return ScythePlugin.GetScythePlugin(); }
    @Override protected Class<ScythePluginPermissions> getKeyClass() { return ScythePluginPermissions.class; }
    @Override protected Class<String> getValueClass() { return String.class; }

    @Override
    protected String getDefaultValueFor(final ScythePluginPermissions key) {
        Objects.requireNonNull(key, "key");
        return key.getPermissionFormat();
    }

    @Override public boolean useSubFolder() { return false; }

    /**
     * <summary>
     * Resolve permission node(s) and determine if a player is authorized (Admin or Player level).
     * </summary>
     * <param name="perm">Permission enum key.</param>
     * <param name="player">Player to check.</param>
     * <param name="sendError">Whether to send the enum's error message when unauthorized.</param>
     * <param name="settings">Chat settings.</param>
     * <returns>true if allowed; false otherwise.</returns>
     */
    public boolean DoesPlayerHavePermission(final ScythePluginPermissions perm, final Player player, final boolean sendError, final DreamMessageSettings settings) {
        Objects.requireNonNull(perm, "perm");
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(settings, "settings");

        final String format = getDefaultValueFor(perm);
        final String pluginName = ScythePlugin.class.getSimpleName();

        final String adminPerm  = PermissionStrings.resolve(format, pluginName, ScythePluginPermissionLevel.Admin);
        final String playerPerm = PermissionStrings.resolve(format, pluginName, ScythePluginPermissionLevel.Player);

        final String cacheKey = player.getUniqueId() + "|" + adminPerm + "|" + playerPerm;

        final boolean allowed = permCache.getOrCompute(cacheKey, () -> {
            final var user = DreamLuckPerms.getUser(player);
            return DreamLuckPerms.hasPermission(user, adminPerm) || DreamLuckPerms.hasPermission(user, playerPerm);
        });

        if (!allowed && sendError) {
            final String denyKey = player.getUniqueId() + "|" + perm.name();
            if (denyCooldown.get(denyKey).isEmpty()) {
                DreamChat.SendMessageToPlayer(player, perm.GetError(), settings);
                denyCooldown.put(denyKey, Boolean.TRUE);
            }
        }

        return allowed;
    }
}