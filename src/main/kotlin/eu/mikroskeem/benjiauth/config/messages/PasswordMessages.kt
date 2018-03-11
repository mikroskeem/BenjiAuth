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
    var dontMatch = "{prefix}&cParoolid ei ühti!"
        private set

    @Setting(value = "wrong", comment = "This message gets sent when password is wrong")
    var wrong = "{prefix}&cVale parool!"
        private set

    @Setting(value = "wrong-title", comment = "This message gets sent when password is wrong. Set both title and " +
            "subtitle empty to avoid sending it")
    var wrongTitle = Title("", "", 20, 45, 20)
        private set

    @Setting(value = "wrong-old-password", comment = "This message gets sent on /changepassword when old password is wrong")
    var wrongOldPassword = "{prefix}&cSisestasid vale vana parooli!"
        private set

    @Setting(value = "password-changed", comment = "This message gets sent when password got changed successfully")
    var changed = "{prefix}Parool vahetatud!"
        private set

    @Setting(value = "username-can-not-be-used", comment = "This message gets sent when player tries to change its " +
            "password to its username")
    var usernameCannotBeUsed = "{prefix}&cKasutajanime ei saa paroolina kasutada!"
        private set

    @Setting(value = "too-long", comment = "This message gets sent when player tries to use too long password")
    var tooLong = "{prefix}&cParool on liiga pikk! Parooli maksimaalne pikkus on &3{max} &ctähemärki!"
        private set

    @Setting(value = "too-short", comment = "This message gets sent when player tries to use too short password")
    var tooShort = "{prefix}&cParool on liiga lühike! Parooli minimaalne pikkus on &3{min} &ctähemärki!"
        private set
}