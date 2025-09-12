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
package com.dreamfirestudios.scytheplugin.API;

import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.Core.SmartInvs.MenuConfig;
import com.dreamfirestudios.scytheplugin.Core.SmartInvs.MenuSlot;
import com.dreamfirestudios.scytheplugin.MenuConfig.AdminMenuConfig;
import com.dreamfirestudios.scytheplugin.MenuConfig.ConfirmResetConfig;
import com.dreamfirestudios.scytheplugin.SmartInvs.AdminMenu;
import com.dreamfirestudios.scytheplugin.SmartInvs.ConfirmResetMenu;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * API entrypoint for working with SmartInventories in ScythePlugin.
 *
 * <p>Provides static helpers to open core menus and manipulate
 * {@link MenuConfig} instances programmatically.</p>
 */
public final class SmartInventoryAPI {

    private SmartInventoryAPI() {
        // Utility class; prevent instantiation
    }

    /**
     * Opens the {@link AdminMenu} for a player.
     *
     * @param player the player to show the menu to (not null)
     */
    public static void OpenAdminMenu(final Player player) {
        Objects.requireNonNull(player, "player");
        AdminMenuConfig.GetCoreMenuConfig(coreMenuConfig ->
                new AdminMenu(coreMenuConfig, player)
        );
    }

    /**
     * Opens the {@link ConfirmResetMenu} for a player.
     *
     * @param player the player to show the menu to (not null)
     */
    public static void OpenConfirmResetMenu(final Player player) {
        Objects.requireNonNull(player, "player");
        ConfirmResetConfig.GetCoreMenuConfig(confirmResetConfig ->
                new ConfirmResetMenu(confirmResetConfig, player)
        );
    }

    /**
     * Adds or updates a {@link MenuSlot} in the given {@link MenuConfig}.
     *
     * <p>The slot is persisted to config immediately via {@link MenuConfig#SaveDreamConfig}.</p>
     *
     * @param config    the menu config to modify (not null)
     * @param key       the inventory item key (not null)
     * @param slot      the new menu slot definition (not null)
     * @param onSuccess callback once the config has been saved (not null)
     * @param <C>       type of the menu config
     */
    public static <C extends MenuConfig<C>> void AddMenuSlot(final C config, final com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems key, final MenuSlot slot, final Consumer<C> onSuccess) {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(onSuccess, "onSuccess");

        config.itemPositions.getHashMap().put(key, slot);
        config.SaveDreamConfig(ScythePlugin.GetInstance(), onSuccess);
    }
}
