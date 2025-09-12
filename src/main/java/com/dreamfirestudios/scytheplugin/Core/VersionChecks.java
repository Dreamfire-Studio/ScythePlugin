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
package com.dreamfirestudios.scytheplugin.Core;

import org.bukkit.Bukkit;

import java.util.logging.Logger;

/**
 * Utility class for detecting and reporting server platform/version details.
 */
public final class VersionChecks {
    private static final String SERVER_VERSION = Bukkit.getServer().getVersion();
    private static final String BUKKIT_VERSION = Bukkit.getBukkitVersion();

    private VersionChecks() {}

    /** @return raw server version string (e.g., "git-Paper-123 (MC: 1.21.1)") */
    public static String getServerVersion() {
        return SERVER_VERSION;
    }

    /** @return raw Bukkit API version (e.g., "1.21.1-R0.1-SNAPSHOT") */
    public static String getBukkitVersion() {
        return BUKKIT_VERSION;
    }

    /** Detects if running on Paper. */
    public static boolean isPaper() {
        try {
            Class.forName("io.papermc.paper.plugin.configuration.PluginMeta");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /** Detects if running on Folia. */
    public static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.FoliaGlobalRegionScheduler");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /** Check if the server is at least the given Minecraft version. */
    public static boolean isAtLeast(int major, int minor) {
        String[] parts = BUKKIT_VERSION.split("\\.");
        int srvMajor = Integer.parseInt(parts[0]);
        int srvMinor = Integer.parseInt(parts[1]);
        return (srvMajor > major) || (srvMajor == major && srvMinor >= minor);
    }

    /**
     * Logs detected platform information at plugin startup.
     *
     * @param logger plugin logger
     */
    public static void logPlatformInfo(Logger logger) {
        logger.info("Detected server: " + SERVER_VERSION);
        logger.info("Bukkit version: " + BUKKIT_VERSION);

        if (isPaper()) {
            logger.info("Platform: Paper");
        } else if (isFolia()) {
            logger.info("Platform: Folia");
        } else {
            logger.info("Platform: Bukkit/Spigot (or derivative)");
        }
    }
}