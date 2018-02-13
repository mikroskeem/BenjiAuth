/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.config.servers

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class ServersSection {
    @Setting(value = "authentication-server", comment = "Server where players are sent first for authentication")
    var authServer = "authlimbo"
        private set

    @Setting(value = "lobby-server", comment = "Server where players are sent after authentication. By default " +
            "it's empty to follow BungeeCord's default fallback cycle")
    var lobbyServer = ""
        private set

    @Setting(value = "kick-if-lobby-is-down", comment = "Whether to kick or not kick player from proxy if lobby server " +
            "is down")
    var kickIfLobbyIsDown = true
        private set
}