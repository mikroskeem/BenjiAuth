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
class AuthenticationSection {
    @Setting(value = "username", comment = "Username configuration")
    var username = UsernameSection()
        private set

    @Setting(value = "commands", comment = "Commands configuration")
    var commands = CommandsSection()
        private set

    @Setting(value = "max-login-retries", comment = "How many login attempts are allowed before getting kicked? 0 = kick instantly on wrong password")
    var maxLoginRetries = 0
        private set

    @Setting(value = "login-timeout", comment = "How much does player have time in seconds to register or login before getting kicked")
    var authTimeout: Long = 60
        private set

    @Setting(value = "session-timeout", comment = "How long should player session last (in minutes?). Player does not " +
            "have to re-enter its password when one connects from same IP. Set to 0 to disable session support")
    var sessionTimeout: Long = 0
        private set

    @Setting(value = "login-notice-interval", comment = "How often should player be told to log in (in seconds)." +
            "This value cannot be under 1!")
    var loginNoticeInterval: Long = 10
        private set
}