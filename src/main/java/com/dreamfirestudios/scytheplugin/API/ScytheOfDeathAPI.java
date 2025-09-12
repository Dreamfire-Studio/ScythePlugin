package com.dreamfirestudios.scytheplugin.API;

import com.dreamfirestudios.dreamcore.DreamItems.DreamItemStacksAPI;
import com.dreamfirestudios.scytheplugin.IDreamItemStack.ScytheOfDeath;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import org.bukkit.entity.Player;

public class ScytheOfDeathAPI {
    public static void giveScythe(Player player) {
        var itemStack = DreamItemStacksAPI.GetItemStackBYID(ScythePlugin.GetInstance(), new ScytheOfDeath().id());
        player.getInventory().addItem(itemStack);
    }
}
