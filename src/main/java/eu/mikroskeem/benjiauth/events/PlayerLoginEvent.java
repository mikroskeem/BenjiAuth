/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

/**
 * @author Mark Vainomaa
 */
public final class PlayerLoginEvent extends Event {
    private final ProxiedPlayer player;
    private final boolean forceLogin;

    public PlayerLoginEvent(ProxiedPlayer player) {
        this(player, false);
    }

    public PlayerLoginEvent(ProxiedPlayer player, boolean forceLogin) {
        this.player = player;
        this.forceLogin = forceLogin;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public boolean isForceLogin() {
        return forceLogin;
    }
}
