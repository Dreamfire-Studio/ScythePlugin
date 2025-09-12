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
package com.dreamfirestudios.scytheplugin.PulseConfig;

import com.dreamfirestudios.dreamconfig.Abstract.StaticEnumPulseConfig;
import com.dreamfirestudios.dreamconfig.Interface.ConfigVersion;
import com.dreamfirestudios.dreamcore.DreamJava.PulseAutoRegister;
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginPermissionLevel;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Static mapping between {@link ScythePluginPermissionLevel} and their string representations.
 */
@PulseAutoRegister
@ConfigVersion(1)
public final class ScythePluginPermissionLevelConfigs
        extends StaticEnumPulseConfig<ScythePluginPermissionLevelConfigs, ScythePluginPermissionLevel, String> {

    @Override public JavaPlugin mainClass() { return ScythePlugin.GetScythePlugin(); }
    @Override protected Class<ScythePluginPermissionLevel> getKeyClass() { return ScythePluginPermissionLevel.class; }
    @Override protected Class<String> getValueClass() { return String.class; }
    @Override public boolean useSubFolder() { return false; }

    /**
     * @param key permission level (non-null)
     * @return default string representation for {@code key}
     */
    @Override
    protected String getDefaultValueFor(final ScythePluginPermissionLevel key) {
        Objects.requireNonNull(key, "key");
        return key.GetPermissionLevel();
    }
}