/*
 * MIT License
 *
 * Copyright (c) 2025 Dreamfire Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to do so, subject to the following
 * conditions:
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
package com.dreamfirestudios.scytheplugin;

import com.dreamfirestudios.dreamcommand.DreamCommand;
import com.dreamfirestudios.dreamcore.DreamChat.DreamMessageSettings;
import com.dreamfirestudios.dreamcore.DreamJava.DreamClassAPI;
import com.dreamfirestudios.dreamcore.DreamVariable.DreamEnumVariableTest;
import com.dreamfirestudios.scytheplugin.API.ScythePluginAPI;
import com.dreamfirestudios.scytheplugin.Core.Services;
import com.dreamfirestudios.scytheplugin.Core.VersionChecks;
import com.dreamfirestudios.scytheplugin.Enum.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Main plugin entry.
 */
public class ScythePlugin extends JavaPlugin {
    private static ScythePlugin ScythePlugin;

    /**
     * Global accessor for the plugin instance.
     * @return active plugin
     */
    public static ScythePlugin GetScythePlugin(){
        return Objects.requireNonNull(ScythePlugin, "plugin not initialized yet");
    }

    /** Bootstrap on enable. */
    @Override
    public void onEnable() {
        ScythePlugin = this;
        Services.bootstrap(this);
        VersionChecks.logPlatformInfo(getLogger());
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(ScythePluginFeatureFlagKey.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(ScythePluginInventoryItems.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(ScythePluginMessages.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(ScythePluginPermissionLevel.class));
        DreamClassAPI.RegisterPulseVariableTest(this, new DreamEnumVariableTest<>(ScythePluginPermissions.class));
        ScythePluginAPI.ScythePluginReloadConfigs(DreamMessageSettings.all());
        DreamClassAPI.RegisterClasses(this);
        DreamCommand.RegisterRaw(this);
    }
}
