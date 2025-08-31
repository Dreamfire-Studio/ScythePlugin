# SmartInvs Integration — Core

## Overview

SmartInvs is a powerful inventory framework for Bukkit/Spigot/Paper, enabling structured and dynamic GUIs for players. The core integrates SmartInvs seamlessly into the plugin ecosystem, providing:

* **Menu foundations** (BaseMenu, PagedMenu).
* **Item helpers** for consistent item construction and persistence.
* **Thread-safe config-driven GUIs** (items defined in configs, resolved at runtime).
* **Permission-aware menu actions**.
* **Tight coupling with Scheduler, Messaging, and Config systems**.

This ensures every plugin can build menus without repeating boilerplate.

---

## Architecture

### Components

* **InventoryProvider**: Defines menu contents (`init` and `update` lifecycle).
* **SmartInventory**: Builder/factory to open menus for players.
* **\_\_project\_name\_\_SmartInvsItems**: Helpers for:

    * Loading items from configs (enum-based).
    * Loading serialized items by ID.
    * Applying runtime feedback/mutations.
    * Wrapping items as `ClickableItem`s with safe main-thread placement.
* **CoreMenu (per plugin)**: Example admin console.

### Flow

1. **Player executes command** → opens a SmartInvs menu.
2. **InventoryProvider.init** called → items loaded via `SmartInvsItems`.
3. **Mutators applied** → context-specific lore/feedback.
4. **Click handlers** validated → permission checks + feature flags.
5. **Action executed** → delegates to API/Config/Event subsystems.

---

## Thread-Safety

* **All item placement deferred to main thread** using `EventBus.runMain` or `Scheduler`.
* **Async config fetches** allowed → items applied after resolution.
* Prevents race conditions between config reloads and GUI rendering.

---

## Configuration Integration

* **Enum-driven items**: Each plugin defines an `InventoryItems` enum with:

    * `Material`
    * `DisplayName`
    * `Lore`
    * `CustomModelData`
    * `Keys` for persistent tagging.
* **InventoryItemsConfig** provides default values and serialization.
* Items are automatically **translated, styled, and localized** via the Messaging system.

---

## Permission Integration

* Menu actions bound to permissions (`ScythePluginPermissionsConfigs`).
* Pre-checks before performing mutations (e.g., toggling system state).
* Prevents unauthorized access to sensitive controls.

---

## Messaging Integration

* Menus use **Messaging System** for player feedback.
* Example: when reloading configs, the click handler not only reloads but also sends confirmation back to the player.

---

## Example: CoreMenu

```java
new ScythePluginCoreMenu(player);
```

Renders an admin console with slots:

* Slot 2 → **System Enabled Toggle** (feedback lore).
* Slot 4 → **Reload Configs**.
* Slot 6 → **Reset Configs**.
* All other slots → **BlankTile passthroughs**.

Each item is:

* Loaded from enum-driven config.
* Mutated dynamically based on current system state.
* Bound to permission checks.

---

## Advanced Usage

### PagedMenu

* Core provides pagination helpers for long lists (e.g., serialized items).
* Standardized navigation (next/prev arrows, back buttons).

### Dynamic Mutation

* Items can mutate based on config values, feature flags, or external APIs.
* Example: Lore updating live with player’s cooldown or cache state.

### Persistent Items

* **DreamPersistentItemStack** ensures items carry custom keys for tracking.
* Used in event listeners to identify clicked items without brittle slot/index logic.

---

## Best Practices

* Always guard click handlers with **permission checks** + **feature flag checks**.
* Use **mutators** to avoid duplicating item creation logic.
* Offload heavy computation async, only mutate/apply results on main thread.
* Prefer **enum-driven config items** for consistency across plugins.

---

## Benefits

* **Consistency**: Every plugin’s menus look and behave the same.
* **Maintainability**: Shared helpers reduce repeated boilerplate.
* **Extensibility**: New plugins can add menus quickly with minimal setup.
* **Safety**: Enforced thread-safety and permission checks by design.

---

## Next Steps

Future improvements may include:

* Built-in support for **animated items**.
* **Search & filter controls** baked into PagedMenu.
* Integration with **RateLimiter** for spam prevention.
* Localization bundles for per-player menu translations.