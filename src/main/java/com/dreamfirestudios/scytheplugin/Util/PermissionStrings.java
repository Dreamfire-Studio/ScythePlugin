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
package com.dreamfirestudios.scytheplugin.Util;

import com.dreamfirestudios.scytheplugin.Enum.ScythePluginPermissionLevel;

import java.util.Objects;

/**
 * /// <summary>
 * Helper to construct concrete permission strings from a formatted pattern.
 * /// </summary>
 * /// <remarks>
 * Typical format: <c>"%s.%s.ReloadConfigs"</c> → <c>Plugin.Level.ReloadConfigs</c>.
 * /// </remarks>
 * /// <example>
 * <code>
 * String node = PermissionStrings.resolve("%s.%s.ReloadConfigs", "GlitchSMP", GlitchSMPPluginPermissionLevel.ADMIN);
 * // "GlitchSMP.Admin.ReloadConfigs"
 * </code>
 * /// </example>
 */
public final class PermissionStrings {
    private PermissionStrings() { }

    /**
     * /// <summary>
     * Resolve a pattern <c>%s.%s.X</c> to <c>pluginName.permissionLevel.X</c>.
     * /// </summary>
     * /// <param name="format">Permission format string containing two %s placeholders.</param>
     * /// <param name="pluginName">Plugin simple name.</param>
     * /// <param name="level">Permission level enum.</param>
     * /// <returns>Concrete permission node.</returns>
     */
    public static String resolve(final String format,
                                 final String pluginName,
                                 final ScythePluginPermissionLevel level) {
        Objects.requireNonNull(format, "format");
        Objects.requireNonNull(pluginName, "pluginName");
        Objects.requireNonNull(level, "level");
        return String.format(format, pluginName, level.GetPermissionLevel());
    }
}