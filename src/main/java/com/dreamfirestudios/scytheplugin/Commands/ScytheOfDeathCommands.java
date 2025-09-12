/*
 * MIT License
 * Copyright (c) 2025 Dreamfire Studio
 */

package com.dreamfirestudios.scytheplugin.Commands;

import com.dreamfirestudios.dreamcore.DreamItems.DreamItemStacksAPI;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.dreamcommand.Annotations.PCMethod;
import com.dreamfirestudios.dreamcommand.Annotations.PCTab;
import com.dreamfirestudios.dreamcommand.Enums.TabType;
import com.dreamfirestudios.scytheplugin.API.ScytheOfDeathAPI;
import com.dreamfirestudios.scytheplugin.Enum.SP_Messages;
import com.dreamfirestudios.scytheplugin.Enum.SP_Permissions;
import com.dreamfirestudios.scytheplugin.IDreamItemStack.ScytheOfDeath;
import com.dreamfirestudios.scytheplugin.StaticEnumPulseConfig.MessagesConfig;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.scytheplugin.StaticEnumPulseConfig.PermissionsConfig;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

@PulseAutoRegister
public final class ScytheOfDeathCommands {

    public static final String COMMAND_NAME = "scythe";
    public static final String[] COMMAND_ALIASES = {};
    public static final boolean COMMAND_DEBUG = false;

    @PCMethod({"get"})
    public void GetScytheSelf(final Player player) {
        Objects.requireNonNull(player, "player");
        PermissionsConfig.DoesPlayerHavePermission(player, SP_Permissions.GiveScythe, true, permissionsConfig -> {
            ScytheOfDeathAPI.giveScythe(player);
            MessagesConfig.GetMessagesConfig(messagesConfig -> messagesConfig.SendToSinglePlayer(com.dreamfirestudios.scytheplugin.Enum.SP_Messages.PlayerReceivedScythe, player, DreamMessageSettings.all()));
            player.playSound(player.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1f, 0.6f);
        });
    }

    @PCMethod({"give"})
    public void GiveScythe(final Player sender, final String targetName) {
        Objects.requireNonNull(sender, "sender");
        Objects.requireNonNull(targetName, "targetName");
        PermissionsConfig.DoesPlayerHavePermission(sender, SP_Permissions.GiveScythe, true, permissionsConfig -> {
            Player target = Bukkit.getPlayerExact(targetName);
            if (target == null) {
                sender.sendMessage("§cPlayer " + targetName + " is not online.");
                return;
            }
            ScytheOfDeathAPI.giveScythe(target);
            MessagesConfig.GetMessagesConfig(messagesConfig -> {
                messagesConfig.SendToSinglePlayer(SP_Messages.PlayerReceivedScythe, target, DreamMessageSettings.all());
                messagesConfig.SendToSinglePlayer(SP_Messages.PlayerGivenScythe, sender, DreamMessageSettings.all());
            });
            target.playSound(target.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1f, 0.6f);
            sender.sendMessage("§aYou gave the Scythe of Eternal Death to " + target.getName());
        });
    }

    @PCMethod({"give"})
    @PCTab(pos = 1, type = TabType.OnlinePlayerNames)
    public void GiveScythe(final CommandSender sender, final String targetName) {
        Objects.requireNonNull(sender, "sender");
        Objects.requireNonNull(targetName, "targetName");
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cPlayer " + targetName + " is not online.");
            return;
        }
        ScytheOfDeathAPI.giveScythe(target);
        MessagesConfig.GetMessagesConfig(messagesConfig -> {
            messagesConfig.SendToSinglePlayer(com.dreamfirestudios.scytheplugin.Enum.SP_Messages.PlayerReceivedScythe, target, DreamMessageSettings.all());
            messagesConfig.SendToConsole(com.dreamfirestudios.scytheplugin.Enum.SP_Messages.PlayerGivenScythe, DreamMessageSettings.all());
        });
        target.playSound(target.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1f, 0.6f);
        sender.sendMessage("§aYou gave the Scythe of Eternal Death to " + target.getName());
    }
}