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
package com.dreamfirestudios.scytheplugin.Enum;

import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageFormatter;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamPersistentData.DreamPersistentItemStack;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * /// <summary>
 * Predefined inventory items used across plugin menus and UI.
 * /// </summary>
 * /// <remarks>
 * Each enum constant defines presentation (name, lore, model data) and optional persistent keys
 * that are stamped onto the item for later identification.
 * /// </remarks>
 * /// <example>
 * <code>
 * ItemStack enabled = ScythePluginInventoryItems.SystemEnabled.ReturnItemStack(msgSettings);
 * </code>
 * /// </example>
 */
public enum ScythePluginInventoryItems {
    /** Decorative blank tile (black glass pane). */
    BlankTile(" ", Material.BLACK_STAINED_GLASS_PANE, List.of(), List.of(), 0),

    /** Action item to indicate/flip "system enabled" state. */
    SystemEnabled("#38b227System Enabled!", Material.GREEN_CONCRETE, List.of(), List.of(), 0),

    /** Action item to reset configs. */
    ResetConfigs("#38b227Reset Configs!", Material.GREEN_CONCRETE, List.of(), List.of(), 0),

    /** Action item to reload configs. */
    ReloadConfigs("#38b227Reload Configs!", Material.GREEN_CONCRETE, List.of(), List.of(), 0);

    /** Display name template (supports color formatting via {@link DreamMessageFormatter}). */
    public final String displayName;

    /** Base item material. */
    public final Material itemMaterial;

    /** Lore lines (unformatted templates). */
    public final List<String> itemLore;

    /** Persistent data keys to embed on the item (value equals key). */
    public final List<String> keys;

    /** Custom model data. */
    public final int modelData;

    ScythePluginInventoryItems(final String displayName,
                                   final Material itemMaterial,
                                   final List<String> itemLore,
                                   final List<String> keys,
                                   final int modelData) {
        this.displayName = displayName;
        this.itemMaterial = itemMaterial;
        this.itemLore = itemLore;
        this.keys = keys;
        this.modelData = modelData;
    }

    /**
     * /// <summary>
     * Build an {@link ItemStack} instance for this enum entry with formatting and PDC tags.
     * /// </summary>
     * /// <param name="dreamMessageSettings">Message settings used to format name/lore.</param>
     * /// <returns>Configured {@link ItemStack}.</returns>
     */
    public ItemStack ReturnItemStack(final DreamMessageSettings dreamMessageSettings) {
        Objects.requireNonNull(dreamMessageSettings, "dreamMessageSettings");

        final ItemStack itemStack = new ItemStack(itemMaterial);
        final var itemMeta = itemStack.getItemMeta();

        itemMeta.displayName(DreamMessageFormatter.format(displayName, dreamMessageSettings));

        final var lore = new ArrayList<Component>();
        for (final var line : itemLore) {
            lore.add(DreamMessageFormatter.format(line, dreamMessageSettings));
        }
        itemMeta.lore(lore);

        itemMeta.setCustomModelData(modelData);
        itemStack.setItemMeta(itemMeta);

        for (final var key : keys) {
            DreamPersistentItemStack.Add(
                    ScythePlugin.GetScythePlugin(),
                    itemStack,
                    PersistentDataType.STRING,
                    key,
                    key
            );
        }
        return itemStack;
    }
}