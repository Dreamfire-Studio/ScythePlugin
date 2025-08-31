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
import org.bukkit.entity.Player;


import java.util.*;
import java.util.stream.Collectors;


/** Resolution helpers for online players. */
public final class PlayerResolver {
    private PlayerResolver() { }


    public static Optional<Player> byName(final String name) {
        if (name == null) return Optional.empty();
        final Player p = Bukkit.getPlayerExact(name);
        return Optional.ofNullable(p);
    }


    public static Optional<Player> byUuid(final UUID id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(Bukkit.getPlayer(id));
    }


    public static List<Player> onlineWithPermission(final String node) {
        if (node == null || node.isBlank()) return List.of();
        return Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission(node)).collect(Collectors.toList());
    }
}