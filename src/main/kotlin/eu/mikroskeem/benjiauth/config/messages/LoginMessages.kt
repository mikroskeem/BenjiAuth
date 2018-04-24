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
class LoginMessages {
    @Setting(value = "please-login", comment = "This message gets sent to player repeatedly until one logs in")
    var pleaseLogin = "{prefix}Logige sisse käsuga &f/login &8<&fparool&8>"
        private set

    @Setting(value = "please-login-title", comment = "This message gets sent to player repeatedly until one gets" +
            "registered. Set both title and subtitle empty to avoid sending it. Timings will be ignored")
    var pleaseLoginTitle = Title("", "")
        private set

    @Setting(value = "must-login", comment = "This message gets sent when player is doing something while not " +
            "being logged in")
    var mustLogin = "{prefix}&cSa pead enne end sisse logima!"
        private set

    @Setting(value = "must-login-before-chatting", comment = "This message gets sent when player is chatting while not " +
            "being logged in")
    var mustLoginBeforeChatting = "{prefix}&cEnne vestlusakna kasutamist palun logi sisse"
        private set

    @Setting(value = "must-login-before-using-commands", comment = "This message gets sent when player is using " +
            " commands while not being logged in")
    var mustLoginBeforeUsingCommands = "{prefix}&cEnne commandide kasutamist palun logi sisse"
        private set

    @Setting(value = "must-login-before-switching-server", comment = "This message gets sent when player is trying " +
            " to switch servers while not being logged in")
    var mustLoginBeforeSwitchingServers = "{prefix}&cEnne serverite vahetamist palun logi sisse"
        private set

    @Setting(value = "login-timeout", comment = "This message gets sent when player doesn't log in fast enough " +
            "(iow hits login timeout)")
    var loginTimeout = "{prefix}&cTe ei loginud piisavalt kiiresti sisse"
        private set

    @Setting(value = "logged-in", comment = "This message gets sent when player logs in successfully")
    var loggedIn = "{prefix}Oled sisse logitud!"
        private set

    @Setting(value = "already-logged-in", comment = "This message gets sent when player is already logged in")
    var alreadyLoggedIn = "{prefix}&cOled juba sisse logitud!"
        private set

    @Setting(value = "logged-out", comment = "This message gets sent when player is logged out successfully")
    var loggedOut = "{prefix}Oled välja logitud!"
        private set
}