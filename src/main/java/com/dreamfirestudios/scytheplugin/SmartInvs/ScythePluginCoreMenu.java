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

import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.SmartInventory;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.content.InventoryContents;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.content.InventoryProvider;
import com.dreamfirestudios.scytheplugin.API.ScythePluginAPI;
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginInventoryItems;
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginPermissions;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.PulseConfig.ScythePluginConfig;
import com.dreamfirestudios.scytheplugin.PulseConfig.ScythePluginPermissionsConfigs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Admin control bar menu for toggling core features.
 *
 * <h2>Layout</h2>
 * <ul>
 *   <li>Single row (1 × 9).</li>
 *   <li>Slots: Blank filler, System Enabled toggle, Reload Configs, Reset Configs.</li>
 * </ul>
 *
 * <h2>Permissions</h2>
 * <ul>
 *   <li>Toggling requires {@link ScythePluginPermissions#EnableSystem}.</li>
 *   <li>Reload requires {@link ScythePluginPermissions#ReloadConfigs}.</li>
 *   <li>Reset requires {@link ScythePluginPermissions#ResetConfigs}.</li>
 * </ul>
 *
 * <h2>Threading</h2>
 * <p>
 * Item production and placement are resolved via config async callbacks and then applied on the main thread.
 * </p>
 */
public final class ScythePluginCoreMenu implements InventoryProvider {
    private final SmartInventory smartInventory;

    /**
     * Constructs and opens the menu for one or more players.
     *
     * @param players recipients (non-null, individual entries non-null)
     */
    public ScythePluginCoreMenu(final Player... players){
        Objects.requireNonNull(players, "players");
        smartInventory = SmartInventory.builder()
                .id("ScythePlugin_CoreMenu")
                .provider(this)
                .size(1, 9)
                .title(NamedTextColor.RED + "GlitchSMP Admin")
                .build();
        for (var p : players) smartInventory.open(Objects.requireNonNull(p, "player"));
    }

    /**
     * Initializes the inventory contents. Called by SmartInvs when the GUI opens.
     *
     * @param player   the viewing player (non-null)
     * @param contents mutable contents wrapper (non-null)
     * @return future completed after initial placement
     */
    @Override
    public CompletableFuture<Void> init(final Player player, final InventoryContents contents) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(contents, "contents");

        CompletableFuture<Void> future = new CompletableFuture<>();
        ScythePluginConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginConfig.class, config -> {
            ScythePluginSmartInvsItems.InventoryItem(
                    player, ScythePluginInventoryItems.BlankTile,
                    ci -> contents.fillRow(0, ci),
                    this::BlankTileClick
            );

            ScythePluginSmartInvsItems.InventoryItemWithFeedback(
                    player, ScythePluginInventoryItems.SystemEnabled,
                    item -> {
                        var meta = item.getItemMeta();
                        var lore = config.systemEnabled
                                ? List.of(Component.text(NamedTextColor.WHITE + "Currently: " + NamedTextColor.GREEN + "ENABLED"))
                                : List.of(Component.text(NamedTextColor.WHITE + "Currently: " + NamedTextColor.RED + "DISABLED"));
                        meta.lore(lore);
                        item.setItemMeta(meta);
                        return item;
                    },
                    ci -> contents.set(0, 2, ci),
                    this::SystemEnabledClick
            );

            ScythePluginSmartInvsItems.InventoryItem(
                    player, ScythePluginInventoryItems.ReloadConfigs,
                    ci -> contents.set(0, 4, ci),
                    this::ReloadConfigsClick
            );

            ScythePluginSmartInvsItems.InventoryItem(
                    player, ScythePluginInventoryItems.ResetConfigs,
                    ci -> contents.set(0, 6, ci),
                    this::ResetConfigsClick
            );

            future.complete(null);
        });
        return future;
    }

    /** No-op click handler for filler tiles; leaves event uncancelled. */
    private void BlankTileClick(final Player player, final InventoryClickEvent e) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(e, "event");
        e.setCancelled(false);
    }

    /** Toggles the system if the viewer has permission, then reopens the menu to reflect state. */
    private void SystemEnabledClick(final Player player, final InventoryClickEvent e){
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(e, "event");
        ScythePluginPermissionsConfigs.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginPermissionsConfigs.class, cfg -> {
            if (!cfg.DoesPlayerHavePermission(ScythePluginPermissions.EnableSystem, player, true, DreamMessageSettings.all())) return;
            ScythePluginAPI.ScythePluginEnableSystem(x -> {});
            smartInventory.open(player);
        });
    }

    /** Reloads configs if authorized, then reopens the menu. */
    private void ReloadConfigsClick(final Player player, final InventoryClickEvent e) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(e, "event");
        ScythePluginPermissionsConfigs.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginPermissionsConfigs.class, cfg -> {
            if (!cfg.DoesPlayerHavePermission(ScythePluginPermissions.ReloadConfigs, player, true, DreamMessageSettings.all())) return;
            ScythePluginAPI.ScythePluginReloadConfigs(DreamMessageSettings.all());
            smartInventory.open(player);
        });
    }

    /** Resets configs if authorized, then reopens the menu. */
    private void ResetConfigsClick(final Player player, final InventoryClickEvent e){
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(e, "event");
        ScythePluginPermissionsConfigs.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginPermissionsConfigs.class, cfg -> {
            if (!cfg.DoesPlayerHavePermission(ScythePluginPermissions.ResetConfigs, player, true, DreamMessageSettings.all())) return;
            ScythePluginAPI.ScythePluginResetConfigs(DreamMessageSettings.all());
            smartInventory.open(player);
        });
    }
}