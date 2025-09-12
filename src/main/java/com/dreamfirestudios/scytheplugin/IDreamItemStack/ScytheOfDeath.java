package com.dreamfirestudios.scytheplugin.IDreamItemStack;

import com.dreamfirestudios.dreamcore.DreamItems.IDreamItemStack;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.scytheplugin.StaticPulseConfig.ScytheConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

@PulseAutoRegister
public class ScytheOfDeath implements IDreamItemStack, Listener {
    private static final String ID = "scythe_of_death";
    private static final Component NAME = Component.text("Scythe of Eternal Death", NamedTextColor.DARK_PURPLE);
    private static final List<Component> LORE = List.of(
            Component.text("Forged in the Nether’s fire...", NamedTextColor.GRAY),
            Component.text("Reaps not wheat, but souls.", NamedTextColor.GRAY),
            Component.text("The Wither follows in its wake.", NamedTextColor.DARK_RED)
    );

    private final Map<UUID, Integer> hitCounter = new HashMap<>();
    private final Random random = new Random();
    private ScytheConfig scytheConfig;

    public ScytheOfDeath(){
        ScytheConfig.GetScytheConfig(scytheConfig -> {
            this.scytheConfig = scytheConfig;
        });
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public Component displayName() {
        return NAME;
    }

    @Override
    public Material type() {
        return Material.NETHERITE_SWORD;
    }

    @Override
    public boolean unbreakable() {
        return true;
    }

    @Override
    public List<Component> lore() {
        return LORE;
    }

    @Override
    public Map<Enchantment, Integer> enchantments() {
        return Map.of(Enchantment.SHARPNESS, 5);
    }

    @Override
    public Map<Attribute, Collection<AttributeModifier>> attributeModifiers() {
        return Map.of(
                Attribute.ATTACK_DAMAGE, List.of(
                        new AttributeModifier(UUID.randomUUID(),
                                "attack_damage",
                                scytheConfig.attackDamage,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.HAND)
                ),
                Attribute.ATTACK_SPEED, List.of(
                        new AttributeModifier(UUID.randomUUID(),
                                "attack_speed",
                                scytheConfig.attackSpeed,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.HAND)
                )
        );
    }

    @Override
    public void writePdc(Plugin plugin, PersistentDataContainer pdc) {

    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        var held = player.getInventory().getItemInMainHand();
        if (held == null || held.getType() != type()) return;

        int hits = hitCounter.getOrDefault(player.getUniqueId(), 0) + 1;
        if (hits >= scytheConfig.hitsForWither) {
            hits = 0;
            if (event.getEntity() instanceof LivingEntity victim) {
                victim.addPotionEffect(new PotionEffect(
                        PotionEffectType.WITHER,
                        scytheConfig.witherDuration,
                        scytheConfig.witherAmplifier
                ));
                victim.getWorld().spawnParticle(
                        Particle.valueOf(scytheConfig.witherParticle),
                        victim.getLocation(),
                        scytheConfig.witherParticleCount,
                        scytheConfig.witherParticleSpreadX,
                        scytheConfig.witherParticleSpreadY,
                        scytheConfig.witherParticleSpreadZ,
                        0
                );
            }
        }
        hitCounter.put(player.getUniqueId(), hits);

        if (random.nextDouble() < scytheConfig.stunChance && event.getEntity() instanceof LivingEntity victim) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, scytheConfig.stunDuration, scytheConfig.stunSlowAmplifier));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, scytheConfig.stunDuration, scytheConfig.stunBlindnessAmplifier));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, scytheConfig.stunDuration, scytheConfig.stunWeaknessAmplifier));
        }
    }

}
