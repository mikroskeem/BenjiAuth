/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.config.servers

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable
import java.util.concurrent.TimeUnit

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

    @Setting(value = "connection-timeout", comment = "Connection timeout in milliseconds. Works only on Waterfall")
    var connectionTimeout = TimeUnit.SECONDS.toMillis(5)
        private set

    @Setting(value = "deny-server-switching-when-unauthenticated", comment = "Whether to deny server switching when " +
            "unauthenticated or not. Recommended to keep this on unless you know what you are doing")
    var denySwitchingWhenUnauthenticated = true
        private set
}