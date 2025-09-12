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

import com.dreamfirestudios.dreamcore.DreamSmartInvs.ClickableItem;
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginInventoryItems;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.PulseConfig.ScythePluginInventoryItemsConfig;
import com.dreamfirestudios.scytheplugin.PulseConfig.ScythePluginSerializableItems;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <summary>Convenience wrappers for loading items into SmartInvs.</summary>
 * <remarks>
 * Asynchronously fetches config values and provides {@link ClickableItem} instances to callers.
 * </remarks>
 * <example>
 * <code>
 * ScythePluginSmartInvsItems.InventoryItem(player, ScythePluginInventoryItems.ResetConfigs, ci -&gt; {
 *     contents.set(0, 6, ci);
 * }, this::ResetConfigsClick);
 * </code>
 * </example>
 */
public final class ScythePluginSmartInvsItems {

    private ScythePluginSmartInvsItems() { }

    /**
     * <summary>Loads a serialized item by ID, optionally mutates a clone, then provides as clickable.</summary>
     * <param name="player">Viewer.</param>
     * <param name="itemID">Serialized ID.</param>
     * <param name="mutator">Mutator to apply to a clone before use.</param>
     * <param name="accept">Consumer receiving the clickable item.</param>
     * <param name="onClick">Click handler.</param>
     */
    public static void SerializedItem(final Player player, final String itemID, final Function<ItemStack, ItemStack> mutator, final Consumer<ClickableItem> place, final BiConsumer<Player, InventoryClickEvent> onClick) {
        Objects.requireNonNull(itemID, "itemID");
        Objects.requireNonNull(mutator, "mutator");
        Objects.requireNonNull(place, "place");
        Objects.requireNonNull(onClick, "onClick");
        final com.dreamfirestudios.scytheplugin.Core.EventBus bus = new com.dreamfirestudios.scytheplugin.Core.EventBus(ScythePlugin.GetScythePlugin());
        ScythePluginSerializableItems.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginSerializableItems.class, cfg -> {
                    final ItemStack src = cfg.GetItemStack(itemID);
                    if (src == null) return;
                    ItemStack item = mutator.apply(src.clone());
                    if (item == null) item = src.clone();
                    final ClickableItem ci = ClickableItem.of(item, e -> onClick.accept(player, e));
                    bus.runMain(() -> place.accept(ci));
                }
        );
    }

    /**
     * <summary>Loads an inventory item by enum key and exposes a clickable after applying a mutator.</summary>
     * <param name="player">Viewer.</param>
     * <param name="key">Inventory item key.</param>
     * <param name="mutator">Item mutator.</param>
     * <param name="accept">Receiver of clickable.</param>
     * <param name="onClick">Click handler.</param>
     */
    public static void InventoryItemWithFeedback(final Player player, final ScythePluginInventoryItems key, final Function<ItemStack, ItemStack> mutator, final Consumer<ClickableItem> place, final BiConsumer<Player, InventoryClickEvent> onClick) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(mutator, "mutator");
        Objects.requireNonNull(place, "place");
        Objects.requireNonNull(onClick, "onClick");
        final com.dreamfirestudios.scytheplugin.Core.EventBus bus = new com.dreamfirestudios.scytheplugin.Core.EventBus(ScythePlugin.GetScythePlugin());
        ScythePluginInventoryItemsConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginInventoryItemsConfig.class, cfg -> {
                    ItemStack base = cfg.GetValue(key);
                    if (base == null) return;
                    ItemStack item = mutator.apply(base.clone());
                    if (item == null) item = base.clone();
                    final ClickableItem ci = ClickableItem.of(item, e -> onClick.accept(player, e));
                    bus.runMain(() -> place.accept(ci));
                }
        );
    }

    /**
     * <summary>Loads an inventory item by enum key and exposes a clickable.</summary>
     * <param name="player">Viewer.</param>
     * <param name="key">Inventory item key.</param>
     * <param name="accept">Receiver of clickable.</param>
     * <param name="onClick">Click handler.</param>
     */
    public static void InventoryItem(final Player player, final ScythePluginInventoryItems key, final Consumer<ClickableItem> place, final BiConsumer<Player, InventoryClickEvent> onClick) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(place, "place");
        Objects.requireNonNull(onClick, "onClick");
        final com.dreamfirestudios.scytheplugin.Core.EventBus bus = new com.dreamfirestudios.scytheplugin.Core.EventBus(ScythePlugin.GetScythePlugin());
        ScythePluginInventoryItemsConfig.ReturnStaticAsync(ScythePlugin.GetScythePlugin(), ScythePluginInventoryItemsConfig.class, cfg -> {
                    ItemStack base = cfg.GetValue(key);
                    if (base == null) return;
                    final ClickableItem ci = ClickableItem.of(base.clone(), e -> onClick.accept(player, e));
                    bus.runMain(() -> place.accept(ci));
                }
        );
    }
}