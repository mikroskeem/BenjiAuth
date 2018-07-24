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