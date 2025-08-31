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
package com.dreamfirestudios.scytheplugin.Core;


import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;


import java.util.Objects;


/** Utilities for version gating and optional dependency checks. */
public final class Platform {
    private Platform() { }


    public static String bukkitVersion() { return Bukkit.getVersion(); }


    public static boolean hasPlugin(final String name) {
        return Bukkit.getPluginManager().getPlugin(Objects.requireNonNull(name, "name")) != null;
    }


    /** naive semver compare: "1.20.1" */
    public static boolean atLeast(final String required) {
        Objects.requireNonNull(required, "required");
        String ver = Bukkit.getBukkitVersion(); // e.g., 1.20.6-R0.1-SNAPSHOT
        int dash = ver.indexOf('-');
        if (dash != -1) ver = ver.substring(0, dash);
        return compare(ver, required) >= 0;
    }


    private static int compare(final String a, final String b) {
        final String[] as = a.split("\\.");
        final String[] bs = b.split("\\.");
        for (int i = 0; i < Math.max(as.length, bs.length); i++) {
            int ai = i < as.length ? parse(as[i]) : 0;
            int bi = i < bs.length ? parse(bs[i]) : 0;
            if (ai != bi) return Integer.compare(ai, bi);
        }
        return 0;
    }


    private static int parse(final String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }
}