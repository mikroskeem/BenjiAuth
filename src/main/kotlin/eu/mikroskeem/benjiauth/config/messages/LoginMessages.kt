package eu.mikroskeem.benjiauth.config.messages

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class LoginMessages {
    @Setting
    var pleaseLogin = "{prefix}Logige sisse käsuga &3/login &8<&3parool&8>"
        private set

    @Setting
    var mustLogin = "{prefix}&cSa pead enne end sisse logima!"
        private set

    @Setting
    var mustLoginBeforeChatting = "{prefix}&cEnne vestlusakna kasutamist palun logi sisse"
        private set

    @Setting
    var mustLoginBeforeUsingCommands = "{prefix}&cEnne commandide kasutamist palun logi sisse"
        private set

    @Setting
    var loginTimeout = "{prefix}&cTe ei loginud piisavalt kiiresti sisse"
        private set

    @Setting
    var loggedIn = "{prefix}Oled sisse logitud!"
        private set

    @Setting
    var alreadyLoggedIn = "{prefix}&cTe olete juba sisse logitud!"
        private set

    @Setting
    var loggedOut = "{prefix}Oled välja logitud!"
        private set
}