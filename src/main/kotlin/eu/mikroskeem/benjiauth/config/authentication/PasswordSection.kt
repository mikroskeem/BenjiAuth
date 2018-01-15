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
class PasswordSection {
    @Setting(value = "minimum-length", comment = "How short password can user have?")
    var minimumLength = 8
        private set

    @Setting(value = "maximum-length", comment = "How long password can user have?")
    var maximumLength = 32
        private set

    @Setting(value = "disallow-using-username-as-a-password", comment = "Whether using username as a password is allowed or not")
    var disallowUsingUsername = true
        private set
}