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

import com.dreamfirestudios.scytheplugin.StaticEnumPulseConfig.InventoryItemsConfig;
import com.dreamfirestudios.scytheplugin.StaticPulseConfig.SerializableItemsConfig;
import com.dreamfirestudios.dreamcore.DreamSmartInvs.ClickableItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Utility methods for creating {@link ClickableItem}s in SmartInventories,
 * using serialized items or configured inventory items.
 */
public class SmartInventoryItems {

    /**
     * Places a serialized item into a menu with a click handler.
     *
     * @param player   the player viewing the menu (not null)
     * @param itemID   the serialized item ID to fetch (not null)
     * @param mutator  function to mutate the cloned {@link ItemStack} before placing (not null)
     * @param place    consumer that places the created {@link ClickableItem} into contents (not null)
     * @param onClick  handler to run when the item is clicked (not null)
     */
    public static void SerializedItem(Player player, String itemID, Function<ItemStack, ItemStack> mutator, Consumer<ClickableItem> place, BiConsumer<Player, InventoryClickEvent> onClick) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(itemID, "itemID");
        Objects.requireNonNull(mutator, "mutator");
        Objects.requireNonNull(place, "place");
        Objects.requireNonNull(onClick, "onClick");

        SerializableItemsConfig.GetSerializableItemsConfig(serializableItemsConfig -> {
            ItemStack src = Objects.requireNonNull(serializableItemsConfig.GetItemStack(itemID, null), "Serialized item not found: " + itemID);
            ItemStack item = mutator.apply(src.clone());
            if (item == null) item = src.clone();
            ClickableItem ci = ClickableItem.of(item, e -> onClick.accept(player, e));
            place.accept(ci);
        });
    }

    /**
     * Places a configured {@link com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems} entry into a menu with feedback handling.
     *
     * @param player   the player viewing the menu (not null)
     * @param key      the inventory item enum key to fetch (not null)
     * @param mutator  function to mutate the cloned {@link ItemStack} before placing (not null)
     * @param place    consumer that places the created {@link ClickableItem} into contents (not null)
     * @param onClick  handler to run when the item is clicked (not null)
     */
    public static void InventoryItemWithFeedback(Player player, com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems key, Function<ItemStack, ItemStack> mutator, Consumer<ClickableItem> place, BiConsumer<Player, InventoryClickEvent> onClick) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(mutator, "mutator");
        Objects.requireNonNull(place, "place");
        Objects.requireNonNull(onClick, "onClick");

        InventoryItemsConfig.GetInventoryItemsConfig(inventoryItemsConfig -> {
            ItemStack src = Objects.requireNonNull(inventoryItemsConfig.GetValue(key), "Configured inventory item not found: " + key);
            ItemStack item = mutator.apply(src.clone());
            if (item == null) item = src.clone();
            ClickableItem ci = ClickableItem.of(item, e -> onClick.accept(player, e));
            place.accept(ci);
        });
    }
}