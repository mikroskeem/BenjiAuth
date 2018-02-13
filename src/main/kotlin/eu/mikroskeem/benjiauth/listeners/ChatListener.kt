/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.listeners

import eu.mikroskeem.benjiauth.authMessage
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.messages
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority.LOWEST

/**
 * @author Mark Vainomaa
 */
class ChatListener: Listener {
    @EventHandler(priority = LOWEST)
    fun on(event: ChatEvent) {
        if(event.isCancelled)
            return

        val player = event.sender as? ProxiedPlayer ?: return

        if(!player.isLoggedIn) {
            // Check if command is allowed
            if(event.isCommand) {
                val command = event.message.split(" ", limit = 2)[0]
                if(config.authentication.commands.allowedCommands.contains(command))
                    return

                if(!player.isRegistered) {
                    player.authMessage(messages.register.mustRegisterBeforeUsingCommands)
                } else if(!player.isLoggedIn) {
                    player.authMessage(messages.login.mustLoginBeforeUsingCommands)
                }
            } else {
                if(!player.isRegistered) {
                    player.authMessage(messages.register.mustRegisterBeforeChatting)
                } else if(!player.isLoggedIn) {
                    player.authMessage(messages.login.mustLoginBeforeChatting)
                }
            }

            // Cancel chat or commands
            event.isCancelled = true
        }
    }
}