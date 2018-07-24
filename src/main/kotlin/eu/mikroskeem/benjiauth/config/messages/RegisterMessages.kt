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
class RegisterMessages {
    @Setting(value = "please-register", comment = "This message gets sent to player repeatedly until one gets registered")
    var pleaseRegister = "{prefix}Register using command &c&l/register &8<&cpassword&8> <&cpassword again&8>"
        private set

    @Setting(value = "please-register-title", comment = "This message gets sent to player repeatedly until one gets" +
            "registered. Set both title and subtitle empty to avoid sending it. Timings will be ignored")
    var pleaseRegisterTitle = Title("", "")
        private set

    @Setting(value = "must-register", comment = "This message gets sent when player is doing something while not " +
            "being registered")
    var mustRegister = "{prefix}&cYou must register first!"
        private set

    @Setting(value = "must-register-before-chatting", comment = "This message gets sent when player is chatting while not " +
            "being registered")
    var mustRegisterBeforeChatting = "{prefix}&cYou must register before chatting!"
        private set

    @Setting(value = "must-register-before-using-commands", comment = "This message gets sent when player is using " +
            " commands while not being registered")
    var mustRegisterBeforeUsingCommands = "{prefix}&cYou must register before using commands!"
        private set

    @Setting(value = "must-register-before-switching-server", comment = "This message gets sent when player is trying " +
            " to switch servers while not being registered")
    var mustRegisterBeforeSwitchingServers = "{prefix}&cYou must register before switching servers!"
        private set

    @Setting(value = "register-timeout", comment = "This message gets sent when player doesn't register fast enough " +
            "(iow hits register timeout)")
    var registerTimeout = "{prefix}&cYou didn't register fast enough!"
        private set

    @Setting(value = "registered", comment = "This message gets sent when player gets registered successfully")
    var registered = "{prefix}You registered successfully!"
        private set

    @Setting(value = "registered-now-log-in", comment = "This message gets sent when player gets registered successfully, " +
            "but has to log in after that")
    var registeredAndMustLogin = "{private}You registered successfully, now log in!"
        private set

    @Setting(value = "already-registered", comment = "This message gets sent when player is already registered")
    var alreadyRegistered = "{prefix}&cYou are already registered!"
        private set

    @Setting(value = "new-registrations-disabled", comment = "This message gets sent to player if registrations are " +
            "disabled")
    var newRegistrationsDisabled = "{prefix}Registering is turned off right now, please try again later!"
        private set
}