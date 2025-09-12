/*
 * MIT License
 * Copyright (c) 2025 Dreamfire Studio
 */

package com.dreamfirestudios.scytheplugin;

import com.dreamfirestudios.scytheplugin.API.ScythePluginAPI;
import com.dreamfirestudios.scytheplugin.Enum.SP_Permissions;
import com.dreamfirestudios.dreamcommand.DreamCommand;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.DreamClassAPI;
import com.dreamfirestudios.dreamcore.DreamVariable.DreamEnumVariableTest;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main entry point for the ScythePlugin plugin.
 *
 * <p>Handles registration of variable tests, command routing, and
 * initial config loading.</p>
 */
public class ScythePlugin extends JavaPlugin {
    private static ScythePlugin instance;

    /**
     * @return the singleton plugin instance
     */
    public static ScythePlugin GetInstance(){
        return instance;
    }

    /**
     * @return the plugin's class name, used in permissions/messages
     */
    public static String GetClassName(){
        return instance.getClass().getSimpleName();
    }

    /**
     * Called when the plugin is enabled.
     *
     * <p>Registers variable tests, command handlers, and loads configs.</p>
     */
    @Override
    public void onEnable() {
        instance = this;

        // Register enums as variable tests for configs/commands
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(SP_Permissions.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(com.dreamfirestudios.scytheplugin.Enum.SP_InventoryItems.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(com.dreamfirestudios.scytheplugin.Enum.SP_Messages.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(com.dreamfirestudios.scytheplugin.Enum.SP_SmartInventories.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(InventoryType.class));

        // Register annotated classes & commands
        DreamClassAPI.RegisterClasses(this);
        DreamCommand.RegisterRaw(this);

        // Load configs
        ScythePluginAPI.ReloadConfigs(DreamMessageSettings.all(), false);
    }

    /**
     * Called when the plugin is disabled.
     *
     * <p>Ensures all inventories are closed to prevent ghost menus.</p>
     */
    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
    }
}