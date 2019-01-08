/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2019 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package eu.mikroskeem.benjiauth.listeners

import eu.mikroskeem.benjiauth.message
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
                    player.message(messages.register.mustRegisterBeforeUsingCommands)
                } else if(!player.isLoggedIn) {
                    player.message(messages.login.mustLoginBeforeUsingCommands)
                }
            } else {
                if(!player.isRegistered) {
                    player.message(messages.register.mustRegisterBeforeChatting)
                } else if(!player.isLoggedIn) {
                    player.message(messages.login.mustLoginBeforeChatting)
                }
            }

            // Cancel chat or commands
            event.isCancelled = true
        }
    }
}