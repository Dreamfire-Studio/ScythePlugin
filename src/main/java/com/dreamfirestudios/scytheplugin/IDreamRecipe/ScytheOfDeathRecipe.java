package com.dreamfirestudios.scytheplugin.IDreamRecipe;

import com.dreamfirestudios.dreamcore.DreamItems.DreamItemStacks;
import com.dreamfirestudios.dreamcore.DreamItems.DreamItemStacksAPI;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.dreamcore.DreamRecipe.IDreamRecipe;
import com.dreamfirestudios.dreamcore.DreamRecipe.RecipeType;
import com.dreamfirestudios.scytheplugin.IDreamItemStack.ScytheOfDeath;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import java.util.HashMap;
import java.util.List;

/**
 * <summary>
 * Crafting recipe for the Scythe of Eternal Death.
 * </summary>
 * <remarks>
 * Uses Netherite, Obsidian, and Soul Sand to reflect its nether/death theme.
 * </remarks>
 */
@PulseAutoRegister
public final class ScytheOfDeathRecipe implements IDreamRecipe {

    @Override
    public RecipeType recipeType() {
        return RecipeType.ShapedRecipe;
    }

    @Override
    public ItemStack recipeResult() {
        return DreamItemStacksAPI.GetItemStackBYID(ScythePlugin.GetInstance(), new ScytheOfDeath().id());
    }

    @Override
    public List<String> recipeShape() {
        return List.of("ONO", "OSO", " S ");
    }

    @Override
    public HashMap<Character, RecipeChoice> recipeMaterials() {
        var map = new HashMap<Character, RecipeChoice>();
        map.put('N', new RecipeChoice.MaterialChoice(Material.NETHERITE_INGOT));
        map.put('O', new RecipeChoice.MaterialChoice(Material.OBSIDIAN));
        map.put('S', new RecipeChoice.MaterialChoice(Material.SOUL_SAND));
        return map;
    }
}