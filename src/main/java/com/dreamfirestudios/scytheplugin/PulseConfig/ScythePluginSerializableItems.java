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
package com.dreamfirestudios.scytheplugin.PulseConfig;

import com.dreamfirestudios.dreamconfig.Abstract.StaticPulseConfig;
import com.dreamfirestudios.dreamconfig.Interface.ConfigVersion;
import com.dreamfirestudios.dreamconfig.SaveableObjects.SaveableHashmap;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Simple string-ID to {@link ItemStack} store with persistence.
 *
 * <h2>Typical Usage</h2>
 * <pre>{@code
 * ScythePluginSerializableItems.ReturnStaticAsync(plugin, ScythePluginSerializableItems.class, cfg -> {
 *   cfg.AddItemStack("core.icon", someItem);
 *   cfg.SaveDreamConfig(plugin, saved -> {});
 * });
 * }</pre>
 */
@PulseAutoRegister
@ConfigVersion(1)
public class ScythePluginSerializableItems extends StaticPulseConfig<ScythePluginSerializableItems> {

    @Override public JavaPlugin mainClass() { return ScythePlugin.GetScythePlugin(); }

    /** Backing storage for ID → ItemStack. */
    public SaveableHashmap<String, ItemStack> itemStackSaveableHashmap =
            new SaveableHashmap<>(String.class, ItemStack.class);

    /**
     * Inserts or replaces an item by ID.
     *
     * @param id        identifier (non-null)
     * @param itemStack item to store (non-null)
     * @throws NullPointerException if {@code id} or {@code itemStack} is null
     */
    public void AddItemStack(final String id, final ItemStack itemStack){
        itemStackSaveableHashmap.getHashMap().put(
                Objects.requireNonNull(id, "id"),
                Objects.requireNonNull(itemStack, "itemStack")
        );
    }

    /** Store at root (no subfolder). */
    @Override public boolean useSubFolder() { return false; }

    /**
     * Retrieves an item by ID.
     *
     * @param id identifier (non-null)
     * @return stored item or {@code null} if missing
     * @throws NullPointerException if {@code id} is null
     */
    public ItemStack GetItemStack(final String id){
        return itemStackSaveableHashmap.getHashMap().getOrDefault(
                Objects.requireNonNull(id, "id"), null);
    }
}