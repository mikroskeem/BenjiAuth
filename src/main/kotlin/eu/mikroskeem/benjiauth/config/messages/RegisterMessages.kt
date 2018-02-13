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
class RegisterMessages {
    @Setting(value = "please-register", comment = "This message gets sent to player repeatedly until one gets registered")
    var pleaseRegister = "{prefix}Registreerige end käsuga &3/register &8<&3parool&8> <&3parool uuesti&8>"
        private set

    @Setting(value = "must-register", comment = "This message gets sent when player is doing something while not " +
            "being registered")
    var mustRegister = "{prefix}&cSa pead enne end registreerima!"
        private set

    @Setting(value = "must-register-before-chatting", comment = "This message gets sent when player is chatting while not " +
            "being registered")
    var mustRegisterBeforeChatting = "{prefix}&cEnne vestlusakna kasutamist peate te end registeerima"
        private set

    @Setting(value = "must-register-before-using-commands", comment = "This message gets sent when player is using " +
            " commands while not being registered")
    var mustRegisterBeforeUsingCommands = "{prefix}&cEnne commandide kasutamist peate te end registeerima"
        private set

    @Setting(value = "register-timeout", comment = "This message gets sent when player doesn't register fast enough " +
            "(iow hits register timeout)")
    var registerTimeout = "{prefix}&cTe ei registreerinud piisavalt kiiresti"
        private set

    @Setting(value = "registered", comment = "This message gets sent when player gets registered successfully")
    var registered = "{prefix}Registreerisid edukalt!"
        private set

    @Setting(value = "registered-now-log-in", comment = "This message gets sent when player gets registered successfully, " +
            "but has to log in after that")
    var registeredAndMustLogin = "{private}Registreerisid edukalt, nüüd logi sisse!"
        private set

    @Setting(value = "already-registered", comment = "This message gets sent when player is already registered")
    var alreadyRegistered = "{prefix}&cTe olete juba registreeritud!"
        private set
}