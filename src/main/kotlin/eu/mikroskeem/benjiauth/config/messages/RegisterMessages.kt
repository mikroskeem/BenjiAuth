/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2019 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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

    @Setting(value = "please-register-title", comment = "This message gets sent to player repeatedly until one gets " +
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
    var registeredAndMustLogin = "{prefix}You registered successfully, now log in!"
        private set

    @Setting(value = "already-registered", comment = "This message gets sent when player is already registered")
    var alreadyRegistered = "{prefix}&cYou are already registered!"
        private set

    @Setting(value = "new-registrations-disabled", comment = "This message gets sent to player if registrations are " +
            "disabled")
    var newRegistrationsDisabled = "{prefix}Registering is turned off right now, please try again later!"
        private set
}