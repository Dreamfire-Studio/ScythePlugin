# Messaging System — Core

> High-level, thread-safe messaging utilities for broadcasting, targeted delivery, templating, and permission-scoped announcements.

---

## Goals & Principles

* **Thread-safe**: All sends are marshalled to the main thread.
* **Template-driven**: Source of truth is `ScythePluginMessages` (enum). Storage & delivery via `ScythePluginMessagesConfig`.
* **Renderer-agnostic**: Format using `DreamMessageFormatter` and output plain text via `PlainTextComponentSerializer` to keep console clean while supporting color codes where applicable.
* **Context-aware**: Use player-aware formatting for placeholders (e.g., player display name).
* **Non-blocking**: Asynchronous config fetch → main-thread send.
* **Consistent**: One place to broadcast, whisper, console-log, or permission-target.

---

## Architecture Overview

```
ScythePluginMessages   (enum of message keys + default templates)
            │
            ▼
ScythePluginMessagesConfig (StaticEnumPulseConfig<Messages, String>)
   • default values from enum
   • format helpers (player-aware / plain)
   • main-thread delivery helpers (broadcast, player, console, context, permission)
```

### Key Classes

* **`ScythePluginMessages`**: Enum of message keys. Each entry holds a template with `%s` placeholder(s). Convention: the *first* `%s` is always the plugin name.
* **`ScythePluginMessagesConfig`**: Stores/resolves templates and exposes delivery helpers. Responsible for:

    * Formatting via `DreamMessageFormatter` with provided `DreamMessageSettings`.
    * Ensuring message sends happen on the main thread with `Scheduler`.
    * Auto-prepending plugin name to args using `withPlugin(...)` helper.

---

## Message Templates

### Template Contract

* **First parameter**: plugin name.
* Additional parameters: supplied by the caller.
* Color markup supported (e.g., `#7fff36`) and processed by `DreamMessageFormatter`.

### Example Enum Entry

```java
ConsoleEnabledSystem("#7fff36[%s]: System has been enabled!")
```

### Formatting Helpers (inside `MessagesConfig`)

* `formatPlain(String template, DreamMessageSettings settings, Object... args)`
* `formatPlain(String template, Player player, DreamMessageSettings settings, Object... args)`

These produce **plain text** using `PlainTextComponentSerializer` after passing the formatted component through `DreamMessageFormatter`.

---

## Delivery APIs

### Broadcast

```java
messagesConfig.SendMessageToBroadcast(ScythePluginMessages.ConsoleEnabledSystem,
    DreamMessageSettings.all());
```

**Notes**

* Main-thread marshalled.
* Good for global announcements.

### Player (Direct)

```java
messagesConfig.SendMessageToPlayer(ScythePluginMessages.PlayerReloadedConfig,
    player, DreamMessageSettings.all());
```

**Notes**

* Uses player-aware formatting variant.
* Main-thread marshalled.

### Console

```java
messagesConfig.SendMessageToConsole(ScythePluginMessages.SystemIsntEnabled,
    DreamMessageSettings.all());
```

**Notes**

* Outputs a plain, color-stripped line suitable for logs.

### Conversation Context

```java
messagesConfig.SendMessageToContext(ScythePluginMessages.PlayerSerializedItem,
    player, ctx, DreamMessageSettings.all(), itemName);
```

**Notes**

* Useful during wizards/prompts using Bukkit Conversations.

### Permission-Scoped Delivery

```java
messagesConfig.SendMessageToPlayerPermission(
    ScythePluginMessages.PlayerReloadedConfig,
    ScythePluginPermissions.AdminConsole,
    DreamMessageSettings.all());
```

**Notes**

* Iterates online players and checks perms via `ScythePluginPermissionsConfigs`.
* Avoids boilerplate in callers.

---

## Threading Model

* Config resolution is **async**, then delivery is **scheduled to main** via `Scheduler.main(...)`.
* All `SendMessage*` helpers enforce main-thread execution.

