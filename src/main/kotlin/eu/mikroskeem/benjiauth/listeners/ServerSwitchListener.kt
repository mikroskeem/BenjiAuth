/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.listeners

import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.message
import eu.mikroskeem.benjiauth.messages
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority.HIGHEST

/**
 * @author Mark Vainomaa
 */
class ServerSwitchListener: Listener {
    @EventHandler(priority = HIGHEST)
    fun on(event: ServerConnectEvent) {
        if(event.isCancelled)
            return

        if(!config.servers.denySwitchingWhenUnauthenticated)
            return

        // Skip if current server is the same as target
        if(event.player.server?.info == event.target)
            return

        // Check if player is not authenticated and target server is not auth server
        if(!event.player.isLoggedIn && event.target.name != config.servers.authServer) {
            if(!event.player.isRegistered) {
                event.player.message(messages.register.mustRegisterBeforeSwitchingServers)
            } else {
                event.player.message(messages.login.mustLoginBeforeSwitchingServers)
            }
            event.isCancelled = true
        }
    }
}