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

import java.util.Arrays;

/**
 * /// <summary>
 * Permission node formats and corresponding default error templates.
 * /// </summary>
 * /// <remarks>
 * <ul>
 *   <li>Permission format shape: {@code "%s.%s.Action"} → {@code Plugin.Level.Action}</li>
 *   <li>Error templates expect plugin name as the first {@code %s} placeholder.</li>
 * </ul>
 * </remarks>
 */
public enum ScythePluginPermissions {
    /** Reload configs action. */
    ReloadConfigs("%s.%s.ReloadConfigs", "#7fff36[%s]: You do not have the permission to use this command!"),

    /** Reset configs action. */
    ResetConfigs("%s.%s.ResetConfigs", "#7fff36[%s]: You do not have the permission to use this command!"),

    /** Enable/disable system action. */
    EnableSystem("%s.%s.EnableSystem", "#7fff36[%s]: You do not have the permission to use this command!"),

    /** Serialize item action. */
    SerializeItem("%s.%s.SerializeItem", "#7fff36[%s]: You do not have the permission to use this command!"),

    /** Admin console-only actions. */
    AdminConsole("%s.%s.AdminConsole", "#7fff36[%s]: You do not have the permission to use this command!"),

    /** Redraw regions action. */
    RedrawRegions("%s.%s.RedrawRegions", "#7fff36[%s]: You do not have the permission to use this command!");

    private final String permission;
    private final String error;

    ScythePluginPermissions(final String permission, final String error) {
        this.permission = permission;
        this.error = error;
    }

    /**
     * /// <summary>
     * Build a concrete permission string for a specific level.
     * /// </summary>
     * /// <param name="permissionLevel">Desired logical level segment.</param>
     * /// <returns>Concrete permission node (e.g., {@code Plugin.Admin.ReloadConfigs}).</returns>
     */
    public String GetPermission(final ScythePluginPermissionLevel permissionLevel) {
        return String.format(permission, ScythePlugin.class.getSimpleName(), permissionLevel.GetPermissionLevel());
    }

    /**
     * /// <summary>
     * Build a formatted error message for missing permission.
     * /// </summary>
     * /// <param name="args">Optional additional args for extended templates.</param>
     * /// <returns>Formatted error line.</returns>
     * /// <remarks>
     * Current templates only consume the plugin name; {@code args} are concatenated via {@link Arrays#toString(Object[])}
     * and injected as a second placeholder if present in the template.
     * </remarks>
     */
    public String GetError(final Object... args) {
        return String.format(error, ScythePlugin.class.getSimpleName(), Arrays.toString(args));
    }

    /**
     * /// <summary>Return the raw permission format string.</summary>
     * /// <returns>Format with placeholders (e.g., {@code "%s.%s.Action"}).</returns>
     */
    public String getPermissionFormat() {
        return permission;
    }
}