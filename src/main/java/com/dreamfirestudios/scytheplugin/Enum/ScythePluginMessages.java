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

/**
 * /// <summary>
 * Message templates (plugin-scoped) for console/player feedback.
 * /// </summary>
 * /// <remarks>
 * <ul>
 *   <li>All templates are raw strings; first {@code %s} is the plugin name.</li>
 *   <li>Use {@link #GetMessage(Object...)} for a convenience formatter, or pipe through your
 *   message system for Adventure components.</li>
 * </ul>
 * </remarks>
 */
public enum ScythePluginMessages {
    /** Console: system was enabled. */
    ConsoleEnabledSystem("#7fff36[%s]: System has been enabled!"),

    /** Console: system was disabled. */
    ConsoleDisableSystem("#7fff36[%s]: System has been disabled!"),

    /** Player: configs reloaded. */
    PlayerReloadedConfig("#7fff36[%s]: Configs have been reloaded!"),

    /** Player: item serialized notice (expects second arg = item name). */
    PlayerSerializedItem("#7fff36[%s]: Item (#ffffff%s) #7fff36 has been added to serialise items!"),

    /** Player: configs reset. */
    PlayerResetConfig("#7fff36[%s]: Configs have been reset!"),

    /** Generic notice when system is disabled. */
    SystemIsntEnabled("#7fff36[%s]: System Isn't Enabled!");

    private final String template;

    ScythePluginMessages(final String template) {
        this.template = template;
    }

    /**
     * /// <summary>Raw template string (unformatted).</summary>
     * /// <returns>Underlying template.</returns>
     */
    public String GetTemplate() {
        return template;
    }

    /**
     * /// <summary>
     * Convenience formatter injecting the plugin name as the first parameter.
     * /// </summary>
     * /// <param name="args">Additional arguments to format into the template.</param>
     * /// <returns>Formatted message string.</returns>
     * /// <remarks>
     * Prefer dedicated message/formatting services when available.
     * </remarks>
     */
    public String GetMessage(final Object... args) {
        final String pluginName = ScythePlugin.class.getSimpleName();
        final int extra = (args == null ? 0 : args.length);
        final Object[] full = new Object[1 + extra];
        full[0] = pluginName;
        if (extra > 0) System.arraycopy(args, 0, full, 1, extra);
        return String.format(template, full);
    }
}