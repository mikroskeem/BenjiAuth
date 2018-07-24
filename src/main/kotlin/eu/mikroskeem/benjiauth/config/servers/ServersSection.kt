/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 Mark Vainomaa <mikroskeem@mikroskeem.eu>
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