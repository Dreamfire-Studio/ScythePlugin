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
import com.dreamfirestudios.scytheplugin.Enum.SP_Permissions;
import com.dreamfirestudios.dreamconfig.Abstract.StaticPulseConfig;
import com.dreamfirestudios.dreamconfig.SaveableObjects.SaveableHashmap;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Base configuration class for all SmartInventory-based menus.
 *
 * <p>Provides:</p>
 * <ul>
 *   <li>Serializable fields for menu title, rows, columns, and behavior flags.</li>
 *   <li>Permission checks for viewing menus.</li>
 *   <li>Automatic handling of default item placement.</li>
 *   <li>Integration with DreamConfig persistence system.</li>
 * </ul>
 *
 * @param <T> The concrete subclass type.
 */
public abstract class MenuConfig<T extends MenuConfig<T>> extends StaticPulseConfig<T> {

    /**
     * Defines the default items required by this menu.
     *
     * @return list of default {@link com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems} (never null)
     */
    protected abstract List<com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems> getDefaultItems();

    /** Menu title (supports color codes). */
    public String title = "&cDefault Menu";

    /** Number of rows in the menu (minimum 1). */
    public int rows = 1;

    /** Number of columns (forced to 9 for chest menus). */
    public int columns = 9;

    /** Whether the menu can be closed manually by players. */
    public boolean closeable = true;

    /** Clickable tile positions that should be interactive. */
    public List<Integer> clickableTiles = new ArrayList<>();

    /** Whether the player’s bottom inventory is clickable while this menu is open. */
    public boolean bottomClickable = false;

    /** Permission required to view this menu. */
    public SP_Permissions viewPermission = SP_Permissions.AdminConsole;

    /** Mapping of item keys to their positions in the menu. */
    public SaveableHashmap<com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems, MenuSlot> itemPositions =
            new SaveableHashmap<>(com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems.class, MenuSlot.class);

    /**
     * Returns the plugin owning this configuration.
     *
     * @return the main plugin instance
     */
    @Override
    public JavaPlugin mainClass() {
        return ScythePlugin.GetInstance();
    }

    /**
     * Whether this config should be stored in a subfolder.
     *
     * @return always true for menu configs
     */
    @Override
    public boolean useSubFolder() {
        return true;
    }

    /**
     * Called the first time the configuration is loaded.
     * Ensures that default items are present.
     */
    @Override
    public void FirstLoadConfig() {
        super.FirstLoadConfig();
        ensureDefaultItems();
    }

    /**
     * Called after loading the configuration.
     * Enforces a 9-column layout and ensures default items.
     */
    @Override
    public void AfterLoadConfig() {
        super.AfterLoadConfig();
        if (columns != 9) {
            ScythePlugin.GetInstance().getLogger().warning(
                    "Forcing menu " + getClass().getSimpleName() + " columns to 9 (CHEST always 9 wide)");
            columns = 9;
        }
        ensureDefaultItems();
    }

    /**
     * Ensures all default items are present in the menu.
     * If missing, they are automatically added in row-major order.
     * Missing space is logged as a warning.
     */
    private void ensureDefaultItems() {
        boolean changed = false;
        int colCounter = 0, rowCounter = 0;

        for (var item : Objects.requireNonNull(getDefaultItems(), "getDefaultItems returned null")) {
            if (!itemPositions.getHashMap().containsKey(item)) {
                if (rowCounter < rows && colCounter < columns) {
                    itemPositions.getHashMap().put(item, new MenuSlot(rowCounter, colCounter));
                    colCounter++;
                    if (colCounter >= columns) {
                        colCounter = 0;
                        rowCounter++;
                    }
                    changed = true;
                } else {
                    ScythePlugin.GetInstance().getLogger().warning(
                            "Menu " + getClass().getSimpleName() +
                                    " has no space for item " + item +
                                    " (rows=" + rows + ", cols=" + columns + ")");
                }
            }
        }

        if (changed) SaveDreamConfig(ScythePlugin.GetInstance(), cfg -> {});
    }
}