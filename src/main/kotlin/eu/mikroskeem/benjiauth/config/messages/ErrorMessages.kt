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

package eu.mikroskeem.benjiauth.config.messages

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class ErrorMessages {
    @Setting(value = "in-game-use-only", comment = "Sent when command requires player")
    var inGameUseOnly = "{prefix}&cGiven command is for in-game players only!"
        private set

    @Setting(value = "invalid-username", comment = "Player gets kicked with this message if username is invalid")
    var invalidUsername = "{prefix}&cInvalid username, pick something else!"
        private set

    @Setting(value = "use-correctly-written-username", comment = "Player gets kicked with this message if username is written in" +
            " different case than originally registered with")
    var writeUsernameCorrectly = "{prefix}&cWrite your username correctly!"
        private set

    @Setting(value = "ip-address-blocked", comment = "Player gets kicked with this message if IP address is blacklisted")
    var ipAddressDisallowed = "{prefix}&cYour IP address is blocked in this server."
        private set

    @Setting(value = "could-not-connect-to-lobby", comment = "Player gets kicked with this message if lobby is down")
    var couldntConnectToLobby = "{prefix}&cCould not connect to the lobby!"
        private set

    @Setting(value = "could-not-connect-to-auth-server", comment = "Player gets kicked with this message if auth server is down")
    var couldntConnectToAuthserver = "{prefix}&cCould not connect to the auth server!"
        private set

    @Setting(value = "unknown-subcommand", comment = "Sent when unknown subcommand is used")
    var unknownSubcommand = "{prefix}&cUnknown subcommand &c&l{subcommand}&c!"
        private set

    @Setting(value = "too-many-registrations", comment = "Sent when player's IP has too many registered users")
    var tooManyRegistrationsPerIP = "{prefix}There are too many registrations on given IP address you joined with!"
        private set

    @Setting(value = "no-such-player", comment = "Sent when command targets player not online on proxy")
    var noSuchPlayer = "{prefix}&cPlayer &c&l{player} &cis not online!"
        private set

    @Setting(value = "user-not-registered", comment = "Sent when command targets unregistered user")
    var userNotRegistered = "{prefix}&cPlayer &c&l{player} &cis not registered!"
        private set

    @Setting(value = "user-already-logged-in", comment = "Sent when command targets already logged in player")
    var userAlreadyLoggedIn = "{prefix}&cPlayer &c&l{player} &cis already registered!"
        private set

    @Setting(value = "user-not-logged-in", comment = "Sent when command targets user who isn't logged in")
    var userNotLoggedIn = "{prefix}&cPlayer &c&l{player} &cis not logged in!"
        private set
}