---

## Integration Points

### Permissions System

* `SendMessageToPlayerPermission` queries `ScythePluginPermissionsConfigs` for efficient, consistent checks.

### Commands Layer

* Command handlers use `MessagesConfig` to present user-facing feedback (success, error, hints) rather than printing raw strings.

### Event System

* Listeners can broadcast or target messages when reacting to events like `ScythePluginConfigReloadEvent`.

### Rate Limiter

* For high-volume emitters (e.g., spammy events), wrap calls with `RateLimiter` in the caller:

```java
if (rateLimiter.tryAcquire(player.getUniqueId())) {
    messagesConfig.SendMessageToPlayer(...);
}
```

### Try / Retry

* For transient formatting data dependencies (e.g., formatting args fetched async), use `Try.run(...)` or `Retry.withBackoff(...).run(...)` around message preparation, **not** around the final main-thread send.

---

## Localization & Overrides

* Enum defaults provide out-of-the-box strings.
* `ScythePluginMessagesConfig` persists templates, enabling server operators to modify messages per key.
* Use the same key across plugins to inherit consistent phrasing.

**Tip**: Keep templates short, imperative, and color-light for readability.

---

## Placeholder Strategy

* Perform data shaping **before** calling send helpers.
* Pass ordered arguments matching the `%s` placeholders.
* Avoid heavy computation inside message formatters; compute first, then format.

**Example**

```java
String regionName = region.getName();
messagesConfig.SendMessageToPlayer(
  ScythePluginMessages.PlayerSerializedItem,
  player,
  DreamMessageSettings.all(),
  regionName
);
```

---

## Safety & Resilience

* **Null-safety**: `Objects.requireNonNull(...)` on inputs.
* **No-op**: Empty or null templates are skipped quietly.
* **Main-thread**: Always ensures correct Bukkit thread affinity.
* **Plain Output**: Console messages are safe for log archiving.

---

## Testing Guide

* **Unit**: Template formatting with various `DreamMessageSettings` (colors on/off, debug on/off).
* **Integration**: Verify main-thread send (`Bukkit.isPrimaryThread()` when handler executes).
* **Permission**: Ensure `SendMessageToPlayerPermission` correctly targets Admin vs Player roles.
* **Load**: With a mocked `RateLimiter`, ensure bursty features remain quiet when blocked.

---

## Migration Notes

* Replace legacy `Bukkit.broadcastMessage(...)`/`player.sendMessage(...)` with the config-backed helpers.
* Ensure all messages exist in `ScythePluginMessages` to centralize control.
* Prefer `DreamMessageSettings.all()` unless you need a special profile.

---

## Anti-Patterns to Avoid

* Formatting inside asynchronous callbacks that also do heavy I/O — precompute earlier.
* Direct Adventure component sends from this layer — maintain the plain-text boundary for console cleanliness.
* Hardcoding plugin name; rely on `withPlugin(...)` auto-prepend.

---

## Quick Reference

* **Enum**: `ScythePluginMessages`
* **Storage & Send**: `ScythePluginMessagesConfig`
* **Format**: `DreamMessageFormatter` → `PlainTextComponentSerializer`
* **Threading**: `Scheduler.main(...)`
* **Perm Targeting**: `SendMessageToPlayerPermission(...)`
* **Contexts**: Broadcast / Player / Console / Conversation

---

## Example End-to-End Flow

```java
ScythePluginMessagesConfig.ReturnStaticAsync(
    ScythePlugin.GetScythePlugin(),
    ScythePluginMessagesConfig.class,
    messagesConfig -> messagesConfig.SendMessageToPlayer(
        ScythePluginMessages.PlayerReloadedConfig,
        player,
        DreamMessageSettings.all()
    )
);
```

This resolves the config async, then safely sends on the main thread with the plugin name auto-prepended into the template.
