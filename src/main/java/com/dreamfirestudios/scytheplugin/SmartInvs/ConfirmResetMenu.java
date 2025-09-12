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
package com.dreamfirestudios.scytheplugin.SmartInvs;

import com.dreamfirestudios.scytheplugin.API.ScythePluginAPI;
import com.dreamfirestudios.scytheplugin.API.SmartInventoryAPI;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.Core.SmartInvs.AbstractMenu;
import com.dreamfirestudios.scytheplugin.MenuConfig.ConfirmResetConfig;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.content.InventoryContents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * SmartInventory menu for confirming configuration resets.
 * <p>
 * Provides buttons to confirm or cancel reset actions.
 * </p>
 */
public class ConfirmResetMenu extends AbstractMenu<ConfirmResetConfig> {

    /**
     * Constructs a new ConfirmResetMenu.
     *
     * @param config  configuration for this menu (never null)
     * @param players players to immediately open the menu for
     */
    public ConfirmResetMenu(ConfirmResetConfig config, Player... players) {
        super(Objects.requireNonNull(config, "config"), false, DreamMessageSettings.all(), players);
    }

    @Override
    public CompletableFuture<Void> init(Player player, InventoryContents contents) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(contents, "contents");
        return super.init(player, contents);
    }

    @Override
    protected void handleItemClick(com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems item, Player player, InventoryClickEvent e) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(e, "event");
        if (item == com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems.ConfirmResetConfigs) {
            ScythePluginAPI.ReloadConfigs(DreamMessageSettings.all(), true);
        } else {
            e.setCancelled(true);
        }
    }

    @Override
    protected void openMenuForPlayer(com.dreamfirestudios.scytheplugin.Enum.SP_SmartInventories menu, Player player, InventoryClickEvent e) {
        Objects.requireNonNull(menu, "menu");
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(e, "event");
        Bukkit.getScheduler().runTask(ScythePlugin.GetInstance(), () -> {
            switch (menu) {
                case AdminMenu -> SmartInventoryAPI.OpenAdminMenu(player);
                case ConfirmResetMenu -> SmartInventoryAPI.OpenConfirmResetMenu(player);
                default -> e.setCancelled(true);
            }
        });
    }
}