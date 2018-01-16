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

    @Setting(value = "password", comment = "Password configuration")
    var password = PasswordSection()
        private set

    @Setting(value = "commands", comment = "Commands configuration")
    var commands = CommandsSection()
        private set

    @Setting(value = "max-login-retries", comment = "How many login attempts are allowed before getting kicked? 0 = kick instantly on wrong password")
    var maxLoginRetries = 0
        private set

    @Setting(value = "login-timeout", comment = "How much does player have time to register or login before getting kicked")
    var authTimeout = 60
        private set

    @Setting(value = "bcrypt-rounds", comment = "How many rounds should BCrypt have? Valid values are 2-30")
    var bcryptRounds = 10
        private set

    @Setting(value = "mark-user-logged-in-after-register", comment = "Whether to mark user logged in or not after successful registering")
    var loginAfterRegister = true
        private set
}