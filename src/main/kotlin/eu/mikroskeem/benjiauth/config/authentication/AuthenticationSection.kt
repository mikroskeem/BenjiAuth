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