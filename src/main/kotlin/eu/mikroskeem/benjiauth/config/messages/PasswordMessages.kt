/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2020 Mark Vainomaa <mikroskeem@mikroskeem.eu>
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

package eu.mikroskeem.benjiauth.config.messages

import eu.mikroskeem.benjiauth.Title
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class PasswordMessages {
    @Setting(value = "do-not-match", comment = "This message gets sent when passwords don't match on /changepassword")
    var dontMatch = "{prefix}&cPasswords don't match!"
        private set

    @Setting(value = "wrong", comment = "This message gets sent when password is wrong")
    var wrong = "{prefix}&cWrong password!"
        private set

    @Setting(value = "wrong-title", comment = "This message gets sent when password is wrong. Set both title and " +
            "subtitle empty to avoid sending it")
    var wrongTitle = Title("", "", 20, 45, 20)
        private set

    @Setting(value = "wrong-old-password", comment = "This message gets sent on /changepassword when old password is wrong")
    var wrongOldPassword = "{prefix}&cYou inserted wrong old password!"
        private set

    @Setting(value = "password-changed", comment = "This message gets sent when password got changed successfully")
    var changed = "{prefix}Password changed!"
        private set

    @Setting(value = "username-can-not-be-used", comment = "This message gets sent when player tries to change its " +
            "password to its username")
    var usernameCannotBeUsed = "{prefix}&cUsername cannot be used as a password!"
        private set

    @Setting(value = "too-long", comment = "This message gets sent when player tries to use too long password")
    var tooLong = "{prefix}&cPassword is too long! Password cannot be longer than &c&l{max} &ccharacters!"
        private set

    @Setting(value = "too-short", comment = "This message gets sent when player tries to use too short password")
    var tooShort = "{prefix}&cPassword is too short! Password cannot be shorter than &c&l{min} &ccharacters!"
        private set

    @Setting(value = "recovery-attempt-invalidated", comment = "This message gets sent when player logs in with password while password was attempted to recover.\n" +
            "Note that this only applies to manual logins, automatic logins won't clear the code")
    var recoveryAttemptInvalidated = "{prefix}Password recovery cancelled invalidated as you logged in with a password"
        private set
}