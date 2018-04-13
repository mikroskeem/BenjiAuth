/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
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
    var inGameUseOnly = "{prefix}&cAntud command on mõeldud mängijatele!"
        private set

    @Setting(value = "invalid-username", comment = "Player gets kicked with this message if username is invalid")
    var invalidUsername = "{prefix}&cVigane kasutajanimi, kasutage midagi muud!"
        private set

    @Setting(value = "ip-address-blocked", comment = "Player gets kicked with this message if IP address is blacklisted")
    var ipAddressDisallowed = "{prefix}&cTeie IP aadress on blokeeritud siin serveris."
        private set

    @Setting(value = "could-not-connect-to-lobby", comment = "Player gets kicked with this message if lobby is down")
    var couldntConnectToLobby = "{prefix}&cEi suutnud peaserveriga ühenduda!"
        private set

    @Setting(value = "could-not-connect-to-auth-server", comment = "Player gets kicked with this message if auth server is down")
    var couldntConnectToAuthserver = "{prefix}&cEi suutnud autentimisserveriga ühenduda!"
        private set

    @Setting(value = "unknown-subcommand", comment = "Sent when unknown subcommand is used")
    var unknownSubcommand = "{prefix}&cTundmatu alamkäsklus &f{subcommand}&c!"
        private set

    @Setting(value = "too-many-registrations", comment = "Sent when player's IP has too many registered users")
    var tooManyRegistrationsPerIP = "{prefix}Sellel IP aadressil on liiga palju kontosid registreeritud!"
        private set

    @Setting(value = "no-such-player", comment = "Sent when command targets player not online on proxy")
    var noSuchPlayer = "{prefix}&cMängija &f{player} &cei ole servers!"
        private set

    @Setting(value = "user-not-registered", comment = "Sent when command targets unregistered user")
    var userNotRegistered = "{prefix}&cMängija &f{player} &cei ole registreeritud!"
        private set

    @Setting(value = "user-already-logged-in", comment = "Sent when command targets already logged in player")
    var userAlreadyLoggedIn = "{prefix}&cMängija &f{player} &con juba sisse logitud!"
        private set

    @Setting(value = "user-not-logged-in", comment = "Sent when command targets user who isn't logged in")
    var userNotLoggedIn = "{prefix}&cMängija &f{player} &cei ole sisse logitud!"
        private set
}