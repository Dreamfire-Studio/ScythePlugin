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
import com.dreamfirestudios.scytheplugin.Enum.ScythePluginFeatureFlagKey;
import com.dreamfirestudios.scytheplugin.ScythePlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Static enum-based feature flags configuration.
 *
 * <h2>Purpose</h2>
 * <p>Provide simple boolean switches keyed by {@link ScythePluginFeatureFlagKey}.</p>
 *
 * <h2>Storage</h2>
 * <p>Backed by {@link StaticEnumPulseConfig}; stored at root (no subfolder).</p>
 */
@PulseAutoRegister
@ConfigVersion(1)
public final class ScythePluginFeatureFlagsConfig
        extends StaticEnumPulseConfig<ScythePluginFeatureFlagsConfig, ScythePluginFeatureFlagKey, Boolean> {

    @Override public JavaPlugin mainClass() { return ScythePlugin.GetScythePlugin(); }
    @Override protected Class<ScythePluginFeatureFlagKey> getKeyClass() { return ScythePluginFeatureFlagKey.class; }
    @Override protected Class<Boolean> getValueClass() { return Boolean.class; }
    @Override public boolean useSubFolder() { return false; }

    /**
     * Default flag values.
     *
     * @param key feature flag key (non-null)
     * @return the default boolean for {@code key}
     */
    @Override
    protected Boolean getDefaultValueFor(final ScythePluginFeatureFlagKey key) {
        Objects.requireNonNull(key, "key");
        return switch (key) {
            case CORE_MENU       -> Boolean.TRUE;
            case SERIALIZE_ITEMS -> Boolean.TRUE;
            case CONFIG_COMMANDS -> Boolean.TRUE;
        };
    }
}