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

    @Setting(value = "register-notice-interval", comment = "How often should player be told to register (in seconds). " +
            "This value cannot be under 1!")
    var registerNoticeInterval: Long = 10
        private set

    @Setting(value = "max-registrations-per-ip", comment = "How many accounts should be allowed to register per IP" +
            " address")
    var maxRegstrationsPerIP = 1
        private set

    @Setting(value = "new-registrations-disabled", comment = "Should new registrations be disabled? See messages.cfg " +
            "for appropriate message")
    var newRegistrationsDisabled = false
        private set

    @Setting(value = "kick-if-registrations-disablec", comment = "Whether to kick unregistered players on join or not. " +
            "Useful if `new-registrations-disabled` is turned on.")
    var kickIfRegistrationsDisabled = true
        private set
}