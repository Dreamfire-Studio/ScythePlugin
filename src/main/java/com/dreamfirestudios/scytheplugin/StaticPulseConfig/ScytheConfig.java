package com.dreamfirestudios.scytheplugin.StaticPulseConfig;

import com.dreamfirestudios.dreamconfig.Abstract.StaticPulseConfig;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

/**
 * <summary>
 * Configuration for the Scythe of Eternal Death.
 * </summary>
 * <remarks>
 * All stats, chances, effects, and visuals are configurable.
 * </remarks>
 */
public final class ScytheConfig extends StaticPulseConfig<ScytheConfig> {

    public static void GetScytheConfig(Consumer<ScytheConfig> repsonse){
        ReturnStaticAsync(ScythePlugin.GetInstance(), ScytheConfig.class, repsonse);
    }

    /** Display name (supports & color codes). */
    public String displayName = "&5&lScythe of Eternal Death";

    /** Lore lines. */
    public String[] lore = {
            "&7Forged in the Nether’s fire...",
            "&7Reaps not wheat, but souls.",
            "&4&oThe Wither follows in its wake."
    };

    // === Item Settings ===
    /** Whether the item is unbreakable. */
    public boolean unbreakable = true;

    /** Attack damage value. */
    public double attackDamage = 10.0;

    /** Attack speed modifier. */
    public double attackSpeed = -3.0;

    /** Enchantment: Sharpness level. */
    public int sharpnessLevel = 5;

    // === Wither Effect ===
    /** How many hits before wither triggers. */
    public int hitsForWither = 10;

    /** Duration of wither in ticks. */
    public int witherDuration = 100;

    /** Amplifier of wither. */
    public int witherAmplifier = 1;

    /** Particle used when wither effect triggers. */
    public String witherParticle = "SMOKE_LARGE";

    /** Amount of particles spawned. */
    public int witherParticleCount = 20;

    /** Spread of particles in X/Y/Z. */
    public double witherParticleSpreadX = 0.5;
    public double witherParticleSpreadY = 0.5;
    public double witherParticleSpreadZ = 0.5;

    // === Stun Effect ===
    /** Chance (0–1) to stun. */
    public double stunChance = 0.03;

    /** Duration of stun effects in ticks. */
    public int stunDuration = 100;

    /** Amplifier of slow effect during stun. */
    public int stunSlowAmplifier = 10;

    /** Amplifier of blindness during stun. */
    public int stunBlindnessAmplifier = 1;

    /** Amplifier of weakness during stun. */
    public int stunWeaknessAmplifier = 1;

    @Override
    public JavaPlugin mainClass() {
        return ScythePlugin.GetInstance();
    }

    @Override
    public boolean useSubFolder() {
        return true;
    }
}