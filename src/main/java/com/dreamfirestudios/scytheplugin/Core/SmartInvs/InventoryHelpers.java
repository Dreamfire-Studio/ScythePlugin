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
package com.dreamfirestudios.scytheplugin.Core.SmartInvs;

import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.StaticEnumPulseConfig.MessagesConfig;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.content.InventoryContents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Utility helpers for working with SmartInventory menus.
 *
 * <p>Includes:</p>
 * <ul>
 *   <li>Bounds checking and logging.</li>
 *   <li>Applying configured messages when clicking a slot.</li>
 *   <li>Playing click sounds.</li>
 *   <li>Providing a default click handler that respects slot settings.</li>
 * </ul>
 */
public final class InventoryHelpers {
    private InventoryHelpers() {}

    /** Record representing menu bounds (rows × cols). */
    public record Bounds(int rows, int cols) {}

    /**
     * Check if a position is outside the inventory bounds.
     *
     * @param row     the row index
     * @param col     the column index
     * @param maxRows total rows
     * @param maxCols total columns
     * @return true if out of bounds, false otherwise
     */
    public static boolean isOutOfBounds(int row, int col, int maxRows, int maxCols) {
        return row < 0 || col < 0 || row >= maxRows || col >= maxCols;
    }

    /**
     * Logs a warning if a slot placement is out of bounds.
     *
     * @param itemKey the inventory item key (never null)
     * @param row     the row attempted
     * @param col     the column attempted
     * @param maxRows total rows
     * @param maxCols total columns
     */
    public static void logOutOfBounds(com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems itemKey, int row, int col, int maxRows, int maxCols) {
        Objects.requireNonNull(itemKey, "itemKey");
        ScythePlugin.GetInstance().getLogger().warning(() -> String.format("Skipping %s at (%d,%d) — out of bounds for %dx%d inventory", itemKey, row, col, maxRows, maxCols));
    }

    /**
     * Applies the configured messages of a {@link MenuSlot} when clicked.
     *
     * @param player   the clicking player (never null)
     * @param slot     the slot clicked (never null)
     * @param settings message formatting settings (never null)
     */
    public static void applyMessages(Player player, MenuSlot slot, DreamMessageSettings settings) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(settings, "settings");

        MessagesConfig.GetMessagesConfig(messagesConfig -> {
            if (slot.messages.sendToClicker.enabled) {
                messagesConfig.SendToSinglePlayer(slot.messages.sendToClicker.message, player, settings);
            }
            if (slot.messages.broadcast.enabled) {
                messagesConfig.SendToAllPlayers(slot.messages.broadcast.message, settings, List.of());
            }
            if (slot.messages.console.enabled) {
                messagesConfig.SendToConsole(slot.messages.console.message, settings);
            }
        });
    }

    /**
     * Plays the configured click sound for a slot, if enabled.
     *
     * @param player the player (never null)
     * @param slot   the slot (never null)
     */
    public static void playClickSound(Player player, MenuSlot slot) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(slot, "slot");

        if (slot.playSound && slot.clickSound != null) {
            player.playSound(player.getLocation(), slot.clickSound, 1.0f, 1.0f);
        }
    }

    /**
     * Provides the default click handler for a menu slot.
     *
     * <p>This respects disabled state, messages, sounds,
     * override commands, and linked SmartInventories.</p>
     *
     * @param itemKey  the inventory item key (never null)
     * @param slot     the slot (never null)
     * @param menu     the parent menu (never null)
     * @param player   the player (never null)
     * @param settings the message settings (never null)
     * @return a BiConsumer to handle clicks
     */
    public static BiConsumer<Player, InventoryClickEvent> defaultClickHandler(com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems itemKey, MenuSlot slot, AbstractMenu<?> menu, Player player, DreamMessageSettings settings) {
        Objects.requireNonNull(itemKey, "itemKey");
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(menu, "menu");
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(settings, "settings");

        return (p, e) -> {
            if (slot.disabled) {
                e.setCancelled(true);
                return;
            }

            playClickSound(player, slot);
            applyMessages(player, slot, settings);

            if (slot.overrideCommand != null && !slot.overrideCommand.isBlank()) {
                Bukkit.dispatchCommand(player, slot.overrideCommand.replace("%player%", player.getName()));
                if (slot.closeOnClick) player.closeInventory();
                return;
            }

            if (slot.openSmartInventory && slot.inventoryName != null) {
                menu.openMenuForPlayer(slot.inventoryName, player, e);
                if (slot.closeOnClick) player.closeInventory();
                return;
            }

            menu.handleItemClick(itemKey, player, e);
            if (slot.closeOnClick) player.closeInventory();
        };
    }
}