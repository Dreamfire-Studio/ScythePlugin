# Cross-Cutting Features — Core

This section documents **cross-cutting concerns** that span across all parts of the plugin core. These are universal design principles and implementations that ensure the framework is robust, maintainable, and safe for production use.

---

## 🔒 Thread-Safety

### Overview

Minecraft’s Bukkit/Spigot API is **not thread-safe**, so care must be taken whenever asynchronous logic interacts with the server state.

### Core Features

* **EventBus and Scheduler integration**

    * Ensures that **all events** and **Bukkit API calls** are safely marshalled to the main thread.
    * Prevents accidental race conditions when configs, events, or commands are handled asynchronously.
* **Explicit APIs for async → sync dispatch**

    * `Scheduler.main(Runnable)` guarantees synchronous execution.
    * `Scheduler.async(Runnable)` allows safe off-thread background tasks.

### Benefits

* Avoids silent corruption of server state.
* Allows plugin developers to safely use async I/O (database, file reads, API calls) without worrying about thread-safety issues.

---

## ❌ Null-Safety

### Overview

Many plugin crashes are caused by **`NullPointerException`**. The core uses **defensive programming** to minimize this risk.

### Core Features

* **`Objects.requireNonNull` checks**

    * Enforced in constructors, event firing, command methods, and config lookups.
    * Produces **clear error messages** like `player cannot be null` instead of vague stack traces.
* **Optional wrappers**

    * Certain configs or APIs return `Optional<T>` where nullability is valid but must be explicitly handled.
* **Fail-fast principle**

    * Invalid inputs are caught **immediately**, making debugging and error reporting faster.

### Benefits

* Clearer debugging when something goes wrong.
* Safer APIs across the board.
* Encourages plugin developers to handle edge cases properly.

---

## 📝 Logging & Diagnostics

### Overview

The core provides structured, flexible logging that scales from **debug development** to **production monitoring**.

### Core Features

* **Log Utility (`Log`)**

    * Built-in debug toggles (`debugConfig` in configs).
    * Category-based logging (`Events`, `Scheduler`, `Commands`).
    * Easy to extend per plugin.
* **Startup & Health Checks**

    * Ensures configs load correctly.
    * Provides warnings when misconfiguration occurs (e.g., missing permissions).
* **Debug-only logs**

    * Prevents spam in production.
    * Enables deep insight during testing.

### Benefits

* Transparent insight into plugin behavior.
* Easier bug reports and troubleshooting.
* Config-driven debug toggles for granular control.

---

## 🔄 Config–Event Synchronization

### Overview

Configuration and events are closely linked: changing a config should **emit an event** so that listeners can respond dynamically.

### Core Features

* **System toggle firing events**

    * When `systemEnabled` changes in config, a `SystemToggleEvent` is dispatched.
* **Config reload/reset events**

    * Listeners (e.g., menus, subsystems) are automatically updated when configs are reloaded.
* **Consistency guarantees**

    * Events are always fired **on the main thread**.
    * Ensures no desync between internal state and visible behavior.

### Benefits

* Plugins remain in sync without manual re-checks.
* Automatic propagation of config changes.
* Unified mechanism across all plugin features.

---

## 🌍 Standardized Patterns

### Overview

The core enforces **consistent design conventions** across all plugins.

### Patterns Enforced

* **Class structure**

    * `Core/`, `Config/`, `Command/`, `Event/`, `Menu/` namespaces are consistent across plugins.
* **Event base class**

    * All plugin events extend from `AbstractScythePluginEvent`, centralizing dispatch and thread-safety.
* **Helper classes**

    * Reusable utilities like `PlayerCommandHelper` ensure permission checks and system-enabled checks are always consistent.

### Benefits

* Developers switching between plugins instantly recognize patterns.
* Less duplicated code.
* Faster onboarding for new contributors.

---

## ✅ Summary

The **Cross-Cutting Features** ensure that all plugins built on this core are:

* Thread-safe.
* Null-safe.
* Debuggable.
* Config-driven and event-synchronized.
* Consistent in structure and patterns.

These are not optional extras — they are enforced throughout the core to guarantee quality, safety, and maintainability across every plugin.