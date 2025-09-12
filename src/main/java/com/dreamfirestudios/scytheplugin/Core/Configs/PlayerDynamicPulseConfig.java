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
package com.dreamfirestudios.scytheplugin.Core.Configs;

import com.dreamfirestudios.scytheplugin.ScythePlugin;
import com.dreamfirestudios.dreamconfig.Abstract.DynamicPulseConfig;
import com.mongodb.lang.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class PlayerDynamicPulseConfig extends DynamicPulseConfig<PlayerDynamicPulseConfig> {

    /**
     * Retrieves (or creates) a {@link PlayerDynamicPulseConfig} for the specified player.
     *
     * @param player           the target player (not null)
     * @param overrideExisting if true, creates a new config even if one exists in memory
     * @param onSuccess        callback executed when the config is loaded (not null)
     */
    public static void GetConfigForPlayer(Player player, boolean overrideExisting, Consumer<PlayerDynamicPulseConfig> onSuccess) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(onSuccess, "onSuccess");
        DynamicPulseConfig.GetConfig(ScythePlugin.GetInstance(), PlayerDynamicPulseConfig.class, player.getUniqueId().toString(), overrideExisting, onSuccess);
    }

    /**
     * Default constructor. Generates a random unique config ID.
     */
    protected PlayerDynamicPulseConfig() {
        super();
    }

    /**
     * Constructs a config with a specific player ID string.
     *
     * @param playerId the player UUID as a string (not null or empty)
     */
    protected PlayerDynamicPulseConfig(String playerId) {
        super(Objects.requireNonNull(playerId, "playerId"));
    }

    /**
     * Constructs a config for a specific player UUID.
     *
     * @param playerUUID the player UUID (not null)
     */
    public PlayerDynamicPulseConfig(UUID playerUUID) {
        this(Objects.requireNonNull(playerUUID, "playerUUID").toString());
    }

    /**
     * Constructs a config for a specific player.
     *
     * @param player the player (not null)
     */
    public PlayerDynamicPulseConfig(Player player) {
        this(Objects.requireNonNull(player, "player").getUniqueId().toString());
    }

    /**
     * @return the player UUID represented by this config
     */
    public UUID getPlayerId() {
        return UUID.fromString(documentID());
    }

    /**
     * @return the online {@link Player} for this config, or {@code null} if offline
     */
    public @Nullable Player getPlayer(){
        return Bukkit.getPlayer(getPlayerId());
    }

    /**
     * @return the {@link OfflinePlayer} for this config (never null)
     */
    public OfflinePlayer getOfflinePlayer(){
        return Bukkit.getOfflinePlayer(getPlayerId());
    }
}