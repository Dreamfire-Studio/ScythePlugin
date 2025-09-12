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
package com.dreamfirestudios.scytheplugin.Commands;

import com.dreamfirestudios.scytheplugin.API.ScythePluginAPI;
import com.dreamfirestudios.scytheplugin.API.SmartInventoryAPI;
import com.dreamfirestudios.scytheplugin.Core.SmartInvs.MenuSlot;
import com.dreamfirestudios.scytheplugin.Enum.SP_Permissions;
import com.dreamfirestudios.scytheplugin.MenuConfig.AdminMenuConfig;
import com.dreamfirestudios.scytheplugin.MenuConfig.ConfirmResetConfig;
import com.dreamfirestudios.scytheplugin.StaticEnumPulseConfig.MessagesConfig;
import com.dreamfirestudios.scytheplugin.StaticEnumPulseConfig.PermissionsConfig;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcommand.Annotations.PCMethod;
import com.dreamfirestudios.dreamcommand.Annotations.PCTab;
import com.dreamfirestudios.dreamcommand.Enums.TabType;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

@PulseAutoRegister
public class Commands {
    public static final String COMMAND_NAME = "BCFM";
    public static final String[] COMMAND_ALIASES = {};
    public static final boolean COMMAND_DEBUG = false;

    /**
     * Opens the main Admin SmartInventory menu for a player.
     *
     * @param player the player to open the menu for (not null)
     */
    @PCMethod({})
    public void BCFMMethod(final Player player) {
        Objects.requireNonNull(player, "player");
        SmartInventoryAPI.OpenAdminMenu(player);
    }

