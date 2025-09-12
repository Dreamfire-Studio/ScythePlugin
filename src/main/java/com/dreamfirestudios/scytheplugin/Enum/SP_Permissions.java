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
package com.dreamfirestudios.scytheplugin.Enum;

import com.dreamfirestudios.scytheplugin.ScythePlugin;

import java.util.Objects;

/**
 * Defines permission keys and corresponding error messages for the plugin.
 * <p>
 * Each enum constant stores a permission node and an error message template,
 * both of which are formatted with the plugin's class name.
 * </p>
 */
public enum SP_Permissions {
    ReloadConfigs("%s.Admin.ReloadConfigs", "#7fff36[%s]: You do not have the permission to use this command!"),
    ResetConfigs("%s.Admin.ResetConfigs", "#7fff36[%s]: You do not have the permission to use this command!"),
    EnableSystem("%s.Admin.EnableSystem", "#7fff36[%s]: You do not have the permission to use this command!"),
    SerializeItem("%s.Admin.SerializeItem", "#7fff36[%s]: You do not have the permission to use this command!"),
    AdminConsole("%s.Admin.AdminConsole", "#7fff36[%s]: You do not have the permission to use this command!"),
    AddMenuSlots("%s.Admin.AddMenuSlots", "#7fff36[%s]: You do not have the permission to use this command!"),
    GiveScythe("%s.Player.GiveScythe", "#7fff36[%s]: You do not have permission to give the Scythe of Eternal Death!");

    private final String permission;
    private final String error;

    SP_Permissions(final String permission, final String error) {
        this.permission = Objects.requireNonNull(permission, "permission");
        this.error = Objects.requireNonNull(error, "error");
    }

    /**
     * Returns the fully qualified permission string for this enum value.
     *
     * @return permission string with plugin name injected
     */
    public String GetPermission() {
        return String.format(permission, ScythePlugin.GetClassName());
    }

    /**
     * Returns the formatted error message string for this permission.
     *
     * @return formatted error message with plugin name injected
     */
    public String GetError() {
        return String.format(error, ScythePlugin.GetClassName());
    }
}
