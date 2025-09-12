package com.dreamfirestudios.scytheplugin.StaticEnumPulseConfig;

import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.Enum.SP_Permissions;
import com.dreamfirestudios.dreamconfig.Abstract.StaticEnumPulseConfig;
import com.dreamfirestudios.dreamconfig.Interface.ConfigVersion;
import com.dreamfirestudios.dreamcore.DreamChat.DreamChat;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.dreamcore.DreamLuckPerms.DreamLuckPerms;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Static config binding {@link SP_Permissions} to their string values.
 *
 * <p>Provides utilities to check player permissions using
 * DreamLuckPerms and send error messages if missing.</p>
 */
@PulseAutoRegister
@ConfigVersion(1)
public class PermissionsConfig extends StaticEnumPulseConfig<PermissionsConfig, SP_Permissions, String> {

    /**
     * Asynchronously loads the PermissionsConfig.
     *
     * @param onSuccess callback that receives the loaded config (never null)
     */
    public static void GetPermissionsConfig(Consumer<PermissionsConfig> onSuccess){
        Objects.requireNonNull(onSuccess, "onSuccess");
        PermissionsConfig.ReturnStaticAsync(ScythePlugin.GetInstance(), PermissionsConfig.class, onSuccess);
    }

    /**
     * Checks whether a player has the given permission, then runs callback if true.
     */
    public static void DoesPlayerHavePermission(Player player, SP_Permissions permissions, boolean sendPlayerErrorMessage, Consumer<PermissionsConfig> onSuccess){
        DoesPlayerHavePermission(player, permissions, sendPlayerErrorMessage, DreamMessageSettings.all(), onSuccess);
    }

    /**
     * Checks whether a player has the given permission, optionally sending an error message.
     *
     * @param player player to check (never null)
     * @param permissions enum permission key (never null)
     * @param sendPlayerErrorMessage whether to send error to player if missing
     * @param dreamMessageSettings formatting settings (never null)
     * @param onSuccess callback run only if permission is present
     */
    public static void DoesPlayerHavePermission(Player player, SP_Permissions permissions, boolean sendPlayerErrorMessage, DreamMessageSettings dreamMessageSettings, Consumer<PermissionsConfig> onSuccess){
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(permissions, "permissions");
        Objects.requireNonNull(dreamMessageSettings, "dreamMessageSettings");
        Objects.requireNonNull(onSuccess, "onSuccess");
        PermissionsConfig.GetPermissionsConfig(permissionsConfig -> {
            if(!permissionsConfig.DoesPlayerHavePermission(permissions, sendPlayerErrorMessage, dreamMessageSettings, player)) return;
            GetPermissionsConfig(onSuccess);
        });
    }

    @Override public JavaPlugin mainClass() { return ScythePlugin.GetInstance(); }
    @Override protected Class<SP_Permissions> getKeyClass() { return SP_Permissions.class; }
    @Override protected Class<String> getValueClass() { return String.class; }

    @Override
    protected String getDefaultValueFor(final SP_Permissions key) {
        Objects.requireNonNull(key, "key");
        return key.GetPermission();
    }

    /**
     * Performs an actual permission check using LuckPerms.
     *
     * @param permissions the permission to check
     * @param sendPlayerErrorMessage whether to send error to player if missing
     * @param dreamMessageSettings formatting settings (never null)
     * @param player target player (never null)
     * @return true if player has permission
     */
    public boolean DoesPlayerHavePermission(SP_Permissions permissions, boolean sendPlayerErrorMessage, DreamMessageSettings dreamMessageSettings, Player player) {
        Objects.requireNonNull(permissions, "permissions");
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(dreamMessageSettings, "dreamMessageSettings");
        var permission = saveableHashmap.getHashMap().getOrDefault(permissions, permissions.GetPermission());
        var error = permissions.GetError();
        var playerHasPermission = DreamLuckPerms.hasPermission(DreamLuckPerms.getUser(player), permission);
        if(playerHasPermission) return true;
        if(sendPlayerErrorMessage) DreamChat.SendMessageToPlayer(player, error, dreamMessageSettings);
        return false;
    }
}