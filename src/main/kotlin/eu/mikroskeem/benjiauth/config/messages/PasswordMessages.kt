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
}