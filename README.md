# ScythePlugin

**Version:** `1.21.8-r0.1`
**Author:** Dreamfire Studio, Joshua Filer
**License:** MIT License

---

## Overview

The **ScythePlugin** introduces a powerful custom weapon — the **Scythe of Eternal Death** — into your Bukkit/Spigot/Paper server.

This unique netherite-based weapon is designed with configurable stats, special effects, and integration with DreamCore’s `DreamConfig`, `DreamCommand`, and `DreamItems` systems.

The scythe combines the **attack damage of an axe** with sword speed, while granting devastating powers: a withering particle cross effect every few hits, and a rare chance to **stun enemies in combat**.

All aspects of the scythe — including damage, speed, lore, name, particle effects, wither duration, stun chance, enchantments, and custom model data — are fully configurable without needing a restart.

---

## Key Features

* **Custom Weapon – Scythe of Eternal Death**

    * Netherite sword with configurable axe-like damage and speed.
    * Fully configurable **name**, **lore**, and **CustomModelData**.
    * Automatically unbreakable (toggleable in config).

* **Combat Effects**

    * Every configurable number of hits (default: 10), spawns a **black X-shaped particle cross**
    * Victims are inflicted with the **Wither effect** for a configurable duration/amplifier.
    * A configurable chance (default: 3%) to **stun victims** with Slowness, Blindness, and Weakness.

* **Dynamic Configs**

    * All scythe stats, visual effects, enchantments, and lore are stored in `ScytheConfig`.
    * Supports live reloads using DreamConfig.

* **Crafting Recipe**

    * Shaped recipe combining **Netherite Ingot**, **Obsidian**, and **Soul Sand**.
    * Resulting item is always built with the latest config values.

* **Integration**

    * Built on DreamCore’s `DreamItems`, `DreamConfig`, and `DreamCommand` frameworks.
    * Includes DreamCore-style **permissions** and **messages** for commands.

---

## Installation

### Download

Download the latest release here:
👉 [GitHub Releases](https://github.com/Dreamfire-Studio/ScythePlugin/releases)

Place the `ScythePlugin.jar` into your server’s `plugins/` folder, then restart the server.

### Maven

```xml
<repository>
  <id>github</id>
  <url>https://maven.pkg.github.com/Dreamfire-Studio/ScythePlugin</url>
</repository>

<dependency>
  <groupId>com.dreamfirestudios</groupId>
  <artifactId>scytheplugin-1.21.8-r0.1</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

---

## Permissions

| Permission Node             | Description                                 |
| --------------------------- | ------------------------------------------- |
| `ScythePlugin.GiveScythe`   | Allows giving yourself or others the scythe |
| `ScythePlugin.ReloadConfig` | Reload scythe configuration files           |
| `ScythePlugin.ResetConfig`  | Reset scythe configuration to defaults      |

---

## Commands

* `/scythe give` → Gives the scythe to yourself (requires `ScythePlugin.GiveScythe`).
* `/scythe give <player>` → Gives the scythe to another player (requires `ScythePlugin.GiveScythe`).
* `/scythe reload` → Reloads the config (requires `ScythePlugin.ReloadConfig`).
* `/scythe reset` → Resets config values to defaults (requires `ScythePlugin.ResetConfig`).

---

## Configuration

All settings are configurable inside `ScytheConfig.yml`.

### Example Defaults

```yaml
displayName: "&5&lScythe of Eternal Death"
lore:
  - "&7Forged in the Nether’s fire..."
  - "&7Reaps not wheat, but souls."
  - "&4&oThe Wither follows in its wake."

unbreakable: true
customModelData: 512

attackDamage: 10.0
attackSpeed: -3.0
sharpnessLevel: 5

hitsForWither: 10
witherDuration: 100
witherAmplifier: 1
witherParticle: SMOKE_LARGE
witherParticleCount: 20
witherParticleSpreadX: 0.5
witherParticleSpreadY: 0.5
witherParticleSpreadZ: 0.5

stunChance: 0.03
stunDuration: 100
stunSlowAmplifier: 10
stunBlindnessAmplifier: 1
stunWeaknessAmplifier: 1
```

---

## Crafting Recipe

```
O N O
O S O
  S
```

Where:

* `N` = Netherite Ingot
* `O` = Obsidian
* `S` = Soul Sand

---

## Links

* 🌐 [Dreamfire Studio Website](https://www.dreamfirestudio.net)
* 🎥 [YouTube Channel](https://www.youtube.com/@Dreamfire_Studio)
* 📖 [Wiki Documentation](https://www.dreamfirestudio.net/wiki/Dreamfire-Studio/ScythePlugin)
* 📦 [GitHub Releases](https://github.com/Dreamfire-Studio/ScythePlugin/releases)

---

## Contributing

Contributions are welcome! Fork the repo, open issues, or submit pull requests.

---

## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
