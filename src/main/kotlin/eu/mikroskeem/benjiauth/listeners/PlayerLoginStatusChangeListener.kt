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
import eu.mikroskeem.benjiauth.events.PlayerLoginEvent
import eu.mikroskeem.benjiauth.events.PlayerLogoutEvent
import eu.mikroskeem.benjiauth.events.PlayerRegisterEvent
import eu.mikroskeem.benjiauth.events.PlayerUnregisterEvent
import eu.mikroskeem.benjiauth.findServer
import eu.mikroskeem.benjiauth.getAuthServer
import eu.mikroskeem.benjiauth.kickWithMessage
import eu.mikroskeem.benjiauth.message
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.movePlayer
import eu.mikroskeem.benjiauth.plugin
import eu.mikroskeem.benjiauth.resetTitle
import eu.mikroskeem.benjiauth.shouldBeSent
import eu.mikroskeem.benjiauth.tasks.LoginMessageTask
import eu.mikroskeem.benjiauth.tasks.RegisterMessageTask
import net.md_5.bungee.api.ServerConnectRequest
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

/**
 * @author Mark Vainomaa
 */
class PlayerLoginStatusChangeListener: Listener {
    @EventHandler
    fun on(event: PlayerRegisterEvent) {
        // Clear title if player got spammed with them before registering in
        if(messages.register.pleaseRegisterTitle.shouldBeSent())
            event.player.resetTitle()

        // Start login message/timeout task if player is required to log in manually after registering
        if(!config.registration.loginAfterRegister)
            LoginMessageTask(event.player).schedule()
    }

    @EventHandler
    fun on(event: PlayerLoginEvent) {
        // Send player to lobby
        val lobby: ServerInfo = config.servers.lobbyServer.takeUnless { it.isEmpty() }?.run(::findServer) ?:
                                findServer(event.player.pendingConnection.listener.serverPriority[0])!!

        // Clear title if player got spammed with them before logging in
        if(messages.login.pleaseLoginTitle.shouldBeSent())
            event.player.resetTitle()

        // Return if player is already in lobby server
        if(event.player.server?.info?.name == lobby.name)
            return

        event.player.movePlayer(lobby) { result, e ->
            when(result) {
                ServerConnectRequest.Result.SUCCESS,
                ServerConnectRequest.Result.EVENT_CANCEL, // HOPE FOR THE BEST
                ServerConnectRequest.Result.ALREADY_CONNECTING,
                ServerConnectRequest.Result.ALREADY_CONNECTED
                -> {
                    // Do not do anything
                    return@movePlayer
                }
                ServerConnectRequest.Result.FAIL -> {
                    if(config.servers.kickIfLobbyIsDown) {
                        event.player.kickWithMessage(messages.error.couldntConnectToLobby)
                    } else {
                        event.player.message(messages.error.couldntConnectToLobby)
                    }
                    plugin.pluginLogger.error("Couldn't connect logged in player ${event.player.name} to lobby", e)
                }
            }
        }
    }

    @EventHandler
    fun on(event: PlayerLogoutEvent) {
        // Get auth server
        val auth: ServerInfo = getAuthServer {
            event.player.kickWithMessage(messages.error.couldntConnectToAuthserver)
        }

        // Start login message/timeout task
        LoginMessageTask(event.player).schedule()

        // Return if player is already in auth server
        if(event.player.server?.info?.name == auth.name)
            return

        // Send player to auth server
        event.player.movePlayer(auth) { result, e ->
            if(result == ServerConnectRequest.Result.FAIL) {
                event.player.kickWithMessage(messages.error.couldntConnectToAuthserver)
                plugin.pluginLogger.error("Couldn't connect logged in player ${event.player.name} to auth server", e)
            }
        }
    }

    @EventHandler
    fun on(event: PlayerUnregisterEvent) {
        // Get auth server
        val auth: ServerInfo = getAuthServer {
            event.player.kickWithMessage(messages.error.couldntConnectToAuthserver)
        }

        // Start register message/timeout task
        RegisterMessageTask(event.player).schedule()

        // Return if player is already in auth server
        if(event.player.server?.info?.name == auth.name)
            return

        event.player.movePlayer(auth) { result, e ->
            if(result == ServerConnectRequest.Result.FAIL) {
                event.player.kickWithMessage(messages.error.couldntConnectToAuthserver)
                plugin.pluginLogger.error("Couldn't connect logged in player ${event.player.name} to auth server", e)
            }
        }
    }
}