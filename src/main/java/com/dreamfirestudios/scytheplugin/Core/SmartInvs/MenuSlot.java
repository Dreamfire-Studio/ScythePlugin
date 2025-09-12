/*
 * MIT License
 *
 * Copyright (c) 2025 Dreamfire Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.dreamfirestudios.scytheplugin.Core.SmartInvs;

import com.dreamfirestudios.dreamconfig.Interface.IPulseClass;
import org.bukkit.Registry;
import org.bukkit.Sound;

import java.util.Objects;

/**
 * Represents a slot in a SmartInventory menu, including position,
 * commands, sounds, and optional feedback messages.
 *
 * <p>Builder-style API allows concise creation, e.g.:</p>
 * <pre>
 * MenuSlot slot = MenuSlot.at(0, 4)
 *     .withCommand("say Hello")
 *     .closeOnClick(true)
 *     .withSound("minecraft:entity.experience_orb.pickup")
 *     .withMessage(BFCM_Messages.PlayerReloadedConfig, MenuSlot.Target.CLICKER);
 * </pre>
 */
public class MenuSlot implements IPulseClass {

    // ──────────────── Core fields ────────────────
    /** Row index of the slot (0-based). */
    public int row = 0;

    /** Column index of the slot (0-based). */
    public int column = 0;

    /** Optional override command to run when the slot is clicked. */
    public String overrideCommand = "";

    /** Target SmartInventory to open if {@link #openSmartInventory} is true. */
    public com.dreamfirestudios.scytheplugin.Enum.SP_SmartInventories inventoryName = com.dreamfirestudios.scytheplugin.Enum.SP_SmartInventories.AdminMenu;

    /** Whether this slot opens another SmartInventory. */
    public boolean openSmartInventory = false;

    /** Whether this slot is disabled (ignores clicks). */
    public boolean disabled = false;

    /** Whether clicking this slot should close the inventory. */
    public boolean closeOnClick = false;

    /** Whether a sound should be played when clicked. */
    public boolean playSound = false;

    /** The sound key to play when clicked (defaults to UI button click). */
    public String clickSound =
            Objects.requireNonNull(Registry.SOUNDS.getKey(Sound.UI_BUTTON_CLICK)).toString();

    /** Messages attached to this slot for various audiences. */
    public Messages messages = new Messages();

    // ──────────────── Constructors ────────────────

    /** Creates a default menu slot at (0,0). */
    public MenuSlot() {}

    /**
     * Creates a slot at the given position.
     *
     * @param row row index (0-based)
     * @param column column index (0-based)
     */
    public MenuSlot(int row, int column) {
        this.row = row;
        this.column = column;
    }

    // ──────────────── Builder-style API ────────────────

    /**
     * Start building a slot at the given position.
     *
     * @param row row index (0-based)
     * @param column column index (0-based)
     * @return a new {@link MenuSlot}
     */
    public static MenuSlot at(int row, int column) {
        return new MenuSlot(row, column);
    }

    /**
     * Assign an override command to run when clicked.
     *
     * @param command the command (nullable; defaults to empty string if null)
     * @return this slot (for chaining)
     */
    public MenuSlot withCommand(String command) {
        this.overrideCommand = Objects.requireNonNullElse(command, "");
        return this;
    }

    /**
     * Configure this slot to open another SmartInventory.
     *
     * @param inv target SmartInventory (not null)
     * @return this slot (for chaining)
     */
    public MenuSlot openInventory(com.dreamfirestudios.scytheplugin.Enum.SP_SmartInventories inv) {
        this.inventoryName = Objects.requireNonNull(inv, "inventoryName");
        this.openSmartInventory = true;
        return this;
    }

    /**
     * Mark this slot as disabled.
     *
     * @param disabled true if disabled
     * @return this slot (for chaining)
     */
    public MenuSlot disabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    /**
     * Whether clicking this slot should close the inventory.
     *
     * @param close true to close on click
     * @return this slot (for chaining)
     */
    public MenuSlot closeOnClick(boolean close) {
        this.closeOnClick = close;
        return this;
    }

    /**
     * Attach a click sound (enables {@code playSound}).
     *
     * @param soundKey the namespaced sound key (not null)
     * @return this slot (for chaining)
     */
    public MenuSlot withSound(String soundKey) {
        this.playSound = true;
        this.clickSound = Objects.requireNonNull(soundKey, "soundKey");
        return this;
    }

    /**
     * Attach a message to be sent when clicked.
     *
     * @param msg    the message to send (not null)
     * @param target the target audience (not null)
     * @return this slot (for chaining)
     */
    public MenuSlot withMessage(com.dreamfirestudios.scytheplugin.Enum.SP_Messages msg, Target target) {
        Objects.requireNonNull(msg, "msg");
        Objects.requireNonNull(target, "target");
        switch (target) {
            case CLICKER -> this.messages.sendToClicker = new ClickMessage(true, msg);
            case BROADCAST -> this.messages.broadcast = new ClickMessage(true, msg);
            case CONSOLE -> this.messages.console = new ClickMessage(true, msg);
        }
        return this;
    }

    // ──────────────── Supporting types ────────────────

    /** Supported message targets. */
    public enum Target { CLICKER, BROADCAST, CONSOLE }

    /**
     * Individual message entry for a target audience.
     */
    public static class ClickMessage implements IPulseClass {
        /** Whether this message is enabled. */
        public boolean enabled = false;

        /** The message to send when enabled. */
        public com.dreamfirestudios.scytheplugin.Enum.SP_Messages message = com.dreamfirestudios.scytheplugin.Enum.SP_Messages.SystemNotEnabled;

        /** Creates a disabled message entry. */
        public ClickMessage() {}

        /**
         * Creates a new message entry.
         *
         * @param enabled whether the message is enabled
         * @param message the message (not null)
         */
        public ClickMessage(boolean enabled, com.dreamfirestudios.scytheplugin.Enum.SP_Messages message) {
            this.enabled = enabled;
            this.message = Objects.requireNonNull(message, "message");
        }
    }

    /**
     * Group of messages for different audiences.
     */
    public static class Messages implements IPulseClass {
        /** Message for the player who clicked. */
        public ClickMessage sendToClicker = new ClickMessage();

        /** Message broadcast to all players. */
        public ClickMessage broadcast = new ClickMessage();

        /** Message sent to console. */
        public ClickMessage console = new ClickMessage();
    }
}