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
 * Defines reusable message templates for the plugin.
 * <p>
 * Messages are parameterized and automatically formatted with the plugin's name.
 * </p>
 */
public enum SP_Messages {
    PlayerReloadedConfig("#7fff36[%s]: Configs have been reloaded!"),
    PlayerResetConfig("#7fff36[%s]: Configs have been reset!"),
    PlayerSerializedItem("#7fff36[%s]: Item has been added to serialise items!"),
    SystemNotEnabled("#7fff36[%s]: System Isn't Enabled!"),
    PlayerReceivedScythe("#7fff36[%s]: You have received the Scythe of Eternal Death!"),
    PlayerGivenScythe("#7fff36[%s]: A player has been granted the Scythe of Eternal Death!");

    private final String template;

    SP_Messages(final String template) {
        this.template = Objects.requireNonNull(template, "template");
    }

    /**
     * Returns the message with the plugin class name injected.
     *
     * @return formatted message string
     */
    public String GetMessage() {
        return String.format(template, ScythePlugin.GetClassName());
    }
}