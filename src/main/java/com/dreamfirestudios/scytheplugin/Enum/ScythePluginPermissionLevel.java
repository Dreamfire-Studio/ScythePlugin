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

/**
 * /// <summary>
 * Logical permission levels used to construct permission nodes.
 * /// </summary>
 * /// <remarks>
 * Pair with {@code PermissionStrings.resolve("%s.%s.SomeAction", pluginName, level)}.
 * </remarks>
 */
public enum ScythePluginPermissionLevel {
    /** Administrative actions (high-privilege). */
    Admin("Admin"),

    /** General player actions (low-privilege). */
    Player("Player");

    private final String permissionLevel;

    ScythePluginPermissionLevel(final String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    /**
     * /// <summary>Permission level string used in node construction.</summary>
     * /// <returns>Level segment (e.g., "Admin").</returns>
     */
    public String GetPermissionLevel() {
        return permissionLevel;
    }
}