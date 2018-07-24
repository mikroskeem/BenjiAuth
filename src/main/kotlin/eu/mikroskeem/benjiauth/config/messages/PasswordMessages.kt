/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
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
    var tooShort = "{prefix}&cPassword is too short! Password cannot be shorter than &c&l{max} &ccharacters!"
        private set
}