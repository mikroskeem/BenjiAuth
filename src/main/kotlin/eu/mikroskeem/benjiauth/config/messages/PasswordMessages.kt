/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.config.messages

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class PasswordMessages {
    @Setting
    var dontMatch = "{prefix}&cParoolid ei ühti!"
        private set

    @Setting
    var wrong = "{prefix}&cVale parool!"
        private set

    @Setting
    var wrongOldPassword = "{prefix}&cSisestasid vale vana parooli!"
        private set

    @Setting
    var changed = "{prefix}Parool vahetatud!"
        private set

    @Setting
    var usernameCannotBeUsed = "{prefix}&cKasutajanime ei saa paroolina kasutada!"
        private set

    @Setting
    var tooLong = "{prefix}&cParool on liiga pikk! Parooli maksimaalne pikkus on &3{max} &ctähemärki!"
        private set

    @Setting
    var tooShort = "{prefix}&cParool on liiga lühike! Parooli minimaalne pikkus on &3{min} &ctähemärki!"
        private set
}