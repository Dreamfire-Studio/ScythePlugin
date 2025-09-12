package com.dreamfirestudios.scytheplugin.StaticPulseConfig;

import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.scytheplugin.StaticEnumPulseConfig.PermissionsConfig;
import com.dreamfirestudios.dreamconfig.Abstract.StaticPulseConfig;
import com.dreamfirestudios.dreamconfig.Interface.ConfigVersion;
import com.dreamfirestudios.dreamconfig.SaveableObjects.SaveableHashmap;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Stores serializable {@link ItemStack} objects for use in menus or APIs.
 *
 * <p>Provides CRUD operations for named item stacks, persisted in a
 * {@link SaveableHashmap}.</p>
 */
@PulseAutoRegister
@ConfigVersion(1)
public class SerializableItemsConfig extends StaticPulseConfig<SerializableItemsConfig> {

    /** Backing map for item stacks keyed by ID. */
    public SaveableHashmap<String, ItemStack> itemStackSaveableHashmap =
            new SaveableHashmap<>(String.class, ItemStack.class);

    /**
     * Asynchronously loads the SerializableItemsConfig.
     *
     * @param onSuccess callback to execute with the loaded config (never null)
     */
    public static void GetSerializableItemsConfig(Consumer<SerializableItemsConfig> onSuccess){
        Objects.requireNonNull(onSuccess, "onSuccess");
        PermissionsConfig.ReturnStaticAsync(ScythePlugin.GetInstance(),
                SerializableItemsConfig.class, onSuccess);
    }

    /**
     * Adds or updates an {@link ItemStack} under the given ID.
     *
     * @param id        identifier key (never null)
     * @param itemStack stack to save (never null)
     * @param onSuccess callback with updated config (never null)
     */
    public static void AddItemStack(String id, ItemStack itemStack, Consumer<SerializableItemsConfig> onSuccess){
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(itemStack, "itemStack");
        Objects.requireNonNull(onSuccess, "onSuccess");
        SerializableItemsConfig.GetSerializableItemsConfig(serializableItemsConfig -> {
            serializableItemsConfig.itemStackSaveableHashmap.getHashMap().put(id, itemStack);
            serializableItemsConfig.SaveDreamConfig(ScythePlugin.GetInstance(), onSuccess);
        });
    }

    /** @return the plugin main class */
    @Override public JavaPlugin mainClass() { return ScythePlugin.GetInstance(); }

    /**
     * Removes an {@link ItemStack} by ID.
     *
     * @param id        identifier key (never null)
     * @param onSuccess callback with updated config (never null)
     */
    public void RemoveItemStack(final String id, Consumer<SerializableItemsConfig> onSuccess){
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(onSuccess, "onSuccess");
        itemStackSaveableHashmap.getHashMap().remove(id);
        SaveDreamConfig(ScythePlugin.GetInstance(), onSuccess);
    }

    /**
     * Retrieves an {@link ItemStack} by ID.
     *
     * @param id               identifier key (never null)
     * @param defaultItemStack default stack if missing (never null)
     * @return stored stack or default
     */
    public ItemStack GetItemStack(final String id, ItemStack defaultItemStack){
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(defaultItemStack, "defaultItemStack");
        return itemStackSaveableHashmap.getHashMap().getOrDefault(id, defaultItemStack);
    }

    /**
     * Checks if an {@link ItemStack} exists by ID.
     *
     * @param id identifier key (never null)
     * @return true if present
     */
    public boolean IsItemStack(final String id){
        Objects.requireNonNull(id, "id");
        return itemStackSaveableHashmap.getHashMap().containsKey(id);
    }

    /**
     * Checks if a given {@link ItemStack} is stored.
     *
     * @param itemStack stack to check (never null)
     * @return true if present
     */
    public boolean IsItemStack(final ItemStack itemStack){
        Objects.requireNonNull(itemStack, "itemStack");
        return itemStackSaveableHashmap.getHashMap().containsValue(itemStack);
    }
}