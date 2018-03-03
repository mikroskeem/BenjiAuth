/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Player unregister event
 *
 * @author Mark Vainomaa
 */
public final class PlayerUnregisterEvent extends Event {
    private final ProxiedPlayer player;

    /**
     * Constructs new unregister event
     *
     * @param player Player who unregistered
     */
    public PlayerUnregisterEvent(@NotNull ProxiedPlayer player) {
        this.player = player;
    }

    /**
     * Gets player who unregistered
     *
     * @return Player who unregistered
     */
    @NotNull
    public ProxiedPlayer getPlayer() {
        return player;
    }
}
