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
 * Player login event
 *
 * @author Mark Vainomaa
 */
public final class PlayerLoginEvent extends Event {
    private final ProxiedPlayer player;
    private final boolean forceLogin;

    /**
     * Constructs new login event
     *
     * @param player Player who logged in
     */
    public PlayerLoginEvent(ProxiedPlayer player) {
        this(player, false);
    }

    /**
     * Constructs new login event
     *
     * @param player Player who logged in
     * @param forceLogin Whether it was a forceful login or not
     */
    public PlayerLoginEvent(ProxiedPlayer player, boolean forceLogin) {
        this.player = player;
        this.forceLogin = forceLogin;
    }

    /**
     * Gets player who logged in
     *
     * @return Player who logged in
     */
    @NotNull
    public ProxiedPlayer getPlayer() {
        return player;
    }

    /**
     * Returns whether login was forceful or not
     *
     * @return Whether login was forceful or not
     */
    public boolean isForceLogin() {
        return forceLogin;
    }
}
