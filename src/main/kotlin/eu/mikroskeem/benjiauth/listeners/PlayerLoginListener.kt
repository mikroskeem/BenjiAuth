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

import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.getAuthServer
import eu.mikroskeem.benjiauth.hook.FastLoginHook
import eu.mikroskeem.benjiauth.isAllowedToJoin
import eu.mikroskeem.benjiauth.isEligibleForSession
import eu.mikroskeem.benjiauth.isForcefullyLoggedIn
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.kickWithMessage
import eu.mikroskeem.benjiauth.loginWithoutPassword
import eu.mikroskeem.benjiauth.logout
import eu.mikroskeem.benjiauth.markReady
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.processMessage
import eu.mikroskeem.benjiauth.tasks.LoginMessageTask
import eu.mikroskeem.benjiauth.tasks.RegisterMessageTask
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.PreLoginEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority

/**
 * @author Mark Vainomaa
 */
class PlayerLoginListener: Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun on(event: PreLoginEvent) {
        if (!event.connection.address.isAllowedToJoin()) {
            event.setCancelReason(*messages.error.ipAddressDisallowed.processMessage())
            event.isCancelled = true
            return
        }

        val username = event.connection.name
        val usernameRegex = config.authentication.username.regex.takeIf { it.isNotEmpty() }?.toRegex()
        if (!(username.length <= 16 && (usernameRegex?.run(username::matches) == true))) {
            event.setCancelReason(*messages.error.invalidUsername.processMessage())
            event.isCancelled = true
            return
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun on(event: PostLoginEvent) {
        val player = event.player

        if (player.isRegistered) {
            // Check if user isn't actually logged in (plugin failed to set login status previously?)
            if (player.isLoggedIn && !player.isForcefullyLoggedIn) {
                player.logout(clearSession = false)
            }

            if (!player.isLoggedIn && player.isEligibleForSession) {
                // Mark player ready
                player.markReady()

                // Note: one may ask why login is done here again. That is to update last IP address and
                //       timestamp
                player.loginWithoutPassword()
                return
            }
        } else {
            // Kick player if new registrations are disabled and plugin is configured to do so
            if (config.registration.newRegistrationsDisabled && config.registration.kickIfRegistrationsDisabled) {
                player.kickWithMessage(messages.register.newRegistrationsDisabled)
                return
            }
        }

        // Mark player ready
        player.markReady()
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun on(event: ServerConnectEvent) {
        val player = event.player
        val target = event.target

        // This listener should only run on initial join
        if (event.reason != ServerConnectEvent.Reason.JOIN_PROXY)
            return

        // Check if player is logged in
        if (!player.isLoggedIn) {
            // Get authentication server info
            val auth: ServerInfo = getAuthServer {
                event.player.kickWithMessage(messages.error.couldntConnectToAuthserver)
            }

            // Set target server
            if (target.name != auth.name) {
                event.target = auth
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun on(event: ServerConnectedEvent) {
        val player = event.player

        // Start login/register message/timeout task
        val delay: Long = if (FastLoginHook.isHooked) 2 else 0
        if (!player.isRegistered) {
            RegisterMessageTask(player, delay = delay).schedule()
        } else if (!player.isLoggedIn) {
            LoginMessageTask(player, delay = delay).schedule()
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun on(event: PlayerDisconnectEvent) {
        // Mark player logged out
        if (event.player.isRegistered)
            event.player.logout(clearSession = false, keepReady = false)
    }
}