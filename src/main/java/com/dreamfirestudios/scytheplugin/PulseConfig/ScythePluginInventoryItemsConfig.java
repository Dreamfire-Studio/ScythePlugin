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

package com.dreamfirestudios.scytheplugin.PulseConfig;

import com.dreamfirestudios.dreamconfig.Abstract.StaticEnumPulseConfig;
import com.dreamfirestudios.dreamconfig.Interface.ConfigVersion;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageFormatter;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.dreamcore.DreamPersistentData.DreamPersistentItemStack;
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginInventoryItems;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Static enum-based configuration for predefined {@link ItemStack} templates.
 */
@PulseAutoRegister
@ConfigVersion(1)
public final class ScythePluginInventoryItemsConfig
        extends StaticEnumPulseConfig<ScythePluginInventoryItemsConfig, ScythePluginInventoryItems, ItemStack> {

    @Override public JavaPlugin mainClass() { return ScythePlugin.GetScythePlugin(); }
    @Override protected Class<ScythePluginInventoryItems> getKeyClass() { return ScythePluginInventoryItems.class; }
    @Override protected Class<ItemStack> getValueClass() { return ItemStack.class; }
    @Override public boolean useSubFolder() { return false; }

    @Override
    protected ItemStack getDefaultValueFor(final ScythePluginInventoryItems key) {
        Objects.requireNonNull(key, "key");
        final var settings = DreamMessageSettings.all();
        final var is = new ItemStack(key.itemMaterial);
        final var meta = is.getItemMeta();

        meta.displayName(DreamMessageFormatter.format(Objects.requireNonNull(key.displayName, "displayName"), settings));

        final var lore = new ArrayList<Component>();
        for (final var l : key.itemLore) {
            lore.add(DreamMessageFormatter.format(Objects.requireNonNull(l, "itemLore line"), settings));
        }
        meta.lore(lore);
        meta.setCustomModelData(key.modelData);
        is.setItemMeta(meta);

        for (final var k : key.keys) {
            DreamPersistentItemStack.Add(ScythePlugin.GetScythePlugin(), is, PersistentDataType.STRING,
                    Objects.requireNonNull(k, "persistent key"), k);
        }
        return is;
    }
}