    /**
     * Serializes the item in the player's main hand into {@code SerializableItemsConfig}.
     *
     * @param player   the player executing the command (not null)
     * @param itemName the name/ID to register the item under (not null)
     */
    @PCMethod({"items", "serialize"})
    @PCTab(pos = 1, type = TabType.PureData, data = "ITEM ID")
    public void SerializeItemMethod(final Player player, final String itemName) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(itemName, "itemName");
        PermissionsConfig.DoesPlayerHavePermission(player, SP_Permissions.SerializeItem, true, permissionsConfig -> {
            ScythePluginAPI.SerialiseItem(itemName, player.getInventory().getItemInMainHand(), serializableItemsConfig -> {
                MessagesConfig.GetMessagesConfig(messagesConfig -> {
                    messagesConfig.SendToSinglePlayer(com.dreamfirestudios.scytheplugin.Enum.SP_Messages.PlayerSerializedItem, player, DreamMessageSettings.all());
                    player.playSound(player.getLocation(), "minecraft:entity.experience_orb.pickup", 1.0f, 1.0f);
                });
            });
        });
    }

    /**
     * Reloads plugin configs for a player.
     *
     * @param player the player executing the command (not null)
     */
    @PCMethod({"configs", "reload"})
    public void ReloadMethod(final Player player){
        Objects.requireNonNull(player, "player");
        PermissionsConfig.DoesPlayerHavePermission(player, SP_Permissions.ReloadConfigs, true, permissionsConfig -> {
            ScythePluginAPI.ReloadConfigs(DreamMessageSettings.all(), false);
            MessagesConfig.GetMessagesConfig(messagesConfig -> {
                messagesConfig.SendToSinglePlayer(com.dreamfirestudios.scytheplugin.Enum.SP_Messages.PlayerReloadedConfig, player, DreamMessageSettings.all());
                player.playSound(player.getLocation(), "minecraft:entity.experience_orb.pickup", 1.0f, 1.0f);
            });
        });
    }

    /**
     * Reloads plugin configs from console or any {@link CommandSender}.
     *
     * @param sender the command sender (not null)
     */
    @PCMethod(value = {"configs", "reload"})
    public void ReloadMethodServer(final CommandSender sender) {
        Objects.requireNonNull(sender, "sender");
        ScythePluginAPI.ReloadConfigs(DreamMessageSettings.all(), false);
        MessagesConfig.GetMessagesConfig(messagesConfig ->
                messagesConfig.SendToConsole(com.dreamfirestudios.scytheplugin.Enum.SP_Messages.PlayerReloadedConfig, DreamMessageSettings.all())
        );
    }

    /**
     * Resets plugin configs for a player.
     *
     * @param player the player executing the command (not null)
     */
    @PCMethod({"configs", "reset"})
    public void ResetMethod(final Player player){
        Objects.requireNonNull(player, "player");
        PermissionsConfig.DoesPlayerHavePermission(player, SP_Permissions.ResetConfigs, true, permissionsConfig -> {
            ScythePluginAPI.ReloadConfigs(DreamMessageSettings.all(), true);
            MessagesConfig.GetMessagesConfig(messagesConfig -> {
                messagesConfig.SendToSinglePlayer(com.dreamfirestudios.scytheplugin.Enum.SP_Messages.PlayerResetConfig, player, DreamMessageSettings.all());
                player.playSound(player.getLocation(), "minecraft:entity.experience_orb.pickup", 1.0f, 1.0f);
            });
        });
    }

    /**
     * Resets plugin configs from console or any {@link CommandSender}.
     *
     * @param sender the command sender (not null)
     */
    @PCMethod(value = {"configs", "reset"})
    public void ResetMethodServer(final CommandSender sender) {
        Objects.requireNonNull(sender, "sender");
        ScythePluginAPI.ReloadConfigs(DreamMessageSettings.all(), true);
        MessagesConfig.GetMessagesConfig(messagesConfig ->
                messagesConfig.SendToConsole(com.dreamfirestudios.scytheplugin.Enum.SP_Messages.PlayerResetConfig, DreamMessageSettings.all())
        );
    }

    /**
     * Adds a new {@link MenuSlot} to a {@link com.dreamfirestudios.scytheplugin.Core.SmartInvs.MenuConfig}.
     *
     * @param player the player executing the command (not null)
     * @param menu   the target menu enum (not null)
     * @param item   the inventory item key (not null)
     * @param row    the row index to place the slot
     * @param column the column index to place the slot
     */
    @PCMethod({"smartinvs", "addslot"})
    @PCTab(pos = 1, type = TabType.Enum, data = "com.dreamfirestudios.scytheplugin.Enum.BCFM_SmartInventories")
    @PCTab(pos = 2, type = TabType.Enum, data = "com.dreamfirestudios.scytheplugin.Enum.BFCM_InventoryItems")
    public void AddMenuSlotCommand(final Player player, com.dreamfirestudios.scytheplugin.Enum.SP_SmartInventories menu, com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems item, int row, int column) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(menu, "menu");
        Objects.requireNonNull(item, "item");
        PermissionsConfig.DoesPlayerHavePermission(player, SP_Permissions.AddMenuSlots, true, permissionsConfig -> {
            MenuSlot slot = MenuSlot.at(row, column)
                    .closeOnClick(false)
                    .disabled(false)
                    .withSound("minecraft:ui.button.click")
                    .withMessage(com.dreamfirestudios.scytheplugin.Enum.SP_Messages.PlayerSerializedItem, MenuSlot.Target.CLICKER);
            switch (menu) {
                case AdminMenu -> AdminMenuConfig.GetCoreMenuConfig(cfg ->
                        SmartInventoryAPI.AddMenuSlot(cfg, item, slot, updated ->
                                player.sendMessage("§aAdded " + item + " to §e" + menu + " §aat row " + row + ", col " + column)
                        )
                );
                case ConfirmResetMenu -> ConfirmResetConfig.GetCoreMenuConfig(cfg ->
                        SmartInventoryAPI.AddMenuSlot(cfg, item, slot, updated ->
                                player.sendMessage("§aAdded " + item + " to §e" + menu + " §aat row " + row + ", col " + column)
                        )
                );
            }
        });
    }
}