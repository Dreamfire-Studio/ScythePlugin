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
import com.dreamfirestudios.scytheplugin.StaticEnumPulseConfig.PermissionsConfig;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageFormatter;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.SmartInventory;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.content.InventoryContents;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.content.InventoryProvider;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Abstract base class for SmartInventory-driven menus.
 *
 * <p>Provides:</p>
 * <ul>
 *   <li>Automatic title formatting via {@link DreamMessageFormatter}.</li>
 *   <li>Permission checks before opening menus.</li>
 *   <li>Tracking of current viewers.</li>
 *   <li>Utility methods for adding/removing players and rendering items.</li>
 * </ul>
 *
 * @param <C> The type of {@link MenuConfig} driving this menu.
 */
public abstract class AbstractMenu<C extends MenuConfig<C>> implements InventoryProvider {
    protected final SmartInventory smartInventory;
    protected final C config;
    private final List<UUID> viewers = new ArrayList<>();

    /**
     * Handle an item click inside the menu.
     *
     * @param item   the clicked inventory item (never null)
     * @param player the clicking player (never null)
     * @param e      the click event (never null)
     */
    protected abstract void handleItemClick(com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems item, Player player, InventoryClickEvent e);

    /**
     * Open another SmartInventory menu for a player.
     *
     * @param menu   the target menu (never null)
     * @param player the player (never null)
     * @param e      the triggering click event (never null)
     */
    protected abstract void openMenuForPlayer(com.dreamfirestudios.scytheplugin.Enum.SP_SmartInventories menu, Player player, InventoryClickEvent e);

    /**
     * Constructs a new menu and opens it for provided players.
     *
     * @param config                 the menu configuration (never null)
     * @param sendPlayerErrorMessage whether to show error messages if permissions fail
     * @param settings               message formatting settings (never null)
     * @param players                optional players to open immediately
     */
    protected AbstractMenu(C config, boolean sendPlayerErrorMessage, DreamMessageSettings settings, Player... players) {
        this.config = Objects.requireNonNull(config, "config");
        Objects.requireNonNull(settings, "settings");

        var formattedTitle = DreamMessageFormatter.format(config.title, settings);
        var plainTitle = PlainTextComponentSerializer.plainText().serialize(formattedTitle);

        this.smartInventory = SmartInventory.builder()
                .provider(this)
                .id(getClass().getSimpleName())
                .title(plainTitle)
                .type(InventoryType.CHEST)
                .size(Math.max(1, config.rows), 9)
                .closeable(config.closeable)
                .bottomClickable(config.bottomClickable)
                .addClickable(config.clickableTiles)
                .build();

        ScythePlugin.GetInstance().getLogger().info(() -> String.format(
                "Built SmartInventory for %s: rows=%d, cols=%d, type=%s",
                getClass().getSimpleName(), smartInventory.getRows(), smartInventory.getColumns(), smartInventory.getType()
        ));

        for (var p : players) AddPlayerToMenu(p, sendPlayerErrorMessage, settings);
    }

    /**
     * Opens this menu for a player (if they have permission).
     *
     * @param player             the player to add (never null)
     * @param sendErrorMessage   whether to send error messages on permission fail
     * @param settings           message settings (never null)
     */
    public void AddPlayerToMenu(Player player, boolean sendErrorMessage, DreamMessageSettings settings) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(settings, "settings");

        PermissionsConfig.GetPermissionsConfig(cfg -> {
            if (!cfg.DoesPlayerHavePermission(config.viewPermission, sendErrorMessage, settings, player)) return;
            Bukkit.getScheduler().runTask(ScythePlugin.GetInstance(), () -> {
                smartInventory.open(player);
                viewers.add(player.getUniqueId());
            });
        });
    }

    /**
     * Closes the menu for a player.
     *
     * @param player the player to remove (never null)
     */
    public void RemovePlayerFromMenu(Player player) {
        Objects.requireNonNull(player, "player");
        Bukkit.getScheduler().runTask(ScythePlugin.GetInstance(), () -> {
            smartInventory.close(player);
            viewers.remove(player.getUniqueId());
        });
    }

    /**
     * Closes the menu for all current viewers.
     */
    public void RemoveAllPlayersFromMenu() {
        new ArrayList<>(viewers).forEach(uuid -> {
            var p = Bukkit.getPlayer(uuid);
            if (p != null) RemovePlayerFromMenu(p);
        });
    }

    /**
     * Check whether a player is currently viewing this menu.
     *
     * @param player the player (never null)
     * @return true if they are viewing, false otherwise
     */
    public boolean IsPlayerViewingMenu(Player player) {
        Objects.requireNonNull(player, "player");
        return viewers.contains(player.getUniqueId());
    }

    /**
     * Asynchronously checks whether a player has permission to view this menu.
     *
     * @param player   the player (never null)
     * @param response callback receiving true/false
     */
    public void CanPlayerViewMenu(Player player, Consumer<Boolean> response) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(response, "response");
        PermissionsConfig.GetPermissionsConfig(cfg ->
                response.accept(cfg.DoesPlayerHavePermission(config.viewPermission, false, DreamMessageSettings.all(), player)));
    }

    /**
     * Initializes the contents of the menu for a given player.
     *
     * @param player   the player (never null)
     * @param contents the inventory contents to populate (never null)
     * @return a completed future (menu init is synchronous)
     */
    @Override
    public CompletableFuture<Void> init(Player player, InventoryContents contents) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(contents, "contents");

        int rows = smartInventory.getRows();
        int cols = 9;

        for (var entry : config.itemPositions.getHashMap().entrySet()) {
            var itemKey = entry.getKey();
            var slot = entry.getValue();

            if (InventoryHelpers.isOutOfBounds(slot.row, slot.column, rows, cols)) {
                InventoryHelpers.logOutOfBounds(itemKey, slot.row, slot.column, rows, cols);
                continue;
            }

            // place directly — no scheduler, no future
            SmartInventoryItems.InventoryItemWithFeedback(player, itemKey, i -> i, ci -> contents.set(slot.row, slot.column, ci),
                    InventoryHelpers.defaultClickHandler(itemKey, slot, this, player, DreamMessageSettings.all()));
        }

        return CompletableFuture.completedFuture(null);
    }
}