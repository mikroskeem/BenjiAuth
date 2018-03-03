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
 * Player logout event
 *
 * @author Mark Vainomaa
 */
public final class PlayerLogoutEvent extends Event {
    private final ProxiedPlayer player;

    /**
     * Constructs new logout event
     *
     * @param player Player who logged out
     */
    public PlayerLogoutEvent(@NotNull ProxiedPlayer player) {
        this.player = player;
    }

    /**
     * Gets player who logged out
     *
     * @return Player who logged out
     */
    @NotNull
    public ProxiedPlayer getPlayer() {
        return player;
    }
}
