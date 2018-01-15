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
class UsernameSection {
    @Setting(value = "valid-characters-regex", comment = "What characters are allowed in username?")
    var regex = "[a-zA-Z0-9_]*"
        private set

    @Setting(value = "valid-length", comment = "What should be the max length of an username?")
    var length = 16
        private set
}