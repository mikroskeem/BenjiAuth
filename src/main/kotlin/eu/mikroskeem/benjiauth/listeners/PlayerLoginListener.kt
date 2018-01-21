/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.listeners

import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isAllowedToJoin
import eu.mikroskeem.benjiauth.isEgilibleForSession
import eu.mikroskeem.benjiauth.isForcefullyLoggedIn
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.loginWithoutPassword
import eu.mikroskeem.benjiauth.logout
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.processMessage
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.PreLoginEvent
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

        if(!event.connection.name.matches(config.authentication.username.regex.toRegex())) {
            event.setCancelReason(*messages.error.invalidUsername.processMessage())
            event.isCancelled = true
            return
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun on(event: PostLoginEvent) {
        val player = event.player

        if(player.isRegistered) {
            // Check if user isn't actually logged in (plugin failed to set login status)
            if(player.isLoggedIn && !player.isForcefullyLoggedIn) {
                player.logout()
            }

            if(!player.isLoggedIn && player.isEgilibleForSession) {
                // Note: one may ask why login is done here again. That is to update last IP address and
                //       timestamp
                player.loginWithoutPassword()
            }
        }
    }
}