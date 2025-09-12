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

import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageFormatter;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamPersistentData.DreamPersistentItemStack;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Enum defining all inventory items used in SmartInventories for this plugin.
 * <p>
 * Each item specifies its display name, material, lore, persistent data,
 * and optional custom model data.
 * </p>
 */
public enum SP_InventoryItems {
    BlankTile(" ", Material.BLACK_STAINED_GLASS_PANE, List.of(), List.of(), List.of(), 0),
    ResetConfigs("#38b227Reset Configs!", Material.BARRIER, List.of(), List.of(), List.of(), 0),
    ReloadConfigs("#38b227Reload Configs!", Material.GREEN_SHULKER_BOX, List.of(), List.of(), List.of(), 0),
    ConfirmResetConfigs("#38b227Confirm Reset!", Material.BARRIER, List.of(), List.of(), List.of(), 0),
    CancelResetConfigs("#38b227Cancel Reset!", Material.RED_SHULKER_BOX, List.of(), List.of(), List.of(), 0);

    public final String displayName;
    public final Material itemMaterial;
    public final List<String> itemLore;
    public final List<String> keys;
    public final List<String> values;
    public final float modelData;

    SP_InventoryItems(final String displayName, final Material itemMaterial, final List<String> itemLore,
                                         final List<String> keys, final List<String> values, final int modelData) {
        this.displayName = Objects.requireNonNull(displayName, "displayName");
        this.itemMaterial = Objects.requireNonNull(itemMaterial, "itemMaterial");
        this.itemLore = Objects.requireNonNull(itemLore, "itemLore");
        this.keys = Objects.requireNonNull(keys, "keys");
        this.values = Objects.requireNonNull(values, "values");
        this.modelData = modelData;
    }

    /**
     * Creates a new {@link ItemStack} with metadata applied.
     *
     * @param dreamMessageSettings the formatting settings
     * @param player               the player for placeholder resolution
     * @return a formatted {@link ItemStack}
     */
    public ItemStack ReturnItemStack(final DreamMessageSettings dreamMessageSettings, Player player) {
        Objects.requireNonNull(dreamMessageSettings, "dreamMessageSettings");
        final ItemStack itemStack = new ItemStack(itemMaterial);
        UpdateItemMeta(itemStack, dreamMessageSettings, player);
        return itemStack;
    }

    /**
     * Updates the item metadata including name, lore, model data, and persistent data.
     */
    public void UpdateItemMeta(ItemStack itemStack, final DreamMessageSettings dreamMessageSettings, Player player) {
        Objects.requireNonNull(itemStack, "itemStack");
        Objects.requireNonNull(dreamMessageSettings, "dreamMessageSettings");
        var itemMeta = Objects.requireNonNull(itemStack.getItemMeta(), "itemMeta");
        UpdateDisplayName(itemStack, itemMeta, dreamMessageSettings, player);
        UpdateItemLore(itemStack, itemMeta, dreamMessageSettings, player);
        UpdateModelData(itemStack, itemMeta);
        UpdatePersistentData(itemStack, itemMeta);
    }

    /**
     * Updates the display name of the item, applying placeholders and formatting.
     */
    public void UpdateDisplayName(ItemStack itemStack, ItemMeta itemMeta,
                                  final DreamMessageSettings dreamMessageSettings, Player player) {
        Objects.requireNonNull(dreamMessageSettings, "dreamMessageSettings");
        Objects.requireNonNull(itemStack, "itemStack");
        Objects.requireNonNull(itemMeta, "itemMeta");
        String processedName = PlaceholderAPI.setPlaceholders(player, displayName);
        itemMeta.displayName(DreamMessageFormatter.format(processedName, dreamMessageSettings));
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Updates the lore of the item, applying placeholders and formatting.
     */
    public void UpdateItemLore(ItemStack itemStack, ItemMeta itemMeta,
                               final DreamMessageSettings dreamMessageSettings, Player player) {
        Objects.requireNonNull(dreamMessageSettings, "dreamMessageSettings");
        Objects.requireNonNull(itemStack, "itemStack");
        Objects.requireNonNull(itemMeta, "itemMeta");
        final var lore = new ArrayList<Component>();
        for (final var line : itemLore) {
            String processedLine = PlaceholderAPI.setPlaceholders(player, line);
            lore.add(DreamMessageFormatter.format(processedLine, dreamMessageSettings));
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Applies custom model data to the item if defined.
     */
    public void UpdateModelData(ItemStack itemStack, ItemMeta itemMeta) {
        Objects.requireNonNull(itemStack, "itemStack");
        Objects.requireNonNull(itemMeta, "itemMeta");
        if (modelData < 0) return;
        var customModelDataComponent = itemMeta.getCustomModelDataComponent();
        customModelDataComponent.setFloats(List.of(modelData));
        itemMeta.setCustomModelDataComponent(customModelDataComponent);
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Applies persistent data to the item, using configured keys and values.
     */
    public void UpdatePersistentData(ItemStack itemStack, ItemMeta itemMeta) {
        Objects.requireNonNull(itemStack, "itemStack");
        Objects.requireNonNull(itemMeta, "itemMeta");
        for (var i = 0; i < Math.min(keys.size(), values.size()); i++) {
            var key = keys.get(i);
            var value = values.get(i);
            DreamPersistentItemStack.Add(
                    ScythePlugin.GetInstance(),
                    itemStack,
                    PersistentDataType.STRING,
                    Objects.requireNonNull(key, "key"),
                    Objects.requireNonNull(value, "value")
            );
        }
    }
}