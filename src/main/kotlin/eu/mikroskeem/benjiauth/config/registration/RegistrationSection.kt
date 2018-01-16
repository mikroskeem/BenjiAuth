/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.config.registration

import eu.mikroskeem.benjiauth.config.authentication.PasswordSection
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class RegistrationSection {
    @Setting(value = "password", comment = "Password configuration")
    var password = PasswordSection()
        private set

    @Setting(value = "mark-user-logged-in-after-register", comment = "Whether to mark user logged in or not after successful registering")
    var loginAfterRegister = true
        private set

    @Setting(value = "bcrypt-rounds", comment = "How many rounds should BCrypt have? Valid values are 2-30")
    var bcryptRounds = 10
        private set
}