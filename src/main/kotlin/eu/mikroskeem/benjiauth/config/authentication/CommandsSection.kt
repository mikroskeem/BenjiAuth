/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.config.authentication

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class CommandsSection {
    @Setting(value = "unauthenticated-allowed-commands", comment = "What commands are allowed for unauthenticated?")
    var allowedCommands = listOf(
            "/login",
            "/l",
            "/register"
    )
        private set
}