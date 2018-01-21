package eu.mikroskeem.benjiauth.config.messages

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class RegisterMessages {
    @Setting
    var pleaseRegister = "{prefix}Registreerige end käsuga &3/register &8<&3parool&8> <&3parool uuesti&8>"
        private set

    @Setting
    var mustRegister = "{prefix}&cSa pead enne end registreerima!"
        private set

    @Setting
    var mustRegisterBeforeChatting = "{prefix}&cEnne vestlusakna kasutamist peate te end registeerima"
        private set

    @Setting
    var mustRegisterBeforeUsingCommands = "{prefix}&cEnne commandide kasutamist peate te end registeerima"
        private set

    @Setting
    var registerTimeout = "{prefix}&cTe ei registreerinud piisavalt kiiresti"
        private set

    @Setting
    var registered = "{private}Registreerisid edukalt!"
        private set

    @Setting
    var registeredAndMustLogin = "{private}Registreerisid edukalt, nüüd logi sisse!"
        private set

    @Setting
    var alreadyRegistered = "{prefix}&cTe olete juba registreeritud!"
        private set
}