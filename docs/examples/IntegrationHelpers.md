# Integration Helpers — Core

This document details the **Integration Helpers** module within the Dreamfire Core framework. Integration Helpers are designed to provide smooth, optional connections between the plugin core and widely-used third-party APIs/libraries. They ensure that external dependencies can be safely detected, leveraged, or gracefully ignored depending on the environment.

---

## Purpose

* Allow plugins to integrate with external APIs (e.g., LuckPerms, Adventure, DreamCommand) without hard dependency failures.
* Abstract boilerplate code for checking versions, resolving capabilities, and conditionally invoking integrations.
* Provide consistent patterns for optional integration and fallback.

---

## Key Components

### 1. **LuckPerms Integration Helper**

* Detects if **LuckPerms** is installed and active.
* Provides utility methods to:

    * Fetch `User` objects from LuckPerms.
    * Check if a user has a permission node.
    * Apply meta values or group checks.
* Abstracts away direct LuckPerms API calls, ensuring plugins degrade gracefully if LuckPerms is not present.

**Example:**

```java
var user = DreamLuckPerms.getUser(player);
boolean allowed = DreamLuckPerms.hasPermission(user, "myplugin.feature");
```

### 2. **Adventure Integration Helper**

* Provides unified access to **Kyori Adventure** messaging APIs.
* Wraps components like `Component`, `MiniMessage`, and formatters.
* Ensures messages are consistently styled and safe to send to:

    * Players
    * Console
    * Broadcasts

**Example:**

```java
DreamChat.SendMessageToPlayer(player, "<red>Error: Access denied!</red>", settings);
```

### 3. **DreamCommand Integration Helper**

* Simplifies usage of the **DreamCommand framework** for command registration.
* Allows plugins to auto-register annotated command classes (`@PCMethod`, `@PCOP`, `@PCTab`).
* Handles tab completion, permissions, and aliases.

**Example:**

```java
DreamCommand.RegisterRaw(plugin);
```

### 4. **Capabilities / Optional Hooks**

* Provides an abstract **Capabilities** system:

    * Checks if a plugin (e.g., `PlaceholderAPI`, `Vault`) is present.
    * Enables optional hooks without hard dependencies.
    * Stores results in a cache for fast lookups.

**Example:**

```java
if (Capabilities.isPluginAvailable("PlaceholderAPI")) {
    // Register placeholders safely
}
```

### 5. **HotReloadWatcher (Optional)**

* A utility for detecting **plugin reloads** or hot swaps.
* Allows plugins to rebind integrations or re-check availability when the environment changes.

---

## Usage Patterns

### Optional Integration (Safe Fallback)

Integration Helpers emphasize **safe fallbacks**:

* If a third-party plugin is unavailable, helpers return null, false, or no-ops.
* Prevents `ClassNotFoundException` or `NoClassDefFoundError`.

### Version & Platform Detection

* Uses **VersionChecker** utilities to:

    * Detect Bukkit/Spigot/Paper platform.
    * Detect server versions (`1.20`, `1.21` etc.).
    * Ensure API compatibility before invoking optional methods.

---

## Advanced Examples

### LuckPerms + Config Permissions

```java
ScythePluginPermissionsConfigs.ReturnStaticAsync(plugin,
    ScythePluginPermissionsConfigs.class, cfg -> {
        if (!DreamLuckPerms.hasPermission(DreamLuckPerms.getUser(player),
                cfg.GetValue(ScythePluginPermissions.AdminAccess))) {
            DreamChat.SendMessageToPlayer(player, "<red>No permission!</red>", DreamMessageSettings.all());
        }
    });
```

### PlaceholderAPI Hook

```java
if (Capabilities.isPluginAvailable("PlaceholderAPI")) {
    PlaceholderAPI.registerExpansion(new MyPluginExpansion(plugin));
}
```

### Adventure Formatting

```java
Component msg = MiniMessage.miniMessage().deserialize("<green>Plugin Enabled!</green>");
DreamChat.SendMessageToConsole(msg, DreamMessageSettings.all());
```

---

## Best Practices

* Always guard integrations with capability checks.
* Prefer helper utilities over raw API calls for maintainability.
* Use `Optional` return values where possible to indicate absent integrations.
* Keep integration **optional** — no plugin should hard-fail if an external dependency is missing.

---

## Benefits

* **Consistency**: All plugins integrate with external APIs in the same way.
* **Resilience**: Plugins gracefully degrade if optional integrations are missing.
* **Maintainability**: Centralized helpers reduce duplication and simplify updates.
* **Future-proofing**: Easier to extend with new integrations or swap APIs.

---

This **Integration Helpers** module ensures plugins built on the Dreamfire Core are safe, resilient, and compatible across a wide range of server environments and third-party ecosystems.