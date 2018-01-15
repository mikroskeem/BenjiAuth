/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.listeners

import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isRegistered
import net.md_5.bungee.api.chat.TextComponent
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
        if(!event.connection.name.matches(config.authentication.username.regex.toRegex())) {
            event.setCancelReason(*TextComponent.fromLegacyText("invalid username")) // TODO
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun on(event: PostLoginEvent) {
        val player = event.player
        if(player.isRegistered) {
            // TODO: ask for a password
        }
    }
}