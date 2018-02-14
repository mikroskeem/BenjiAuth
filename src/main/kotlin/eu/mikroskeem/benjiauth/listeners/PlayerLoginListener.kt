/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.listeners

import eu.mikroskeem.benjiauth.authKickMessage
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.getAuthServer
import eu.mikroskeem.benjiauth.isAllowedToJoin
import eu.mikroskeem.benjiauth.isEgilibleForSession
import eu.mikroskeem.benjiauth.isForcefullyLoggedIn
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.loginWithoutPassword
import eu.mikroskeem.benjiauth.logout
import eu.mikroskeem.benjiauth.markReady
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.processMessage
import eu.mikroskeem.benjiauth.tasks.LoginMessageTask
import eu.mikroskeem.benjiauth.tasks.RegisterMessageTask
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.PreLoginEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority

/**
 * @author Mark Vainomaa
 */
class PlayerLoginListener: Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun on(event: PreLoginEvent) {
        if(!event.connection.address.isAllowedToJoin()) {
            event.setCancelReason(*messages.error.ipAddressDisallowed.processMessage())
            event.isCancelled = true
            return
        }

        if(!(
                        event.connection.name.length <= 16 &&
                        event.connection.name.matches(config.authentication.username.regex.toRegex())
        )) {
            event.setCancelReason(*messages.error.invalidUsername.processMessage())
            event.isCancelled = true
            return
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun on(event: PostLoginEvent) {
        val player = event.player

        if(player.isRegistered) {
            // Check if user isn't actually logged in (plugin failed to set login status previously?)
            if(player.isLoggedIn && !player.isForcefullyLoggedIn) {
                player.logout(clearSession = false)
            }

            if(!player.isLoggedIn && player.isEgilibleForSession) {
                // Mark player ready
                player.markReady()

                // Note: one may ask why login is done here again. That is to update last IP address and
                //       timestamp
                player.loginWithoutPassword()
                return
            }
        }

        // Mark player ready
        player.markReady()

        // Start login/register message/timeout task
        if(!player.isRegistered) {
            RegisterMessageTask(player).schedule()
        } else if(!player.isLoggedIn) {
            LoginMessageTask(player).schedule()
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun on(event: ServerConnectEvent) {
        val player = event.player
        val target = event.target

        // Check if player is logged in
        if(!player.isLoggedIn) {
            // Get authentication server info
            val auth: ServerInfo = getAuthServer {
                event.player.authKickMessage(messages.error.couldntConnectToAuthserver)
            }

            // Set target server
            if(target.name != auth.name) {
                event.target = auth
            }
        }
    }
}