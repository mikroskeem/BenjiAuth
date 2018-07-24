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
    var pleaseLogin = "{prefix}Log in using command &c&l/login &8<&cpassword&8>"
        private set

    @Setting(value = "please-login-title", comment = "This message gets sent to player repeatedly until one gets" +
            "registered. Set both title and subtitle empty to avoid sending it. Timings will be ignored")
    var pleaseLoginTitle = Title("", "")
        private set

    @Setting(value = "must-login", comment = "This message gets sent when player is doing something while not " +
            "being logged in")
    var mustLogin = "{prefix}&cYou must log in first!"
        private set

    @Setting(value = "must-login-before-chatting", comment = "This message gets sent when player is chatting while not " +
            "being logged in")
    var mustLoginBeforeChatting = "{prefix}&cYou must log in before chatting!"
        private set

    @Setting(value = "must-login-before-using-commands", comment = "This message gets sent when player is using " +
            " commands while not being logged in")
    var mustLoginBeforeUsingCommands = "{prefix}&cYou must log in before using commands!"
        private set

    @Setting(value = "must-login-before-switching-server", comment = "This message gets sent when player is trying " +
            " to switch servers while not being logged in")
    var mustLoginBeforeSwitchingServers = "{prefix}&cYou must log in before switching servers!"
        private set

    @Setting(value = "login-timeout", comment = "This message gets sent when player doesn't log in fast enough " +
            "(iow hits login timeout)")
    var loginTimeout = "{prefix}&cYou didn't log in fast enough!"
        private set

    @Setting(value = "logged-in", comment = "This message gets sent when player logs in successfully")
    var loggedIn = "{prefix}You are logged in!"
        private set

    @Setting(value = "already-logged-in", comment = "This message gets sent when player is already logged in")
    var alreadyLoggedIn = "{prefix}&cYou are already logged in!"
        private set

    @Setting(value = "logged-out", comment = "This message gets sent when player is logged out successfully")
    var loggedOut = "{prefix}You are logged out!"
        private set
}