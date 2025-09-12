/*
 * MIT License
 *
 * Copyright (c) 2025 Dreamfire Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to do so, subject to the following
 * conditions:
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
package com.dreamfirestudios.scytheplugin.Core;

import com.dreamfirestudios.dreamcore.DreamSmartInvs.ClickableItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * /// <summary>
 * Factory helpers for SmartInvs {@link ClickableItem} constructs.
 * /// </summary>
 * /// <remarks>
 * Keeps click binding concise and consistent across menus.
 * Also enforces a non-null {@code InventoryClickEvent} at the boundary.
 * /// </remarks>
 */
public final class InventoryFactory {
    private InventoryFactory() { }

    /**
     * /// <summary>
     * Create a {@link ClickableItem} that forwards both the click event and backing item.
     * </summary>
     * <param name="item">Item to render in the slot and pass back on click.</param>
     * <param name="onClick">Handler invoked when the item is clicked.</param>
     * <returns>Bound {@link ClickableItem}.</returns>
     */
    public static ClickableItem clickable(final ItemStack item,
                                          final BiConsumer<InventoryClickEvent, ItemStack> onClick) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(onClick, "onClick");
        return ClickableItem.of(item, e -> onClick.accept(
                Objects.requireNonNull(e, "event"),
                item
        ));
    }
}