# Configuration System

The configuration system is the backbone of the core plugin framework. It standardizes how plugin configuration is defined, stored, accessed, and updated. The goal is to make configurations **safe, reusable, and declarative**, ensuring consistency across all plugins built on this framework.

---

## Key Features

1. **Static Configs**

    * Extend `StaticPulseConfig<T>` for configs tied to a single plugin instance.
    * Provides persistent key/value storage with automatic serialization.
    * Example: `ScythePluginConfig` handles system enable/disable state and debug toggles.

2. **Enum-Based Configs**

    * Extend `StaticEnumPulseConfig<TConfig, TKey, TValue>` for enum-driven key/value mappings.
    * Strongly typed enums ensure compile-time safety for configuration keys.
    * Example: `ScythePluginInventoryItemsConfig` binds enum values like `BlankTile` or `SystemEnabled` to concrete `ItemStack` definitions.

3. **Feature Flags**

    * Enum-driven toggles for enabling/disabling optional systems.
    * Defined in `ScythePluginFeatureFlagsConfig` with default values.
    * Example: `FeatureFlagKey.CORE_MENU = true` enables GUI menus by default.

4. **Permissions Configs**

    * Store permission node formats linked to enums like `Permissions` and `PermissionLevel`.
    * Dynamically resolves full permission strings with helpers like `PermissionStrings.resolve()`.
    * Integrates with LuckPerms via `DreamLuckPerms`.

5. **Messages Configs**

    * Store templated message strings mapped to enums like `Messages`.
    * Send messages to players, console, or broadcasts through `MessagesConfig` utilities.
    * Supports Adventure API via `DreamChat` for formatting and color.

6. **Serializable Items**

    * Persist `ItemStack`s with a string identifier in `SerializableItems`.
    * Used for custom menus, tokens, or persistent gameplay items.
    * Example: `/plugin serialize STARTER_SWORD` saves a sword template to config.

---

## Thread Safety

* All configuration retrievals and saves are **asynchronous by default** using `ReturnStaticAsync`.
* Any Bukkit or Adventure API interaction is marshalled onto the main thread via the `Scheduler`.

---

## Integration with Events

* The configuration system is closely tied to the event bus.
* Example: toggling the system state in `Config` fires a `SystemToggleEvent` for other systems to react.
* Reload and Reset actions also trigger respective events.

---

## Example Workflow

### 1. Adding a new feature flag:

```java
public enum MyPluginFeatureFlagKey {
    COOLDOWN_SYSTEM,
    DOUBLE_JUMP
}

@PulseAutoRegister
@ConfigVersion(1)
public final class MyPluginFeatureFlagsConfig extends StaticEnumPulseConfig<...> {
    @Override protected Boolean getDefaultValueFor(MyPluginFeatureFlagKey key) {
        return switch (key) {
            case COOLDOWN_SYSTEM -> true;
            case DOUBLE_JUMP -> false;
        };
    }
}
```

### 2. Checking config at runtime:

```java
MyPluginFeatureFlagsConfig.ReturnStaticAsync(plugin, MyPluginFeatureFlagsConfig.class, cfg -> {
    if (cfg.GetValue(MyPluginFeatureFlagKey.COOLDOWN_SYSTEM)) {
        // initialize cooldown feature
    }
});
```

---

## Benefits

* **Consistency:** Every plugin follows the same config patterns.
* **Safety:** Null checks and enum-based keys prevent common mistakes.
* **Reusability:** Shared config utilities (messages, items, flags) can be reused across plugins.
* **Flexibility:** Supports both static values and dynamic toggles.

---

## Next Steps

* Add schema validation for configs to catch misconfigured values at startup.
* Provide a command helper to list or modify feature flags in-game.
* Extend support for remote config synchronization (future).